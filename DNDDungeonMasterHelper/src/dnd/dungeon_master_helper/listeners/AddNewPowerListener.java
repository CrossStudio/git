package dnd.dungeon_master_helper.listeners;

import dnd.dungeon_master_helper.DNDCharacter;
import dnd.dungeon_master_helper.activities.AddPowerActivity;
import android.content.Intent;
import android.sax.StartElementListener;
import android.view.View;
import android.view.View.OnClickListener;

public class AddNewPowerListener implements OnClickListener {

	DNDCharacter currentCharacter;
	
	public AddNewPowerListener(DNDCharacter tempCharacter) {
		currentCharacter = tempCharacter;
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(v.getContext(), AddPowerActivity.class);
		intent.putExtra("character", currentCharacter);
		v.getContext().startActivity(intent);
	}

}
