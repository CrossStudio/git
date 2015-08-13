package dnd.dungeon_master_helper2.listeners;

import java.util.ArrayList;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import dnd.dungeon_master_helper2.DNDCharacter;
import dnd.dungeon_master_helper2.R;
import dnd.dungeon_master_helper2.activities.MainActivity;

public class CurrentHPClickListener implements OnClickListener {

	Activity activity;
	DNDCharacter activeCharacter;
	ArrayList<String> hpChangeLog;
	
	@Override
	public void onClick(View v) {
		
		activity = (Activity) v.getContext();
		activeCharacter = MainActivity.activeCharacter;
		hpChangeLog = activeCharacter.getCharHPChanges();
		
		DialogFragment currentHPDialog = new CurrentHPDialogFragment();
		currentHPDialog.show(activity.getFragmentManager(), "Current HP log");
	}

	class CurrentHPDialogFragment extends DialogFragment implements OnClickListener{
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
			getDialog().setTitle("Damage and heals:");
			View hpLogView = inflater.inflate(R.layout.current_hp_log, null);
			
			loadHPChangesLog(hpLogView);		
			
			Button btnHPLogClose = (Button) hpLogView.findViewById(R.id.btnHPLogClose);
			btnHPLogClose.setOnClickListener(this);
			
			Button btnClearHPLog = (Button) hpLogView.findViewById(R.id.btnClearHPLog);
			btnClearHPLog.setOnClickListener(this);
			
			return hpLogView;
			
		}
		
		/**
		 * Loads all the heals and damages to HP that have been saved already
		 * @param parentView parent View to which each log item will be added
		 */
		private void loadHPChangesLog(View parentView) {
			LayoutInflater inflater = activity.getLayoutInflater();
			LinearLayout llCurrentHPLog = (LinearLayout) parentView.findViewById(R.id.llCurrentHPLog);

			for (int i = 0; i < hpChangeLog.size(); i++){
				String currentItem = hpChangeLog.get(i);
				View logItem = inflater.inflate(R.layout.hp_change_item, null);
				
				TextView tvHPLogItemText = (TextView) logItem.findViewById(R.id.tvHPLogItemText);
				tvHPLogItemText.setText(currentItem);
				
				llCurrentHPLog.addView(logItem);
			}

		}


		@Override
		public void onClick(View v) {
			switch (v.getId()){
			case R.id.btnHPLogClose:
				break;
			case R.id.btnClearHPLog:
				activeCharacter.getCharHPChanges().clear();
				break;
			}
			dismiss();
		}
		
	}
	
}
