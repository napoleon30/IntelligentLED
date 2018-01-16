package cn.sharelink.intelligentled.sql7_group_device_list;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class GroupDeviceListDaoImpl implements GroupDeviceListDao{
	private Context context;

	public GroupDeviceListDaoImpl(Context context) {
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
	public long insert(GroupDeviceList student) {
		// 1. 准备返回值
		long id = -1;

		// 2. 创建DBOpenHelper的对象
		DBDeviceListOpenHelper dbOpenHelper = new DBDeviceListOpenHelper(context);

		// 3. 获取SQLiteDatabase的对象
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

		// 4. 执行业务(增加)，并获取结果
		String table = DatabaseGroupDeviceList.Student.TABLE_NAME;
		String nullColumnHack = DatabaseGroupDeviceList.Student.Columns.ID;
		ContentValues values = new ContentValues();
		values.put(DatabaseGroupDeviceList.Student.Columns.TYPE, student.getType());
		values.put(DatabaseGroupDeviceList.Student.Columns.PHYSICAL, student.getPhysical());
		values.put(DatabaseGroupDeviceList.Student.Columns.GTOUPNAME, student.getGroupName());
		id = db.insert(table, nullColumnHack, values);

		// 5. 释放资源
		db.close();

		// 6. 返回
		return id;
	}

	// 查询数据
	@Override
	public List<GroupDeviceList> query(String whereClause, String[] whereArgs) {
		// 日志
		Log.i("tedu", "StudentDaoImpl.query() start.");

		// 1. 准备返回值
		List<GroupDeviceList> students = new ArrayList<GroupDeviceList>();

		// 2. 创建DBOpenHelper的对象
		DBDeviceListOpenHelper dbOpenHelper = new DBDeviceListOpenHelper(context);

		// 3. 获取SQLiteDatabase的对象
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

		// 4.1. 执行业务(查询)，并获取结果
		String table = DatabaseGroupDeviceList.Student.TABLE_NAME;
		String[] columns = { DatabaseGroupDeviceList.Student.Columns.ID, // 0
				DatabaseGroupDeviceList.Student.Columns.TYPE, //1
				DatabaseGroupDeviceList.Student.Columns.PHYSICAL,//2
				DatabaseGroupDeviceList.Student.Columns.GTOUPNAME//3
		};
		String selection = whereClause; // WHERE子句，例如：_id=?
		String[] selectionArgs = whereArgs;
		String groupBy = null;
		String having = null;
		String orderBy = DatabaseGroupDeviceList.Student.Columns.ID + " DESC";
		Cursor c = db.query(table, columns, selection, selectionArgs, groupBy,
				having, orderBy);
		// 4.2. 分析查询结果
		if (c.moveToFirst()) {
			for (; !c.isAfterLast(); c.moveToNext()) {
				// 读取数据
				GroupDeviceList student = new GroupDeviceList();
				student.setId(c.getLong(0));
				student.setType(c.getString(1));
				student.setPhysical(c.getString(2));
				student.setGroupName(c.getString(3));
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
		DBDeviceListOpenHelper dbOpenHelper = new DBDeviceListOpenHelper(context);

		// 3. 获取SQLiteDatabase的对象
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

		// 4. 执行业务(删除)，并获取结果
		String table = DatabaseGroupDeviceList.Student.TABLE_NAME;
		String whereClause = DatabaseGroupDeviceList.Student.Columns.ID + "=?";
		String[] whereArgs = { id + "" };
		affectedRows = db.delete(table, whereClause, whereArgs);

		// 5. 释放资源
		db.close();

		// 6. 返回
		return affectedRows;
	}

	// 更新数据
	@Override
	public long update(long id,String type,String physical,String groupName) {
		// 1. 准备返回值
		int affectedRows = 0;

		// 2. 创建DBOpenHelper的对象
		DBDeviceListOpenHelper dbOpenHelper = new DBDeviceListOpenHelper(context);

		// 3. 获取SQLiteDatabase的对象
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

		// 4. 执行业务(更新)，并获取结果
		String table = DatabaseGroupDeviceList.Student.TABLE_NAME;
		String whereClause = DatabaseGroupDeviceList.Student.Columns.ID + "=?";
		String[] whereArgs = { id + "" };
		ContentValues values = new ContentValues();
		values.put(DatabaseGroupDeviceList.Student.Columns.TYPE, type);
		values.put(DatabaseGroupDeviceList.Student.Columns.PHYSICAL, physical);
		values.put(DatabaseGroupDeviceList.Student.Columns.GTOUPNAME, groupName);
		affectedRows = db.update(table, values, whereClause, whereArgs);
		// 5. 释放资源
		db.close();

		return affectedRows;
	}


}
