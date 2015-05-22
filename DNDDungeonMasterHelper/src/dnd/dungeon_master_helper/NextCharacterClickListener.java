package dnd.dungeon_master_helper;

import java.util.ArrayList;

import android.view.View;
import android.view.View.OnClickListener;

/**
 * Uses OnClickListener interface to make Next Character button of the main interface function
 * @author XAM
 *
 */
public class NextCharacterClickListener implements OnClickListener {

	ArrayList<DNDCharacter> charactersList = MainActivity.dndCharacterArrayList;
	DNDCharacter activeCharacter = MainActivity.activeCharacter;
	
	@Override
	public void onClick(View v) {
		for (DNDCharacter character : charactersList)
		{
			if (character.equals(activeCharacter))
			{
				nextCharacter(activeCharacter);
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
		if (charactersList.indexOf(activeCharacter) < charactersList.size() - 1)
		{
			activeCharacter = charactersList.
					get(charactersList.indexOf(activeCharacter) + 1);
		}
		else
		{
			activeCharacter = charactersList.get(0);
		}
		MainActivity.tvActiveCharacter.setText("Active Character: " + activeCharacter.getCharName());
	}


}
