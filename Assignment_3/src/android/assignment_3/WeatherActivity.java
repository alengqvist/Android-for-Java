package android.assignment_3;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

/**
 * This is a first prototype for a weather app. It is currently 
 * only downloading weather data for Växjö. 
 * 
 * This activity downloads weather data and constructs a WeatherReport,
 * a data structure containing weather data for a number of periods ahead.
 * 
 * The WeatherHandler is a SAX parser for the weather reports 
 * (forecast.xml) produced by www.yr.no. The handler constructs
 * a WeatherReport containing meta data for a given location
 * (e.g. city, country, last updated, next update) and a sequence 
 * of WeatherForecasts.
 * Each WeatherForecast represents a forecast (weather, rain, wind, etc)
 * for a given time period.
 * 
 * The next task is to construct a list based GUI where each row 
 * displays the weather data for a single period.
 * 
 *  
 * @author jlnmsi
 *
 */



public class WeatherActivity extends Activity {

	private WeatherReport report = null;
	WeatherAdapter adapter;
	private ListView listView;
	public static String PACKAGE_NAME;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        
        // Used in the Adapter.
        PACKAGE_NAME = getApplicationContext().getPackageName();
        
  
        // Create a WeatherAdapter in which all forecast will be displayed.
		adapter = new WeatherAdapter(this);
		listView = (ListView) findViewById(R.id.weather_list);
		listView.setEmptyView(findViewById(R.id.emptyWeatherList));
		listView.setAdapter(adapter);

		
		City city = (City) getIntent().getSerializableExtra("city");

		if (checkInternet() && city != null) {
			setTitle(getResources().getString(R.string.weather_in) + " " + city);
			new WeatherRetriever().execute(city);
		}
    }
    
    // Check if there's an Internet connection on the device (also if airplane mode is ON).
	private boolean checkInternet() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		
		if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
			return true;
		} else {
			listView.setEmptyView(findViewById(R.id.emptyWeatherList));
			return false;
		}
	}
    
    // Loops through all the forecast and adds them to the adapter.
    private void AddForecastToAdapter() {
    	if (this.report != null) {
    		for (WeatherForecast forecast : report) {
    			adapter.add(forecast);
    		}
    	}
    	else {
			listView.setEmptyView(findViewById(R.id.emptyWeatherList));
    	}
    }
    
    // Retriever that fetches Weather Reports and when finished calls the AddForecastToAdapter.
    private class WeatherRetriever extends AsyncTask<City, Void, WeatherReport> {
		protected WeatherReport doInBackground(City... cities) {
			try {
				return WeatherHandler.getWeatherReport(cities[0]);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

    	protected void onProgressUpdate(Void... progress) {

    	}

    	protected void onPostExecute(WeatherReport result) {
    		report = result;
    		AddForecastToAdapter();
    	}
    }
}
