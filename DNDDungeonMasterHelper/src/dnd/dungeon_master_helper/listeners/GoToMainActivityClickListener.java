package dnd.dungeon_master_helper.listeners;

import dnd.dungeon_master_helper.activities.MainActivity;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class GoToMainActivityClickListener implements OnClickListener {

	Activity activity;
	
	@Override
	public void onClick(View v) {
		activity = (Activity) v.getContext();
		
		Intent intent = new Intent(activity, MainActivity.class);
		activity.startActivity(intent);
	}

}
