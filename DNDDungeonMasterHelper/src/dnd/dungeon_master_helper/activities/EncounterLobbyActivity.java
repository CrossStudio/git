package dnd.dungeon_master_helper.activities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
		
		refreshAvailableCharactersList();
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
		
		final List<Map<String,String>> listCharactersDataToFillList = new ArrayList<>();
		Map<String, String> mapCharacterData;
		
		String[] takeDataFromKey = {"name", "class"};
		int[] writeDataToKey = {R.id.tvAvCharacterName, R.id.tvAvCharacterClass};
		
		for (DNDCharacter character : DNDCharacter.getAllCharacters()){
			mapCharacterData = new HashMap<>();
			mapCharacterData.put("name", character.getCharName());
			mapCharacterData.put("class", "(" + character.getCharClass() + ")");
			listCharactersDataToFillList.add(mapCharacterData);
			Log.d("myLog", "Character in the list: " + character.getCharName());
		}
		
		final SimpleAdapter adapter = new SimpleAdapter(this, listCharactersDataToFillList, R.layout.available_character_item, takeDataFromKey, writeDataToKey);
		lvAvailableCharacters.setAdapter(adapter);
		lvAvailableCharacters.setOnItemClickListener(new OnItemClickListener(){
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {
				int idCharacterClicked = lvAvailableCharacters.getPositionForView(v);
				DNDCharacter clickedCharacter = DNDCharacter.getAllCharacters().get(idCharacterClicked);
				if (clickedCharacter.isSelected()){
					Toast.makeText(EncounterLobbyActivity.this, clickedCharacter.getCharName() + " already chosen", Toast.LENGTH_SHORT).show();
				}
				else{
					addCharacterToSelectedCharactersList(clickedCharacter);
				}
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
					arrayOfCharacterNamesForDeletion[counter] = DNDCharacter.getAllCharacters().get(i).getCharName();
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
					listCharactersDataToFillList.remove((int)listOfIndicesForDeletion.get(i));
					DNDCharacter.getAllCharacters().remove(i);
					Log.d("myLog", "Item deleted " + listOfIndicesForDeletion.get(i));
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
				db.delete("characters", "name IN (" + new String(new char[arrayOfCharacterNamesForDeletion.length-1]).replace("\0","?,") + "?)",
						arrayOfCharacterNamesForDeletion);
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
				Log.d("myLog", "Child count = " + lvAvailableCharacters.getChildCount());
				Log.d("myLog", "Item id = " + lvAvailableCharacters);
				Log.d("myLog", "selectedView = " + selectedView);
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
		
		String[] takeDataFromKey = {"name", "class", "current HP", "initiative"};
		int[] writeDataToKey = {R.id.etSelCharacterName, R.id.tvSelCharacterClass, R.id.etSelCharacterHP, R.id.etSelCharacterInit};
		
		for (DNDCharacter selectedCharacter : DNDCharacter.getSelectedCharacters()){
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
				
		Log.d("myLog", "Copy button clicked " + clickedCharacter.getCharName());
	}

	@Override
	public void onPause(){
		super.onPause();
		refreshSelectedCharacterParams();
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
