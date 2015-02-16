package com.example.news;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

public class SearchActivity extends Activity {

	EditText etSearchText;
	ListView lvNewsResources;
	
	// имена атрибутов для Map
	final String ATTRIBUTE_NAME_TEXT = "text";
	final String ATTRIBUTE_NAME_IMAGE = "image";
	
	String[] texts = {"Газета.РУ", "НТВ.РУ", "Зеркало Недели"};
	int[] img = {R.drawable.gazeta_og_image, R.drawable.ntv_logo, R.drawable.zn_logo};
	
	ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>(texts.length);
	Map<String, Object> map;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		assignViews();
		
		//Fill the data arrayList with map objects which contain all the data for each list item
		for (int i = 0; i < texts.length; i++){
			map = new HashMap<String, Object>();
			map.put(ATTRIBUTE_NAME_TEXT, texts[i]);
			map.put(ATTRIBUTE_NAME_IMAGE, img[i]);
			data.add(map);
		}
		
		String[] from = {ATTRIBUTE_NAME_TEXT,ATTRIBUTE_NAME_IMAGE};
		int[] to = {R.id.tvResource, R.id.ivLogo};
		
		SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.news_resource, from, to);
		lvNewsResources.setAdapter(adapter);
		
		RelativeLayout rlClickable = (RelativeLayout) findViewById(R.id.rlClickable);
		rlClickable.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	/**
	 * Assigns views' variables to the appropriate xml views for the Search screen
	 */
	private void assignViews() {
		etSearchText = (EditText) findViewById(R.id.etSearchText);
		lvNewsResources = (ListView) findViewById(R.id.lvNewsResources);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
