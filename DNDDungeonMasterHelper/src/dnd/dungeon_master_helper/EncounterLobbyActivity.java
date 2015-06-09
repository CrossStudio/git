package dnd.dungeon_master_helper;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

public class EncounterLobbyActivity extends Activity {

	Button btnAddCharacter;
	Button btnStartEncounter;
	
	@Override
	protected void onCreate(Bundle savedBundle){
		super.onCreate(savedBundle);
		setContentView(R.layout.encounter_lobby);
		initializeViews();
	}

	private void initializeViews() {
		btnAddCharacter = (Button) findViewById(R.id.btnAddCharacter);
		btnStartEncounter = (Button) findViewById(R.id.btnStartEncounter);
		
		btnAddCharacter.setOnClickListener(new GoToCharacterCreationListener());
		btnStartEncounter.setOnClickListener(new GoToMainActivityClickListener());
	}
}
