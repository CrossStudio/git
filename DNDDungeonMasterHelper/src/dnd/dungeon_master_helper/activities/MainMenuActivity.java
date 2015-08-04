package dnd.dungeon_master_helper.activities;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import dnd.dungeon_master_helper.DBHelper;
import dnd.dungeon_master_helper.R;
import dnd.dungeon_master_helper.listeners.ContinueClickListener;
import dnd.dungeon_master_helper.listeners.EncounterClickListener;

public class MainMenuActivity extends Activity {

	Button btnContinue;
	Button btnNewEncounter;
	Button btnHelp;
	
	@Override
	protected void onCreate(Bundle savedBundle){
		super.onCreate(savedBundle);
		setContentView(R.layout.main_menu);
		
		DBHelper dbHelper = new DBHelper(this);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		dbHelper.loadAllCharactersFromDB(db);
		
		initializeViews();
		
	}

	/**
	 * Assign initial values to various View variables along with listeners
	 */
	private void initializeViews() {
		btnContinue = (Button) findViewById(R.id.btnContinue);
		btnNewEncounter = (Button) findViewById(R.id.btnNewEncounter);
		//btnHelp = (Button) findViewById(R.id.btnHelp);
		
		btnContinue.setOnClickListener(new ContinueClickListener());
		btnNewEncounter.setOnClickListener(new EncounterClickListener());
		//btnHelp.setOnClickListener(new HelpClickListener());
	}
	
	
}
