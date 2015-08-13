package dnd.dungeon_master_helper2.listeners;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import dnd.dungeon_master_helper2.DNDCharacter;
import dnd.dungeon_master_helper2.activities.MainActivity;

public class GoToMainActivityClickListener implements OnClickListener {

	Activity activity;
	
	@Override
	public void onClick(View v) {
		activity = (Activity) v.getContext();
		
		if (DNDCharacter.getSelectedCharacters().size() == 0){
			Toast.makeText(activity, "Please add characters to Encounter", Toast.LENGTH_LONG).show();
		}
		
		else {
			Intent intent = new Intent(activity, MainActivity.class);
			intent.putExtra("activity", "encounterLobby");
			activity.startActivity(intent);
		}
	}

}
