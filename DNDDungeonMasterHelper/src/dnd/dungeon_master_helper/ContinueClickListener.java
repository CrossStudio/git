package dnd.dungeon_master_helper;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class ContinueClickListener implements OnClickListener {

	Activity activity;
	
	@Override
	public void onClick(View v) {
		activity = (Activity) v.getContext();
		
		Intent intent = new Intent(activity, EncounterLobbyActivity.class);
		activity.startActivity(intent);
	}

}
