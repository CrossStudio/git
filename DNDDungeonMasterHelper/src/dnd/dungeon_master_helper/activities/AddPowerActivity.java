package dnd.dungeon_master_helper.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import dnd.dungeon_master_helper.Power;
import dnd.dungeon_master_helper.R;

public class AddPowerActivity extends Activity {

	Button btnPowerCancel;
	Button btnPowerAdd;
	RadioGroup rgPowerType;
	EditText etPowerTitle;
	EditText etPowerAmount;
	
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
				String title = etPowerTitle.getText().toString();
				
				RadioButton rbClicked = (RadioButton) rgPowerType.findViewById(rgPowerType.getCheckedRadioButtonId());
				String type = (String) rbClicked.getText();
				
				int maxAmount = Integer.valueOf(etPowerAmount.getText().toString());

				intent.putExtra("title", title);
				intent.putExtra("type", type);
				intent.putExtra("maxAmount", maxAmount);
			}
		});
	}
}