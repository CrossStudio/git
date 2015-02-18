package com.example.news;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import cross.xam.newsplusplus.R;

public class SearchActivity extends Activity {

	EditText etSearchText;
	ListView lvNewsResources;
	
	public ArrayList<NewsResource> allNewsResources = new ArrayList<NewsResource>();
	
	private static SearchActivity activity;
	
	public SearchActivity(){
		activity = this;
	}
	
	public static synchronized SearchActivity getInstance(){
		if (activity == null){
			activity = new SearchActivity();
		}
		return activity;
	}
	
	// ����� ��������� ��� Map
	final String ATTRIBUTE_NAME_TEXT = "text";
	final String ATTRIBUTE_NAME_IMAGE = "image";
	
	String[] names = {"������.��", "���.��", "������� ������", "�����.��", "�����.��", "BBC", "CNN"};
	String[] URLs = {"gazeta.ru", "ntv.ru", "gazeta.zn.ua", "vesti.ru",
			"lenta.ru", "bbc.co.uk", "cnn.com"};
	int[] images = {R.drawable.gazeta_og_image, R.drawable.ntv_logo, R.drawable.zn_logo,
			R.drawable.vesti_logo, R.drawable.lenta_logo, R.drawable.bbc_ogo, R.drawable.cnn_logo};
	
	ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>(names.length);
	Map<String, Object> map;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		assignViews();
		
		addAllNewsResources();
		
		String[] from = {ATTRIBUTE_NAME_TEXT, ATTRIBUTE_NAME_IMAGE};
		int[] to = {R.id.tvResource, R.id.ivLogo};
		
		SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.news_resource, from, to);
		lvNewsResources.setAdapter(adapter);
		
		lvNewsResources.setOnItemClickListener(new NewsItemClickListener());
	}
	
	/**
	 * Fill the data arrayList with map objects which contain all the data for each list item
	 */
	private void addAllNewsResources() {
		for (int i = 0; i < names.length; i++){
			NewsResource resource = new NewsResource(names[i], URLs[i], images[i]);
			allNewsResources.add(resource);
			map = new HashMap<String, Object>();
			map.put(ATTRIBUTE_NAME_TEXT, resource.getName());
			map.put(ATTRIBUTE_NAME_IMAGE, resource.getLogoID());
			data.add(map);
			resource.setPositionInList(data.indexOf(map));
		}
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
