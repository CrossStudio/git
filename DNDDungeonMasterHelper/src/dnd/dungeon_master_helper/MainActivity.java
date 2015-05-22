package dnd.dungeon_master_helper;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity {

	/**
	 * List of all the characters (player controlled, NPCs and monsters) that are currently in the game
	 */
	static ArrayList<DNDCharacter> dndCharacterArrayList = new ArrayList<>();
	
	static LinearLayout llInitiativeOrder;
	
	static Button btnNextCharacter;
	
	static TextView tvActiveCharacter;
	
	static DNDCharacter activeCharacter;
	
	static void setActiveCharacter(int charactersIndex)
	{
		activeCharacter = dndCharacterArrayList.get(charactersIndex);
	}
	
	String[] arrayOfModifierTypes = {"Attack", "AC", "Fortitude", "Reflex", "Will", "Damage"};
	
	String[] arrayOfModifierTargets;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		llInitiativeOrder = (LinearLayout) findViewById(R.id.llInitiativeOrder);
		btnNextCharacter = (Button) findViewById(R.id.btnNextCharacter);
		tvActiveCharacter = (TextView) findViewById(R.id.tvActiveCharName);
		
		
		btnNextCharacter.setOnClickListener(new NextCharacterClickListener());
		
		DNDCharacter.addNewCharacterToGame("Father Tuck", "Human", "Cleric", dndCharacterArrayList);
		DNDCharacter.addNewCharacterToGame("Lol1", "Human", "Paladin", dndCharacterArrayList);
		DNDCharacter.addNewCharacterToGame("Leroy", "Dragonborn", "Fighter", dndCharacterArrayList);
		
		arrayOfModifierTargets = new String[dndCharacterArrayList.size()];
		
		LayoutInflater inflater = getLayoutInflater();
		
		/**
		 * Adapter to set the values of spinner items of modifier types spinner
		 */
		{
			ArrayAdapter<String> modifierTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrayOfModifierTypes);
			modifierTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			Spinner spinModifierType = (Spinner) findViewById(R.id.spinModifierType);
			spinModifierType.setAdapter(modifierTypeAdapter);
		}
		
		for (DNDCharacter character : dndCharacterArrayList)
		{
			View inflatedInitOrderCharName = inflater.inflate(R.layout.init_order_char_name, null, false);
			TextView tvInitOrderCharName = (TextView) inflatedInitOrderCharName.findViewById(R.id.tvInitOrderCharName);
			tvInitOrderCharName.setText(character.getCharName());
			llInitiativeOrder.addView(inflatedInitOrderCharName);
			/**
			 * Fill in the array of modifier targets for spinner's adapter (transfer it to separate method, you dense mofo
			 */
			arrayOfModifierTargets[dndCharacterArrayList.indexOf(character)] = character.getCharName();
		}
		/**
		 * Adapter to set the values of spinner items of modifier targets spinner
		 */
		{
			ArrayAdapter<String> modifierTargetAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrayOfModifierTargets);
			modifierTargetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			Spinner spinModifierTarget = (Spinner) findViewById(R.id.spinModifierTarget);
			spinModifierTarget.setAdapter(modifierTargetAdapter);
		}
		
		if (dndCharacterArrayList.size() > 0)
		{
			activeCharacter = dndCharacterArrayList.get(0);
			tvActiveCharacter.setText("Active Character: " + activeCharacter.getCharName());
		}
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
