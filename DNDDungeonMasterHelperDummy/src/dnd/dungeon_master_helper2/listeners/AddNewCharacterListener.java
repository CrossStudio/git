package dnd.dungeon_master_helper2.listeners;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import dnd.dungeon_master_helper2.DBHelper;
import dnd.dungeon_master_helper2.DNDCharacter;
import dnd.dungeon_master_helper2.Power;
import dnd.dungeon_master_helper2.activities.CharacterCreationActivity;
import dnd.dungeon_master_helper2.activities.EncounterLobbyActivity;
import dnd.dungeon_master_helper2.activities.MainActivity;

public class AddNewCharacterListener implements OnClickListener {

	Activity activity;
	DBHelper dbHelper;
	
	SharedPreferences prefs;
	DNDCharacter currentCharacter;
	
	public static final String CREATION_FROM_MAIN = "main";
	public static final String CREATION_FROM_LOBBY = "lobby";
	
	public static String creationMode = "";
	
	public AddNewCharacterListener(){
		
	}
	
	public AddNewCharacterListener(String creationMode){
		this.creationMode = creationMode;
	}
	
	@Override
	public void onClick(View v) {
		activity = (Activity) v.getContext();
		
		currentCharacter = CharacterCreationActivity.currentCharacter;
		
		fillCharacterParameters();
		
		dbHelper = new DBHelper(activity);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		if (dbHelper.checkForDuplicates(currentCharacter.getCharName(), db)){
			addNewCharacterToGame(currentCharacter);
			
			dbHelper.saveCharactersToDB(db);
			dbHelper.close();
			
			DNDCharacter.removeDummyCharacter();
			
			Intent intent;
			
			if (creationMode.equals(CREATION_FROM_MAIN)){
				intent = new Intent(activity, MainActivity.class);
			}
			else {
				intent = new Intent(activity, EncounterLobbyActivity.class);
			}
			activity.startActivity(intent);
		}
		else {
			Toast.makeText(activity, currentCharacter.getCharName() + " is already playing", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Adds new currentCharacter with passed parameters to the game
	 * @param charName - name of the currentCharacter
	 * @param charClass - class of the currentCharacter
	 * @param charInitiative - encounter initiative of the currentCharacter
	 * @param charMaxHP - maximum health points of the currentCharacter
	 */
	private void addNewCharacterToGame(DNDCharacter character) {	
		DNDCharacter.addNewCharacterToGame(character);
		if (creationMode.equals(CREATION_FROM_MAIN)){
			DNDCharacter.getSelectedCharacters().add(character);
		}
		Toast.makeText(activity, character.getCharName() + " created", Toast.LENGTH_SHORT).show();
	}

	private void fillCharacterParameters(){
		setCharacterParameters();
		saveCharacterParameters();
	}
	
	/**
	 * Sets current currentCharacter parameters
	 */
	private void setCharacterParameters() {
		currentCharacter.setCharName(CharacterCreationActivity.etCharName.getText().toString());
		currentCharacter.setCharClass(CharacterCreationActivity.spCharClass.getSelectedItem().toString());
		if (CharacterCreationActivity.etMaxHP.getText().length() > 0){
			currentCharacter.setCharHPMax(Integer.parseInt(CharacterCreationActivity.etMaxHP.getText().toString()));
			currentCharacter.setCharHPCurrent(currentCharacter.getCharHPMax());
			currentCharacter.setCharBloodiedValue(currentCharacter.getCharHPMax() / 2);
		}
		if (CharacterCreationActivity.etCurrentInitiative.getText().length() > 0){
			currentCharacter.setCharInitiativeEncounter(Integer.parseInt(CharacterCreationActivity.etCurrentInitiative.getText().toString()));
		}
	}

	/**
	 * Saves currentCharacter's parameters in Preferences
	 */
	private void saveCharacterParameters() {
		prefs = activity.getSharedPreferences("CurrentCharacter", activity.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putString("charName", currentCharacter.getCharName());
		editor.putString("charClass", currentCharacter.getCharClass());
		editor.putInt("charInit", currentCharacter.getCharInitiativeEncounter());
		editor.putInt("charMaxHP", currentCharacter.getCharHPMax());
		
		saveCharacterPowers(editor);
		
		editor.commit();
	}

	/**
	 * Saves currentCharacter's powers to Preferences in form "power\i" where \i is power's index in currentCharacter's list of powers
	 * @param editor Editor object that edits preferences
	 */
	private void saveCharacterPowers(Editor editor) {
		ArrayList<Power> powers = currentCharacter.getCharPowers();
		for (int i = 0; i < powers.size(); i++){
			Power power = powers.get(i);
			editor.putString("powerTitle" + i, power.getTitle());
			editor.putString("powerType" + i, power.getType().toString());
			editor.putInt("powerAmount" + i, power.getMaxAmount());
		}
	}
	
}
