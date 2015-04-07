package android.assignment_2;

import android.app.ActionBar;
import android.app.Activity;
import android.assignment_2.R;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;



public class MyCountries extends Activity {

	private CountryDataSource datasource;									// Database object.
	private CustomAdapter adapter;											// Adapter object.

	private ListView listView;												// ListView object.
	
	private String sortBy;													// SortBy string from case or preferences.	
	
	private static final String ASC = " ASC";								// Ascending sorting.
	private static final String DESC = " DESC";								// Descending sorting.	
	
	private static final String savedSortBy = "savedSortBy";				// Static string for fetching saved sortBy.

	public static final String COUNTRY_TO_UPDATE = "COUNTRY_TO_UPDATE";		// Country object to update.
	public static final String RESULT = "RESULT";							// The newly created Country object.

	private static final int DELETE	= 0;									// Request code for deleting.
	private static final int UPDATE	= 1;									// Request code for updating.
	private static final int CREATE	= 2;									// Request code for creating.
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_countries);
		
		// Create instance of CountryDataSource and open the database.
        datasource = new CountryDataSource(this);
        datasource.open();
       
        // Creates a CustomAdapter in which all countries that has been visited is displayed.
		adapter = new CustomAdapter(this);
		listView = (ListView) findViewById(R.id.country_list);
		listView.setEmptyView(findViewById(R.id.emptyList));
		
		// Set preference settings.
		reloadPreference(listView);

		// Reload the adapter.
		reloadAdapter();
		
		// Get ActionBar.
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        
        // Set the ContextMenu to the ListView.
        registerForContextMenu(listView);
	}
	
    @Override
    protected void onResume() {
    	datasource.open();
    	reloadPreference(listView);
    	reloadAdapter();
    	super.onResume();
    }

    @Override
    protected void onPause() {
    	datasource.close();
    	super.onPause();
    }
    
	@Override
	protected void onStop(){
    	datasource.close();
		super.onStop();
		
		// Save the preference state of the sortBy.
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor edit = prefs.edit();
		edit.putString(savedSortBy, sortBy);
		edit.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.my_countries, menu);
		return true;
	}

	// The ActionBars selections listed with each selection as a case.
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        	
        	case R.id.country_preferences: 
        		Intent intent_settings = new Intent(this, CountryPreference.class);
        		startActivity(intent_settings);
		        return true;
		        
		    case R.id.add_country:
		    	Intent intent = new Intent(this, CountryMaker.class);
		        startActivityForResult(intent, CREATE);
		        return true;
		        
		    case R.id.sort_country_D:
		    	sortBy = CountryDbHelper.COLUMN_COUNTRY + DESC;
	    		reloadAdapter();
		        return true;
		        
		    case R.id.sort_country_A:
		    	sortBy = CountryDbHelper.COLUMN_COUNTRY + ASC;
	    		reloadAdapter();
		        return true;
		        
		    case R.id.sort_year_D:
		    	sortBy = CountryDbHelper.COLUMN_YEAR + DESC;
	    		reloadAdapter();
		        return true;
		        
		    case R.id.sort_year_A:
		    	sortBy = CountryDbHelper.COLUMN_YEAR + ASC;
	    		reloadAdapter();
		        return true;
		        
		    default:
		        return super.onOptionsItemSelected(item);
        } 
	}
	
    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent result) {
        super.onActivityResult(requestCode, resultCode, result);
        datasource.open();
    	if (resultCode == Activity.RESULT_OK) {

    		// The returning Country object from CountryMaker.
    		Country country = (Country) result.getSerializableExtra(RESULT);
    		
    		if(requestCode == UPDATE){
        		datasource.updateCountry(country);
			}
			else{
				datasource.createCountry(country);
			}
    		reloadAdapter();
     	}
    }

    // Refreshing method for the adapter when something have been created, updated o deleted.
    private void reloadAdapter() {
    	adapter.clear();
    	adapter.addAll(datasource.getAllCountries(sortBy));
		listView.setAdapter(adapter);
	}
    
    // Refreshing method for the preferences. Gets each preference and sets it.
	private void reloadPreference(ListView listView) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		this.sortBy = prefs.getString(savedSortBy, null);
		listView.setBackgroundColor(Color.parseColor(prefs.getString(getResources().getString(R.string.pref_country_background_key), getResources().getString(R.string.pref_country_background_dValue))));
		adapter.setFontSize(Float.parseFloat(prefs.getString(getResources().getString(R.string.pref_country_fontsize_key), getResources().getString(R.string.pref_country_fontsize_dValue))));
		adapter.setFontColor(Color.parseColor(prefs.getString(getResources().getString(R.string.pref_country_fontcolor_key), getResources().getString(R.string.pref_country_fontcolor_dValue))));
	}

	// The context menu when user long click on one of the list items. 
    @Override  
    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {  
    	super.onCreateContextMenu(menu, v, menuInfo);  
    	
    	// Setup the context menu and its context selections.
		if (v.getId() == R.id.country_list) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
			menu.setHeaderTitle(adapter.getItem(info.position).toString());
			menu.add(0, UPDATE, 0, R.string.title_update);
			menu.add(0, DELETE, 0, R.string.title_delete);
		}
    }
    
    // Selections in the context menu.
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
		Country country = adapter.getItem(info.position);

        switch (item.getItemId()) {

        case UPDATE:
	    	Intent intent = new Intent(this, CountryMaker.class);
	    	intent.putExtra(COUNTRY_TO_UPDATE, country);
	        startActivityForResult(intent, UPDATE);
	        return true;
	        
        case DELETE:
    		datasource.deleteCountry(country);
    		adapter.remove(country);
    		reloadAdapter();
    		return true;
	        
		default:
			return super.onContextItemSelected(item);
        }
	}
}