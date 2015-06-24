package dnd.dungeon_master_helper.listeners;

import dnd.dungeon_master_helper.activities.MainActivity;
import android.view.View;
import android.view.View.OnClickListener;

public class AddModifierClickListener implements OnClickListener {

	@Override
	public void onClick(View v) {
		String newModifier = MainActivity.etModifierValue.getText().toString() + " ";
		for (String modifierPart : MainActivity.modifierToAdd)
		{
			newModifier += modifierPart;
		}
		if (MainActivity.etCharModifiers.getText().toString().equals("")){
			MainActivity.etCharModifiers.setText(newModifier);
		}
		else {
			MainActivity.etCharModifiers.setText(MainActivity.etCharModifiers.getText() + "\n" + newModifier);
		}
	}

}
