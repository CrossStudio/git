package dnd.dungeon_master_helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

	final static int DB_VERSION = 2;
	
	public DBHelper (Context context){
		super(context,"myDB", null, DB_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table characters (id integer primary key autoincrement, class text, name text, maxhp integer, currenthp integer, modifiers text, init integer);");
		db.execSQL("create table powers (id integer primary key autoincrement, title text, type text, maxamount integer, encamount integer);");
		Log.d("myLog", "New database has been created!");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d("myLog", " --- onUpgrade database from " + oldVersion
		          + " to " + newVersion + " version --- ");
		if (oldVersion == 1 && newVersion == 2){
			
			db.beginTransaction();
			try {
				db.execSQL("alter table characters add column class text;");
				db.execSQL("alter table characters add column maxhp integer;");
				db.execSQL("alter table characters add column currenthp integer;");
				db.setTransactionSuccessful();
			}
			finally {
				db.endTransaction();
			}
			
		}
	}
	@Override
	public void onOpen(SQLiteDatabase db){
		super.onOpen(db);
		Log.d("myLog", "onOpen");
	}

}
