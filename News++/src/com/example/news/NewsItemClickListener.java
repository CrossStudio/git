package com.example.news;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;

public class NewsItemClickListener implements OnItemClickListener {
	
	static SearchActivity activity;
	
	static final String GOOGLE_IM_LUCKY_URL = "http://google.com/search?btnI=&q=";
	
	static final String GOOGLE_OQ = "&oq=";
	
	static final String GOOGLE_SITE_SEARCH = "+site%3A";
	
	EditText etSearchText;
	
	static {
		activity = SearchActivity.getInstance();
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {	
		etSearchText = (EditText) activity.findViewById(R.id.etSearchText);
		
		NewsResource chosenResource = activity.allNewsResources.get(position);
		
		String query = makeQuery(""+etSearchText.getText(), chosenResource);
		
		Intent intent = new Intent(activity, SearchResult.class);
		intent.putExtra("Query", query);
		activity.startActivity(intent);
	}

	/**
	 * Creates a google query consisting from actual text to be searched, site to search the text in and special google parameters
	 * @param text text that will be searched in google
	 * @param chosenResource news resource which is the source of information for this search
	 * @return google query that can be used in a browser
	 */
	private String makeQuery(String text, NewsResource chosenResource) {
		String[] words = text.split(" ");
		String query = "";
		for (String word : words){
			query += word + "+";
		}
		query = query.substring(0, query.length()-1);
		query = GOOGLE_IM_LUCKY_URL + query + GOOGLE_SITE_SEARCH + chosenResource.getURL()
				+ GOOGLE_OQ + query + GOOGLE_SITE_SEARCH + chosenResource.getURL();
		return query;
	}

}
