package dnd.dungeon_master_helper.activities;

import java.util.ArrayList;
import java.util.Collections;

import dnd.dungeon_master_helper.DBHelper;
import dnd.dungeon_master_helper.DNDCharacter;
import dnd.dungeon_master_helper.DNDCharacterInitiativeComparator;
import dnd.dungeon_master_helper.R;
import dnd.dungeon_master_helper.R.id;
import dnd.dungeon_master_helper.R.layout;
import dnd.dungeon_master_helper.R.menu;
import dnd.dungeon_master_helper.listeners.AddModifierClickListener;
import dnd.dungeon_master_helper.listeners.ModifierTargetSelectedListener;
import dnd.dungeon_master_helper.listeners.ModifierTypeSelectedListener;
import dnd.dungeon_master_helper.listeners.NextCharacterClickListener;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
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
	public static ArrayList<DNDCharacter> dndCharacterArrayList = DNDCharacter.getCharacters();
	
	static LinearLayout llInitiativeOrder;
	
	static Button btnNextCharacter;
	
	static Button btnAddModifier;
	
	public static TextView tvActiveCharacter;
	
	public static EditText etModifierValue;
	
	public static EditText etCharModifiers;
	
	public static DNDCharacter activeCharacter;
	
	Spinner spinModifierType;
	
	Spinner spinModifierTarget;
	
	public static void setActiveCharacter(int charactersIndex)
	{
		activeCharacter = dndCharacterArrayList.get(charactersIndex);
	}
	
	public static String[] arrayOfModifierTypes = {"Attack", "AC", "Fortitude", "Reflex", "Will", "Damage"};
	
	public static String[] arrayOfModifierTargets;
	
	public static String[] modifierToAdd = new String[4];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initializeViews();
		
		arrayOfModifierTargets = new String[dndCharacterArrayList.size()];	
		
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
	
	@Override
	protected void onResume(){
		super.onResume();
		sortCharactersByInitiative();
		LayoutInflater inflater = getLayoutInflater();
		fillInitiativeOrderLine(inflater);
		
	}
	
	/**
	 * Sorts characters within character list by their encounter initiative
	 */
	private void sortCharactersByInitiative() {
		Collections.sort(dndCharacterArrayList, new DNDCharacterInitiativeComparator());
	}
	/**
	 * Fills EditText responsible for modifier handling with applied modifier of the active character
	 */
	public static void loadActiveCharacterModifiers() {
		etCharModifiers.setText("");
		for (String appliedModifier : activeCharacter.getListOfAppliedModifiers()){
			String longStringOfModifiers = "";
			if (activeCharacter.getListOfAppliedModifiers().indexOf(appliedModifier)==0){
				longStringOfModifiers += appliedModifier;
			}
			else {
				longStringOfModifiers += "\n" + appliedModifier;
			}
			etCharModifiers.setText(longStringOfModifiers);
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
	protected void onPause(){
		super.onPause();
		saveCharactersToDB();
	}

	/**
	 * Saves all the characters with their current parameters to the database
	 */
	private void saveCharactersToDB() {
		DBHelper dbHelper = new DBHelper(this);
		
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		ContentValues cv = new ContentValues();
		
		for (DNDCharacter character : dndCharacterArrayList){
			
			cv.put("class", character.getCharClass());
			cv.put("name", character.getCharName());
			cv.put("maxhp", character.getCharHPMax());
			cv.put("currenthp",character.getCharHPCurrent());
			String modifiers = "";
			for (String modifier : character.getListOfAppliedModifiers()){
				if (character.getListOfAppliedModifiers().indexOf(modifier) == 0){
					modifiers += modifier;
				}
				else {
					modifiers += "\n" + modifier;
				}
			}
			cv.put("modifiers", modifiers);
			
			int updCount = db.update("characters", cv, "name = ?", new String[] {character.getCharName()});
			if (updCount == 0){
				long rowID = db.insert("characters", null, cv);
				Log.d("myLog", "Row number: " + rowID);
			}
		}
	}
}
