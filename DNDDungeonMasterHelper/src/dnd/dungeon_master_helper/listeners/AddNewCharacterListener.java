package dnd.dungeon_master_helper.listeners;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.sax.StartElementListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import dnd.dungeon_master_helper.DBHelper;
import dnd.dungeon_master_helper.DNDCharacter;
import dnd.dungeon_master_helper.Power;
import dnd.dungeon_master_helper.R;
import dnd.dungeon_master_helper.activities.CharacterCreationActivity;
import dnd.dungeon_master_helper.activities.EncounterLobbyActivity;

public class AddNewCharacterListener implements OnClickListener {

	Activity activity;
	DBHelper dbHelper;
	
	@Override
	public void onClick(View v) {
		activity = (Activity) v.getContext();
		
		DNDCharacter character = CharacterCreationActivity.currentCharacter;
		
		addNewCharacterToGame(character);
		
		Intent intent = new Intent(activity, EncounterLobbyActivity.class);
		activity.startActivity(intent);
		}

	/**
	 * Adds new character with passed parameters to the game
	 * @param charName - name of the character
	 * @param charClass - class of the character
	 * @param charInitiative - encounter initiative of the character
	 * @param charMaxHP - maximum health points of the character
	 */
	private void addNewCharacterToGame(DNDCharacter character) {
		if (checkForDuplicates(character.getCharName())){			
			DNDCharacter newCharacter = DNDCharacter.addNewCharacterToGame(character);
			saveNewCharacterToDB(newCharacter);
			saveCharacterPowersToDB(newCharacter);
			Toast.makeText(activity, character.getCharName() + " created", Toast.LENGTH_SHORT).show();
		}
		else {
			Toast.makeText(activity, character.getCharName() + " is already playing", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Checks whether the new character has the same name as some character in the database
	 * @param newCharacter - new character, whose name will be checked against the database
	 * @return true if the new character's name is unique; false - otherwise
	 */
	private boolean checkForDuplicates(String charName) {
		dbHelper = new DBHelper(activity);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
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

	/**
	 * Adds newly created character to the database
	 */
	private void saveNewCharacterToDB(DNDCharacter character) {
		ContentValues cv = new ContentValues();
		
		cv.put("class", character.getCharClass());
		cv.put("name", character.getCharName());
		cv.put("maxhp", character.getCharHPMax());
		cv.put("currenthp",character.getCharHPCurrent());
		String modifiers = "";
		for (String modifier : character.getListOfAppliedModifiers()){
			if (character.getListOfAppliedModifiers().indexOf(modifier) == 0){
				modifiers += modifier;
			}
			else {
				modifiers += "\n" + modifier;
			}
		}
		cv.put("modifiers", modifiers);
		
		dbHelper = new DBHelper(activity);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		long rowID = db.insert("characters", null, cv);
		Log.d("myLog", "Row number: " + rowID);
	}
	
	private void saveCharacterPowersToDB(DNDCharacter newCharacter) {
		fillContentValuesPowers(newCharacter);
	}
	/**
	 * Put all the data that will be written to database table Power into ContentValues object and return it
	 * @param character - character whose stats will be written to the ContentValues object
	 * @return - ContentValues object containing info about one character that will be written to the DB
	 */
	private void fillContentValuesPowers(DNDCharacter character) {
		ContentValues cvToPowersTable = null;
		ArrayList<Power> charPowers = character.getCharPowers();

		dbHelper = new DBHelper(activity);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
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
			db.insert("powers", null, cvToPowersTable);
		}
	}

}
