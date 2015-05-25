package dnd.dungeon_master_helper;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class CharacterCreationActivity extends Activity {

	Button btnAddNewCharacter;
	Button btnProceedToGame;
	EditText etCharName;
	EditText etCurrentInitiative;
	EditText etMaxHP;
	Spinner spCharClass;
	
	@Override
	public void onCreate(Bundle savedBundle){
		super.onCreate(savedBundle);
		setContentView(R.layout.character_creation);
		
		initializeViews();
	}
	
	/**
	 * Assign initial values to various View variables along with listeners
	 */
	private void initializeViews() {
		btnAddNewCharacter = (Button) findViewById(R.id.btnAddNewCharacter);
		btnProceedToGame = (Button) findViewById(R.id.btnProceedToGame);
		etCharName = (EditText) findViewById(R.id.etCharName);
		etCurrentInitiative = (EditText) findViewById(R.id.etCurrentInitiative);
		etMaxHP = (EditText) findViewById(R.id.etMaxHP);
		spCharClass = (Spinner) findViewById(R.id.spCharClass);
		
		btnAddNewCharacter.setOnClickListener(new AddNewCharacterListener());
		btnProceedToGame.setOnClickListener(new ProceedToGameListener());
		
		spCharClass.setOnItemSelectedListener(new CharClassSelectedListener());
	}
}
