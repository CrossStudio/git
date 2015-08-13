package dnd.dungeon_master_helper2.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TextView;
import dnd.dungeon_master_helper2.DNDCharacter;
import dnd.dungeon_master_helper2.Power;
import dnd.dungeon_master_helper2.PowerType;
import dnd.dungeon_master_helper2.R;
import dnd.dungeon_master_helper2.listeners.AddNewCharacterListener;
import dnd.dungeon_master_helper2.listeners.AddNewPowerListener;
import dnd.dungeon_master_helper2.listeners.CharClassSelectedListener;

public class CharacterCreationActivity extends Activity {
	
	SharedPreferences prefs;
	
	public static DNDCharacter currentCharacter;
	
	Button btnAddNewCharacter;
	Button btnAddNewPower;
	public static EditText etCharName;
	public static EditText etCurrentInitiative;
	public static EditText etMaxHP;
	public static Spinner spCharClass;
	public static final String CREATION_FROM_MAIN = "main";
	public static final String CREATION_FROM_LOBBY = "lobby";
	
	public static String creationMode = "";
	
	String classes[] = {"Cleric", "Fighter", "Paladin", "Ranger", "Rogue", "Warlock", "Warlord", "Wizard", "Monster"};
	
	@Override
	public void onCreate(Bundle savedBundle){
		super.onCreate(null);
		setContentView(R.layout.character_creation);
		Intent intent = getIntent();
		
		if (intent.getStringExtra("activity") != null){
			if (intent.getStringExtra("activity").equals("MainActivity")){
				creationMode = CREATION_FROM_MAIN;
			}
			else {
				creationMode = CREATION_FROM_LOBBY;
			}
		}
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
		
		btnAddNewCharacter.setOnClickListener(new AddNewCharacterListener(creationMode));
		btnAddNewPower.setOnClickListener(new AddNewPowerListener());
		
		spCharClass.setOnItemSelectedListener(new CharClassSelectedListener());
	}
	
