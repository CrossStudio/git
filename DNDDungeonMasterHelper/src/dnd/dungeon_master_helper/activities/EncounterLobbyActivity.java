package dnd.dungeon_master_helper.activities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import dnd.dungeon_master_helper.DBHelper;
import dnd.dungeon_master_helper.DNDCharacter;
import dnd.dungeon_master_helper.R;
import dnd.dungeon_master_helper.listeners.GoToCharacterCreationListener;
import dnd.dungeon_master_helper.listeners.GoToMainActivityClickListener;

public class EncounterLobbyActivity extends Activity {

	Button btnAddCharacter;
	Button btnStartEncounter;
	
	ListView lvAvailableCharacters;
	ListView lvSelectedCharacters;
	
	
	DBHelper dbHelper;
	
	@Override
	protected void onCreate(Bundle savedBundle){
		super.onCreate(savedBundle);
		setContentView(R.layout.encounter_lobby);
		
		loadCharactersFromDB();
		
		initializeViews();
		
		refreshAvailableCharactersList();
	}

	/**
	 * Loads all saved characters from the database
	 */
	private void loadCharactersFromDB() {
		DNDCharacter.getCharacters().clear();
		
		dbHelper = new DBHelper(this);
		
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Log.d("myLog", "DB version = " + db.getVersion());
		Cursor cursor = db.query("characters",null,null,null,null,null,null);
		
		if (cursor.moveToFirst()){
			int idColIndex = cursor.getColumnIndex("id");
			int idClassIndex = cursor.getColumnIndex("class");
			int idNameIndex = cursor.getColumnIndex("name");
			int idMaxHPIndex = cursor.getColumnIndex("maxhp");
			int idCurrentHPIndex = cursor.getColumnIndex("currenthp");
			int idModifiersIndex = cursor.getColumnIndex("modifiers");
			do {
				Log.d("myLog", "ID = " + cursor.getInt(idColIndex) + ", name = " + cursor.getString(idNameIndex));
				ArrayList<String> modifiers = new ArrayList<>();
				String longStringOfModifiers = cursor.getString(idModifiersIndex);
				if (longStringOfModifiers != null){
					modifiers.addAll(Arrays.asList(longStringOfModifiers.split(" \\n")));
				}
				Log.d("myLog", "Modifiers: " + modifiers);
				DNDCharacter.addNewCharacterToGame(cursor.getString(idNameIndex), cursor.getString(idClassIndex), cursor.getInt(idMaxHPIndex), 
						cursor.getInt(idCurrentHPIndex), modifiers);
			}
			while (cursor.moveToNext());
		}
	}
	
	private void initializeViews() {
		btnAddCharacter = (Button) findViewById(R.id.btnAddCharacter);
		btnStartEncounter = (Button) findViewById(R.id.btnStartEncounter);
		
		btnAddCharacter.setOnClickListener(new GoToCharacterCreationListener());
		btnStartEncounter.setOnClickListener(new GoToMainActivityClickListener());
		
		lvAvailableCharacters = (ListView) findViewById(R.id.lvAvailableCharacters);
		lvSelectedCharacters = (ListView) findViewById(R.id.lvSelectedCharacters);
		
	}
	
	/**
	 * Fills the ListView which holds all available characters with characters previously loaded from the database
	 */
	private void refreshAvailableCharactersList(){
		
		List<Map<String,String>> listCharactersDataToFillList = new ArrayList<>();
		Map<String, String> mapCharacterData;
		
		String[] takeDataFromKey = {"name", "class"};
		int[] writeDataToKey = {R.id.tvAvCharacterName, R.id.tvAvCharacterClass};
		
		for (DNDCharacter character : DNDCharacter.getCharacters()){
			mapCharacterData = new HashMap<>();
			mapCharacterData.put("name", character.getCharName());
			mapCharacterData.put("class", "(" + character.getCharClass() + ")");
			listCharactersDataToFillList.add(mapCharacterData);
		}
		
		SimpleAdapter adapter = new SimpleAdapter(this, listCharactersDataToFillList, R.layout.available_character_item, takeDataFromKey, writeDataToKey);
		lvAvailableCharacters.setAdapter(adapter);
	}

}
