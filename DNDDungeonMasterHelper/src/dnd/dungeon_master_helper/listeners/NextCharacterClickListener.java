package dnd.dungeon_master_helper.listeners;

import java.util.ArrayList;

import dnd.dungeon_master_helper.DNDCharacter;
import dnd.dungeon_master_helper.activities.MainActivity;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

/**
 * Uses OnClickListener interface to make Next Character button of the main interface function
 * @author XAM
 *
 */
public class NextCharacterClickListener implements OnClickListener {

	ArrayList<DNDCharacter> charactersList = MainActivity.dndCharacterArrayList;
	
	@Override
	public void onClick(View v) {
		
		DNDCharacter activeCharacter = MainActivity.activeCharacter;
		if (activeCharacter != null){
			saveActiveCharacterParams(activeCharacter);
			nextCharacter(activeCharacter);
		}
	}
	
	/**
	 * Saves parameters of currently active player
	 * @param activeCharacter
	 */
	private void saveActiveCharacterParams(DNDCharacter activeCharacter) {
		activeCharacter.setCharHPCurrent(Integer.valueOf(MainActivity.tvHPCurrentValue.getText().toString()));
		saveActiveCharacterModifiers(activeCharacter);
	}

	/**
	 * Saves modifiers of currently active character
	 * @param activeCharacter
	 */
	private void saveActiveCharacterModifiers(DNDCharacter activeCharacter) {
		activeCharacter.getListOfAppliedModifiers().clear();
		activeCharacter.getListOfAppliedModifiers().add(MainActivity.etCharModifiers.getText().toString());
	}

	/**
	 * Sets currently active character equal to the next character with regards to the character sent to the method
	 * Also changes text in the "Active Character" TextView
	 * @param activeCharacter - currently active character
	 */
	private void nextCharacter(DNDCharacter activeCharacter)
	{
		int indexOfActiveCharacter = charactersList.indexOf(activeCharacter);
		if (indexOfActiveCharacter < charactersList.size() - 1)
		{
			MainActivity.setActiveCharacter(indexOfActiveCharacter + 1);
		}
		else
		{
			MainActivity.setActiveCharacter(0);
		}
		MainActivity.tvActiveCharacter.setText(MainActivity.activeCharacter.getCharName() + " (" + MainActivity.activeCharacter.getCharClass() + ")");
		MainActivity.loadActiveCharacterParams();
	}


}
