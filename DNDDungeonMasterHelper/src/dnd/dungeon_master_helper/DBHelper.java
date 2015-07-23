package dnd.dungeon_master_helper;

import java.util.ArrayList;
import java.util.Arrays;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

	final static int DB_VERSION = 2;
	
	ArrayList<DNDCharacter> characters;
	
	public DBHelper (Context context){
		super(context,"myDB", null, DB_VERSION);
		characters = DNDCharacter.getAllCharacters();
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table characters (id integer primary key autoincrement,"
				+ " class text, name text, maxhp integer, currenthp integer,"
				+ "currenthplog integer, modifiers text, initiative integer);");
		db.execSQL("create table powers (id integer primary key autoincrement, "
				+ "characterid integer, title text, type text, maxamount integer, encamount integer);");
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
	
	public synchronized void saveCharactersToDB(SQLiteDatabase db){
		for (DNDCharacter character : characters){
			saveCharacterParamsToDB(character, db);
			saveCharacterPowersToDB(character, db);
		}
	}
	
	private synchronized void saveCharacterParamsToDB(DNDCharacter character, SQLiteDatabase db){

		ContentValues cv = new ContentValues();
		cv.put("class", character.getCharClass());
		cv.put("name", character.getCharName());
		cv.put("maxhp", character.getCharHPMax());
		cv.put("currenthp",character.getCharHPCurrent());
		cv.put("initiative", character.getCharInitiativeEncounter());
		StringBuilder modifiers = new StringBuilder();
		for (String modifier : character.getListOfAppliedModifiers()){
			if (character.getListOfAppliedModifiers().indexOf(modifier) == 0){
				modifiers.append(modifier);
			}
			else {
				modifiers.append("\n" + modifier);
			}
		}
		cv.put("modifiers", modifiers + "");
		
		StringBuilder currentHPLog = new StringBuilder();
		for (String logItem : character.getCharHPChanges()){
			if (character.getCharHPChanges().indexOf(logItem) == 0){
				currentHPLog.append(logItem);
			}
			else {
				currentHPLog.append("\n" + logItem);
			}
		}
		cv.put("currenthplog", currentHPLog + "");
		int updCount = db.update("characters", cv, "name = ?", new String[] {character.getCharName()});
		if (updCount == 0){
			long rowID = db.insert("characters", null, cv);
			Log.d("myLog", "Row number: " + rowID);
		}
		
	}
	
	private synchronized void saveCharacterPowersToDB(DNDCharacter character, SQLiteDatabase db){
		
		ContentValues cvToPowersTable = null;
		ArrayList<Power> charPowers = character.getCharPowers();
		
		Cursor cursor = db.query("characters", null, "name = ?", new String[]{character.getCharName()},
				null, null, null);
		
		int characterID = 0;
		if (cursor.moveToFirst()){
			characterID = cursor.getInt(0);
		}
		
		cvToPowersTable = new ContentValues();
		for (int i = 0; i < charPowers.size(); i++){
			Power power = charPowers.get(i);
			cvToPowersTable.put("characterid", characterID);
			cvToPowersTable.put("title", power.getTitle());
			cvToPowersTable.put("type", power.getType().toString());
			cvToPowersTable.put("maxamount", power.getMaxAmount());
			cvToPowersTable.put("encamount", power.getCurrentAmount());
			int numRows = db.update("powers", cvToPowersTable, "powers.title = ?", new String[]{power.getTitle()});
			if (numRows == 0){
				db.insert("powers", null, cvToPowersTable);
			}
		}

	}
	
	public synchronized boolean checkForDuplicates(String charName, SQLiteDatabase db) {

		String[] columnsToCheck = {"name"};
		String[] nameToCheck = {charName};
		
		Cursor cursor = db.query("characters", columnsToCheck, "name=?", nameToCheck, null, null, null);
		if (cursor.getCount() == 0){
			return true;
		}
		else {
			return false;
		}
		
	}

	
	public synchronized void loadCharactersFromDB(SQLiteDatabase db){
		loadCharactersParamsFromDB(db);
		loadCharactersPowersFromDB(db);
	}
	
	private synchronized void loadCharactersParamsFromDB(SQLiteDatabase db) {
		DNDCharacter.getAllCharacters().clear();
		
		Cursor cursor = db.query("characters",null,null,null,null,null,null);
		
		if (cursor.moveToFirst()){
			int idColIndex = cursor.getColumnIndex("id");
			int idClassIndex = cursor.getColumnIndex("class");
			int idNameIndex = cursor.getColumnIndex("name");
			int idMaxHPIndex = cursor.getColumnIndex("maxhp");
			int idCurrentHPIndex = cursor.getColumnIndex("currenthp");
			int idModifiersIndex = cursor.getColumnIndex("modifiers");
			int idInit = cursor.getColumnIndex("initiative");
			do {
				ArrayList<String> modifiers = new ArrayList<>();
				String longStringOfModifiers = cursor.getString(idModifiersIndex);
				if (longStringOfModifiers != null){
					modifiers.addAll(Arrays.asList(longStringOfModifiers.split(" \\n")));
				}
				DNDCharacter.addNewCharacterToGame(cursor.getString(idNameIndex), 
						cursor.getString(idClassIndex), cursor.getInt(idMaxHPIndex), 
						cursor.getInt(idCurrentHPIndex), cursor.getInt(idInit), modifiers);
			}
			while (cursor.moveToNext());
		}
	}

	private synchronized void loadCharactersPowersFromDB(SQLiteDatabase db) {
		
		String sqlQuery = 
				"select characters.id, characters.name, powers.title, powers.type, powers.maxamount, powers.encamount "
				+ "from powers "
				+ "inner join characters "
				+ "on powers.characterid = characters.id ";
		
		Cursor cursor = db.rawQuery(sqlQuery, null);
		
		if (cursor.moveToFirst()){
			do {
				for (DNDCharacter character : DNDCharacter.getAllCharacters()){
					if (character.getCharName().equals(cursor.getString(1))){
						
						String type = cursor.getString(3);
						
						ArrayList<Power> powers = character.getCharPowers();
						switch (type) {
							case "ATWILL": 
								powers.add(new Power(cursor.getString(2), PowerType.ATWILL, 
										cursor.getInt(4), cursor.getInt(5)));
								break;
							case "ENCOUNTER":
								powers.add(new Power(cursor.getString(2), PowerType.ENCOUNTER, 
										cursor.getInt(4), cursor.getInt(5)));
								break;
							case "DAILY":
								powers.add(new Power(cursor.getString(2), PowerType.DAILY, 
										cursor.getInt(4), cursor.getInt(5)));
								break;
						}
						break;
					}
				}
				
			}
			while (cursor.moveToNext());
		}
		
	}
}
