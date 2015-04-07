package android.assignment1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Spinner;
import android.assignment1.Country;



public class AddCountry extends Activity {

	private Spinner sCountry;
	private Spinner sYear;
	private Button bAddCountry;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_country);
		
		initFields();
		bAddCountry.setOnClickListener(new ButtonClick());  
		

	}


	// Initializing the GUI-components.
	private void initFields() {
		sCountry = (Spinner) findViewById(R.id.spinnerCountries);
		sYear = (Spinner) findViewById(R.id.spinnerYears);
		bAddCountry = (Button) findViewById(R.id.button_add_country);
	}
	
	
	private class ButtonClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			
			String country = String.valueOf(sCountry.getSelectedItem());
			String year = String.valueOf(sYear.getSelectedItem());
			
			Country newCountry = new Country();
			newCountry.setCountry(country);
			newCountry.setYear(year);
			
			
			Intent result = new Intent();
			result.putExtra("result", newCountry);
			setResult(Activity.RESULT_OK, result);
			finish();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}
}
