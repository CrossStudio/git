package dnd.dungeon_master_helper.listeners;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import dnd.dungeon_master_helper.DBHelper;
import dnd.dungeon_master_helper.DNDCharacter;
import dnd.dungeon_master_helper.activities.CharacterCreationActivity;
import dnd.dungeon_master_helper.activities.EncounterLobbyActivity;

public class AddNewCharacterListener implements OnClickListener {

	Activity activity;
	DBHelper dbHelper;
	
	@Override
	public void onClick(View v) {
		activity = (Activity) v.getContext();
		
		DNDCharacter character = CharacterCreationActivity.currentCharacter;
		
		dbHelper = new DBHelper(activity);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		if (dbHelper.checkForDuplicates(character.getCharName(), db)){
			addNewCharacterToGame(character);
			
			dbHelper.saveCharactersToDB(db);
			dbHelper.close();
			
			CharacterCreationActivity.currentCharacter = null;
			
			Intent intent = new Intent(activity, EncounterLobbyActivity.class);
			activity.startActivity(intent);
		}
		else {
			Toast.makeText(activity, character.getCharName() + " is already playing", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Adds new character with passed parameters to the game
	 * @param charName - name of the character
	 * @param charClass - class of the character
	 * @param charInitiative - encounter initiative of the character
	 * @param charMaxHP - maximum health points of the character
	 */
	private void addNewCharacterToGame(DNDCharacter character) {	
		DNDCharacter newCharacter = DNDCharacter.addNewCharacterToGame(character);
		Toast.makeText(activity, character.getCharName() + " created", Toast.LENGTH_SHORT).show();
	}

}
