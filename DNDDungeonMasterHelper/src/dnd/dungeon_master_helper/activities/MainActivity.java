package dnd.dungeon_master_helper.activities;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import dnd.dungeon_master_helper.DBHelper;
import dnd.dungeon_master_helper.DNDCharacter;
import dnd.dungeon_master_helper.DNDCharacterInitiativeComparator;
import dnd.dungeon_master_helper.Power;
import dnd.dungeon_master_helper.R;
import dnd.dungeon_master_helper.listeners.AddModifierClickListener;
import dnd.dungeon_master_helper.listeners.DamageClickListener;
import dnd.dungeon_master_helper.listeners.HealClickListener;
import dnd.dungeon_master_helper.listeners.ModifierTargetSelectedListener;
import dnd.dungeon_master_helper.listeners.ModifierTypeSelectedListener;

public class MainActivity extends Activity {

	/**
	 * List of all the characters (player controlled, NPCs and monsters) that are currently in the game
	 */
	public static ArrayList<DNDCharacter> dndCharacterArrayList = DNDCharacter.getSelectedCharacters();
	
	static LinearLayout llInitiativeOrder;
	
	static Button btnNextCharacter;
	
	static Button btnAddModifier;
	
	static Button btnHeal;
	
	static Button btnDamage;
	
	public static TextView tvActiveCharacter;
	
	public static TextView tvHPCurrentValue;
	
	public static TextView tvHPMaxValue;
	
