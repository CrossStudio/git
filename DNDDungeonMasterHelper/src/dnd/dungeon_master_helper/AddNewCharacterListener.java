package dnd.dungeon_master_helper;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Spinner;

public class AddNewCharacterListener implements OnClickListener {

	Activity activity;
	
	@Override
	public void onClick(View v) {
		activity = (Activity) v.getContext();
		String charName = ((EditText) activity.findViewById(R.id.etCharName)).getText().toString();
		Spinner spCharClass = ((Spinner) activity.findViewById(R.id.spCharClass));
		String charClass = spCharClass.getSelectedItem().toString();
		String charInitiative = ((EditText) activity.findViewById(R.id.etCurrentInitiative)).getText().toString();
		String charMaxHP = ((EditText) activity.findViewById(R.id.etMaxHP)).getText().toString();
		addNewCharacterToGame(charName, charClass, charInitiative, charMaxHP);
	}

	/**
	 * Adds new character with passed parameters to the game
	 * @param charName - name of the character
	 * @param charClass - class of the character
	 * @param charInitiative - encounter initiative of the character
	 * @param charMaxHP - maximum health points of the character
	 */
	private void addNewCharacterToGame(String charName, String charClass, String charInitiative, String charMaxHP) {
		
	}

}
