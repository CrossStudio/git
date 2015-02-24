package cross.xam.newsplusplus;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ListView;

public class NewResourceForm extends Activity {
	
	ListView lvCategory;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_resource_form);
		WindowManager.LayoutParams params = getWindow().getAttributes();  
	    Log.d("myLog", "Parameters: " + params);
	    params.height = 500;  
	    params.width = 700;  

	    this.getWindow().setAttributes(params); 
	    
	    lvCategory = (ListView) findViewById(R.id.lvCategory);
	}
}
