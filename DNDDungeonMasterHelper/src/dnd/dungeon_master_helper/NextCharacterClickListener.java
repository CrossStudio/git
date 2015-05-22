package dnd.dungeon_master_helper;

import android.view.View;
import android.view.View.OnClickListener;

public class NextCharacterClickListener implements OnClickListener {

	@Override
	public void onClick(View v) {
		for (DNDCharacter character : MainActivity.dndCharacterArrayList)
		{
			if (character.equals(MainActivity.activeCharacter))
			{
				if (MainActivity.dndCharacterArrayList.indexOf(character) < MainActivity.dndCharacterArrayList.size() - 1)
				{
					MainActivity.activeCharacter = MainActivity.dndCharacterArrayList.
							get(MainActivity.dndCharacterArrayList.indexOf(character) + 1);
				}
				else
				{
					MainActivity.activeCharacter = MainActivity.dndCharacterArrayList.get(0);
				}
				MainActivity.tvActiveCharacter.setText("Active Character: " + MainActivity.activeCharacter.getCharName());
				break;
			}
		}
		
	}


}
