package cn.sharelink.intelligentled.sql3_for_time_project;

import java.util.ArrayList;
import java.util.List;

import cn.sharelink.intelligentled.sql2.Database;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ProjectDaoImpl implements IProjectDao {
	private Context context;

	public ProjectDaoImpl(Context context) {
		super();
		setContext(context);
	}

	public void setContext(Context context) {
		if (context == null) {
			throw new IllegalArgumentException("参数Context不允许为null！！！");
		}
		this.context = context;
	}

	/**
	 * 增
	 */
	@Override
	public long insert(Project project) {
		// 1. 准备返回值
		long id = -1;
		// 2. 创建DBProjectOpenHelper的对象
		DBProjectOpenHelper dbOpenHelper = new DBProjectOpenHelper(context);
		// 3. 获取SQLiteDatabase的对象
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		// 4. 执行业务(增加)，并获取结果
		String tableName = ProjectDataBase.Projects.TABLE_NAME;
		String nullColumnHack = ProjectDataBase.Projects.Columns.ID;
		ContentValues values = new ContentValues();
		values.put(ProjectDataBase.Projects.Columns.NAME, project.getName());
		values.put(ProjectDataBase.Projects.Columns.TYPE, project.getType());
		values.put(ProjectDataBase.Projects.Columns.WEEKDAY,
				project.getWeekday());
		values.put(ProjectDataBase.Projects.Columns.BEGINTIME1,
				project.getBegintime1());
		values.put(ProjectDataBase.Projects.Columns.ENDTIME1,
				project.getEndtime1());
		values.put(ProjectDataBase.Projects.Columns.SEEK11, project.getSeek11());
		values.put(ProjectDataBase.Projects.Columns.SEEK12, project.getSeek12());

		values.put(ProjectDataBase.Projects.Columns.BEGINTIME2,
				project.getBegintime2());
		values.put(ProjectDataBase.Projects.Columns.ENDTIME2,
				project.getEndtime2());
		values.put(ProjectDataBase.Projects.Columns.SEEK21, project.getSeek21());
		values.put(ProjectDataBase.Projects.Columns.SEEK22, project.getSeek22());

		values.put(ProjectDataBase.Projects.Columns.BEGINTIME3,
				project.getBegintime3());
		values.put(ProjectDataBase.Projects.Columns.ENDTIME3,
				project.getEndtime3());
		values.put(ProjectDataBase.Projects.Columns.SEEK31, project.getSeek31());
		values.put(ProjectDataBase.Projects.Columns.SEEK32, project.getSeek32());

		values.put(ProjectDataBase.Projects.Columns.BEGINTIME4,
				project.getBegintime4());
		values.put(ProjectDataBase.Projects.Columns.ENDTIME4,
				project.getEndtime4());
		values.put(ProjectDataBase.Projects.Columns.SEEK41, project.getSeek41());
		values.put(ProjectDataBase.Projects.Columns.SEEK42, project.getSeek42());
		values.put(ProjectDataBase.Projects.Columns.GROUPNAME, project.getGroupName());
		values.put(ProjectDataBase.Projects.Columns.PHYSICAL, project.getPhysical());
		id = db.insert(tableName, nullColumnHack, values);
		// 5. 释放资源
		db.close();
		// 6. 返回
		return id;
	}

	/**
	 * 查
	 */
	@Override
	public List<Project> query(String whereClause, String[] whereArgs) {
		// 1. 准备返回值
		List<Project> projects = new ArrayList<Project>();
		// 2. 创建DBProjectOpenHelper的对象
		DBProjectOpenHelper dbOpenHelper = new DBProjectOpenHelper(context);
		// 3. 获取SQLiteDatabase的对象
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		// 4.1. 执行业务(查询)，并获取结果
		String tableName = ProjectDataBase.Projects.TABLE_NAME;
		String[] columns = { ProjectDataBase.Projects.Columns.ID, // 0
				ProjectDataBase.Projects.Columns.NAME,//1
				ProjectDataBase.Projects.Columns.TYPE,//2
				ProjectDataBase.Projects.Columns.WEEKDAY, // 3
				ProjectDataBase.Projects.Columns.BEGINTIME1, //4
				ProjectDataBase.Projects.Columns.ENDTIME1, // 5
				ProjectDataBase.Projects.Columns.SEEK11, // 6
				ProjectDataBase.Projects.Columns.SEEK12, // 7
				ProjectDataBase.Projects.Columns.BEGINTIME2, // 8
				ProjectDataBase.Projects.Columns.ENDTIME2, //9
				ProjectDataBase.Projects.Columns.SEEK21, // 10
				ProjectDataBase.Projects.Columns.SEEK22, // 11
				ProjectDataBase.Projects.Columns.BEGINTIME3, // 12
				ProjectDataBase.Projects.Columns.ENDTIME3, // 13
				ProjectDataBase.Projects.Columns.SEEK31, // 14
				ProjectDataBase.Projects.Columns.SEEK32, // 15
				ProjectDataBase.Projects.Columns.BEGINTIME4, // 16
				ProjectDataBase.Projects.Columns.ENDTIME4, // 17
				ProjectDataBase.Projects.Columns.SEEK41, // 18
				ProjectDataBase.Projects.Columns.SEEK42, // 19
				ProjectDataBase.Projects.Columns.GROUPNAME,
				ProjectDataBase.Projects.Columns.PHYSICAL
		};
		String selection = whereClause; // WHERE子句，例如：_id=?
		String[] selectionArgs = whereArgs;
		String groupBy = null;
		String having = null;
		String orderBy = ProjectDataBase.Projects.Columns.ID + " DESC";
		Cursor c = db.query(tableName, columns, selection, selectionArgs,
				groupBy, having, orderBy);
		// 4.2. 分析查询结果
		if (c.moveToFirst()) {
			for (; !c.isAfterLast(); c.moveToNext()) {
				// 读取数据
				Project project = new Project();
				project.setId(c.getLong(0));
				project.setName(c.getString(1));
				project.setType(c.getInt(2));
				project.setWeekday(c.getInt(3));
				project.setBegintime1(c.getString(4));
				project.setEndtime1(c.getString(5));
				project.setSeek11(c.getString(6));
				project.setSeek12(c.getString(7));
				project.setBegintime2(c.getString(8));
				project.setEndtime2(c.getString(9));
				project.setSeek21(c.getString(10));
				project.setSeek22(c.getString(11));
				project.setBegintime3(c.getString(12));
				project.setEndtime3(c.getString(13));
				project.setSeek31(c.getString(14));
				project.setSeek32(c.getString(15));
				project.setBegintime4(c.getString(16));
				project.setEndtime4(c.getString(17));
				project.setSeek41(c.getString(18));
				project.setSeek42(c.getString(19));
				project.setGroupName(c.getString(20));
				project.setPhysical(c.getString(21));

				projects.add(project);
			}
		}
		// 5. 释放资源
		c.close();
		db.close();
		// 6. 返回
		return projects;
	}

	/**
	 * 删
	 */
	@Override
	public int delete(long id) {
		// 1. 准备返回值
		int affectedRows = 0;
		DBProjectOpenHelper dbOpenHelper = new DBProjectOpenHelper(context);
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		// 4. 执行业务(删除)，并获取结果
		String tableName = ProjectDataBase.Projects.TABLE_NAME;
		String whereClause = ProjectDataBase.Projects.Columns.ID + "=?";
		String[] whereArgs = { id + "" };
		affectedRows = db.delete(tableName, whereClause, whereArgs);
		// 5. 释放资源
		db.close();

		// 6. 返回
		return affectedRows;
	}

	/**
	 * 改
	 */
	@Override
	public long update(long id, String name, int type,int weekday, String begintime1,
			String endtime1, String seek11, String seek12, String begintime2,
			String endtime2, String seek21, String seek22, String begintime3,
			String endtime3, String seek31, String seek32, String begintime4,
			String endtime4, String seek41, String seek42,String groupName,String physical) {
		// 1. 准备返回值
		int affectedRows = 0;
		// 2. 创建DBProjectOpenHelper的对象
		DBProjectOpenHelper dbOpenHelper = new DBProjectOpenHelper(context);
		// 3. 获取SQLiteDatabase的对象
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		// 4. 执行业务(更新)，并获取结果
		String tableName = ProjectDataBase.Projects.TABLE_NAME;
		String whereClause = ProjectDataBase.Projects.Columns.ID + "=?";
		String[] whereArgs = { id + "" };
		ContentValues values = new ContentValues();
		values.put(ProjectDataBase.Projects.Columns.NAME, name);
		values.put(ProjectDataBase.Projects.Columns.TYPE, type);
		values.put(ProjectDataBase.Projects.Columns.WEEKDAY, weekday);
		values.put(ProjectDataBase.Projects.Columns.BEGINTIME1, begintime1);
		values.put(ProjectDataBase.Projects.Columns.ENDTIME1, endtime1);
		values.put(ProjectDataBase.Projects.Columns.SEEK11, seek11);
		values.put(ProjectDataBase.Projects.Columns.SEEK12, seek12);
		values.put(ProjectDataBase.Projects.Columns.BEGINTIME2, begintime2);
		values.put(ProjectDataBase.Projects.Columns.ENDTIME2, endtime2);
		values.put(ProjectDataBase.Projects.Columns.SEEK21, seek21);
		values.put(ProjectDataBase.Projects.Columns.SEEK22, seek22);
		values.put(ProjectDataBase.Projects.Columns.BEGINTIME3, begintime3);
		values.put(ProjectDataBase.Projects.Columns.ENDTIME3, endtime3);
		values.put(ProjectDataBase.Projects.Columns.SEEK31, seek31);
		values.put(ProjectDataBase.Projects.Columns.SEEK32, seek32);
		values.put(ProjectDataBase.Projects.Columns.BEGINTIME4, begintime4);
		values.put(ProjectDataBase.Projects.Columns.ENDTIME4, endtime4);
		values.put(ProjectDataBase.Projects.Columns.SEEK41, seek41);
		values.put(ProjectDataBase.Projects.Columns.SEEK42, seek42);
		values.put(ProjectDataBase.Projects.Columns.GROUPNAME, groupName);
		values.put(ProjectDataBase.Projects.Columns.PHYSICAL, physical);
		affectedRows = db.update(tableName, values, whereClause, whereArgs);
		// 5. 释放资源
		db.close();
		return affectedRows;
	}

}
