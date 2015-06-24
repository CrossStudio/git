package dnd.dungeon_master_helper.listeners;

import dnd.dungeon_master_helper.activities.MainActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

public class ModifierTargetSelectedListener implements OnItemSelectedListener {

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		MainActivity.modifierToAdd[2] = MainActivity.arrayOfModifierTargets[position];
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}

}
