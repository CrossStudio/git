package dnd.dungeon_master_helper.listeners;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import dnd.dungeon_master_helper.DNDCharacter;
import dnd.dungeon_master_helper.Power;
import dnd.dungeon_master_helper.activities.AddPowerActivity;
import dnd.dungeon_master_helper.activities.CharacterCreationActivity;

public class AddNewPowerListener implements OnClickListener {

	Activity activity;
	
	SharedPreferences prefs;
	DNDCharacter currentCharacter;
	
	@Override
	public void onClick(View v) {
		activity = (Activity) v.getContext();
		currentCharacter = CharacterCreationActivity.currentCharacter;
		fillCharacterParameters();
		
		Intent intent = new Intent(v.getContext(), AddPowerActivity.class);
		v.getContext().startActivity(intent);
	}

	private void fillCharacterParameters(){
		setCharacterParameters();
		saveCharacterParameters();
	}
	
	/**
	 * Sets current currentCharacter parameters
	 */
	private void setCharacterParameters() {
		Log.d("myLog", "---setCharacterParameters() in CharacterCreationActivity---");
		Log.d("myLog", "currentCharacter = " + currentCharacter);
		currentCharacter.setCharName(CharacterCreationActivity.etCharName.getText().toString());
		currentCharacter.setCharClass(CharacterCreationActivity.spCharClass.getSelectedItem().toString());
		if (CharacterCreationActivity.etMaxHP.getText().length() > 0){
			currentCharacter.setCharHPMax(Integer.parseInt(CharacterCreationActivity.etMaxHP.getText().toString()));
			currentCharacter.setCharHPCurrent(currentCharacter.getCharHPMax());
		}
		if (CharacterCreationActivity.etCurrentInitiative.getText().length() > 0){
			currentCharacter.setCharInitiativeEncounter(Integer.parseInt(CharacterCreationActivity.etCurrentInitiative.getText().toString()));
		}
	}

	/**
	 * Saves currentCharacter's parameters in Preferences
	 */
	private void saveCharacterParameters() {
		prefs = activity.getSharedPreferences("CurrentCharacter", activity.MODE_PRIVATE);
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
	
	
}
