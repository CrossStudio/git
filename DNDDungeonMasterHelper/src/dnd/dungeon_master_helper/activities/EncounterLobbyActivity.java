package dnd.dungeon_master_helper.activities;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.camera2.CameraManager.AvailabilityCallback;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import dnd.dungeon_master_helper.DBHelper;
import dnd.dungeon_master_helper.DNDCharacter;
import dnd.dungeon_master_helper.Power;
import dnd.dungeon_master_helper.R;
import dnd.dungeon_master_helper.listeners.GoToCharacterCreationListener;
import dnd.dungeon_master_helper.listeners.GoToMainActivityClickListener;

public class EncounterLobbyActivity extends Activity {

	Button btnAddCharacter;
	Button btnStartEncounter;
	
	ListView lvAvailableCharacters;
	static ListView lvSelectedCharacters;
	
	DBHelper dbHelper;
	
	@Override
	protected void onCreate(Bundle savedBundle){
		super.onCreate(savedBundle);
		setContentView(R.layout.encounter_lobby);
		
		loadCharactersFromDB();
		
		initializeViews();
		
		ArrayList<DNDCharacter> availableCharacters = DNDCharacter.getNotSelectedCharacters();
		
		availableCharacters.clear();
		
		availableCharacters.addAll(DNDCharacter.getAllCharacters());
		
		refreshAvailableCharactersList();
		
		DNDCharacter.getSelectedCharacters().clear();
		refreshSelectedCharactersList();
		
		
	}

	/**
	 * Loads all saved characters from the database
	 */
	private void loadCharactersFromDB() {
		DNDCharacter.getAllCharacters().clear();
		
		dbHelper = new DBHelper(this);
		
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		Cursor cursor = db.query("characters",null,null,null,null,null,null);
		
		if (cursor.moveToFirst()){
			int idColIndex = cursor.getColumnIndex("id");
			int idClassIndex = cursor.getColumnIndex("class");
			int idNameIndex = cursor.getColumnIndex("name");
			int idMaxHPIndex = cursor.getColumnIndex("maxhp");
			int idCurrentHPIndex = cursor.getColumnIndex("currenthp");
			int idModifiersIndex = cursor.getColumnIndex("modifiers");
			do {
				ArrayList<String> modifiers = new ArrayList<>();
				String longStringOfModifiers = cursor.getString(idModifiersIndex);
				if (longStringOfModifiers != null){
					modifiers.addAll(Arrays.asList(longStringOfModifiers.split(" \\n")));
				}
				DNDCharacter.addNewCharacterToGame(cursor.getString(idNameIndex), cursor.getString(idClassIndex), cursor.getInt(idMaxHPIndex), 
						cursor.getInt(idCurrentHPIndex), modifiers);
			}
			while (cursor.moveToNext());
		}
	}
	
	/**
	 * Save changes made to selected characters to the database
	 */
	private void saveCharactersToDB(){
		dbHelper = new DBHelper(this);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		
		
		
		ArrayList<DNDCharacter> selectedCharacters = DNDCharacter.getSelectedCharacters();
		
		for (DNDCharacter character : selectedCharacters){
			ContentValues cvToCharactersTable = fillContentValuesCharacters(character);
			ContentValues cvToPowersTable = fillContentValuesPowers(character);
			int updCount = db.update("characters", cvToCharactersTable, "name = ?", new String[] {character.getCharName()});
			if (updCount == 0){
				long rowID = db.insert("characters", null, cvToCharactersTable);
				Log.d("myLog", "Inserted new row, number: " + rowID);
			}
		}
	}
	


