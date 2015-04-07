package android.assignment_3;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.app.ListActivity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;



public class WidgetConfig extends ListActivity {
	
	private List<City> cities = new ArrayList<City>();
	ListView listview;

	int mAppWidgetId;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// If exit the activity.
		setResult(RESULT_CANCELED);
		
		// Hard coded Cities.
    	cities.add(new City("Sweden", "Kalmar", "Kalmar"));
    	cities.add(new City("Sweden", "Kronoberg", "Växjö"));
    	cities.add(new City("Sweden", "Stockholm", "Stockholm"));
		
        // Find the widget id from the Intent. 
		Bundle extras = getIntent().getExtras();
		
		// Set the appWidgetId to the fetched Intent Id. If there is one. Otherwise Invalid Id.
		if (extras != null) {
		    mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
		}
		
		// Setup Adapter.
        setListAdapter(new ArrayAdapter<City>(this, R.layout.activity_main_row, cities));
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		City city = (City) getListView().getItemAtPosition(position);

			// Create and start a Widget Service and put the City and WidgetId to it.
			Intent intent = new Intent(WidgetConfig.this, WidgetService.class);
			intent.putExtra(WidgetService.SERVICE_INTENT_COMMAND_EXTRA, WidgetService.WIDGET_INIT);
			intent.putExtra("city", city);
			intent.putExtra("appWidgetId", mAppWidgetId);
			this.startService(intent);

			// Create a resultIntent in which we add the WidgetId.
			Intent resultValue = new Intent();
			resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
			setResult(RESULT_OK, resultValue);

			finish();
	}
}