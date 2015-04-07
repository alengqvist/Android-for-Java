package android.assignment_3;

import java.util.Iterator;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.Toast;



public class WidgetService extends Service {

	private final IBinder binder = new WidgetServiceBinder();

	private Updater updater;

	protected static final String PREF_KEY_CITY = "city";
	
	public static final String SERVICE_INTENT_COMMAND_EXTRA = "command";
	public static final int WIDGET_INIT = 0;								// RequestCode for initializing.
	public static final int WIDGET_UPDATE = 1;								// RequestCode for Updating.

	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		if (intent == null) {
			return START_STICKY;
		}
		
		int command = intent.getIntExtra(SERVICE_INTENT_COMMAND_EXTRA, -1);

		processIntentCommand(intent, command);

		return START_STICKY;

	}
	
	
	/*
	 *  Switch/case for different commands.
	 */
	private void processIntentCommand(Intent intent, int command) {
		switch (command) {
		
			// When initializing Widget.
			case WIDGET_INIT: {
	
				int appWidgetId = intent.getIntExtra("appWidgetId", -1);
	
				saveCity(appWidgetId, (City) intent.getSerializableExtra("city"));
	
				Intent updateWidget = new Intent(getApplicationContext(), WeatherWidget.class);
				updateWidget.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
				int[] ids = { appWidgetId };
				updateWidget.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
				sendBroadcast(updateWidget);
				break;
			}
			
			// When updating Widget.
			case WIDGET_UPDATE: {
	
				int appWidgetId = intent.getIntExtra("appWidgetId", -1);
				retrieveWeatherReport(appWidgetId);
				break;
			}
			
			default: {
				break;
			}

		}
	}

	
	/*
	 *  Store the City to Preferences.
	 */
	public void saveCity(int appWidgetId, City city) {
		SharedPreferences.Editor prefs = PreferenceManager.getDefaultSharedPreferences(this).edit();
		prefs.putString(PREF_KEY_CITY + appWidgetId, city.getFullCityName());
		prefs.apply();
	}
	

	/*
	 *  Get City from Preferences.
	 */
	public City getCity(int appWidgetId) {

		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		String cityFull = prefs.getString(PREF_KEY_CITY + appWidgetId, null);
		
		if (cityFull == null) {
			return null;
		}
		
		return new City(cityFull);
	}

	
	/*
	 *  Retriever of WeatherReport. Starts the Updater in which the updating of forecast is done.
	 */
	public void retrieveWeatherReport(int appWidgetId) {
		updater = new Updater(appWidgetId, getCity(appWidgetId));
		updater.start();
	}

	
	private class Updater extends Thread {
		private City city;
		private int appWidgetId;

		public Updater(int appWidgetId, City city) {
			this.city = city;
			this.appWidgetId = appWidgetId;

		}

		@Override
		public void run() {
			WeatherReport report = WeatherHandler.getWeatherReport(city);
			WeatherForecast firstForecast = null;
			
			if (city == null) {
				return;
			}

			Context context = WidgetService.this;
			RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.weather_widget);


			Iterator<WeatherForecast> iter = report.iterator();

			// Take the first Forecast and sets the GUI-components in the Widget.
			if (iter.hasNext()) {
				firstForecast = iter.next();
				
				// Set the Icon.
				views.setImageViewResource(R.id.icon, getResourceId("icon_", firstForecast.getWeatherCode(), firstForecast.getPeriodCode()));

				// Set the Temperature.
				views.setTextViewText(R.id.temperature, firstForecast.getTemp() + getResources().getString(R.string.temp_unit));
			}

			// Registering onClickListener. In which if the user click on the Widget starts a WeatherActivity.
			Intent clickIntent = new Intent(context, WeatherActivity.class);
			clickIntent.putExtra("city", city);

			views.setTextViewText(R.id.city, city.getName());

			PendingIntent pendingIntent = PendingIntent.getActivity(context, appWidgetId, clickIntent, 0);

			views.setOnClickPendingIntent(R.id.layoutWidget, pendingIntent);

			// Updating all the views to the widget
			AppWidgetManager manager = AppWidgetManager.getInstance(WidgetService.this);
			manager.updateAppWidget(appWidgetId, views);
		}
		
		
		// Gets which weather icon to represent the forecast by concatenating the iconPath-string a bit odd could just use a switch case instead, I know..
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
	

	@Override
	public IBinder onBind(Intent arg0) {
		return binder;
	}

	public class WidgetServiceBinder extends Binder {
		WidgetService getService() {
			return WidgetService.this;
		}
	}
}