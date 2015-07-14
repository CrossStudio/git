package dnd.dungeon_master_helper.listeners;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import dnd.dungeon_master_helper.activities.AddPowerActivity;

public class AddNewPowerListener implements OnClickListener {

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(v.getContext(), AddPowerActivity.class);
		v.getContext().startActivity(intent);
	}

}
