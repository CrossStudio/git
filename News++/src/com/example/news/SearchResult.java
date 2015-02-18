package com.example.news;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import cross.xam.newsplusplus.R;

public class SearchResult extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_result);
		
		Intent intent = getIntent();
		
		WebView myWebView = (WebView) findViewById(R.id.wvResult);

		myWebView.setWebViewClient(new WebViewClient() {
	        @Override
	        public boolean shouldOverrideUrlLoading(WebView view, String url) {
	            return false;
	        }
	    });
		myWebView.loadUrl(intent.getStringExtra("Query"));
	}
}