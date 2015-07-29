package dnd.dungeon_master_helper.activities;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
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
import android.widget.TextView;
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
	SQLiteDatabase db;
	
	@Override
	protected void onCreate(Bundle savedBundle){
		super.onCreate(savedBundle);
		setContentView(R.layout.encounter_lobby);		
		
		dbHelper = new DBHelper(this);
		db = dbHelper.getWritableDatabase();
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
				dbHelper = new DBHelper(getBaseContext());
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				dbHelper.deleteChosenCharactersFromDB(arrayOfCharacterNamesForDeletion, db);
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
					DNDCharacter.getAllCharacters().remove(characterToDelete);
					availableCharacters.remove(characterToDelete);
				}
				listOfIndicesForDeletion.clear();
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
	 * Adds chosen currentCharacter to the list of selected characters
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
		int[] writeDataToKey = {R.id.tvSelCharacterName, R.id.tvSelCharacterClass, R.id.etSelCharacterHP, R.id.etSelCharacterInit};
		
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
	 * Makes a copy of a corresponding currentCharacter and adds it to the selected characters list
	 * @param v - clicked view
	 */
	public void copyCharacterClickHandler(View v){
		LinearLayout vwParentRow = (LinearLayout)v.getParent();
		
		DNDCharacter clickedCharacter = DNDCharacter.getSelectedCharacters().get(lvSelectedCharacters.getPositionForView(vwParentRow));
		
		DNDCharacter character = DNDCharacter.getDummyCharacter();
		character.copyCharacter(clickedCharacter);
		
		String newName = addDigitToName(character.getCharName());
		
		if (newName != null){
			character.setCharName(newName);
			DNDCharacter.addNewCharacterToGame(character);
			
			ArrayList<DNDCharacter> selectedCharacters = DNDCharacter.getSelectedCharacters();
			selectedCharacters.add(character);
		}

		DNDCharacter.removeDummyCharacter();
		
		refreshSelectedCharactersList();
	}
	
    
    /**
     * Adds 1 to character's name if original character had no digits at the end of his name
     * or increases the number at the end of his name by 1
     * @param charName - name to be taken as an original
     * @return - altered name with digit added or number incremented
     */
    private String addDigitToName(String charName) {
    	String newName = "";
	    	try {
	    		int digit = Integer.parseInt(charName.substring(charName.length()-1, charName.length()));
	    		newName = charName.substring(0, (charName.length()-1)) + ++digit;
	    		if (checkForDuplicateNames(newName, DNDCharacter.getAllCharacters())){
	    			return newName;
	    		}
	    		else {
	    			Toast.makeText(this, newName + " is already playing", Toast.LENGTH_SHORT).show();
	    			return null;
	    		}
	    	}
	    	catch (NumberFormatException ex) {
	    		newName = charName + "1";
	    		if (checkForDuplicateNames(newName, DNDCharacter.getAllCharacters())){
	    			return newName;
	    		}
	    		else {
	    			Toast.makeText(this, newName + " is already playing", Toast.LENGTH_SHORT).show();
	    			return null;
	    		}
	    	}
	}
	
	private boolean checkForDuplicateNames(String newName, ArrayList<DNDCharacter> allCharacters) {
		for (DNDCharacter character : allCharacters){
			if (character.getCharName().equals(newName)){
				return false;
			}
		}
		return true;
	}
	

	/**
	 * Removes currentCharacter from the selected characters list
	 * @param v - remove button of the currentCharacter that will be removed
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
		
		dbHelper.saveCharactersToDB(db);

	}
	
	public void onResume(){
		super.onResume();

		initializeViews();
		
		ArrayList<DNDCharacter> availableCharacters = DNDCharacter.getNotSelectedCharacters();
		
		availableCharacters.clear();
		
		availableCharacters.addAll(DNDCharacter.getAllCharacters());
		
		refreshAvailableCharactersList();
		
		DNDCharacter.getSelectedCharacters().clear();
		refreshSelectedCharactersList();
	}


	/**
	 * Sets parameters of all the selected characters to corresponding values in layout list view
	 */
	public static void refreshSelectedCharacterParams() {
		for (int i = 0; i < lvSelectedCharacters.getChildCount(); i++){
			View listItem = lvSelectedCharacters.getChildAt(i);
			TextView tvSelCharacterName = (TextView) listItem.findViewById(R.id.tvSelCharacterName);
			EditText etSelCharacterHP = (EditText) listItem.findViewById(R.id.etSelCharacterHP);
			EditText etSelCharacterInit = (EditText) listItem.findViewById(R.id.etSelCharacterInit);
			
			DNDCharacter character = DNDCharacter.getSelectedCharacters().get(i);
			character.setCharName(tvSelCharacterName.getText().toString());
			character.setCharHPCurrent(Integer.valueOf(etSelCharacterHP.getText().toString()));
			character.setCharInitiativeEncounter(Integer.valueOf(etSelCharacterInit.getText().toString()));
		}
	}
}
