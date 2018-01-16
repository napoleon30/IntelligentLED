package cn.sharelink.intelligentled.sql4_for_rgb_project;

import java.util.ArrayList;
import java.util.List;

import cn.sharelink.intelligentled.sql2.Database;
import cn.sharelink.intelligentled.sql3_for_time_project.ProjectDataBase;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ProjectRGBDaoImpl implements IProjectRGBDao {
	private Context context;

	public ProjectRGBDaoImpl(Context context) {
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
	public long insert(ProjectRGB project) {
		// 1. 准备返回值
		long id = -1;
		// 2. 创建DBProjectOpenHelper的对象
		DBProjectRGBOpenHelper dbOpenHelper = new DBProjectRGBOpenHelper(context);
		// 3. 获取SQLiteDatabase的对象
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		// 4. 执行业务(增加)，并获取结果
		String tableName = ProjectRGBDataBase.Projects.TABLE_NAME;
		String nullColumnHack = ProjectRGBDataBase.Projects.Columns.ID;
		ContentValues values = new ContentValues();
		values.put(ProjectRGBDataBase.Projects.Columns.NAME, project.getName());
		values.put(ProjectRGBDataBase.Projects.Columns.WEEKDAY,project.getWeekday());
		values.put(ProjectRGBDataBase.Projects.Columns.MODE, project.getMode());
		
		values.put(ProjectRGBDataBase.Projects.Columns.ETONE, project.getEtOne());
		values.put(ProjectRGBDataBase.Projects.Columns.ETTWO, project.getEtTwo());
		values.put(ProjectRGBDataBase.Projects.Columns.ETTHREE, project.getEtThree());
		values.put(ProjectRGBDataBase.Projects.Columns.ETFOUR, project.getEtFour());
		values.put(ProjectRGBDataBase.Projects.Columns.ETFIVE, project.getEtFive());
		values.put(ProjectRGBDataBase.Projects.Columns.ETSIX, project.getEtSix());
		values.put(ProjectRGBDataBase.Projects.Columns.ETSEVEN, project.getEtSeven());
		values.put(ProjectRGBDataBase.Projects.Columns.ETEIGHT, project.getEtEight());
		values.put(ProjectRGBDataBase.Projects.Columns.ETONER, project.getEtOneR());
		values.put(ProjectRGBDataBase.Projects.Columns.ETONEG, project.getEtOneG());
		values.put(ProjectRGBDataBase.Projects.Columns.ETONEB, project.getEtOneB());
		values.put(ProjectRGBDataBase.Projects.Columns.ETTWOR, project.getEtTwoR());
		values.put(ProjectRGBDataBase.Projects.Columns.ETTWOG, project.getEtTwoG());
		values.put(ProjectRGBDataBase.Projects.Columns.ETTWOB, project.getEtTwoB());
		values.put(ProjectRGBDataBase.Projects.Columns.ETTHREER, project.getEtThreeR());
		values.put(ProjectRGBDataBase.Projects.Columns.ETTHREEG, project.getEtThreeG());
		values.put(ProjectRGBDataBase.Projects.Columns.ETTHREEB, project.getEtThreeB());
		values.put(ProjectRGBDataBase.Projects.Columns.ETFOURR, project.getEtFourR());
		values.put(ProjectRGBDataBase.Projects.Columns.ETFOURG, project.getEtFourG());
		values.put(ProjectRGBDataBase.Projects.Columns.ETFOURB, project.getEtFourB());
		values.put(ProjectRGBDataBase.Projects.Columns.ETFIVER, project.getEtFiveR());
		values.put(ProjectRGBDataBase.Projects.Columns.ETFIVEG, project.getEtFiveG());
		values.put(ProjectRGBDataBase.Projects.Columns.ETFIVEB, project.getEtFiveB());
		values.put(ProjectRGBDataBase.Projects.Columns.ETROTATIONTIME, project.getEtRotationTime());
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
	public List<ProjectRGB> query(String whereClause, String[] whereArgs) {
		// 1. 准备返回值
		List<ProjectRGB> projects = new ArrayList<ProjectRGB>();
		// 2. 创建DBProjectOpenHelper的对象
		DBProjectRGBOpenHelper dbOpenHelper = new DBProjectRGBOpenHelper(context);
		// 3. 获取SQLiteDatabase的对象
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		// 4.1. 执行业务(查询)，并获取结果
		String tableName = ProjectRGBDataBase.Projects.TABLE_NAME;
		String[] columns = { ProjectRGBDataBase.Projects.Columns.ID, // 0
				ProjectRGBDataBase.Projects.Columns.NAME,//1
				ProjectRGBDataBase.Projects.Columns.WEEKDAY, // 2
				ProjectRGBDataBase.Projects.Columns.MODE,//3
				ProjectRGBDataBase.Projects.Columns.ETONE, // 4
				ProjectRGBDataBase.Projects.Columns.ETTWO, // 5
				ProjectRGBDataBase.Projects.Columns.ETTHREE, // 6
				ProjectRGBDataBase.Projects.Columns.ETFOUR, // 7
				ProjectRGBDataBase.Projects.Columns.ETFIVE, // 8
				ProjectRGBDataBase.Projects.Columns.ETSIX, // 9
				ProjectRGBDataBase.Projects.Columns.ETSEVEN, // 10
				ProjectRGBDataBase.Projects.Columns.ETEIGHT, // 11
				ProjectRGBDataBase.Projects.Columns.ETONER, // 12
				ProjectRGBDataBase.Projects.Columns.ETONEG, // 13
				ProjectRGBDataBase.Projects.Columns.ETONEB, // 14
				ProjectRGBDataBase.Projects.Columns.ETTWOR, // 15
				ProjectRGBDataBase.Projects.Columns.ETTWOG, // 16
				ProjectRGBDataBase.Projects.Columns.ETTWOB, // 17
				ProjectRGBDataBase.Projects.Columns.ETTHREER, // 18
				ProjectRGBDataBase.Projects.Columns.ETTHREEG, // 19
				ProjectRGBDataBase.Projects.Columns.ETTHREEB, // 20
				ProjectRGBDataBase.Projects.Columns.ETFOURR, // 21
				ProjectRGBDataBase.Projects.Columns.ETFOURG, // 22
				ProjectRGBDataBase.Projects.Columns.ETFOURB, // 23
				ProjectRGBDataBase.Projects.Columns.ETFIVER, // 24
				ProjectRGBDataBase.Projects.Columns.ETFIVEG, // 25
				ProjectRGBDataBase.Projects.Columns.ETFIVEB, // 26
				ProjectRGBDataBase.Projects.Columns.ETROTATIONTIME, //27
				ProjectRGBDataBase.Projects.Columns.GROUPNAME,
				ProjectRGBDataBase.Projects.Columns.PHYSICAL
		};
		String selection = whereClause; // WHERE子句，例如：_id=?
		String[] selectionArgs = whereArgs;
		String groupBy = null;
		String having = null;
		String orderBy = ProjectRGBDataBase.Projects.Columns.ID + " DESC";
		Cursor c = db.query(tableName, columns, selection, selectionArgs,
				groupBy, having, orderBy);
		// 4.2. 分析查询结果
		if (c.moveToFirst()) {
			for (; !c.isAfterLast(); c.moveToNext()) {
				// 读取数据
				ProjectRGB project = new ProjectRGB();
				project.setId(c.getLong(0));
				project.setName(c.getString(1));
				project.setWeekday(c.getInt(2));
				project.setMode(c.getInt(3));
				project.setEtOne(c.getString(4));
				project.setEtTwo(c.getString(5));
				project.setEtThree(c.getString(6));
				project.setEtFour(c.getString(7));
				project.setEtFive(c.getString(8));
				project.setEtSix(c.getString(9));
				project.setEtSeven(c.getString(10));
				project.setEtEight(c.getString(11));
				project.setEtOneR(c.getString(12));
				project.setEtOneG(c.getString(13));
				project.setEtOneB(c.getString(14));
				project.setEtTwoR(c.getString(15));
				project.setEtTwoG(c.getString(16));
				project.setEtTwoB(c.getString(17));
				project.setEtThreeR(c.getString(18));
				project.setEtThreeG(c.getString(19));
				project.setEtThreeB(c.getString(20));
				project.setEtFourR(c.getString(21));
				project.setEtFourG(c.getString(22));
				project.setEtFourB(c.getString(23));
				project.setEtFiveR(c.getString(24));
				project.setEtFiveG(c.getString(25));
				project.setEtFiveB(c.getString(26));
				project.setEtRotationTime(c.getString(27));
				project.setGroupName(c.getString(28));
				project.setPhysical(c.getString(29));
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
		DBProjectRGBOpenHelper dbOpenHelper = new DBProjectRGBOpenHelper(context);
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		// 4. 执行业务(删除)，并获取结果
		String tableName = ProjectRGBDataBase.Projects.TABLE_NAME;
		String whereClause = ProjectRGBDataBase.Projects.Columns.ID + "=?";
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
	public long update(long id,String name,int weekday,int mode,
			String etOne,String etTwo,String etThree,String etFour,String etFive,
			String etSix,String etSeven,String etEight,
			String etOneR,String etOneG,String etOneB,
			String etTwoR,String etTwoG,String etTwoB,
			String etThreeR,String etThreeG,String etThreeB,
			String etFourR,String etFourG,String etFourB,
			String etFiveR,String etFiveG,String etFiveB,String etRotationTime,
			String groupName,String physical) {
		// 1. 准备返回值
		int affectedRows = 0;
		// 2. 创建DBProjectOpenHelper的对象
		DBProjectRGBOpenHelper dbOpenHelper = new DBProjectRGBOpenHelper(context);
		// 3. 获取SQLiteDatabase的对象
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		// 4. 执行业务(更新)，并获取结果
		String tableName = ProjectRGBDataBase.Projects.TABLE_NAME;
		String whereClause = ProjectRGBDataBase.Projects.Columns.ID + "=?";
		String[] whereArgs = { id + "" };
		ContentValues values = new ContentValues();
		values.put(ProjectRGBDataBase.Projects.Columns.NAME, name);
		values.put(ProjectRGBDataBase.Projects.Columns.WEEKDAY, weekday);
		values.put(ProjectRGBDataBase.Projects.Columns.MODE, mode);
		values.put(ProjectRGBDataBase.Projects.Columns.ETONE, etOne);
		values.put(ProjectRGBDataBase.Projects.Columns.ETTWO, etTwo);
		values.put(ProjectRGBDataBase.Projects.Columns.ETTHREE, etThree);
		values.put(ProjectRGBDataBase.Projects.Columns.ETFOUR, etFour);
		values.put(ProjectRGBDataBase.Projects.Columns.ETFIVE, etFive);
		values.put(ProjectRGBDataBase.Projects.Columns.ETSIX, etSix);
		values.put(ProjectRGBDataBase.Projects.Columns.ETSEVEN, etSeven);
		values.put(ProjectRGBDataBase.Projects.Columns.ETEIGHT, etEight);
		values.put(ProjectRGBDataBase.Projects.Columns.ETONER, etOneR);
		values.put(ProjectRGBDataBase.Projects.Columns.ETONEG, etOneG);
		values.put(ProjectRGBDataBase.Projects.Columns.ETONEB, etOneB);
		values.put(ProjectRGBDataBase.Projects.Columns.ETTWOR, etTwoR);
		values.put(ProjectRGBDataBase.Projects.Columns.ETTWOG, etTwoG);
		values.put(ProjectRGBDataBase.Projects.Columns.ETTWOB, etTwoB);
		values.put(ProjectRGBDataBase.Projects.Columns.ETTHREER, etThreeR);
		values.put(ProjectRGBDataBase.Projects.Columns.ETTHREEG, etThreeG);
		values.put(ProjectRGBDataBase.Projects.Columns.ETTHREEB, etThreeB);
		values.put(ProjectRGBDataBase.Projects.Columns.ETFOURR, etFourR);
		values.put(ProjectRGBDataBase.Projects.Columns.ETFOURG, etFourG);
		values.put(ProjectRGBDataBase.Projects.Columns.ETFOURB, etFourB);
		values.put(ProjectRGBDataBase.Projects.Columns.ETFIVER, etFiveR);
		values.put(ProjectRGBDataBase.Projects.Columns.ETFIVEG, etFiveG);
		values.put(ProjectRGBDataBase.Projects.Columns.ETFIVEB, etFiveB);
		values.put(ProjectRGBDataBase.Projects.Columns.ETROTATIONTIME, etRotationTime);
		values.put(ProjectRGBDataBase.Projects.Columns.GROUPNAME, groupName);
		values.put(ProjectRGBDataBase.Projects.Columns.PHYSICAL, physical);
		
		affectedRows = db.update(tableName, values, whereClause, whereArgs);
		// 5. 释放资源
		db.close();
		return affectedRows;
	}

}
