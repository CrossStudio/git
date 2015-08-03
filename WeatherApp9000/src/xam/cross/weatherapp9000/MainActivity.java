package xam.cross.weatherapp9000;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends Activity implements OnMapReadyCallback {
	
	EditText etInput;
	TextView tvCity;
	TextView tvWeatherDescription;
	TextView tvTemperature;
	TextView tvPressure;
	TextView tvHumidity;
	ImageView ivWeatherIcon;
 
    // JSON Node names
   
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
        
        new GetWeather().execute();
        
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.fMap);
        mapFragment.getMapAsync(this);

    }

    private void initializeViews() {
    	etInput = (EditText) findViewById(R.id.etInput);
    	tvCity = (TextView) findViewById(R.id.tvCity);
    	tvWeatherDescription = (TextView) findViewById(R.id.tvWeatherDescription);
    	tvTemperature = (TextView) findViewById(R.id.tvTemperature);
    	tvPressure = (TextView) findViewById(R.id.tvPressure);
    	tvHumidity = (TextView) findViewById(R.id.tvHumidity);
    	ivWeatherIcon = (ImageView) findViewById(R.id.ivWeatherIcon);
	}

	@Override
    public void onMapReady(GoogleMap map) {
        LatLng sydney = new LatLng(-33.867, 151.206);

        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));

        map.addMarker(new MarkerOptions()
                .title("Sydney")
                .snippet("The most populous city in Australia.")
                .position(sydney));

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
            String jsonStr = sh.makeServiceCall("http://api.openweathermap.org/data/2.5/weather?q=London,uk", ServiceHandler.GET);
 
            Log.d("myLog", "> " + jsonStr);
 
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                     
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
                        
                        Log.d("myLog", "Weather id = " + weatherId);
                        Log.d("myLog", "Weather main = " + weatherMain);
                        Log.d("myLog", "Weather description = " + weatherDescription);
                        Log.d("myLog", "Weather icon = " + weatherIcon);
                        
                        
                   
                    }
                    
                    mainTemp = main.getString(TAG_MAIN_TEMP);
                    mainPressure = main.getString(TAG_MAIN_PRESSURE);
                    mainHumidity = main.getString(TAG_MAIN_HUMIDITY);
                    
                    Log.d("myLog", "Main temperature = " + mainTemp);
                    Log.d("myLog", "Main pressure = " + mainPressure);
                    Log.d("myLog", "Main humidity = " + mainHumidity);
                    
                    
          
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
 
            return null;
        }
 
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            tvWeatherDescription.setText(weatherDescription);

            ivWeatherIcon.setImageURI(Uri.parse(getWeatherIcon(weatherIcon)));
            tvCity.setText(etInput.getText().toString());
            tvTemperature.setText(mainTemp);
            tvPressure.setText(mainPressure);
            tvHumidity.setText(mainHumidity);
        }
 
    }

	public String getWeatherIcon(String weatherIcon) {
		String iconUrl = "http://api.openweathermap.org/img/w/";
		
		iconUrl += weatherIcon;
		
		iconUrl += ".png";
		
		return iconUrl;
	}
	
}