	/**
	 * Sets the values of spinner items of currentCharacter classes spinner
	 */
	private void fillCharacterClassesSpinner() {
		ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, classes);
		spCharClass.setAdapter(adapter);
	}
	
	public void onPause(){
		super.onPause();
		
		setCharacterParameters();
		saveCharacterParameters();
	}
	
	/**
	 * Sets current currentCharacter parameters
	 */
	private void setCharacterParameters() {
		currentCharacter.setCharName(etCharName.getText().toString());
		currentCharacter.setCharClass(spCharClass.getSelectedItem().toString());
		if (etMaxHP.getText().length() > 0){
			currentCharacter.setCharHPMax(Integer.parseInt(etMaxHP.getText().toString()));
			currentCharacter.setCharHPCurrent(currentCharacter.getCharHPMax());
		}
		if (etCurrentInitiative.getText().length() > 0){
			currentCharacter.setCharInitiativeEncounter(Integer.parseInt(etCurrentInitiative.getText().toString()));
		}
	}

	/**
	 * Saves currentCharacter's parameters in Preferences
	 */
	private void saveCharacterParameters() {
		prefs = getSharedPreferences("CurrentCharacter", MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putString("charName", currentCharacter.getCharName());
		editor.putString("charClass", currentCharacter.getCharClass());
		editor.putInt("charInit", currentCharacter.getCharInitiativeEncounter());
		editor.putInt("charMaxHP", currentCharacter.getCharHPMax());
		
		saveCharacterPowers(editor);
		
		editor.commit();
	}

	/**
	 * Saves currentCharacter's powers to Preferences in form "power\i" where \i is power's index in currentCharacter's list of powers
	 * @param editor Editor object that edits preferences
	 */
	private void saveCharacterPowers(Editor editor) {
		ArrayList<Power> powers = currentCharacter.getCharPowers();
		for (int i = 0; i < powers.size(); i++){
			Power power = powers.get(i);
			editor.putString("powerTitle" + i, power.getTitle());
			editor.putString("powerType" + i, power.getType().toString());
			editor.putInt("powerAmount" + i, power.getMaxAmount());
		}
	}

	public void onResume(){
		super.onResume();
		
		currentCharacter = DNDCharacter.getDummyCharacter();
		
		initializeViews();
		
		fillCharacterClassesSpinner();
		
		loadCharacterParameters();
		drawCurrentCharacterPowers();
	}

	/**
	 * Loads currentCharacter's parameters from Preferences
	 */
	private void loadCharacterParameters() {
		prefs = getSharedPreferences("CurrentCharacter", MODE_PRIVATE);
		
		if (prefs.getString("charName", "").length() > 0){
			etCharName.setText(prefs.getString("charName", ""));
		}
		
		if (prefs.getInt("charInit", 0) > 0){
			etCurrentInitiative.setText("" + prefs.getInt("charInit", 0));
		}
		
		if (prefs.getInt("charMaxHP", 0) > 0){
			etMaxHP.setText(""+prefs.getInt("charMaxHP", 0));
		}
		
		String selectedClass = prefs.getString("charClass", "");
		int classID = -1;
		for (int i = 0; i < classes.length; i++){
			if (selectedClass.equals(classes[i])){
				classID = i;
			}
		}
		
		spCharClass.setSelection(classID);
		
		Editor editor = prefs.edit();
		editor.clear().commit();
		
		prefs = getSharedPreferences("NewPower", MODE_PRIVATE);
		loadCharacterPowers(prefs);
		
		
	}

	/**
	 * Loads currentCharacter powers
	 * @param prefs
	 */
	private void loadCharacterPowers(SharedPreferences prefs) {

		ArrayList<Power> powers = currentCharacter.getCharPowers();
		for (int i = 0; i < powers.size(); i++){
			Power power = powers.get(i);
		}
		

		if (prefs.contains("newPowerTitle")){
			
			String title = prefs.getString("newPowerTitle", "");
			
			int typeID = prefs.getInt("newPowerTypeID", -1);
			
			int maxAmount = prefs.getInt("newPowerMaxAmount", 0);
			
			int curAmount = prefs.getInt("newPowerCurAmount", 0);

			switch (typeID) {
				case R.id.rbAtWill: 
					powers.add(new Power(title, PowerType.ATWILL, maxAmount, curAmount));
					break;
				case R.id.rbEncounter:
					powers.add(new Power(title, PowerType.ENCOUNTER, maxAmount, curAmount));
					break;
				case R.id.rbDaily:
					powers.add(new Power(title, PowerType.DAILY, maxAmount, curAmount));
					break;
			}
			Editor editor = prefs.edit();
			editor.clear().commit();
		}
	}

	/**
	 * Draws powers of current active currentCharacter on CharacterCreationActivity using LayoutInflater
	 */
	private void drawCurrentCharacterPowers(){
		LayoutInflater inflater = getLayoutInflater();
		LinearLayout llCharPowers = (LinearLayout) findViewById(R.id.llCharPowersCreation);
		llCharPowers.removeAllViews();
		final ArrayList<Power> powers = currentCharacter.getCharPowers();
		for (final Power power : powers){
			LinearLayout item = (LinearLayout) inflater.inflate(R.layout.power_item, llCharPowers, false);
			
			TextView tvPowerTitle = (TextView) item.findViewById(R.id.tvPowerTitle);
			tvPowerTitle.setText(power.getTitle());
			
			TextView tvPowerType = (TextView) item.findViewById(R.id.tvPowerType);
			tvPowerType.setText(power.getType()+"");
			
			LinearLayout llPowerCheckBoxes = (LinearLayout) item.findViewById(R.id.llPowerCheckBoxes);
			
			Button btnDeletePower = (Button) item.findViewById(R.id.btnDeletePower);
			btnDeletePower.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					powers.remove(power);
					drawCurrentCharacterPowers();
				}
			});
			
			LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.gravity = Gravity.CENTER_VERTICAL;
			
			for (int i = 0; i < power.getMaxAmount(); i++){
				CheckBox cbPowerAmount = new CheckBox(this);
				cbPowerAmount.setLayoutParams(params);
				llPowerCheckBoxes.addView(cbPowerAmount);
			}
			llCharPowers.addView(item);
			
		}
	}
	
}
