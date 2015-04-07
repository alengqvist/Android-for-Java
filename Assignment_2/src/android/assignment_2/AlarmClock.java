package android.assignment_2;

import java.util.Calendar;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.assignment_2.R;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.format.DateFormat;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


/*
 *  NOTE: From API 19+ android have done some changes for the RTC wake up.
 * 		  Alarms that is set to close to each other will shoot at the same time. This to save battery.
 */


public class AlarmClock extends Activity {

	private AlarmDataSource datasource;										// Database object.
	private AlarmAdapter adapter;											// Adapter object.
	
	private Handler handler = new Handler();

	private ListView listView;												// ListView object.
	private TextView timeDisplay;											// Time display.
	
	private boolean keep_displaying_time = true;
	private static final String TIME_FORMAT = "HH:mm:ss";
	
	public static final String ALARM_TO_UPDATE = "ALARM_TO_UPDATE";			// Alarm object to update.
	public static final String ALARM_RESULT = "ALARM_RESULT";				// The newly created Alarm object.
	public static final String ALARM = "alarm";
	
	public static final String INTENT_BROADCAST 
	= "android.assignment_2.ALARM_BROADCAST";								// Broadcast variable.
	
	private static final int DELETE	= 0;									// Request code for deleting.
	private static final int UPDATE	= 1;									// Request code for updating.
	private static final int CREATE	= 2;									// Request code for creating.

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_clock);
		
		// Create instance of CountryDataSource and open the database.
        datasource = new AlarmDataSource(this);
        datasource.open();       
        
        // Create a AlarmAdapter in which all alarms is displayed.
		adapter = new AlarmAdapter(this);
		listView = (ListView) findViewById(R.id.alarm_list);
		timeDisplay = (TextView) findViewById(R.id.timeDisplay);
		listView.setEmptyView(findViewById(R.id.emptyList));
		
		// Reload the adapter.
		reloadAdapter();
		
		// Get ActionBar.
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        
        // Create a  thread in which the time display is displayed and updated each five second.
		Thread thr = new Thread(null, time_display_work, "Time Display");
		thr.start();
		
        // Set the ContextMenu to the ListView.
        registerForContextMenu(listView);	
    }
	
    @Override
    protected void onResume() {
    	datasource.open();
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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.alarm_clock, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

	    case R.id.add_alarm:
	    	Intent intent = new Intent(this, AlarmMaker.class);
	        startActivityForResult(intent, CREATE);
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

    		// The returning Country object from AlarmMaker.
    		Alarm alarm = (Alarm) result.getSerializableExtra(ALARM_RESULT);
    		
    		
    		if(requestCode == UPDATE){
        		datasource.updateAlarm(alarm);
        		addAlarmToBroadCast(alarm);
			}
			else{
	    		addAlarmToBroadCast(datasource.createAlarm(alarm));
			}
    		reloadAdapter();
     	}
    }

	// The context menu when user long click on one of the list items. 
    @Override  
    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {  
    	super.onCreateContextMenu(menu, v, menuInfo);  
    	
    	// Setup the context menu and its context selections.
		if (v.getId() == R.id.alarm_list) {
			menu.setHeaderTitle(R.string.title_alarm_context);
			menu.add(0, UPDATE, 0, R.string.title_update);
			menu.add(0, DELETE, 0, R.string.title_delete);
		}
    }
    
    // Selections in the context menu.
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
		Alarm alarm = adapter.getItem(info.position);

        switch (item.getItemId()) {

        case UPDATE:
	    	Intent intent = new Intent(this, AlarmMaker.class);
	    	intent.putExtra(ALARM_TO_UPDATE, alarm);
	        startActivityForResult(intent, UPDATE);
	        return true;
	        
        case DELETE:
    		datasource.deleteAlarm(alarm);
    		removeAlarmToBroadCast(alarm);
    		adapter.remove(alarm);
    		reloadAdapter();
    		return true;
	        
		default:
			return super.onContextItemSelected(item);
        }
	}
	
    // Refreshing method for the adapter when something have been created, updated o deleted.
    private void reloadAdapter() {
    	adapter.clear();
    	adapter.addAll(datasource.getAllAlarms());
		listView.setAdapter(adapter);
	}
    
    // Create a PendingIntent in which the alarm is saved.
	private PendingIntent alarmToBroadCastMaker(Alarm alarm) {
		Intent intent = new Intent(INTENT_BROADCAST);
		intent.putExtra(ALARM, alarm);
		return PendingIntent.getBroadcast(AlarmClock.this, (int) alarm.getId(), intent, 0);
	}
    
    // Add an broadcast to the BroadCastReceiver.
	public void addAlarmToBroadCast(Alarm alarm) {
		
		// Schedule the alarm.
		AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
		am.set(AlarmManager.RTC_WAKEUP, alarm.getTime(), alarmToBroadCastMaker(alarm));		
	}
	
	// Delete an broadcast in the BroadCastReceiver.
	private void removeAlarmToBroadCast(Alarm alarm) {
		
		// Cancel the alarm.
		AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
		am.cancel(alarmToBroadCastMaker(alarm));		
	}
    
    // Runnable to run the display time Runnable.
	private Runnable time_display_work = new Runnable() {

		public void run() {
			while (keep_displaying_time) {
				handler.post(time_display_update);
				SystemClock.sleep(5000);
			}
		}
	};
	
	// Runnable to set display time for each fifth second.
	private Runnable time_display_update = new Runnable() {
		public void run() {
			timeDisplay.setText(DateFormat.format(TIME_FORMAT, Calendar.getInstance().getTime()));
		}
	};
}
