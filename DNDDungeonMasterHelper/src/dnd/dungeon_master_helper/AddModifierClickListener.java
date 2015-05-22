package dnd.dungeon_master_helper;

import android.view.View;
import android.view.View.OnClickListener;

public class AddModifierClickListener implements OnClickListener {

	@Override
	public void onClick(View v) {
		String newModifier = "";
		for (String modifierPart : MainActivity.modifierToAdd)
		{
			newModifier += modifierPart;
		}
		MainActivity.etCharModifiers.setText(MainActivity.etCharModifiers.getText() + newModifier + "\n");
	}

}
