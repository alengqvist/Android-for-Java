package android.assignment1;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import android.assignment1.Country;

public class My_country extends Activity {

	CustomAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_country);
		
		ArrayList<Country> countries = new ArrayList<Country>();
		
		adapter = new CustomAdapter(this, countries);

		ListView listView = (ListView) findViewById(R.id.country_list);
		listView.setEmptyView(findViewById(R.id.emptyList));
		listView.setAdapter(adapter);
		
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.my_country, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
		    case R.id.add_country:
		    	Intent intent = new Intent(this, AddCountry.class);
		        startActivityForResult(intent, 1);
		        return true;
		        
		    default:
		        return super.onOptionsItemSelected(item);
        } 
	}
	
    // Catches the returning data from the "Add country"-activity using the Serializable for returning a Country-object.
    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent result) {
        super.onActivityResult(requestCode, resultCode, result);
    	if (resultCode == Activity.RESULT_OK) {
    		Bundle b = result.getExtras();
    		Country newCountry = (Country) b.getSerializable("result");
    		adapter.add(newCountry);
    		adapter.notifyDataSetChanged();
    	}
    }
}
