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
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

public class SearchActivity extends Activity {

	final int MENU_DELETE = 0;
	
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
	
	// имена атрибутов для Map
	final String ATTRIBUTE_NAME_TEXT = "text";
	final String ATTRIBUTE_NAME_IMAGE = "image";
	
	String[] from = {ATTRIBUTE_NAME_TEXT, ATTRIBUTE_NAME_IMAGE};
	int[] to = {R.id.tvResource, R.id.ivLogo};
	
	private ArrayList<Map<String, Object>> data;
	private List<String> names = new ArrayList<String>();
	private List<String> URLs = new ArrayList<String>(); 
	TypedArray images;
	
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
		allNewsResources = new ArrayList<NewsResource>();
		Log.d("myLog", "----fillInitialResourceArrays()----");
		names = Arrays.asList(getResources().getStringArray(R.array.resource_names));
		URLs = Arrays.asList(getResources().getStringArray(R.array.URLs));
		images = getResources().obtainTypedArray(R.array.images);
		for (int i = 0; i < names.size(); i++){
			allNewsResources.add(new NewsResource(names.get(i), URLs.get(i), images.getResourceId(i, -1)));
		}
	}

	/**
	 * Add new resource with transfered parameters to the list of news resources 
	 * @param name - name of the news resource to appear on the screen
	 * @param URL - URL of the news resource to be launched to find info on the news resource
	 * @param categories - categories of the news resource to be assigned to
	 */
	public void addNewResource (String name, String URL, ArrayList<String> category){
		Log.d("myLog", "----addNewResource()----");
		NewsResource resource = new NewsResource(name, URL, -1);
		resource.addToCategories(category);
		allNewsResources.add(resource);
	}
	
	/**
	 * Add passed resource to the list of news resources
	 * @param resource to be added to the list of news resources
	 */
	private void addNewResource(NewsResource resource) {
		Log.d("myLog", "----addNewResource(NewsResource)----");
		allNewsResources.add(resource);
	}
	
	/**
	 * Fill the container 'data' with the information to populate the list of news resources
	 */
	private void fillAdapterData(){
		Log.d("myLog", "----fillAdapterData()----");
		data = new ArrayList<Map<String, Object>>();
		for (NewsResource resource : currentNewsResources){
			map = new HashMap<String, Object>();
			map.put(ATTRIBUTE_NAME_TEXT, resource.getName());
			map.put(ATTRIBUTE_NAME_IMAGE, resource.getLogoID());
			data.add(map);
			resource.setPositionInList(data.indexOf(map));
			Log.d("myLog", "Adapter data list added " + resource.getName());
		}
		fillListWithAdapter();
	}
	
	/**
	 * Populate the list with help of an adapter with values from 'data' 
	 */
	private void fillListWithAdapter() {
		Log.d("myLog", "----fillListWithAdapter()----");
		SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.news_resource, from, to);
		lvNewsResources.setAdapter(adapter);
		lvNewsResources.invalidateViews();
	}
	
	/**
	 * Sets currently shown resources for respectful categories
	 * @param chosenCategory - categories of resources that will be shown
	 */
	public void setCurrentResources(String chosenCategory){
		Log.d("myLog", "----setCurrentResources()----");
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
	}

	private void loadSavedResources() {
		sPref = getPreferences(MODE_PRIVATE);
	}

	/**
	 * Assigns views' variables to the appropriate xml views for the Search screen
	 */
	private void assignViews() {
		etSearchText = (EditText) findViewById(R.id.etSearchText);
		
		listViewSetUp();
		
		btnAddResource = (Button) findViewById(R.id.btnAddResource);
		btnAddResource.setOnClickListener(new OpenAddResourceFormClickListener());
		spCategories = (Spinner) findViewById(R.id.spCategories);
		spCategories.setOnItemSelectedListener(new SpinnerCategorySelectedListener());
	}
	
	/**
	 * Assigning ListView to a variable lvNewsResources
	 * Adding LongClickListener to each item of the list to open console action bar 
	 */
	private void listViewSetUp() {
		lvNewsResources = (ListView) findViewById(R.id.lvNewsResources);
		lvNewsResources.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		
		lvNewsResources.setOnItemClickListener(new NewsItemClickListener());
		lvNewsResources.setMultiChoiceModeListener(new MultiChoiceModeListener() {
		
		private ArrayList<Integer> checkedItemsPositions = new ArrayList<Integer>();	
		
	    @Override
	    public void onItemCheckedStateChanged(ActionMode mode, int position,
	                                          long id, boolean checked) {
	    	Log.d("myLog", "position = " + position + ", checked = " + checked);
	        checkedItemsPositions.add(position);
	    	// Here you can do something when items are selected/de-selected,
	        // such as update the title in the CAB
	    }

	    @Override
	    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
	        // Respond to clicks on the actions in the CAB
	    	switch (item.getItemId()){
	    		case R.id.delete:
	    			deleteSelectedItems();
	    			Log.d("myLog", "Delete action item was clicked");
	    			break;
    			default:
    				break;
	    	}
	    	return true;
	    }
	    
	    /**
	     * Removes selected list items
	     */
	    private void deleteSelectedItems() {
	    	deleteItemsFromPreferences();
		}

		/**
		 * Removes selected list items from the preferences file
		 */
		private void deleteItemsFromPreferences(){
			sPref = getSharedPreferences("SearchActivity", MODE_PRIVATE);
			Editor editor = sPref.edit();
			Set<String> resourcesURLsSet = sPref.getStringSet("resourcesURLs", new HashSet<String>());
			
			for (int item : checkedItemsPositions){
				Log.d("myLog", "Deleting item: " + currentNewsResources.get(item).getURL());
				String URLofResource = currentNewsResources.get(item).getURL();
				resourcesURLsSet.remove(URLofResource);
				editor.putStringSet("resourcesURLs", null);
				editor.commit();
				editor.putStringSet("resourcesURLs", resourcesURLsSet);
				editor.commit();
				editor.remove(URLofResource);
				editor.commit();
			}
			
			loadNewsResources();
			
		}
		
		@Override
	    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
	        // Inflate the menu for the CAB
	        MenuInflater inflater = mode.getMenuInflater();
	        inflater.inflate(R.menu.context, menu);
	        return true;
	    }

	    @Override
	    public void onDestroyActionMode(ActionMode mode) {
	    	checkedItemsPositions.clear();
	        // Here you can make any necessary updates to the activity when
	        // the CAB is removed. By default, selected items are deselected/unchecked.
	    }

	    @Override
	    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
	        // Here you can perform updates to the CAB due to
	        // an invalidate() request
	        return false;
	    }
	});
	}
	
	protected void onResume(){
		
		fillInitialResourceArrays();
			
		loadNewsResources();
	
		super.onResume();
	}

	private void loadNewsResources() {
		Log.d("myLog", "----loadNewsResources()----");
		sPref = getPreferences(MODE_PRIVATE);
		//If preferences are empty (first launch of the app) add empty set named 'resourcesLabels' where all new resources' names will go
		if (sPref.getAll().size() == 0){
			saveInitialSetOfResources();
		}
		//If preferences are not empty do as told (main goal is to fill allNewsResources with correct resources and remove excess ones)
		else {
			sPref = getSharedPreferences("SearchActivity", MODE_PRIVATE);
			Set<String> resourceURLsSets = sPref.getStringSet("resourcesURLs", null);
			Set<NewsResource> dummyRes = new HashSet<NewsResource>();
			Log.d("myLog", "allNewsResources.size() = " + allNewsResources.size());
			for (NewsResource resource : allNewsResources){
				Log.d("myLog", resource.getName() + " url is " + resource.getURL());
				Log.d("myLog", "Current preferences contain " + resource.getURL() + " " + resourceURLsSets.contains(resource.getURL()));
				if (!resourceURLsSets.contains(resource.getURL())){
					dummyRes.add(resource);
				}
			}
			for (NewsResource res : dummyRes){
				Log.d("myLog", "Resource to be removed: " + res.getName());
				allNewsResources.remove(res);
			}
			for (String resourceURL : resourceURLsSets){
				String URL = "";
				String label = "";
				ArrayList<String> categories = new ArrayList<String>();
				int logoID = -1;
				for (String parameter : sPref.getStringSet(resourceURL, null)){
					if (parameter.substring(0, 3).equals("URL")){
						URL = parameter.substring(4);
					}
					else if (parameter.substring(0, 3).equals("LAB")){
						label = parameter.substring(4);
					}
					else if (parameter.substring(0, 3).equals("CAT")){
						categories.add(parameter.substring(4));
					}
					else if (parameter.substring(0, 3).equals("IMG")){
						logoID = Integer.parseInt(parameter.substring(4));
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
					NewsResource resource = new NewsResource(label, URL, logoID);
					resource.addToCategories(categories);
					addNewResource(resource);
				}
			}
			
		}
		assignViews();
		setCurrentResources("All");
		spCategories.setSelection(0);
	}
	
	private void saveInitialSetOfResources() {
		Editor editor = sPref.edit();
		Set<String> newSet = new HashSet<String>();
		editor.putStringSet("resourcesURLs", newSet);
		editor.commit();
		Set<String> resourceURL = sPref.getStringSet("resourcesURLs", new HashSet<String>());
		for (NewsResource resource : allNewsResources){
			resourceURL.add(resource.getURL());
		}
		editor.putStringSet("resourcesURLs", null);
		editor.commit();
		editor.putStringSet("resourcesURLs", resourceURL);
		editor.commit();
		
		for (NewsResource resource : allNewsResources){
			Set<String> resourceInfo = new HashSet<String>();
			for (String category : resource.getCategories()){
				resourceInfo.add("CAT:" + category);
			}
			resourceInfo.add("URL:" + resource.getURL());
			resourceInfo.add("LAB:" + resource.getName());
			resourceInfo.add("IMG:" + resource.getLogoID());
			editor.putStringSet(resource.getURL(), resourceInfo);
			editor.commit();
		}
		Log.d("myLog", "Initial set of resources saved");
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
