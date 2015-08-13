package dnd.dungeon_master_helper2;

import java.util.ArrayList;
import java.util.Arrays;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import dnd.dungeon_master_helper2.activities.MainActivity;

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
				+ " currenthplog text, modifiers text, initiative integer);");
		db.execSQL("create table powers (id integer primary key autoincrement, "
				+ "characterid integer, title text, type text, maxamount integer, encamount integer);");
		db.execSQL("create table encounter (id integer primary key autoincrement, characters text, activecharacter text);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
	
	@Override
	public void onOpen(SQLiteDatabase db){
		super.onOpen(db);
	}
	
	public synchronized void saveCharactersToDB(SQLiteDatabase db){
		for (DNDCharacter character : characters){
			db.beginTransaction();
				saveCharacterParamsToDB(character, db);
			db.setTransactionSuccessful();
			db.endTransaction();
			db.beginTransaction();
				saveCharacterPowersToDB(character, db);
			db.setTransactionSuccessful();
			db.endTransaction();
		}
	}
	
	private synchronized void saveCharacterParamsToDB(DNDCharacter character, SQLiteDatabase db){
		String modifiers = "";
		for (String modifier : character.getListOfAppliedModifiers()){
			modifiers += modifier;
		}
		
		String currentHPLog = "";
		for (String logItem : character.getCharHPChanges()){
			if (character.getCharHPChanges().indexOf(logItem) == 0){
				currentHPLog += logItem;
			}
			else {
				currentHPLog  += "\n" + logItem;
			}

		}
		
		ContentValues cv = new ContentValues();
		cv.put("class", character.getCharClass());
		cv.put("name", character.getCharName());
		cv.put("maxhp", character.getCharHPMax());
		cv.put("currenthp",character.getCharHPCurrent());
		cv.put("modifiers", modifiers);
		cv.put("currenthplog", currentHPLog);
		cv.put("initiative", character.getCharInitiativeEncounter());
		
		int updCount = db.update("characters", cv, "name = ?", new String[] {character.getCharName()});
		if (updCount == 0){
			long rowID = db.insertOrThrow("characters", null, cv);
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
		cursor.close();
		
		cvToPowersTable = new ContentValues();
		for (int i = 0; i < charPowers.size(); i++){
			Power power = charPowers.get(i);
			cvToPowersTable.put("characterid", characterID);
			cvToPowersTable.put("title", power.getTitle());
			cvToPowersTable.put("type", power.getType().toString());
			cvToPowersTable.put("maxamount", power.getMaxAmount());
			cvToPowersTable.put("encamount", power.getCurrentAmount());
			int numRows = db.update("powers", cvToPowersTable, 
					"powers.title = ? and powers.characterid = ?", new String[]{power.getTitle(), ""+characterID});
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
			cursor.close();
			return true;
		}
		else {
			cursor.close();
			return false;
		}
		
	}

	public synchronized void saveCurrentEncounter(ArrayList<DNDCharacter> characters, DNDCharacter activeCharacter, SQLiteDatabase db){
		if (characters != null && characters.size() > 0){
			putEncounterInfoToDB(characters, activeCharacter, db);
		}
	}
	

	//TODO
	private String[] getCharactersIDs(ArrayList<DNDCharacter> characters, SQLiteDatabase db) {
		
		String [] charactersNames = new String[characters.size()];
		
		String placeholders = "";
		
		for (int i = 0; i < characters.size(); i++){
			charactersNames[i] = characters.get(i).getCharName();
			if (i == 0){
				placeholders += "?";
			}
			else {
				placeholders += ",?";
			}
		}
		
		String sqlQuery = "select characters.id "
				+ "from characters "
				+ "where characters.name in (" + placeholders +")";
		
		Cursor cursor = db.rawQuery(sqlQuery, charactersNames);
		
		String[] charactersIDs = new String[characters.size()];
		
		if (cursor.moveToFirst()){
			int count = 0;
			do {
				charactersIDs[count++] = cursor.getString(0);
			} while (cursor.moveToNext());
		}
		return charactersIDs;
	}

	private void putEncounterInfoToDB(ArrayList<DNDCharacter> characters, DNDCharacter activeCharacter, SQLiteDatabase db) {
		String listOfCharacters = "";
		for (DNDCharacter character : characters){
			if (characters.indexOf(character) != 0){
				listOfCharacters += "\n" + character.getCharName();
			}
			else {
				listOfCharacters += character.getCharName();
			}
		}
		ContentValues cv = new ContentValues();
		cv.put("characters", listOfCharacters);
		cv.put("activecharacter", activeCharacter.getCharName());
		
		long rowID = db.insertOrThrow("encounter", null, cv);
	}
	
	public synchronized void loadAllCharactersFromDB(SQLiteDatabase db){
		db.beginTransaction();
		loadAllCharactersParamsFromDB(db);
		db.setTransactionSuccessful();
		db.endTransaction();
		db.beginTransaction();
		loadAllCharactersPowersFromDB(db);
		db.setTransactionSuccessful();
		db.endTransaction();
	}
 
	private synchronized void loadAllCharactersParamsFromDB(SQLiteDatabase db) {
		characters.clear();
		
		Cursor cursor = db.query("characters",null,null,null,null,null,null);
		
		if (cursor.moveToFirst()){
			int idColIndex = cursor.getColumnIndex("id");
			int idClassIndex = cursor.getColumnIndex("class");
			int idNameIndex = cursor.getColumnIndex("name");
			int idMaxHPIndex = cursor.getColumnIndex("maxhp");
			int idCurrentHPIndex = cursor.getColumnIndex("currenthp");
			int idCurrentHPLog = cursor.getColumnIndex("currenthplog");
			int idModifiersIndex = cursor.getColumnIndex("modifiers");
			int idInit = cursor.getColumnIndex("initiative");
			do {
				ArrayList<String> modifiers = new ArrayList<>();
				String longStringOfModifiers = cursor.getString(idModifiersIndex);

				if (longStringOfModifiers != null){
					modifiers.addAll(Arrays.asList(longStringOfModifiers.split(" \\n")));
				}
				
				ArrayList<String> currentHPLog = new ArrayList<>();
				String longStringOfLogs = cursor.getString(idCurrentHPLog);

				if (longStringOfLogs != null){
					currentHPLog.addAll(Arrays.asList(longStringOfLogs.split(" \\n")));
				}
				
				DNDCharacter.addNewCharacterToGame(cursor.getString(idNameIndex), 
						cursor.getString(idClassIndex), cursor.getInt(idMaxHPIndex), 
						cursor.getInt(idCurrentHPIndex), cursor.getInt(idInit), modifiers, currentHPLog);
			}
			while (cursor.moveToNext());
		}
		cursor.close();
	}

	private synchronized void loadAllCharactersPowersFromDB(SQLiteDatabase db) {
		
		String sqlQuery = 
				"select characters.id, characters.name, powers.title, powers.type, powers.maxamount, powers.encamount "
				+ "from powers "
				+ "inner join characters "
				+ "on powers.characterid = characters.id ";
		
		Cursor cursor = db.rawQuery(sqlQuery, null);
		
		if (cursor.moveToFirst()){
			do {
				for (DNDCharacter character : characters){
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
		cursor.close();
	}

	/**
	 * Loads characters that have been in the last encounter from database
	 * @param db - database to look the characters in
	 * @return arraylist of characters in loaded encounter with active character being the first item in the list
	 */
	public ArrayList<DNDCharacter> loadEncounterFromDB(SQLiteDatabase db){
		ArrayList<String> charactersNames = getEncounterCharactersNamesFromDB(db);
		if (charactersNames == null || charactersNames.size() == 0){
			return null;
		}
		return loadEncounterCharacters(charactersNames, db);
	}
	
	private ArrayList<String> getEncounterCharactersNamesFromDB(SQLiteDatabase db) {
		String sqlQuery = "select encounter.activecharacter, encounter.characters "
				+ "from encounter ";
		Cursor cursor = db.rawQuery(sqlQuery, null);
		
		ArrayList<String> charactersNames = new ArrayList<>();
		
		if (cursor.moveToFirst()){
			charactersNames.add(cursor.getString(0));
			ArrayList<String> dummyList = breakLongStringOfNames(cursor.getString(1));
			if (dummyList != null){
				charactersNames.addAll(dummyList);
			}
		}
		
		return charactersNames;
	}

	private ArrayList<String> breakLongStringOfNames(String  longStringOfNames) {
		if (longStringOfNames != null && longStringOfNames.length() > 0){
			return new ArrayList<String>(Arrays.asList(longStringOfNames.split("\\n")));
		}
		return null;
	}

	private ArrayList<DNDCharacter> loadEncounterCharacters(ArrayList<String> charactersNames, SQLiteDatabase db) {
		ArrayList<DNDCharacter> encounterCharacters = new ArrayList<DNDCharacter>();
		
		ArrayList<DNDCharacter> allCharacters = DNDCharacter.getAllCharacters();
		
		for (DNDCharacter encounterCharacter : allCharacters){
			for (String dbCharacterName : charactersNames.subList(1, charactersNames.size())){
				if (encounterCharacter.getCharName().equals(dbCharacterName)){
					encounterCharacters.add(encounterCharacter);
					if (dbCharacterName.equals(charactersNames.get(0))){
						MainActivity.activeCharacter = encounterCharacter;
					}
				}
			}
		}
		/**
		 * Check for the situation when one or more of the encounter characters have been removed from characters
		 * but still exists in the encounter table of db
		 */
		if (encounterCharacters.size() != charactersNames.size() - 1){
			clearEncounter(db);
			return null;
		}
		return encounterCharacters;
	}

	/**
	 * Deletes all the entries in the encounter table
	 * @param db - database to be queried
	 */
	public void clearEncounter(SQLiteDatabase db) {
		int numRows = db.delete("encounter", null, null);
	}

	public synchronized void deleteChosenCharactersFromDB(String[] characters, SQLiteDatabase db){
		int numRows = db.delete("characters", "name IN (" + new String(new char[characters.length-1]).
				replace("\0","?,") + "?)", characters);
		
		getPowersWithNoHost(db);
		
	}

	private void getPowersWithNoHost(SQLiteDatabase db) {
		String sqlQuery = "select powers.id "
				+ "from powers "
				+ "left join characters "
				+ "on powers.characterid = characters.id "
				+ "where characters.id is null ";
		Cursor cursor = db.rawQuery(sqlQuery, null);
		
		
		if (cursor.moveToFirst()){
			do {
				int numRows = db.delete("powers", "powers.id = ?", new String[]{cursor.getInt(0)+""});
			}
			while (cursor.moveToNext());
		}
			
	}
}
