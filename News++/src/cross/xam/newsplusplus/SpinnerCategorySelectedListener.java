package cross.xam.newsplusplus;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

public class SpinnerCategorySelectedListener implements OnItemSelectedListener {

	static SearchActivity activity;
	
	static {
		activity = SearchActivity.getInstance();
	}
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}

}
