package dnd.dungeon_master_helper;

import java.util.ArrayList;

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
		
		for (DNDCharacter character : charactersList)
		{
			if (character.equals(MainActivity.activeCharacter))
			{
				nextCharacter(character);
				break;
			}
		}
		
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
		MainActivity.tvActiveCharacter.setText("Active Character: " + MainActivity.activeCharacter.getCharName());
		MainActivity.loadActiveCharacterModifiers();
	}


}
