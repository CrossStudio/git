package dnd.dungeon_master_helper;

import android.app.Activity;
import android.sax.StartElementListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddNewCharacterListener implements OnClickListener {

	Activity activity;
	
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
		DNDCharacter.addNewCharacterToGame(charName, charClass, charInitiative, charMaxHP);
	}

}
