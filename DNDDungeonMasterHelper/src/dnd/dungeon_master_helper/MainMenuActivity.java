package dnd.dungeon_master_helper;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

public class MainMenuActivity extends Activity {

	Button btnContinue;
	Button btnNewEncounter;
	Button btnHelp;
	
	@Override
	protected void onCreate(Bundle savedBundle){
		super.onCreate(savedBundle);
		setContentView(R.layout.main_menu);
		
		initializeViews();
		
	}

	/**
	 * Assign initial values to various View variables along with listeners
	 */
	private void initializeViews() {
		btnContinue = (Button) findViewById(R.id.btnContinue);
		btnNewEncounter = (Button) findViewById(R.id.btnNewEncounter);
		btnHelp = (Button) findViewById(R.id.btnHelp);
		
		btnContinue.setOnClickListener(new ContinueClickListener());
		btnNewEncounter.setOnClickListener(new EncounterClickListener());
		btnHelp.setOnClickListener(new HelpClickListener());
	}
	
	
}
