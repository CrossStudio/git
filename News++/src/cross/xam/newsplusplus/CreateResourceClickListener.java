package cross.xam.newsplusplus;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

public class CreateResourceClickListener implements OnClickListener {

	private static NewResourceForm currentActivity;
	
	private static SearchActivity searchActivity;
	
	static {
		currentActivity = NewResourceForm.getInstance();
		searchActivity = SearchActivity.getInstance();
	}
	
	private EditText etURL;
	private EditText etLabel;
	private ListView lvCategory;
	
	String URL;
	String label;
	ArrayList<String> categories = new ArrayList<String>();
	SharedPreferences sPref;
	Editor editor;
	
	@Override
	public void onClick(View v) {
		
		assignViews();
		
		URL = etURL.getText().toString();
		
		label = etLabel.getText().toString();
		
		getCategoriesFromUserInput();
		
		//Adding the new resource to the list of all resources
		searchActivity.addNewResource(label, URL, categories);
		
		//Saving this newly added resource to a file for later use
		saveResourceToFile();
		
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
				categories.add(cbCategory.getText().toString());
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
	
	/**
	 * Saves info on the new resource to a file for later use
	 */
	private void saveResourceToFile(){
		
		sPref = currentActivity.getSharedPreferences("SearchActivity", currentActivity.MODE_PRIVATE);
		editor = sPref.edit();
		
		if (!addNewResourceIdentifier()){
			return;
		}

		addNewResourceInfo();
	
	}

	/**
	 * Adds new Resource id (its URL) to an appropriate file 
	 * @return - true if this URL is a new one, false if it is already in the list
	 */
	private boolean addNewResourceIdentifier() {	
		Set<String> resourceURL = sPref.getStringSet("resourcesURLs", new HashSet<String>());
		if (resourceURL.contains(URL)){
			return false;
		}
		resourceURL.add(URL);
		editor.putStringSet("resourcesURLs", null);
		editor.commit();
		editor.putStringSet("resourcesURLs", resourceURL);
		editor.commit();
		return true;
	}

	/**
	 * Fills set that will be saved into file as info about new resource
	 */
	private void addNewResourceInfo() {
		Set<String> resourceInfo = new HashSet<String>();
		for (String category : categories){
			resourceInfo.add("CAT:" + category);
		}
		resourceInfo.add("URL:" + URL);
		resourceInfo.add("LAB:" + label);
		editor.putStringSet(URL, resourceInfo);
		editor.commit();
	}
	
}
