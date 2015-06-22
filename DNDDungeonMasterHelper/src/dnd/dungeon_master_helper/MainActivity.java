package dnd.dungeon_master_helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity {

	/**
	 * List of all the characters (player controlled, NPCs and monsters) that are currently in the game
	 */
	static ArrayList<DNDCharacter> dndCharacterArrayList = DNDCharacter.getCharacters();
	
	static LinearLayout llInitiativeOrder;
	
	static Button btnNextCharacter;
	
	static Button btnAddModifier;
	
	static TextView tvActiveCharacter;
	
	static EditText etModifierValue;
	
	static EditText etCharModifiers;
	
	static DNDCharacter activeCharacter;
	
	Spinner spinModifierType;
	
	Spinner spinModifierTarget;
	
	static void setActiveCharacter(int charactersIndex)
	{
		activeCharacter = dndCharacterArrayList.get(charactersIndex);
	}
	
	static String[] arrayOfModifierTypes = {"Attack", "AC", "Fortitude", "Reflex", "Will", "Damage"};
	
	static String[] arrayOfModifierTargets;
	
	static String[] modifierToAdd = new String[4];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initializeViews();
		
		DNDCharacter.addNewCharacterToGame("Father Tuck", "Cleric", 16, 28);
		DNDCharacter.addNewCharacterToGame("Lol1", "Paladin", 8, 32);
		DNDCharacter.addNewCharacterToGame("Leroy", "Fighter", 12, 36);
		
		arrayOfModifierTargets = new String[dndCharacterArrayList.size()];
		LayoutInflater inflater = getLayoutInflater();
		
		sortCharactersByInitiative();
		
		fillInitiativeOrderLine(inflater);
		
		fillModifierTypesSpinner();
		
		fillModifierTargetsSpinner();
		
		fillModifierStringCombo();
		
		if (dndCharacterArrayList.size() > 0)
		{
			activeCharacter = dndCharacterArrayList.get(0);
			tvActiveCharacter.setText("Active Character: " + activeCharacter.getCharName());
			loadActiveCharacterModifiers();
		}
	}
	
	/**
<<<<<<< HEAD
	 * Sorts characters within character list by their encounter initiative
	 */
	private void sortCharactersByInitiative() {
		Collections.sort(dndCharacterArrayList, new DNDCharacterInitiativeComparator());
	}
	/**
	 * Fills EditText responsible for modifier handling with applied modifier of the active character
	 */
	static void loadActiveCharacterModifiers() {
		etCharModifiers.setText("");
		for (String appliedModifier : activeCharacter.getListOfAppliedModifiers()){
			etCharModifiers.setText(etCharModifiers.getText() + appliedModifier  + "\n");
		}
	}

	/**
	 * Fills String array of modifier with initial values
	 */
	private void fillModifierStringCombo() {
		modifierToAdd[0] = " ";
		modifierToAdd[1] = " ";
		modifierToAdd[2] = " vs ";
		modifierToAdd[3] = " ";		
	}

	/**
	 * Sets the values of spinner items of modifier targets spinner
	 */
	private void fillModifierTargetsSpinner() {
		for (DNDCharacter character : dndCharacterArrayList)
		{
			arrayOfModifierTargets[dndCharacterArrayList.indexOf(character)] = character.getCharName();
		}
		ArrayAdapter<String> modifierTargetAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrayOfModifierTargets);
		modifierTargetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		spinModifierTarget.setAdapter(modifierTargetAdapter);
	}


	/**
	 * Sets initiative order line to its initial value
	 * @param inflater
	 */
	private void fillInitiativeOrderLine(LayoutInflater inflater) {
		/**
		 * Check whether there are no characters in the initiative line yet (there have to be only one object - initiative line nametag)
		 */
		if (llInitiativeOrder.getChildCount() == 1){
			for (DNDCharacter character : dndCharacterArrayList)
			{
				View inflatedInitOrderCharName = inflater.inflate(R.layout.init_order_char_name, null, false);
				TextView tvInitOrderCharName = (TextView) inflatedInitOrderCharName.findViewById(R.id.tvInitOrderCharName);
				tvInitOrderCharName.setText(character.getCharName());
				llInitiativeOrder.addView(inflatedInitOrderCharName);
			}
		}
	}


	/**
	 * Sets the values of spinner items of modifier types spinner
	 */
	private void fillModifierTypesSpinner() {
		ArrayAdapter<String> modifierTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrayOfModifierTypes);
		modifierTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		spinModifierType.setAdapter(modifierTypeAdapter);
	}

	/**
	 * Assign initial values to various View variables along with listeners
	 */
	private void initializeViews() {
		llInitiativeOrder = (LinearLayout) findViewById(R.id.llInitiativeOrder);
		btnNextCharacter = (Button) findViewById(R.id.btnNextCharacter);
		tvActiveCharacter = (TextView) findViewById(R.id.tvActiveCharName);
		btnAddModifier = (Button) findViewById(R.id.btnAddModifier);
		etCharModifiers = (EditText) findViewById(R.id.etCharModifiers);
		etModifierValue = (EditText) findViewById(R.id.etModifierValue);
		spinModifierType = (Spinner) findViewById(R.id.spinModifierType);
		spinModifierTarget = (Spinner) findViewById(R.id.spinModifierTarget);
		
		btnNextCharacter.setOnClickListener(new NextCharacterClickListener());
		btnAddModifier.setOnClickListener(new AddModifierClickListener());
		spinModifierTarget.setOnItemSelectedListener(new ModifierTargetSelectedListener());
		spinModifierType.setOnItemSelectedListener(new ModifierTypeSelectedListener());
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
