package xam.cross.weatherapp9000;

import java.io.InputStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity{
	
	//Views names
	
	EditText etInput;
	TextView tvCity;
	TextView tvWeatherDescription;
	TextView tvTemperature;
	TextView tvPressure;
	TextView tvHumidity;
	ImageView ivWeatherIcon;
	Button btnGetWeather;
	WebView wvMap;
	
    // JSON Node names
   
	private static final String TAG_CITY_NAME = "name";
    private static final String TAG_COORD = "coord";
    private static final String TAG_WEATHER = "weather";
    private static final String TAG_WEATHER_ID = "id";
    private static final String TAG_WEATHER_MAIN = "main";
    private static final String TAG_WEATHER_DESCRIPTION = "description";
    private static final String TAG_WEATHER_ICON = "icon";
    private static final String TAG_MAIN = "main";
    private static final String TAG_MAIN_TEMP = "temp";
    private static final String TAG_MAIN_PRESSURE = "pressure";
    private static final String TAG_MAIN_HUMIDITY = "humidity";
    
    private static String cityName;
    private static String weatherId;
    private static String weatherMain;
    private static String weatherDescription;
    private static String weatherIcon;
    private static String mainTemp;
    private static String mainPressure;
    private static String mainHumidity;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initializeViews();

    }

    /**
     * Initializes all views that are present within current MainActivity
     */
    private void initializeViews() {
    	etInput = (EditText) findViewById(R.id.etInput);
    	tvCity = (TextView) findViewById(R.id.tvCity);
    	tvWeatherDescription = (TextView) findViewById(R.id.tvWeatherDescription);
    	tvTemperature = (TextView) findViewById(R.id.tvTemperature);
    	tvPressure = (TextView) findViewById(R.id.tvPressure);
    	tvHumidity = (TextView) findViewById(R.id.tvHumidity);
    	ivWeatherIcon = (ImageView) findViewById(R.id.ivWeatherIcon);
    	btnGetWeather = (Button) findViewById(R.id.btnGetWeather);
    	wvMap = (WebView) findViewById(R.id.wvMap);
    	
    	btnGetWeather.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new GetWeather().execute();
			}
		});
	}

    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
	
	/**
     * Async task class to get json by making HTTP call
     * */
    private class GetWeather extends AsyncTask<Void, Void, Void> {
 
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
 
        }
 
        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler sh = new ServiceHandler();
 
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(getJSONqueryURL(etInput.getText().toString()), ServiceHandler.GET);
 
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    
                    cityName = jsonObj.getString(TAG_CITY_NAME);
                    
                    // Getting JSON Array node
                    JSONArray weather = jsonObj.getJSONArray(TAG_WEATHER);
                    JSONObject main = jsonObj.getJSONObject(TAG_MAIN);               
                    
                    // looping through weather array
                    for (int i = 0; i < weather.length(); i++) {
                        JSONObject c = weather.getJSONObject(i);
                         
                        weatherId = c.getString(TAG_WEATHER_ID);
                        weatherMain = c.getString(TAG_WEATHER_MAIN);
                        weatherDescription = c.getString(TAG_WEATHER_DESCRIPTION);
                        weatherIcon = c.getString(TAG_WEATHER_ICON);
      
                    }
                    
                    mainTemp = main.getString(TAG_MAIN_TEMP);
                    mainPressure = main.getString(TAG_MAIN_PRESSURE);
                    mainHumidity = main.getString(TAG_MAIN_HUMIDITY);     
                            
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                catch (IllegalArgumentException e){
                	e.printStackTrace();
                	Toast.makeText(MainActivity.this, "Please enter correct location", Toast.LENGTH_LONG).show();
                }
            }
 
            return null;
        }
 
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            tvWeatherDescription.setText(weatherDescription);

            new DownloadImageTask(ivWeatherIcon).execute(getWeatherIconURL(weatherIcon));
            tvCity.setText(cityName);
            tvTemperature.setText(mainTemp);
            tvPressure.setText(mainPressure);
            tvHumidity.setText(mainHumidity);
            wvMap.setWebViewClient(new WebViewClient());
            WebSettings webSettings= wvMap.getSettings();
            webSettings.setJavaScriptEnabled(true);
            wvMap.loadUrl(getCityOnMapURL(cityName));
        }
 
    }
    
    /**
     * Returns url which will be called for JSON response
     * @param city - city of your choice for which weather will be looked
     * @return url which will be called for JSON response
     */
    private static String getJSONqueryURL(String city){
    	String queryURL = "http://api.openweathermap.org/data/2.5/weather?q=";
    	city = city.replace(" ", "%20");
    	queryURL += city;
    	return queryURL;
    }
    
    /**
     * Returns url which will be called to draw google map for a correct location
     * @param city - location on which google maps will be centered
     * @return url which will be called to draw google map for a correct location
     */
    private static String getCityOnMapURL(String city){
    	String cityOnMapURL = "https://www.google.com/maps/place/";
    	cityOnMapURL += city;
    	return cityOnMapURL;
    }
    
    /**
     * Returns url which will be used to get weather icon
     * @param weatherIcon - weather icon id
     * @return url which will be used to get weather icon
     */
	private static String getWeatherIconURL(String weatherIcon) {
		String iconUrl = "http://api.openweathermap.org/img/w/";
		
		iconUrl += weatherIcon;
		
		iconUrl += ".png";
		
		return iconUrl;
	}
	
	
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
	    ImageView bmImage;

	    public DownloadImageTask(ImageView bmImage) {
	        this.bmImage = bmImage;
	    }

	    protected Bitmap doInBackground(String... urls) {
	        String urlDisplay = urls[0];
	        Bitmap dummyBitmap = null;
	        try {
	            InputStream in = new java.net.URL(urlDisplay).openStream();
	            dummyBitmap = BitmapFactory.decodeStream(in);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return dummyBitmap;
	    }

	    protected void onPostExecute(Bitmap result) {
	        bmImage.setImageBitmap(result);
	    }
	}
	
}

