package android.assignment_3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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



public class MainActivity extends ListActivity {
	
	// List of Activities.
	private List<String> activities = new ArrayList<String>();
	private Map<String, Class> name2class = new HashMap<String, Class>();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setups the Activities and set the ListAdapter.
        setup_activities();
        setListAdapter(new ArrayAdapter<String>(this, R.layout.activity_main_row, activities));
        
        ListView lv = getListView();
        lv.setOnItemClickListener(new OnItemClick()); 
	}
    
	// Adds each activity.
    private void setup_activities() {
    	addActivity("Incoming Call History", CallHistory.class);  	 	
    }
    
    // Takes the name and the activity and adds it to the ArrayList using HashMap.
    private void addActivity(String name, Class activity) {
    	activities.add(name);
    	name2class.put(name, activity);    	
    }
    
    // When user clicks on one of the Activities create a new Intent and starts the Activity.
    private class OnItemClick implements OnItemClickListener {
    	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    		
    		String activity_name = activities.get(position);
			Class activity_class = name2class.get(activity_name);

    		Intent intent = new Intent(MainActivity.this,activity_class);
    		MainActivity.this.startActivity(intent);
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