package dnd.dungeon_master_helper.listeners;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import dnd.dungeon_master_helper.R;

public class HealClickListener implements OnClickListener {

	final static int DIALOG_TAG = 1;
	
	Activity activity;
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		activity = (Activity) v.getContext();
		DialogFragment dialog = new FireMissilesDialogFragment();
		dialog.show(activity.getFragmentManager(), "heal dialog");
	}

	class FireMissilesDialogFragment extends DialogFragment implements OnClickListener{
		final String LOG_TAG = "myLogs";

		  public View onCreateView(LayoutInflater inflater, ViewGroup container,
		      Bundle savedInstanceState) {
		    getDialog().setTitle("Title!");
		    View v = inflater.inflate(R.layout.heal_dialog, null);
		    
		    NumberPicker npHeal = (NumberPicker) v.findViewById(R.id.npHeal);
		    npHeal.setMinValue(0);
		    npHeal.setMaxValue(100);
		    npHeal.setWrapSelectorWheel(false);
		    npHeal.setValue(0);
		    
		    Button btnHeal = (Button) v.findViewById(R.id.btnHeal);
		    btnHeal.setOnClickListener(this);
		    
		    Button btnCancelHeal = (Button) v.findViewById(R.id.btnCancelHeal);
		    btnCancelHeal.setOnClickListener(this);
		    
		    return v;
		  }

		  public void onClick(View v) {
		    Log.d(LOG_TAG, "Dialog 1: " + ((Button) v).getText());
		    dismiss();
		  }

		  public void onDismiss(DialogInterface dialog) {
		    super.onDismiss(dialog);
		    Log.d(LOG_TAG, "Dialog 1: onDismiss");
		  }

		  public void onCancel(DialogInterface dialog) {
		    super.onCancel(dialog);
		    Log.d(LOG_TAG, "Dialog 1: onCancel");
		  }
	}
	
	
}
