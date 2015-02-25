package cross.xam.newsplusplus;

import java.util.ArrayList;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

public class CreateResourceClickListener implements OnClickListener {

	private static NewResourceForm currentActivity;
	
	private static SearchActivity searchActivity;
	
	private EditText etURL;
	private EditText etLabel;
	private ListView lvCategory;
	
	String URL;
	String label;
	ArrayList<String> category = new ArrayList<String>();
	
	@Override
	public void onClick(View v) {
		currentActivity = NewResourceForm.getInstance();
		searchActivity = SearchActivity.getInstance();
		assignViews();
		
		URL = etURL.getText().toString();
		
		label = etLabel.getText().toString();
		
		getCategoriesFromUserInput();
		
		//Adding the new resource to the list of all resources
		searchActivity.addNewResource(label, URL, category);
		
		//Returning to original search activity
		currentActivity.finish();
	}

	/**
	 * Add categories that user has selected to the list of categories
	 */
	private void getCategoriesFromUserInput() {
		for (int i = 0; i < lvCategory.getAdapter().getCount(); i++){
			CheckBox cbCategory = (CheckBox) lvCategory.getChildAt(i);
			if (cbCategory.isChecked()){
				category.add(cbCategory.getText().toString());
			}
		}		
	}

	/**
	 * Assign variables to the corresponding views of the NewResourceForm currentActivity
	 */
	private void assignViews(){
		etURL = (EditText) currentActivity.findViewById(R.id.etURL);
		etLabel = (EditText) currentActivity.findViewById(R.id.etLabel);
		lvCategory = (ListView) currentActivity.findViewById(R.id.lvCategory);
	}
	
}
