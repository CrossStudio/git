package dnd.dungeon_master_helper.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import dnd.dungeon_master_helper.DNDCharacter;
import dnd.dungeon_master_helper.Power;
import dnd.dungeon_master_helper.PowerType;
import dnd.dungeon_master_helper.R;
import dnd.dungeon_master_helper.listeners.AddNewCharacterListener;
import dnd.dungeon_master_helper.listeners.AddNewPowerListener;
import dnd.dungeon_master_helper.listeners.CharClassSelectedListener;

public class CharacterCreationActivity extends Activity {
	
	SharedPreferences prefs;
	
	public static DNDCharacter currentCharacter;
	
	Button btnAddNewCharacter;
	Button btnAddNewPower;
	static EditText etCharName;
	static EditText etCurrentInitiative;
	static EditText etMaxHP;
	Spinner spCharClass;
	
	String classes[] = {"Cleric", "Fighter", "Paladin", "Ranger", "Rogue", "Warlock", "Warlord", "Wizard", "Monster"};
	
	@Override
	public void onCreate(Bundle savedBundle){
		super.onCreate(null);
		setContentView(R.layout.character_creation);
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
		
		btnAddNewCharacter.setOnClickListener(new AddNewCharacterListener());
		btnAddNewPower.setOnClickListener(new AddNewPowerListener());
		
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
		
		setCharacterParameters();
		saveCharacterParameters();
	}
	
	/**
	 * Sets current character parameters
	 */
	private void setCharacterParameters() {
		Log.d("myLog", "---setCharacterParameters() in CharacterCreationActivity---");
		Log.d("myLog", "currentCharacter = " + currentCharacter);
		currentCharacter.setCharName(etCharName.getText().toString());
		currentCharacter.setCharClass(spCharClass.getSelectedItem().toString());
		if (etMaxHP.getText().length() > 0){
			currentCharacter.setCharHPMax(Integer.parseInt(etMaxHP.getText().toString()));
		}
		if (etCurrentInitiative.getText().length() > 0){
			currentCharacter.setCharInitiativeEncounter(Integer.parseInt(etCurrentInitiative.getText().toString()));
		}
	}

	/**
	 * Saves character's parameters in Preferences
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
	 * Saves character's powers to Preferences in form "power\i" where \i is power's index in character's list of powers
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
	 * Loads character's parameters from Preferences
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
	 * Loads character powers
	 * @param prefs
	 */
	private void loadCharacterPowers(SharedPreferences prefs) {

		ArrayList<Power> powers = currentCharacter.getCharPowers();
		for (int i = 0; i < powers.size(); i++){
			Power power = powers.get(i);
			Log.d("myLog", "powerTitle" + i + " = " + power.getTitle());
			Log.d("myLog", "powerType" + i + " = " + power.getType().toString());
			Log.d("myLog", "powerAmount" + i + " = " + power.getMaxAmount());
		}
		

		if (prefs.contains("newPowerTitle")){
			
			Log.d("myLog", "Inside loadCharPowers; found NewPower preferences");
			String title = prefs.getString("newPowerTitle", "");
			
			int typeID = prefs.getInt("newPowerTypeID", -1);
			
			int maxAmount = prefs.getInt("newPowerAmount", 0);

			switch (typeID) {
				case R.id.rbAtWill: 
					powers.add(new Power(title, PowerType.ATWILL, maxAmount));
					break;
				case R.id.rbEncounter:
					powers.add(new Power(title, PowerType.ENCOUNTER, maxAmount));
					break;
				case R.id.rbDaily:
					powers.add(new Power(title, PowerType.DAILY, maxAmount));
					break;
			}
			Log.d("myLog", "Added new Power: " + powers.get(powers.size()-1).getTitle());
			Editor editor = prefs.edit();
			editor.clear().commit();
		}
	}

	/**
	 * Draws powers of current active character on CharacterCreationActivity using LayoutInflater
	 */
	private void drawCurrentCharacterPowers(){
		LayoutInflater inflater = getLayoutInflater();
		LinearLayout llCharPowers = (LinearLayout) findViewById(R.id.llCharPowersCreation);
		llCharPowers.removeAllViews();
		ArrayList<Power> powers = currentCharacter.getCharPowers();
		for (Power power : powers){
			Log.d("myLog", "---Inside a loop of drawCurrentCharacterPowers(); CharacterCreationActivity---");
			LinearLayout item = (LinearLayout) inflater.inflate(R.layout.power_item, llCharPowers, false);
			
			TextView tvPowerTitle = (TextView) item.findViewById(R.id.tvPowerTitle);
			tvPowerTitle.setText(power.getTitle());
			
			TextView tvPowerType = (TextView) item.findViewById(R.id.tvPowerType);
			tvPowerType.setText(power.getType()+"");
			
			
			LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.gravity = Gravity.CENTER_VERTICAL;
			
			for (int i = 0; i < power.getMaxAmount(); i++){
				CheckBox cbPowerAmount = new CheckBox(this);
				cbPowerAmount.setLayoutParams(params);
				item.addView(cbPowerAmount);
			}
			llCharPowers.addView(item);
			
		}
	}
	
}
