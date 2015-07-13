package dnd.dungeon_master_helper.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import dnd.dungeon_master_helper.DNDCharacter;
import dnd.dungeon_master_helper.Power;
import dnd.dungeon_master_helper.PowerType;
import dnd.dungeon_master_helper.R;

public class AddPowerActivity extends Activity {

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
		
		Intent intent = getIntent();
		currentCharacter = (DNDCharacter) intent.getExtras().get("character");
		
		try {
			Class.forName("Power");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
				String title = etPowerTitle.getText().toString();
				
				int typeID = rgPowerType.getCheckedRadioButtonId();
				
				int maxAmount = Integer.valueOf(etPowerAmount.getText().toString());
				
				switch (typeID) {
					case 0: 
						currentCharacter.getCharPowers().add(new Power(title, PowerType.ATWILL, maxAmount));
						break;
					case 1:
						currentCharacter.getCharPowers().add(new Power(title, PowerType.ENCOUNTER, maxAmount));
						break;
					case 2:
						currentCharacter.getCharPowers().add(new Power(title, PowerType.DAILY, maxAmount));
						break;
				}
			}
		});
	}
}