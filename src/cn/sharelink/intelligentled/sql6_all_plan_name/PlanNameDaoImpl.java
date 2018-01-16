package cn.sharelink.intelligentled.sql6_all_plan_name;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class PlanNameDaoImpl implements PlanNameDao{
	private Context context;

	public PlanNameDaoImpl(Context context) {
		super();
		setContext(context);
	}

	public void setContext(Context context) {
		if (context == null) {
			throw new IllegalArgumentException("参数Context不允许为null！！！");
		}
		this.context = context;
	}

	// 增加数据
	@Override
	public long insert(PlanName student) {
		// 1. 准备返回值
		long id = -1;

		// 2. 创建DBOpenHelper的对象
		DBPlanNameOpenHelper dbOpenHelper = new DBPlanNameOpenHelper(context);

		// 3. 获取SQLiteDatabase的对象
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

		// 4. 执行业务(增加)，并获取结果
		String table = DatabasePlanname.Student.TABLE_NAME;
		String nullColumnHack = DatabasePlanname.Student.Columns.ID;
		ContentValues values = new ContentValues();
		values.put(DatabasePlanname.Student.Columns.TYPE, student.getType());
		values.put(DatabasePlanname.Student.Columns.PLANNAME, student.getPlanName());
		values.put(DatabasePlanname.Student.Columns.PHYSICAL, student.getPhysical());
		values.put(DatabasePlanname.Student.Columns.GROUPNAME, student.getGroupName());
		id = db.insert(table, nullColumnHack, values);

		// 5. 释放资源
		db.close();

		// 6. 返回
		return id;
	}

	// 查询数据
	@Override
	public List<PlanName> query(String whereClause, String[] whereArgs) {
		// 日志
		Log.i("tedu", "StudentDaoImpl.query() start.");

		// 1. 准备返回值
		List<PlanName> students = new ArrayList<PlanName>();

		// 2. 创建DBOpenHelper的对象
		DBPlanNameOpenHelper dbOpenHelper = new DBPlanNameOpenHelper(context);

		// 3. 获取SQLiteDatabase的对象
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

		// 4.1. 执行业务(查询)，并获取结果
		String table = DatabasePlanname.Student.TABLE_NAME;
		String[] columns = { DatabasePlanname.Student.Columns.ID, // 0
				
				DatabasePlanname.Student.Columns.TYPE,//1
				DatabasePlanname.Student.Columns.PLANNAME,//2
				DatabasePlanname.Student.Columns.PHYSICAL,//3
				DatabasePlanname.Student.Columns.GROUPNAME//4
		};
		String selection = whereClause; // WHERE子句，例如：_id=?
		String[] selectionArgs = whereArgs;
		String groupBy = null;
		String having = null;
		String orderBy = DatabasePlanname.Student.Columns.ID + " DESC";
		Cursor c = db.query(table, columns, selection, selectionArgs, groupBy,
				having, orderBy);
		// 4.2. 分析查询结果
		if (c.moveToFirst()) {
			for (; !c.isAfterLast(); c.moveToNext()) {
				// 读取数据
				PlanName student = new PlanName();
				student.setId(c.getLong(0));
				
				student.setType(c.getInt(1));
				student.setPlanName(c.getString(2));
				student.setPhysical(c.getString(3));
				student.setGroupName(c.getString(4));
				students.add(student);
				Log.v("tedu", "" + student);
			}
		}

		// 5. 释放资源
		c.close();
		db.close();

		// 日志
		Log.i("tedu", "StudentDaoImpl.query() end.");

		// 6. 返回
		return students;
	}

	// 删除数据
	@Override
	public int delete(long id) {
		// 1. 准备返回值
		int affectedRows = 0;

		// 2. 创建DBOpenHelper的对象
		DBPlanNameOpenHelper dbOpenHelper = new DBPlanNameOpenHelper(context);

		// 3. 获取SQLiteDatabase的对象
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

		// 4. 执行业务(删除)，并获取结果
		String table = DatabasePlanname.Student.TABLE_NAME;
		String whereClause = DatabasePlanname.Student.Columns.ID + "=?";
		String[] whereArgs = { id + "" };
		affectedRows = db.delete(table, whereClause, whereArgs);

		// 5. 释放资源
		db.close();

		// 6. 返回
		return affectedRows;
	}

	// 更新数据
	@Override
	public long update(long id,int type,String planName,String physical,String groupName) {
		// 1. 准备返回值
		int affectedRows = 0;

		// 2. 创建DBOpenHelper的对象
		DBPlanNameOpenHelper dbOpenHelper = new DBPlanNameOpenHelper(context);

		// 3. 获取SQLiteDatabase的对象
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

		// 4. 执行业务(更新)，并获取结果
		String table = DatabasePlanname.Student.TABLE_NAME;
		String whereClause = DatabasePlanname.Student.Columns.ID + "=?";
		String[] whereArgs = { id + "" };
		ContentValues values = new ContentValues();
		
		values.put(DatabasePlanname.Student.Columns.TYPE, type);
		values.put(DatabasePlanname.Student.Columns.PLANNAME, planName);
		values.put(DatabasePlanname.Student.Columns.PHYSICAL, physical);
		values.put(DatabasePlanname.Student.Columns.GROUPNAME, groupName);
		affectedRows = db.update(table, values, whereClause, whereArgs);
		// 5. 释放资源
		db.close();

		return affectedRows;
	}


}
