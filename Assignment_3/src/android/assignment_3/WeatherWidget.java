package android.assignment_3;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.Toast;



public class WeatherWidget extends AppWidgetProvider {

	
	private Context context;

	@Override
	  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		this.context = context;
		
		final int N = appWidgetIds.length;
		
		// Loop all widgets.
		for (int i = 0; i < N; i++) {
			int appWidgetId = appWidgetIds[i];

			// Create and start Widget Service. If there's Internet.
			if(checkInternet()){
				Intent intent = new Intent(context, WidgetService.class);
				intent.putExtra(WidgetService.SERVICE_INTENT_COMMAND_EXTRA, WidgetService.WIDGET_UPDATE);
				intent.putExtra("appWidgetId", appWidgetId);
				context.startService(intent);
			}
			// Create a RemoteView in which we can handle the data to update.
			RemoteViews views = createDefaultRemoteView(context, appWidgetId);
			appWidgetManager.updateAppWidget(appWidgetId, views);
		}
	  }

	private RemoteViews createDefaultRemoteView(Context context, int appWidgetId) {

		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.weather_widget);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		String cityFromPrefs = prefs.getString(WidgetService.PREF_KEY_CITY + appWidgetId, null);

		if (cityFromPrefs != null) {
			City city = new City(cityFromPrefs);
			views.setTextViewText(R.id.city, city.getName());
		}
		
		int[] ids = { appWidgetId };


		Intent updateClick = new Intent(context, WeatherWidget.class);
		updateClick.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
				
		updateClick.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
		PendingIntent updatePen = PendingIntent.getBroadcast(context, appWidgetId, updateClick, 0);
		
		// NOTICE. I have the update button as the weather icon.
		views.setOnClickPendingIntent(R.id.icon, updatePen);
		return views;
	}
	
    // Check if there's an Internet connection on the device (also if airplane mode is ON).
	private boolean checkInternet() {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		
		if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
			return true;
		} else {
			Toast.makeText(context, context.getResources().getString(R.string.widget_internet), Toast.LENGTH_SHORT).show();			
			return false;
		}
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
	}
	
}