	/**
	 * Put all the data that will be written to database table Characters into ContentValues object and return it
	 * @param character - character whose stats will be written to the ContentValues object
	 * @return - ContentValues object containing info about one character that will be written to the DB
	 */
	private ContentValues fillContentValuesCharacters(DNDCharacter character) {
		ContentValues cvToCharactersTable = new ContentValues();
		cvToCharactersTable.put("class", character.getCharClass());
		cvToCharactersTable.put("name", character.getCharName());
		cvToCharactersTable.put("maxhp", character.getCharHPMax());
		cvToCharactersTable.put("currenthp",character.getCharHPCurrent());
		cvToCharactersTable.put("init", character.getCharInitiativeEncounter());
		StringBuilder modifiers = new StringBuilder("");
		for (String modifier : character.getListOfAppliedModifiers()){
			if (character.getListOfAppliedModifiers().indexOf(modifier) == 0){
				modifiers.append(modifier);
			}
			else {
				modifiers.append("\n" + modifier);
			}
		}
		cvToCharactersTable.put("modifiers", modifiers.toString());
		return cvToCharactersTable;
	}

	
	private ContentValues fillContentValuesPowers(DNDCharacter character) {
		ContentValues cvToPowersTable = new ContentValues();
		ArrayList<Power> charPowers = character.getCharPowers();
		for (int i = 0; i < charPowers.size(); i++){
			Power power = charPowers.get(i);
			cvToPowersTable.put("title", power.getTitle());
			cvToPowersTable.put("type", power.getType().toString());
			cvToPowersTable.put("maxamount", power.getMaxAmount());
			cvToPowersTable.put("encamount", power.getCurrentAmount());
		}
		return cvToPowersTable;
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
		
		final List<Map<String,String>> listCharactersDataToFillList = new ArrayList<>();
		Map<String, String> mapCharacterData;
		
		String[] takeDataFromKey = {"name", "class"};
		int[] writeDataToKey = {R.id.tvAvCharacterName, R.id.tvAvCharacterClass};
		
		final ArrayList<DNDCharacter> availableCharacters = DNDCharacter.getNotSelectedCharacters();
		
		for (DNDCharacter character : availableCharacters){
			mapCharacterData = new HashMap<>();
			mapCharacterData.put("name", character.getCharName());
			mapCharacterData.put("class", "(" + character.getCharClass() + ")");
			listCharactersDataToFillList.add(mapCharacterData);
		}
		
		final SimpleAdapter adapter = new SimpleAdapter(this, listCharactersDataToFillList, R.layout.available_character_item, takeDataFromKey, writeDataToKey);
		lvAvailableCharacters.setAdapter(adapter);
		lvAvailableCharacters.setOnItemClickListener(new OnItemClickListener(){
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {
				int idCharacterClicked = lvAvailableCharacters.getPositionForView(v);
				Log.d("myLog", "Id of clicked view = " + idCharacterClicked);
				DNDCharacter clickedCharacter = availableCharacters.get(idCharacterClicked);

				addCharacterToSelectedCharactersList(clickedCharacter);
				availableCharacters.remove(clickedCharacter);
				listCharactersDataToFillList.remove(idCharacterClicked);
				adapter.notifyDataSetChanged();

			}
			
		});
		lvAvailableCharacters.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		
		/**
		 * Adds context action bar upon long click on the list item. CAB lets you delete multiple list items at once
		 * both from the list and from the database
		 */
		lvAvailableCharacters.setMultiChoiceModeListener(new MultiChoiceModeListener(){
			
			private ArrayList<Integer> listOfIndicesForDeletion = new ArrayList<>();
			private String[] arrayOfCharacterNamesForDeletion;
			
			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
				// Respond to clicks on the actions in the CAB
		        fillArrayOfCharacterNamesForDeletion();
		        
		        switch (item.getItemId()) {
		            case R.id.action_delete:
		            	deleteSelectedItems();
		                mode.finish(); // Action picked, so close the CAB
		                return true;
		            default:
		                return false;
		        }
			}
			/**
			 * Populates an array of names of characters that will be deleted
			 */
			private void fillArrayOfCharacterNamesForDeletion() {
				arrayOfCharacterNamesForDeletion = new String[listOfIndicesForDeletion.size()];
				int counter = 0;
				for (int i : listOfIndicesForDeletion){
					arrayOfCharacterNamesForDeletion[counter] = availableCharacters.get(i).getCharName();
					counter++;
				}
			}
			/**
			 * Removes selected items from the list of available characters as well as from the database
			 */
			private void deleteSelectedItems() {
				removeItemsFromList();
				removeItemsFromDatabase();
			}

			/**
			 * Removes selected items from the list
			 */
			private void removeItemsFromList() {
				Collections.sort(listOfIndicesForDeletion);
				for (int i = listOfIndicesForDeletion.size() - 1; i >= 0 ; i--){
					int indexOfCharacterToDelete = listOfIndicesForDeletion.get(i);
					DNDCharacter characterToDelete = availableCharacters.get(indexOfCharacterToDelete);
					listCharactersDataToFillList.remove(indexOfCharacterToDelete);
					Log.d("myLog", characterToDelete + " will be deleted from all characters list");
					DNDCharacter.getAllCharacters().remove(characterToDelete);
					availableCharacters.remove(characterToDelete);
				}
				listOfIndicesForDeletion.clear();
				((SimpleAdapter) lvAvailableCharacters.getAdapter()).notifyDataSetChanged();
				
			}
			
			/**
			 * Removes selected items from the database
			 */
			private void removeItemsFromDatabase() {
				dbHelper = new DBHelper(getBaseContext());
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				int numRows = db.delete("characters", "name IN (" + new String(new char[arrayOfCharacterNamesForDeletion.length-1]).replace("\0","?,") + "?)",
						arrayOfCharacterNamesForDeletion);
				Log.d("myLog", "Number of rows deleted = " + numRows);
			}
			
			@Override
			public boolean onCreateActionMode(ActionMode mode, Menu menu) {
				MenuInflater inflater = mode.getMenuInflater();
		        inflater.inflate(R.menu.encounter_lobby_action_bar, menu);
		        return true;
			}

			@Override
			public void onDestroyActionMode(ActionMode mode) {
				for (int i=0; i < lvAvailableCharacters.getChildCount(); i++){
					lvAvailableCharacters.getChildAt(i).setAlpha(1);
				}
				listOfIndicesForDeletion.clear();
			}

			@Override
			public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
				int firstPosition = lvAvailableCharacters.getFirstVisiblePosition();
				View selectedView = lvAvailableCharacters.getChildAt(position - firstPosition);

				if (checked){
					selectedView.setAlpha(0.3f);
					listOfIndicesForDeletion.add(position);
				}
				else {
					selectedView.setAlpha(1);
					listOfIndicesForDeletion.remove((Object)position);
				}
			}
			
		});
	}

	/**
	 * Adds chosen character to the list of selected characters
	 * @param clickedCharacter
	 */
	private void addCharacterToSelectedCharactersList(DNDCharacter clickedCharacter) {
		ArrayList<DNDCharacter> selectedCharacters = DNDCharacter.getSelectedCharacters();
		selectedCharacters.add(clickedCharacter);
		clickedCharacter.setSelected(true);
		refreshSelectedCharactersList();
	}
	
	/**
	 * Fills the ListView which holds all selected characters
	 */
	private void refreshSelectedCharactersList(){
		List<Map<String,String>> listCharactersDataToFillList = new ArrayList<>();
		Map<String, String> mapCharacterData;
		
		ArrayList<DNDCharacter> selectedCharacters = DNDCharacter.getSelectedCharacters();
		
		String[] takeDataFromKey = {"name", "class", "current HP", "initiative"};
		int[] writeDataToKey = {R.id.etSelCharacterName, R.id.tvSelCharacterClass, R.id.etSelCharacterHP, R.id.etSelCharacterInit};
		
		for (DNDCharacter selectedCharacter : selectedCharacters){
			mapCharacterData = new HashMap<>();
			mapCharacterData.put("name", selectedCharacter.getCharName());
			mapCharacterData.put("class", "(" + selectedCharacter.getCharClass() + ")");
			mapCharacterData.put("current HP", ""+selectedCharacter.getCharHPCurrent());
			mapCharacterData.put("initiative", ""+selectedCharacter.getCharInitiativeEncounter());
			listCharactersDataToFillList.add(mapCharacterData);
		}
		
		SimpleAdapter adapter = new SimpleAdapter(this, listCharactersDataToFillList, R.layout.selected_character_item, takeDataFromKey, writeDataToKey);
		lvSelectedCharacters.setAdapter(adapter);
		
	}
	
	/**
	 * Makes a copy of a corresponding character and adds it to the selected characters list
	 * @param v - clicked view
	 */
	public void copyCharacterClickHandler(View v){
		
		LinearLayout vwParentRow = (LinearLayout)v.getParent();
		
		DNDCharacter clickedCharacter = DNDCharacter.getSelectedCharacters().get(lvSelectedCharacters.getPositionForView(vwParentRow));
		
		String name = clickedCharacter.getCharName();
		String charClass = clickedCharacter.getCharClass();
		int charMaxHP = clickedCharacter.getCharHPMax();
		int charCurrentHP = clickedCharacter.getCharHPCurrent();
		ArrayList<String> modifiers = clickedCharacter.getListOfAppliedModifiers();
		
		DNDCharacter newCharacter = DNDCharacter.addNewCharacterToGame(name, charClass, charMaxHP, charCurrentHP, modifiers);
		
		ArrayList<DNDCharacter> selectedCharacters = DNDCharacter.getSelectedCharacters();
		selectedCharacters.add(newCharacter);
		
		refreshSelectedCharactersList();
	}
	
	/**
	 * Removes character from the selected characters list
	 * @param v - remove button of the character that will be removed
	 */
	public void removeCharacterClickHandler(View v){
		LinearLayout vwParentRow = (LinearLayout)v.getParent();
		
		DNDCharacter clickedCharacter = DNDCharacter.getSelectedCharacters().get(lvSelectedCharacters.getPositionForView(vwParentRow));
		
		ArrayList<DNDCharacter> selectedCharacters = DNDCharacter.getSelectedCharacters();
		ArrayList<DNDCharacter> availableCharacters = DNDCharacter.getNotSelectedCharacters();
		
		selectedCharacters.remove(clickedCharacter);
		availableCharacters.add(clickedCharacter);
		refreshAvailableCharactersList();
		refreshSelectedCharactersList();
	}
	
	@Override
	public void onPause(){
		super.onPause();
		refreshSelectedCharacterParams();
		saveCharactersToDB();
	}
	
	/**
	 * Sets parameters of all the selected characters to corresponding values in layout list view
	 */
	public static void refreshSelectedCharacterParams() {
		for (int i = 0; i < lvSelectedCharacters.getChildCount(); i++){
			View listItem = lvSelectedCharacters.getChildAt(i);
			EditText etSelCharacterName = (EditText) listItem.findViewById(R.id.etSelCharacterName);
			EditText etSelCharacterHP = (EditText) listItem.findViewById(R.id.etSelCharacterHP);
			EditText etSelCharacterInit = (EditText) listItem.findViewById(R.id.etSelCharacterInit);
			
			DNDCharacter character = DNDCharacter.getSelectedCharacters().get(i);
			character.setCharName(etSelCharacterName.getText().toString());
			character.setCharHPCurrent(Integer.valueOf(etSelCharacterHP.getText().toString()));
			character.setCharInitiativeEncounter(Integer.valueOf(etSelCharacterInit.getText().toString()));
		}
	}
}
