package dnd.dungeon_master_helper.listeners;

import dnd.dungeon_master_helper.DNDCharacter;
import dnd.dungeon_master_helper.activities.CharacterCreationActivity;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.view.View.OnClickListener;

public class GoToCharacterCreationListener implements OnClickListener {

	Activity activity;
	
	@Override
	public void onClick(View v) {
		activity = (Activity) v.getContext();
		
		DNDCharacter.getDummyCharacter();
		
		SharedPreferences prefs = activity.getSharedPreferences("CurrentCharacter", activity.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.clear().commit();
		prefs = activity.getSharedPreferences("NewPower", activity.MODE_PRIVATE);
		editor = prefs.edit();
		editor.clear().commit();
		
		Intent intent = new Intent(activity, CharacterCreationActivity.class);
		activity.startActivity(intent);
	}

}
