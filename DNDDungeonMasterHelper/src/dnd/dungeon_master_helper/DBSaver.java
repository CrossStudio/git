package dnd.dungeon_master_helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DBSaver implements Runnable {

	DBHelper dbHelper;
	Thread saverThread;
	
	public DBSaver(Context context){
		saverThread = new Thread(this);
		dbHelper = new DBHelper(context);
		saverThread.start();
	}
	
	public Thread getThread(){
		return saverThread;
	}
	
	@Override
	public void run() {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		dbHelper.saveCharactersToDB(db);
	}

}
