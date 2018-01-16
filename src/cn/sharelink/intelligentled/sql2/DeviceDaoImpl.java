package cn.sharelink.intelligentled.sql2;

import java.util.ArrayList;
import java.util.List;

import cn.sharelink.intelligentled.sql2.Database.Student;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DeviceDaoImpl implements IDeviceDao {
	private Context context;

	public DeviceDaoImpl(Context context) {
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
	public long insert(Device student) {
		// 1. 准备返回值
		long id = -1;

		// 2. 创建DBOpenHelper的对象
		DBOpenHelper dbOpenHelper = new DBOpenHelper(context);

		// 3. 获取SQLiteDatabase的对象
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

		// 4. 执行业务(增加)，并获取结果
		String table = Database.Student.TABLE_NAME;
		String nullColumnHack = Database.Student.Columns.ID;
		ContentValues values = new ContentValues();
		values.put(Database.Student.Columns.NAME, student.getName());
		values.put(Database.Student.Columns.DEVICEID,student.getDeviceID());
		values.put(Database.Student.Columns.PHYSICALDEVICEID, student.getPhysicalDeviceID());
		values.put(Database.Student.Columns.TYPE, student.getType());
		values.put(Database.Student.Columns.GROUP, student.getGroup());
		values.put(Database.Student.Columns.GROUPTYPE, student.getGrouptype());
		id = db.insert(table, nullColumnHack, values);

		// 5. 释放资源
		db.close();

		// 6. 返回
		return id;
	}

	// 查询数据
	@Override
	public List<Device> query(String whereClause, String[] whereArgs) {
		// 日志
		Log.i("tedu", "StudentDaoImpl.query() start.");

		// 1. 准备返回值
		List<Device> students = new ArrayList<Device>();

		// 2. 创建DBOpenHelper的对象
		DBOpenHelper dbOpenHelper = new DBOpenHelper(context);

		// 3. 获取SQLiteDatabase的对象
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

		// 4.1. 执行业务(查询)，并获取结果
		String table = Database.Student.TABLE_NAME;
		String[] columns = { Database.Student.Columns.ID, // 0
				Database.Student.Columns.NAME, // 1
				Database.Student.Columns.DEVICEID,//2
				Database.Student.Columns.PHYSICALDEVICEID,//3
				Database.Student.Columns.TYPE, // 4
				Database.Student.Columns.GROUP, // 5
				Database.Student.Columns.GROUPTYPE//6
		};
		String selection = whereClause; // WHERE子句，例如：_id=?
		String[] selectionArgs = whereArgs;
		String groupBy = null;
		String having = null;
		String orderBy = Database.Student.Columns.ID + " DESC";
		Cursor c = db.query(table, columns, selection, selectionArgs, groupBy,
				having, orderBy);
		// 4.2. 分析查询结果
		if (c.moveToFirst()) {
			for (; !c.isAfterLast(); c.moveToNext()) {
				// 读取数据
				Device student = new Device();
				student.setId(c.getLong(0));
				student.setName(c.getString(1));
				student.setDeviceID(c.getString(2));
				student.setPhysicalDeviceID(c.getString(3));
				student.setType(c.getInt(4));
				student.setGroup(c.getString(5));
				student.setGrouptype(c.getInt(6));
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
		DBOpenHelper dbOpenHelper = new DBOpenHelper(context);

		// 3. 获取SQLiteDatabase的对象
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

		// 4. 执行业务(删除)，并获取结果
		String table = Database.Student.TABLE_NAME;
		String whereClause = Database.Student.Columns.ID + "=?";
		String[] whereArgs = { id + "" };
		affectedRows = db.delete(table, whereClause, whereArgs);

		// 5. 释放资源
		db.close();

		// 6. 返回
		return affectedRows;
	}

	// 更新数据
	@Override
	public long update(long id, String name, String deviceID,
			String physicalDeviceID, int type, String group,int grouptype) {
		// 1. 准备返回值
		int affectedRows = 0;

		// 2. 创建DBOpenHelper的对象
		DBOpenHelper dbOpenHelper = new DBOpenHelper(context);

		// 3. 获取SQLiteDatabase的对象
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

		// 4. 执行业务(更新)，并获取结果
		String table = Database.Student.TABLE_NAME;
		String whereClause = Database.Student.Columns.ID + "=?";
		String[] whereArgs = { id + "" };
		ContentValues values = new ContentValues();
		values.put(Database.Student.Columns.NAME, name);
		values.put(Database.Student.Columns.DEVICEID, deviceID);
		values.put(Database.Student.Columns.PHYSICALDEVICEID, physicalDeviceID);
		values.put(Database.Student.Columns.TYPE, type);
		values.put(Database.Student.Columns.GROUP, group);
		values.put(Database.Student.Columns.GROUPTYPE,grouptype);
		affectedRows = db.update(table, values, whereClause, whereArgs);
		// 5. 释放资源
		db.close();

		return affectedRows;
	}


}
