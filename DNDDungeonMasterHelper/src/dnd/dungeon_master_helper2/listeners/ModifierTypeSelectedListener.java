package dnd.dungeon_master_helper2.listeners;

import dnd.dungeon_master_helper2.activities.MainActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

public class ModifierTypeSelectedListener implements OnItemSelectedListener {
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		MainActivity.modifierToAdd[1] = MainActivity.arrayOfModifierTypes[position];
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}

}
