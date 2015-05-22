package dnd.dungeon_master_helper;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

	/**
	 * List of all the characters (player controlled, NPCs and monsters) that are currently in the game
	 */
	ArrayList<DNDCharacter> dndCharacterArrayList = new ArrayList<>();
	
	LinearLayout llInitiativeOrder;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		llInitiativeOrder = (LinearLayout) findViewById(R.id.llInitiativeOrder);
		
		DNDCharacter.addNewCharacterToGame("Father Tuck", "Human", "Cleric", dndCharacterArrayList);
		DNDCharacter.addNewCharacterToGame("Lol1", "Human", "Paladin", dndCharacterArrayList);
		DNDCharacter.addNewCharacterToGame("Leroy", "Dragonborn", "Fighter", dndCharacterArrayList);
		
		LayoutInflater inflater = getLayoutInflater();
		
		for (DNDCharacter character : dndCharacterArrayList)
		{
			View inflatedInitOrderCharName = inflater.inflate(R.layout.init_order_char_name, null, false);
			TextView tvInitOrderCharName = (TextView) inflatedInitOrderCharName.findViewById(R.id.tvInitOrderCharName);
			tvInitOrderCharName.setText(character.getCharName());
			llInitiativeOrder.addView(inflatedInitOrderCharName);
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