	public static TextView tvBloodiedValue;
	
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
		
	}

	@Override
	protected void onResume(){
		super.onResume();
		
		initializeViews();
		
		arrayOfModifierTargets = new String[dndCharacterArrayList.size()];	
		
		fillModifierTypesSpinner();
		
		fillModifierTargetsSpinner();
		
		fillModifierStringCombo();
		
		if (dndCharacterArrayList.size() > 0)
		{
			sortCharactersByInitiative();
			activeCharacter = dndCharacterArrayList.get(0);
			tvActiveCharacter.setText(activeCharacter.getCharName() + " (" + activeCharacter.getCharClass() + ")");
			loadActiveCharacterParams();
			loadActiveCharacterPowers();
		}
		
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
	 * Fills layout views with parameters of current active player
	 */
	public void loadActiveCharacterParams() {
		tvHPCurrentValue.setText(""+activeCharacter.getCharHPCurrent());
		tvHPMaxValue.setText(""+activeCharacter.getCharHPMax());
		tvBloodiedValue.setText(activeCharacter.getCharHPMax() / 2 + "");
		loadActiveCharacterModifiers();
		btnNextCharacter.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				if (activeCharacter != null){
					saveActiveCharacterParams();
					nextCharacter();
				}
			}
			
			/**
			 * Saves parameters of currently active player
			 * @param activeCharacter
			 */
			private void saveActiveCharacterParams() {
				activeCharacter.setCharHPCurrent(Integer.valueOf(tvHPCurrentValue.getText().toString()));
				saveActiveCharacterModifiers();
			}

			/**
			 * Saves modifiers of currently active character
			 * @param activeCharacter
			 */
			private void saveActiveCharacterModifiers() {
				activeCharacter.getListOfAppliedModifiers().clear();
				activeCharacter.getListOfAppliedModifiers().add(etCharModifiers.getText().toString());
			}

			/**
			 * Sets currently active character equal to the next character with regards to the character sent to the method
			 * Also changes text in the "Active Character" TextView
			 * @param activeCharacter - currently active character
			 */
			private void nextCharacter()
			{
				int indexOfActiveCharacter = dndCharacterArrayList.indexOf(activeCharacter);
				Log.d("myLog", "Index of active character = " + indexOfActiveCharacter);
				if (indexOfActiveCharacter < dndCharacterArrayList.size() - 1)
				{
					setActiveCharacter(indexOfActiveCharacter + 1);
				}
				else
				{
					setActiveCharacter(0);
				}
				tvActiveCharacter.setText(activeCharacter.getCharName() + " (" + activeCharacter.getCharClass() + ")");
				loadActiveCharacterParams();
				loadActiveCharacterPowers();
			}
		});
	}
	
	/**
	 * Fills EditText responsible for modifier handling with applied modifier of the active character
	 */
	private void loadActiveCharacterModifiers() {
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
	 * Fills listView of active character's powers
	 */
	/*private void loadActiveCharacterPowers() {
		List<Map<String, String>> listPowersData = new ArrayList<>();
		
		String[] takeDataFromKey = {"title", "type"};
		int[] writeDataToKey = {R.id.tvPowerTitle, R.id.tvPowerType};
		
		ArrayList<Power> powers = activeCharacter.getCharPowers();
		for (Power power : powers){
			Map<String, String> mapPowersData = new HashMap<>();
			mapPowersData.put("title", power.getTitle());
			mapPowersData.put("type", power.getType().toString());
			listPowersData.add(mapPowersData);
			Log.d("myLog", "Power: " + power.getTitle());
		}
		SimpleAdapter adapter = new SimpleAdapter(this, listPowersData, R.layout.power_item, takeDataFromKey, writeDataToKey);
		lvCharPowers.setAdapter(adapter);
		
		addAmountOfPowersCheckBoxes(adapter);
	}*/
	
	/**
	 * Draws powers of current active character on MainActivity using LayoutInflater
	 */
	private void loadActiveCharacterPowers(){
		LayoutInflater inflater = getLayoutInflater();
		LinearLayout llCharPowers = (LinearLayout) findViewById(R.id.llCharPowersMain);
		llCharPowers.removeAllViews();
		ArrayList<Power> powers = activeCharacter.getCharPowers();
		for (final Power power : powers){
			LinearLayout item = (LinearLayout) inflater.inflate(R.layout.power_item, llCharPowers, false);
			
			TextView tvPowerTitle = (TextView) item.findViewById(R.id.tvPowerTitle);
			tvPowerTitle.setText(power.getTitle());
			
			TextView tvPowerType = (TextView) item.findViewById(R.id.tvPowerType);
			tvPowerType.setText(power.getType()+"");
			
			
			LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.gravity = Gravity.CENTER_VERTICAL;
			
			int usedPowerCounter = power.getMaxAmount() - power.getCurrentAmount();
			
			for (int i = 0; i < power.getMaxAmount(); i++){
				CheckBox cbPowerAmount = new CheckBox(this);
				cbPowerAmount.setLayoutParams(params);
				
				if (usedPowerCounter > 0){
					cbPowerAmount.setChecked(true);
					usedPowerCounter--;
				}
				
				item.addView(cbPowerAmount);

				cbPowerAmount.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						if (isChecked){
							power.setCurrentAmount(power.getCurrentAmount() - 1);
						}
						else {
							power.setCurrentAmount(power.getCurrentAmount() + 1);
						}
						Log.d("myLog", "Current amount of uses = " + power.getCurrentAmount());
					}
				});
			}
			llCharPowers.addView(item);
			
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
			Log.d("myLog", character.getCharName() + " added to spinner");
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
		tvHPCurrentValue = (TextView) findViewById(R.id.tvHPCurrentValue);
		tvHPMaxValue = (TextView) findViewById(R.id.tvHPMaxValue);
		btnHeal = (Button) findViewById(R.id.btnHeal);
		btnDamage = (Button) findViewById(R.id.btnDamage);
		tvBloodiedValue = (TextView) findViewById(R.id.tvBloodiedValue);
		btnAddModifier = (Button) findViewById(R.id.btnAddModifier);
		etCharModifiers = (EditText) findViewById(R.id.etCharModifiers);
		etModifierValue = (EditText) findViewById(R.id.etModifierValue);
		spinModifierType = (Spinner) findViewById(R.id.spinModifierType);
		spinModifierTarget = (Spinner) findViewById(R.id.spinModifierTarget);
		
		
		
		btnAddModifier.setOnClickListener(new AddModifierClickListener());
		btnHeal.setOnClickListener(new HealClickListener());
		btnDamage.setOnClickListener(new DamageClickListener());
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
