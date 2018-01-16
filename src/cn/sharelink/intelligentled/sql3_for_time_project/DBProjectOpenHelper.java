package cn.sharelink.intelligentled.sql3_for_time_project;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBProjectOpenHelper extends SQLiteOpenHelper{
	public DBProjectOpenHelper(Context context) {
		super(context, "project.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "CREATE TABLE "+ProjectDataBase.Projects.TABLE_NAME +" ("
				+ProjectDataBase.Projects.Columns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ProjectDataBase.Projects.Columns.NAME + " VARCHAR(30), "
				+ProjectDataBase.Projects.Columns.TYPE + " INTEGER, "
				+ProjectDataBase.Projects.Columns.WEEKDAY + " INTEGER, "
				+ProjectDataBase.Projects.Columns.BEGINTIME1 + " VARCHAR(30), "
				+ProjectDataBase.Projects.Columns.ENDTIME1 + " VARCHAR(30), "
				+ProjectDataBase.Projects.Columns.SEEK11 + " VARCHAR(30), "
				+ProjectDataBase.Projects.Columns.SEEK12 + " VARCHAR(30), "
				+ProjectDataBase.Projects.Columns.BEGINTIME2+ " VARCHAR(30), "
				+ProjectDataBase.Projects.Columns.ENDTIME2 + " VARCHAR(30), "
				+ProjectDataBase.Projects.Columns.SEEK21 + " VARCHAR(30), "
				+ProjectDataBase.Projects.Columns.SEEK22 + " VARCHAR(30), "
				+ProjectDataBase.Projects.Columns.BEGINTIME3 + " VARCHAR(30), "
				+ProjectDataBase.Projects.Columns.ENDTIME3 + " VARCHAR(30), "
				+ProjectDataBase.Projects.Columns.SEEK31 + " VARCHAR(30), "
				+ProjectDataBase.Projects.Columns.SEEK32 + " VARCHAR(30), "
				+ProjectDataBase.Projects.Columns.BEGINTIME4 + " VARCHAR(30), "
				+ProjectDataBase.Projects.Columns.ENDTIME4 + " VARCHAR(30), "
				+ProjectDataBase.Projects.Columns.SEEK41 + " VARCHAR(30), "
				+ProjectDataBase.Projects.Columns.SEEK42 + " VARCHAR(30), " 
				+ProjectDataBase.Projects.Columns.GROUPNAME + " VARCHAR(30), "
				+ProjectDataBase.Projects.Columns.PHYSICAL + " VARCHAR(30) "
				+ ")";
		db.execSQL(sql);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
