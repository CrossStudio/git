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
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class NewResourceForm extends Activity {
	
	ListView lvCategory;
	final String ATTRIBUTE_NAME = "name";
	final String ATTRIBUTE_CONDITION = "condition";
	
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_resource_form);
		WindowManager.LayoutParams params = getWindow().getAttributes();  
	    Log.d("myLog", "Parameters: " + params);
	    params.height = 750;  
	    params.width = 700;  

	    this.getWindow().setAttributes(params); 
	    
	    lvCategory = (ListView) findViewById(R.id.lvCategory);
	    
	    ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
	    Map<String, Object> map;
	    
	    
	    List<String> categories = Arrays.asList(getResources().getStringArray(R.array.categories));
	    List<Boolean> categoryChosen = new ArrayList<Boolean>();
	    
	    //Add boolean placeholder with false in it for each category
	    for (String category : categories){
	    	categoryChosen.add(false);
	    }
	    
	    //Add all the categories data to a single 'data' piece
	    for (int i = 0; i < categories.size(); i++){
	    	map = new HashMap<String, Object>();
	    	map.put(ATTRIBUTE_NAME, categories.get(i));
	    	map.put(ATTRIBUTE_CONDITION, categoryChosen.get(i));
	    	data.add(map);
	    }
	    
	    String[] from = {ATTRIBUTE_NAME, ATTRIBUTE_CONDITION};
		int[] to = {R.id.cbCategory, R.id.cbCategory};
		
		//Insert all the categories data into a listView through adapter
		SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.category, from, to);
		lvCategory.setAdapter(adapter);
		
		
	}
}
