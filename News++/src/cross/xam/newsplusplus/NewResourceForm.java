package cross.xam.newsplusplus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class NewResourceForm extends Activity {
	
	Button btnCreate;
	ListView lvCategory;
	
	
	final String ATTRIBUTE_NAME = "name";
	final String ATTRIBUTE_CONDITION = "condition";
	
	private static NewResourceForm activity;
	
	public NewResourceForm(){
		activity = this;
	}
	
	public static synchronized NewResourceForm getInstance(){
		if (activity == null){
			activity = new NewResourceForm();
		}
		return activity;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_resource_form);
		WindowManager.LayoutParams params = getWindow().getAttributes();  
	    Log.d("myLog", "Parameters: " + params);
	    params.height = 750;  
	    params.width = 700;  

	    this.getWindow().setAttributes(params); 
	    
	    assignViews();
	    
	    ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
	    Map<String, Object> map;
	    
	    
	    List<String> categories = Arrays.asList(getResources().getStringArray(R.array.categories));
	    List<Boolean> categoryChosen = new ArrayList<Boolean>();
	    
	    //Add boolean placeholder with false in it for each categories
	    for (String category : categories){
	    	categoryChosen.add(false);
	    }
	    
	    //Add all the categories data to a single 'data' piece
	    for (int i = 1; i < categories.size(); i++){
	    	map = new HashMap<String, Object>();
	    	map.put(ATTRIBUTE_NAME, categories.get(i));
	    	map.put(ATTRIBUTE_CONDITION, categoryChosen.get(i));
	    	data.add(map);
	    }
	    
	    String[] from = {ATTRIBUTE_NAME, ATTRIBUTE_CONDITION};
		int[] to = {R.id.cbCategory, R.id.cbCategory};
		
		//Insert all the categories data into a listView through adapter
		SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.category, from, to);
		lvCategory.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		lvCategory.setAdapter(adapter);
	}
	
	/**
	 * Assign variables to the corresponding views of the NewResourceForm activity
	 */
	private void assignViews(){
		btnCreate = (Button) findViewById(R.id.btnCreate);
		btnCreate.setOnClickListener(new CreateResourceClickListener());
		lvCategory = (ListView) findViewById(R.id.lvCategory);
	}
}
