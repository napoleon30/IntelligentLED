package cn.sharelink.intelligentled.sql7_group_device_list;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBDeviceListOpenHelper extends SQLiteOpenHelper {

	
	public DBDeviceListOpenHelper(Context context) {
		super(context, "groupdevicelist1.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// 当创建数据库时，应该创建数据表，如果需要默认添加数据，则还应该创建数据表后添加数据
		String sql = "CREATE TABLE " + DatabaseGroupDeviceList.Student.TABLE_NAME + " ("
				+ DatabaseGroupDeviceList.Student.Columns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ DatabaseGroupDeviceList.Student.Columns.TYPE + " VARCHAR(30), "
				+ DatabaseGroupDeviceList.Student.Columns.PHYSICAL + " VARCHAR(30), "
				+ DatabaseGroupDeviceList.Student.Columns.GTOUPNAME + " VARCHAR(30)"
				+ ")";
		db.execSQL(sql); // execute sql
	}

	/**
	 * 当检测与前一次创建数据库版本不一样时，先删除表再创建新表
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + DatabaseGroupDeviceList.Student.TABLE_NAME);
		onCreate(db);
	}
	
	/**
	 * 变更列名
	 * @param db
	 * @param oldColumn
	 * @param newColumn
	 * @param typeColumn
	 */
	public void updateColumn(SQLiteDatabase db, String oldColumn, String newColumn, String typeColumn){
		try{
			db.execSQL("ALTER TABLE " +
					DatabaseGroupDeviceList.Student.TABLE_NAME + " CHANGE " +
					oldColumn + " "+ newColumn +
					" " + typeColumn
			);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
