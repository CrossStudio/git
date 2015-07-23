package dnd.dungeon_master_helper.listeners;

import android.app.Activity;
import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import dnd.dungeon_master_helper.DNDCharacter;
import dnd.dungeon_master_helper.R;
import dnd.dungeon_master_helper.activities.MainActivity;

public class DamageClickListener implements OnClickListener {

	Activity activity;
	DNDCharacter activeCharacter;
	
	@Override
	public void onClick(View v) {
		activity = (Activity) v.getContext();
		activeCharacter = MainActivity.activeCharacter;
		
		DialogFragment damageDialog = new DamageDialogFragment();
		damageDialog.show(activity.getFragmentManager(), "Damage Dialog");
	}
	
	class DamageDialogFragment extends DialogFragment implements OnClickListener{

		NumberPicker npDamage;
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedStance){
			getDialog().setTitle("Damage Dealt:");
			View v = inflater.inflate(R.layout.damage_dialog, null);
			
			npDamage = (NumberPicker) v.findViewById(R.id.npDamage);
			
			npDamage.setMinValue(0);
			npDamage.setMaxValue(100);
			npDamage.setWrapSelectorWheel(false);
			npDamage.setValue(0);
			
			Button btnDamage = (Button) v.findViewById(R.id.btnDamage);
			btnDamage.setOnClickListener(this);
			
			Button btnCancelDamage = (Button) v.findViewById(R.id.btnCancelDamage);
			btnCancelDamage.setOnClickListener(this);
			
			return v;
		}
		
		@Override
		public void onClick(View v) {
			switch(v.getId()){
	    	case R.id.btnDamage:
	    		activeCharacter.sufferDamage(npDamage.getValue());
	    		MainActivity.tvHPCurrentValue.setText(activeCharacter.getCharHPCurrent()+"");
	    		MainActivity.checkForBloodied();
	    		break;
	    	case R.id.btnCancelDamage:
	    		break;
	    }
	    dismiss();
			
		}
		
	}

}
