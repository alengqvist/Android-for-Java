package android.assignment1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

// Most of the code is taken from Jonas MainList-example.
public class MasterMenu extends ListActivity {
	
	private List<String> activities = new ArrayList<String>();
	private Map<String, Class> name2class = new HashMap<String, Class>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_menu);

        setup_activities();
        setListAdapter(new ArrayAdapter<String>(this, R.layout.master_menu_row, activities));
        
        ListView lv = getListView();
        lv.setOnItemClickListener(new OnItemClick()); 
	}
    
    private void setup_activities() {
    	addActivity("Random", Random.class);
    	addActivity("BMI", BMI.class);
    	addActivity("Color Display", ColorDisplay.class);
    	addActivity("My Countries", My_country.class);  	
    	addActivity("Weather App", VaxjoWeather.class);  	
    }
    
    
    private class OnItemClick implements OnItemClickListener {
    	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    		
    		String activity_name = activities.get(position);
    		Class activity_class = name2class.get(activity_name);

    		Intent intent = new Intent(MasterMenu.this,activity_class);
    		MasterMenu.this.startActivity(intent);
    	}   	
    }
    
    
    private void addActivity(String name, Class activity) {
    	activities.add(name);
    	name2class.put(name, activity);    	
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
