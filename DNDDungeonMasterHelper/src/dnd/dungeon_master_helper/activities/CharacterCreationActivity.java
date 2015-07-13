package dnd.dungeon_master_helper.activities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import dnd.dungeon_master_helper.DNDCharacter;
import dnd.dungeon_master_helper.Power;
import dnd.dungeon_master_helper.R;
import dnd.dungeon_master_helper.listeners.AddNewCharacterListener;
import dnd.dungeon_master_helper.listeners.AddNewPowerListener;
import dnd.dungeon_master_helper.listeners.CharClassSelectedListener;

public class CharacterCreationActivity extends Activity {
	
	DNDCharacter tempCharacter;
	
	Button btnAddNewCharacter;
	Button btnAddNewPower;
	EditText etCharName;
	EditText etCurrentInitiative;
	EditText etMaxHP;
	Spinner spCharClass;
	
	ListView lvCharPowers;
	
	String classes[] = {"Cleric", "Fighter", "Paladin", "Ranger", "Rogue", "Warlock", "Warlord", "Wizard", "Monster"};
	
	@Override
	public void onCreate(Bundle savedBundle){
		super.onCreate(savedBundle);
		setContentView(R.layout.character_creation);		
		
		initializeViews();
		
		tempCharacter = DNDCharacter.createDummyCharacter();
		
		fillCharacterClassesSpinner();
	}

	/**
	 * Assign initial values to various View variables along with listeners
	 */
	private void initializeViews() {
		btnAddNewCharacter = (Button) findViewById(R.id.btnAddNewCharacter);
		btnAddNewPower = (Button) findViewById(R.id.btnAddNewPower);
		etCharName = (EditText) findViewById(R.id.etCharName);
		etCurrentInitiative = (EditText) findViewById(R.id.etCurrentInitiative);
		etMaxHP = (EditText) findViewById(R.id.etMaxHP);
		spCharClass = (Spinner) findViewById(R.id.spCharClass);
		lvCharPowers = (ListView) findViewById(R.id.lvCharPowersMain);
		
		btnAddNewCharacter.setOnClickListener(new AddNewCharacterListener());
		btnAddNewPower.setOnClickListener(new AddNewPowerListener(tempCharacter));
		
		spCharClass.setOnItemSelectedListener(new CharClassSelectedListener());
	}
	
	/**
	 * Sets the values of spinner items of character classes spinner
	 */
	private void fillCharacterClassesSpinner() {
		ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, classes);
		spCharClass.setAdapter(adapter);
	}
	
	public void onPause(){
		super.onPause();
		
		writingTempCharacterToFile();
		
	}

	/**
	 * Saving parameters already entered and powers already added to files DNDCharacter.ser and Powers.ser respectively
	 */
	private void writingTempCharacterToFile() {
		
		String charName = etCharName.getText().toString();
		tempCharacter.setCharName(charName);
		
		if (etCurrentInitiative.getText().length() > 0){
			int charInit = Integer.parseInt(etCurrentInitiative.getText().toString());
			tempCharacter.setCharInitiativeEncounter(charInit);
		}
		
		if (etMaxHP.getText().length() > 0){
			int charMaxHP = Integer.parseInt(etMaxHP.getText().toString());
			tempCharacter.setCharHPMax(charMaxHP);
		}
		
		String charClass = spCharClass.getSelectedItem().toString();
		tempCharacter.setCharClass(charClass);
		
		ArrayList<Power> powers = null;
		if (tempCharacter.getCharPowers() != null){
			powers = new ArrayList<>(tempCharacter.getCharPowers());
		}
		
		try {
			File fileCharacter = new File("DNDCharacter.ser");
			File filePowersArray = new File ("Powers.ser");
			
			FileOutputStream fos = new FileOutputStream(fileCharacter);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(tempCharacter);
			fos.close();
			oos.close();
			
			fos = new FileOutputStream(filePowersArray);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(powers);
			fos.close();
			oos.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public void onResume(){
		super.onResume();
		
		loadTempCharacterFromFile();
	}

	/**
	 * Loading parameters of the character that have already been entered
	 */
	private void loadTempCharacterFromFile() {
		// TODO Auto-generated method stub
		
	}
}
