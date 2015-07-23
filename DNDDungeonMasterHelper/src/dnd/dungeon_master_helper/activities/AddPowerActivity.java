package dnd.dungeon_master_helper.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import dnd.dungeon_master_helper.DNDCharacter;
import dnd.dungeon_master_helper.Power;
import dnd.dungeon_master_helper.PowerType;
import dnd.dungeon_master_helper.R;

public class AddPowerActivity extends Activity {

	SharedPreferences prefs;
	
	Button btnPowerCancel;
	Button btnPowerAdd;
	RadioGroup rgPowerType;
	EditText etPowerTitle;
	EditText etPowerAmount;
	
	DNDCharacter currentCharacter;
	
	@Override
	public void onCreate(Bundle savedState){
		super.onCreate(savedState);
		setContentView(R.layout.power_creation);
		setTitle("Create New Power");

		initializeViews();
	}

	private void initializeViews() {
		btnPowerCancel = (Button) findViewById(R.id.btnPowerCancel);
		btnPowerAdd = (Button) findViewById(R.id.btnPowerAdd);
		rgPowerType = (RadioGroup) findViewById(R.id.rgPowerType);
		etPowerTitle = (EditText) findViewById(R.id.etPowerTitle);
		etPowerAmount = (EditText) findViewById(R.id.etPowerAmount);
		
		btnPowerCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AddPowerActivity.this, CharacterCreationActivity.class);
				AddPowerActivity.this.startActivity(intent);
			}
		});
		
		btnPowerAdd.setOnClickListener(new OnClickListener() {
			Intent intent;
			@Override
			public void onClick(View v) {
				intent = new Intent(AddPowerActivity.this, CharacterCreationActivity.class);
				
				addNewPowerToPowersList();
				AddPowerActivity.this.startActivity(intent);
			}

			/**
			 * Creates new power based on the entered data and sends it to CharacterCreationActivity
			 */
			private void addNewPowerToPowersList() {
				prefs = getSharedPreferences("NewPower", MODE_PRIVATE);
				Editor editor = prefs.edit();
				
				String title = etPowerTitle.getText().toString();
				editor.putString("newPowerTitle", title);
				
				int typeID = rgPowerType.getCheckedRadioButtonId();
				editor.putInt("newPowerTypeID", typeID);
				
				int maxAmount = Integer.valueOf(etPowerAmount.getText().toString());
				editor.putInt("newPowerMaxAmount", maxAmount);
				
				int curAmount = maxAmount;
				editor.putInt("newPowerCurAmount", curAmount);
				
				editor.commit();
			}
		});
	}
}