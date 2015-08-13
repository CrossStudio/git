package dnd.dungeon_master_helper2.listeners;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import dnd.dungeon_master_helper2.DBHelper;
import dnd.dungeon_master_helper2.DNDCharacter;
import dnd.dungeon_master_helper2.activities.MainActivity;

public class ContinueClickListener implements OnClickListener {

	Activity activity;
	
	@Override
	public void onClick(View v) {
		activity = (Activity) v.getContext();
		
		DBHelper helper = new DBHelper(activity);
		SQLiteDatabase db = helper.getWritableDatabase();
		ArrayList<DNDCharacter> encounterCharacters = helper.loadEncounterFromDB(db);
		
		if (encounterCharacters != null){
			ArrayList<DNDCharacter> dummyCharacters = DNDCharacter.getSelectedCharacters();
			dummyCharacters.clear();
			dummyCharacters.addAll(encounterCharacters);
			Intent intent = new Intent(activity, MainActivity.class);
			intent.putExtra("activity", "mainMenu");
			activity.startActivity(intent);
		}
		else {
			Toast.makeText(activity, "Please add characters to Encounter", Toast.LENGTH_LONG).show();
		}
		
	}

}
