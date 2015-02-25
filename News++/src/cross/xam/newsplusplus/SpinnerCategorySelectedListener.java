package cross.xam.newsplusplus;

import java.util.Arrays;
import java.util.List;

import android.util.Log;
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
		List<String> categories = Arrays.asList(activity.getResources().getStringArray(R.array.categories));
		Log.d("myLog", "Current category is: " + categories.get(position));
		activity.setCurrentResources(categories.get(position));
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}

}
