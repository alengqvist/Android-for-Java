package android.assignment_2;

import android.app.ActionBar;
import android.app.Activity;
import android.assignment_2.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Spinner;



public class CountryMaker extends Activity {

	private Spinner sCountry;
	private Spinner sYear;
	private long countryIdToUpdate;
	private Country countryToUpdate;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_country_maker);
		
		initFields();
		
		// Fetch the update to be Country object if there is one.
		countryToUpdate = (Country) getIntent().getSerializableExtra(MyCountries.COUNTRY_TO_UPDATE);
		
		if(countryToUpdate != null){
			setTitle(R.string.title_activity_country_maker_update);
			// Set the Id on which country to update. Is used in the OnClickListener.
			countryIdToUpdate = countryToUpdate.getId();
			
			// Set the spinner selection to the already selected value.
			sCountry.setSelection(getIndex(sCountry, countryToUpdate.getCountry()));
			sYear.setSelection(getIndex(sYear, countryToUpdate.getYear()));
		}

		// Get ActionBar.
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
	}

	// Initializing the GUI-components.
	private void initFields() {
		sCountry = (Spinner) findViewById(R.id.spinnerCountries);
		sYear = (Spinner) findViewById(R.id.spinnerYears);
	}
	
	// Get the value from spinner to select the already selected spinner value in a update to be Country object.
	private int getIndex(Spinner spinner, String value){
		int index = 0; 
		for (int i = 0; i<spinner.getCount(); i++){
			if (spinner.getItemAtPosition(i).equals(value)){
				index = i;
			}
		}
		return index;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.country_maker, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

	    case R.id.save_country:
			// Create a new Country-object and set the values to it.
			String countryString = String.valueOf(sCountry.getSelectedItem());
			String yearString = String.valueOf(sYear.getSelectedItem());
			
			// Create a new Country-object and set the values to it.
			Country country = new Country();
			country.setCountry(countryString);
			country.setYear(yearString);
			
			// If its an update.
			if(countryToUpdate != null){
				country.setId(countryIdToUpdate);
			}
			
			// Create a Intent and send the newly created to the MyCountry-Activity.
			Intent result = new Intent();
			result.putExtra(MyCountries.RESULT, country);
			setResult(Activity.RESULT_OK, result);
			finish();
	        return true;
	        
	    default:
	        return super.onOptionsItemSelected(item);
        } 
	}
}