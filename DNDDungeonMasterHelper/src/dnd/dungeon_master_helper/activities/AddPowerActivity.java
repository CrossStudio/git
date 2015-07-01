package dnd.dungeon_master_helper.activities;

import android.app.Activity;
import android.os.Bundle;
import dnd.dungeon_master_helper.R;

public class AddPowerActivity extends Activity {

	@Override
	public void onCreate(Bundle savedState){
		super.onCreate(savedState);
		setContentView(R.layout.power_creation);
		setTitle("Create New Power");
		
	}
}
