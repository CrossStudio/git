package cross.xam.newsplusplus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

public class SearchActivity extends Activity {

	EditText etSearchText;
	ListView lvNewsResources;
	Button btnAddResource;
	Spinner spCategories;
	
	SharedPreferences sPref;
	
	public ArrayList<NewsResource> allNewsResources = new ArrayList<NewsResource>();
	public ArrayList<NewsResource> currentNewsResources = new ArrayList<NewsResource>();
	
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
	
	String[] from = {ATTRIBUTE_NAME_TEXT, ATTRIBUTE_NAME_IMAGE};
	int[] to = {R.id.tvResource, R.id.ivLogo};
	
	private ArrayList<Map<String, Object>> data;
	private List<String> names = new ArrayList<String>();
	private List<String> URLs = new ArrayList<String>(); 
	int[] images = {R.drawable.gazeta_og_image, R.drawable.ntv_logo, R.drawable.zn_logo,
			R.drawable.vesti_logo, R.drawable.lenta_logo, R.drawable.bbc_ogo, R.drawable.cnn_logo};
	
	//ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>(names.length);
	Map<String, Object> map;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);

	}
	
	/**
	 * Fill the names and URLs arrays with info about resources' names and urls
	 */
	private void fillInitialResourceArrays() {
		names = Arrays.asList(getResources().getStringArray(R.array.resource_names));
		URLs = Arrays.asList(getResources().getStringArray(R.array.URLs));		
	}
	
	/**
	 * Fill the list of news resources with starting pack of resources
	 */
	private void addInitialNewsResources(){
		for (int i = 0; i < names.size(); i++){
			boolean unique = true;
			for (NewsResource curRes : allNewsResources){
				if (curRes.getURL().equals(URLs.get(i))){
					unique = false;
					break;
				}
				else if (curRes.getName().equals(names.get(i))){
					unique = false;
					break;
				}
			}
			if (unique){
				NewsResource resource = new NewsResource(names.get(i), URLs.get(i), images[i]);
				allNewsResources.add(resource);
			}
		}
	}

	/**
	 * Add new resource with transfered parameters to the list of news resources 
	 * @param name - name of the news resource to appear on the screen
	 * @param URL - URL of the news resource to be launched to find info on the news resource
	 * @param categories - categories of the news resource to be assigned to
	 */
	public void addNewResource (String name, String URL, ArrayList<String> category){
		NewsResource resource = new NewsResource(name, URL, -1);
		resource.addToCategories(category);
		allNewsResources.add(resource);
	}
	
	/**
	 * Fill the container 'data' with the information to populate the list of news resources
	 */
	private void fillAdapterData(){
		data = new ArrayList<Map<String, Object>>();
		for (NewsResource resource : currentNewsResources){
			map = new HashMap<String, Object>();
			map.put(ATTRIBUTE_NAME_TEXT, resource.getName());
			map.put(ATTRIBUTE_NAME_IMAGE, resource.getLogoID());
			
			data.add(map);
			resource.setPositionInList(data.indexOf(map));
		}
	}
	
	/**
	 * Populate the list with help of an adapter with values from 'data' 
	 */
	private void fillListWithAdapter() {
		SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.news_resource, from, to);
		lvNewsResources.setAdapter(adapter);
		lvNewsResources.invalidateViews();
	}
	
	/**
	 * Sets currently shown resources for respectful categories
	 * @param chosenCategory - categories of resources that will be shown
	 */
	public void setCurrentResources(String chosenCategory){
		currentNewsResources.clear();
		loadSavedResources();
		for (NewsResource resource : allNewsResources){
			for (String resourceCategory : resource.getCategories()){
				
				if (resourceCategory.equals(chosenCategory)){
					Log.d("myLog", "Adding " + resource.getName() + " to current resources");
					currentNewsResources.add(resource);
				}
			}
		}
		fillAdapterData();
		fillListWithAdapter();
	}

	private void loadSavedResources() {
		sPref = getPreferences(MODE_PRIVATE);
		Set<String> savedCategory = new HashSet<String>();
		//savedCategory = sPref.getStringSet(key, defValues)
	}

	/**
	 * Assigns views' variables to the appropriate xml views for the Search screen
	 */
	private void assignViews() {
		etSearchText = (EditText) findViewById(R.id.etSearchText);
		lvNewsResources = (ListView) findViewById(R.id.lvNewsResources);
		btnAddResource = (Button) findViewById(R.id.btnAddResource);
		btnAddResource.setOnClickListener(new OpenAddResourceFormClickListener());
		spCategories = (Spinner) findViewById(R.id.spCategories);
		spCategories.setOnItemSelectedListener(new SpinnerCategorySelectedListener());
	}
	
	protected void onResume(){
		/*for (NewsResource resource : allNewsResources){
			Log.d("myLog", resource.getName() + " categories: ");
			for (String category : resource.getCategories()){
				Log.d("myLog", category);
			}
			Log.d("myLog", "-----------------");
		}*/
		
		checkPreferences();
		
		assignViews();
		
		fillInitialResourceArrays();
			
		addInitialNewsResources();
		
		//Initial category of news resources to be shown
		setCurrentResources("All");
		
		fillAdapterData();
		
		fillListWithAdapter();
		
		spCategories.setSelection(0);
		
		lvNewsResources.setOnItemClickListener(new NewsItemClickListener());
		super.onResume();
	}

	private void checkPreferences() {
		sPref = getPreferences(MODE_PRIVATE);
		//If preferences are empty (first launch of the app) add empty set named 'resourcesLabels' where all new resources' names will go
		if (sPref.getAll().size() == 0){
			Editor editor = sPref.edit();
			Set<String> newSet = new HashSet<String>();
			editor.putStringSet("resourcesURLs", newSet);
			editor.commit();
		}
		//If preferences are not empty do as told
		else {
			sPref = getSharedPreferences("SearchActivity", MODE_PRIVATE);
			Set<String> resourceURLsSets = sPref.getStringSet("resourcesURLs", null);
			for (String resourceURL : resourceURLsSets){
				String URL = "";
				String label = "";
				ArrayList<String> category = new ArrayList<String>();
				for (String parameter : sPref.getStringSet(resourceURL, null)){
					if (parameter.substring(0, 3).equals("URL")){
						URL = parameter.substring(4);
					}
					else if (parameter.substring(0, 3).equals("LAB")){
						label = parameter.substring(4);
					}
					else if (parameter.substring(0, 3).equals("CAT")){
						category.add(parameter.substring(4));
					}
				}
				boolean unique = true;
				for (NewsResource curRes : allNewsResources){
					if (curRes.getURL().equals(URL)){
						unique = false;
						break;
					}
					else if (curRes.getName().equals(label)){
						unique = false;
						break;
					}
				}
				if (unique){
					addNewResource(label, URL, category);
				}
			}
		}
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
