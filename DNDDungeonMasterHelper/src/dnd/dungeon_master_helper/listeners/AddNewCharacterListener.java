package dnd.dungeon_master_helper.listeners;

import dnd.dungeon_master_helper.DBHelper;
import dnd.dungeon_master_helper.DNDCharacter;
import dnd.dungeon_master_helper.R;
import dnd.dungeon_master_helper.R.id;
import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddNewCharacterListener implements OnClickListener {

	Activity activity;
	DBHelper dbHelper;
	
	@Override
	public void onClick(View v) {
		activity = (Activity) v.getContext();
		String charName = ((EditText) activity.findViewById(R.id.etCharName)).getText().toString();
		Spinner spCharClass = ((Spinner) activity.findViewById(R.id.spCharClass));
		String charClass = spCharClass.getSelectedItem().toString();
		int charInitiative = Integer.valueOf(((EditText) activity.findViewById(R.id.etCurrentInitiative)).getText().toString());
		int charMaxHP = Integer.valueOf(((EditText) activity.findViewById(R.id.etMaxHP)).getText().toString());
		
		addNewCharacterToGame(charName, charClass, charInitiative, charMaxHP);
		Toast.makeText(activity, "New Character created", Toast.LENGTH_SHORT).show();
	}

	/**
	 * Adds new character with passed parameters to the game
	 * @param charName - name of the character
	 * @param charClass - class of the character
	 * @param charInitiative - encounter initiative of the character
	 * @param charMaxHP - maximum health points of the character
	 */
	private void addNewCharacterToGame(String charName, String charClass, int charInitiative, int charMaxHP) {
		DNDCharacter newCharacter = DNDCharacter.addNewCharacterToGame(charName, charClass, charInitiative, charMaxHP);
		saveNewCharacterToDB(newCharacter);
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
}
