package dnd.dungeon_master_helper;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends Activity {

	ArrayList<DNDCharacter> dndCharacterArrayList = new ArrayList<>();
	
	TextView tvCharacters;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		tvCharacters = (TextView) findViewById(R.id.tvHelloWorld);
		
		DNDCharacter.addNewCharacterToGame("Father Tuck", "Human", "Cleric", dndCharacterArrayList);
		DNDCharacter.addNewCharacterToGame("Lol1", "Human", "Paladin", dndCharacterArrayList);
		DNDCharacter.addNewCharacterToGame("Leroy", "Dragonborn", "Fighter", dndCharacterArrayList);
		for (DNDCharacter character : dndCharacterArrayList)
		{
			tvCharacters.setText(tvCharacters.getText() + " " + character.getCharName());
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
