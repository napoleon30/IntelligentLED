package cn.sharelink.intelligentled.sql4_for_rgb_project;

import cn.sharelink.intelligentled.sql3_for_time_project.ProjectDataBase;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBProjectRGBOpenHelper extends SQLiteOpenHelper{
	public DBProjectRGBOpenHelper(Context context) {
		super(context, "projectrgb.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "CREATE TABLE "+ProjectRGBDataBase.Projects.TABLE_NAME +" ("
				+ProjectRGBDataBase.Projects.Columns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ProjectRGBDataBase.Projects.Columns.NAME + " VARCHAR(30), "
				+ProjectRGBDataBase.Projects.Columns.WEEKDAY + " INTEGER, "
				+ProjectRGBDataBase.Projects.Columns.MODE + " INTEGER, "
				+ProjectRGBDataBase.Projects.Columns.ETONE + " VARCHAR(30), "
				+ProjectRGBDataBase.Projects.Columns.ETTWO + " VARCHAR(30), "
				+ProjectRGBDataBase.Projects.Columns.ETTHREE + " VARCHAR(30), "
				+ProjectRGBDataBase.Projects.Columns.ETFOUR + " VARCHAR(30), "
				+ProjectRGBDataBase.Projects.Columns.ETFIVE + " VARCHAR(30), "
				+ProjectRGBDataBase.Projects.Columns.ETSIX + " VARCHAR(30), "
				+ProjectRGBDataBase.Projects.Columns.ETSEVEN + " VARCHAR(30), "
				+ProjectRGBDataBase.Projects.Columns.ETEIGHT + " VARCHAR(30), "
				+ProjectRGBDataBase.Projects.Columns.ETONER + " VARCHAR(30), "
				+ProjectRGBDataBase.Projects.Columns.ETONEG + " VARCHAR(30), "
				+ProjectRGBDataBase.Projects.Columns.ETONEB + " VARCHAR(30), "
				+ProjectRGBDataBase.Projects.Columns.ETTWOR + " VARCHAR(30), "
				+ProjectRGBDataBase.Projects.Columns.ETTWOG + " VARCHAR(30), "
				+ProjectRGBDataBase.Projects.Columns.ETTWOB + " VARCHAR(30), "
				+ProjectRGBDataBase.Projects.Columns.ETTHREER + " VARCHAR(30), "
				+ProjectRGBDataBase.Projects.Columns.ETTHREEG + " VARCHAR(30), "
				+ProjectRGBDataBase.Projects.Columns.ETTHREEB + " VARCHAR(30), "
				+ProjectRGBDataBase.Projects.Columns.ETFOURR + " VARCHAR(30), "
				+ProjectRGBDataBase.Projects.Columns.ETFOURG + " VARCHAR(30), "
				+ProjectRGBDataBase.Projects.Columns.ETFOURB + " VARCHAR(30), "
				+ProjectRGBDataBase.Projects.Columns.ETFIVER + " VARCHAR(30), "
				+ProjectRGBDataBase.Projects.Columns.ETFIVEG + " VARCHAR(30), "
				+ProjectRGBDataBase.Projects.Columns.ETFIVEB + " VARCHAR(30), "
				+ProjectRGBDataBase.Projects.Columns.ETROTATIONTIME + " VARCHAR(30), "
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
