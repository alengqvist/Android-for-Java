package android.assignment1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

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

public class VaxjoWeather extends ListActivity {

	private InputStream input;
	private WeatherReport report = null;
	WeatherAdapter adapter;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        adapter = new WeatherAdapter(this);
        setListAdapter(adapter);

        
        try {
        	URL url = new URL("http://www.yr.no/sted/Sverige/Kronoberg/V%E4xj%F6/forecast.xml");
        	AsyncTask task = new WeatherRetriever().execute(url);
        } catch (IOException ioe ) {
    		ioe.printStackTrace();
    	}
        
    }
    
    private void PrintReportToConsole() {
    	if (this.report != null) {
    		for (WeatherForecast forecast : report) {
    			adapter.add(forecast);
    		}
    	}
    	else {
    		System.out.println("Weather report has not been loaded.");
    	}
    }
    
    private class WeatherRetriever extends AsyncTask<URL, Void, WeatherReport> {
    	protected WeatherReport doInBackground(URL... urls) {
    		try {
    			return WeatherHandler.getWeatherReport(urls[0]);
    		} catch (Exception e) {
    			throw new RuntimeException(e);
    		} 
    	}

    	protected void onProgressUpdate(Void... progress) {

    	}

    	protected void onPostExecute(WeatherReport result) {
    		report = result;
    		PrintReportToConsole();
    	}
    }
    
	private class ViewHolder {
		TextView date;
		TextView time;
		TextView temperature;
		TextView precipitation;
		ImageView icon;
	}

    
    class WeatherAdapter  extends ArrayAdapter<WeatherForecast> {
    	public WeatherAdapter(Context context) {
    		super(context,R.layout.forecast_row);
    	}
    	
    	@Override
    	public View getView(int position, View convertView, ViewGroup parent) {
    		
       	 // Using a ViewHolder to speed things up. Basically just saves all the objects to solve the DRY-issue.
		ViewHolder viewHolder;
       	 if (convertView == null) {
                
       		 	viewHolder = new ViewHolder();
       		 	
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.forecast_row, parent, false);
                viewHolder.date = (TextView)convertView.findViewById(R.id.date);
                viewHolder.time = (TextView)convertView.findViewById(R.id.time);
                viewHolder.temperature = (TextView)convertView.findViewById(R.id.temperature);
                viewHolder.precipitation = (TextView)convertView.findViewById(R.id.precipitation);
                viewHolder.icon = (ImageView)convertView.findViewById(R.id.icon);
                viewHolder.date.setText(this.getItem(position).getStartMMDD());

                convertView.setTag(viewHolder);
             } else {
                 viewHolder = (ViewHolder) convertView.getTag();
             }
       	 
       	 WeatherForecast forecast = this.getItem(position);
       
       	 viewHolder.date.setText(forecast.getStartMMDD());
       	 viewHolder.time.setText(String.valueOf(forecast.getStartHHMM() + "-\n" + forecast.getEndHHMM()));
       	 viewHolder.temperature.setText(Integer.toString(forecast.getTemp())+" °C");
       	 viewHolder.precipitation.setText(String.valueOf(forecast.getRain())+" mm");
       	 viewHolder.icon.setImageResource(getResourceId("icon_", forecast.getWeatherCode(), forecast.getPeriodCode()));
		   
       	 return convertView;
		}

    	// Gets which weather icon to represent the forecast by concatenating the iconPath-string.
		private int getResourceId(String iconPath, int weatherCode, int periodCode) {
			 	
			if (weatherCode <= 9){
			    iconPath = iconPath+"0"+(Integer.toString(weatherCode));
			}
			else {
			    iconPath = iconPath+(Integer.toString(weatherCode));
			}
			
			// Trying to get the image.
			int resourceId = getResources().getIdentifier(iconPath, "drawable", getPackageName());

			// If the image doesn't exist set the periodcode.
			if (resourceId == 0){
				
				if (periodCode == 2 || periodCode == 1){
				
				    iconPath = iconPath +"d";
				}
				else {
				    iconPath = iconPath +"n";
				}
				resourceId = getResources().getIdentifier(iconPath, "drawable", getPackageName());
				return resourceId;
			}
			
			return resourceId;
		}
    	
    	
    }
}
