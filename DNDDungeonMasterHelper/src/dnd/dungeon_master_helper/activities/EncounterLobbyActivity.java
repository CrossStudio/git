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
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
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
		refreshSelectedCharactersList();
		
		
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
		
		final List<Map<String,String>> listCharactersDataToFillList = new ArrayList<>();
		Map<String, String> mapCharacterData;
		
		String[] takeDataFromKey = {"name", "class"};
		int[] writeDataToKey = {R.id.tvAvCharacterName, R.id.tvAvCharacterClass};
		
		for (DNDCharacter character : DNDCharacter.getCharacters()){
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
				int idCharacterClicked = lvAvailableCharacters.indexOfChild(v);
				DNDCharacter clickedCharacter = DNDCharacter.getCharacters().get(idCharacterClicked);
				
				addCharacterToSelectedCharactersList(clickedCharacter);
			}
			
		});
		lvAvailableCharacters.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		lvAvailableCharacters.setMultiChoiceModeListener(new MultiChoiceModeListener(){
			
			private ArrayList<Integer> listOfSelectedViewsForDeletion = new ArrayList<>();
			
			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
				// Respond to clicks on the actions in the CAB
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
			 * Removes selected items from the list of available characters as well as from the database
			 */
			private void deleteSelectedItems() {
				for (int i = listOfSelectedViewsForDeletion.size() - 1; i >= 0 ; i--){
					Collections.sort(listOfSelectedViewsForDeletion);
					listCharactersDataToFillList.remove((int)listOfSelectedViewsForDeletion.get(i));
					Log.d("myLog", "Item deleted " + listOfSelectedViewsForDeletion.get(i));
				}
				listOfSelectedViewsForDeletion.clear();
				((SimpleAdapter) lvAvailableCharacters.getAdapter()).notifyDataSetChanged();
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
				
			}

			@Override
			public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
				View selectedView = lvAvailableCharacters.getChildAt(position);
				if (checked){
					selectedView.setAlpha(0.3f);
					listOfSelectedViewsForDeletion.add(position);
				}
				else {
					selectedView.setAlpha(1);
					listOfSelectedViewsForDeletion.remove((Object)position);
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
		
		refreshSelectedCharactersList();
	}
	
	/**
	 * Fills the ListView which holds all selected characters
	 */
	private void refreshSelectedCharactersList(){
		List<Map<String,String>> listCharactersDataToFillList = new ArrayList<>();
		Map<String, String> mapCharacterData;
		
		String[] takeDataFromKey = {"name", "class", "current HP", "initiative"};
		int[] writeDataToKey = {R.id.tvSelCharacterName, R.id.tvSelCharacterClass, R.id.tvSelCharacterHP, R.id.tvSelCharacterInit};
		
		for (DNDCharacter selectedCharacter : DNDCharacter.getSelectedCharacters()){
			mapCharacterData = new HashMap<>();
			mapCharacterData.put("name", selectedCharacter.getCharName());
			mapCharacterData.put("class", "(" + selectedCharacter.getCharClass() + ")");
			mapCharacterData.put("current HP", "HP: " + selectedCharacter.getCharHPCurrent());
			mapCharacterData.put("initiative", "Initiative: " + selectedCharacter.getCharInitiativeEncounter());
			listCharactersDataToFillList.add(mapCharacterData);
		}
		
		SimpleAdapter adapter = new SimpleAdapter(this, listCharactersDataToFillList, R.layout.selected_character_item, takeDataFromKey, writeDataToKey);
		lvSelectedCharacters.setAdapter(adapter);
	}
}
