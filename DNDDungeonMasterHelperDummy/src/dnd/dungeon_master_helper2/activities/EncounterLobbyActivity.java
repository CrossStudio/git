package dnd.dungeon_master_helper2.activities;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import dnd.dungeon_master_helper2.DBHelper;
import dnd.dungeon_master_helper2.DNDCharacter;
import dnd.dungeon_master_helper2.R;
import dnd.dungeon_master_helper2.listeners.GoToCharacterCreationListener;
import dnd.dungeon_master_helper2.listeners.GoToMainActivityClickListener;

public class EncounterLobbyActivity extends Activity {

	Button btnAddCharacter;
	Button btnStartEncounter;

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

		lvSelectedCharacters = (ListView) findViewById(R.id.lvSelectedCharacters);
		
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
		refreshAvailableCharacters();
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
		
		refreshAvailableCharacters();
		
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
	
	private void refreshAvailableCharacters(){
		LayoutInflater inflater = getLayoutInflater();
		final LinearLayout llAvailableCharacters = (LinearLayout) findViewById(R.id.llAvailableCharacters);
		llAvailableCharacters.removeAllViews();
		for (DNDCharacter character : DNDCharacter.getNotSelectedCharacters()){
			LinearLayout llCharacterItem = (LinearLayout) inflater.inflate(R.layout.available_character_item, null);
			TextView tvAvCharacterName = (TextView) llCharacterItem.findViewById(R.id.tvAvCharacterName);
			TextView tvAvCharacterClass = (TextView) llCharacterItem.findViewById(R.id.tvAvCharacterClass);
			Button btnAddCharacterToEncounter = (Button) llCharacterItem.findViewById(R.id.btnAddCharacterToEncounter);
			Button btnDeleteFromAvailable = (Button) llCharacterItem.findViewById(R.id.btnDeleteFromAvailable);
			
			tvAvCharacterName.setText(character.getCharName());
			tvAvCharacterClass.setText("(" + character.getCharClass() + ")");
			btnDeleteFromAvailable.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//TODO
					ArrayList<DNDCharacter> availableCharacters = DNDCharacter.getNotSelectedCharacters();
					ArrayList<DNDCharacter> allCharacters = DNDCharacter.getAllCharacters();
					DNDCharacter clickedCharacter = null;
					for (int i = 0; i < llAvailableCharacters.getChildCount(); i++){
						if (v.getParent().equals(llAvailableCharacters.getChildAt(i))){
							clickedCharacter = availableCharacters.get(i);
							availableCharacters.remove(clickedCharacter);
							allCharacters.remove(clickedCharacter);
							dbHelper.deleteChosenCharactersFromDB(new String[]{clickedCharacter.getCharName()}, db);
							refreshAvailableCharacters();
							break;
						}
					}
				}
			});
			
			btnAddCharacterToEncounter.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					ArrayList<DNDCharacter> availableCharacters = DNDCharacter.getNotSelectedCharacters();
					DNDCharacter clickedCharacter = null;
					for (int i = 0; i < llAvailableCharacters.getChildCount(); i++){
						if (v.getParent().equals(llAvailableCharacters.getChildAt(i))){
							clickedCharacter = availableCharacters.get(i);
							addCharacterToSelectedCharactersList(clickedCharacter);
							availableCharacters.remove(clickedCharacter);
							refreshAvailableCharacters();
							break;
						}
					}
					
				}
			});
			
			llAvailableCharacters.addView(llCharacterItem);
		}
	}
	
}
