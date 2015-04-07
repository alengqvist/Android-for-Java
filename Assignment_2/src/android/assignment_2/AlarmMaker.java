package android.assignment_2;

import java.util.Calendar;
import android.app.ActionBar;
import android.app.Activity;
import android.assignment_2.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Spinner;
import android.widget.TimePicker;



public class AlarmMaker extends Activity {

	private TimePicker tpAlarm;
	private Spinner sAlarmType;
	private Alarm alarmToUpdate;
	private long alarmIdToUpdate;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_maker);
		
		initFields();
		
		// Fetch the update to be Country object if there is one.
		alarmToUpdate = (Alarm) getIntent().getSerializableExtra(AlarmClock.ALARM_TO_UPDATE);
		
		if(alarmToUpdate != null){
			setTitle(R.string.title_activity_alarm_maker_update);
			// Set the Id on which country to update. Is used in the OnClickListener.
			alarmIdToUpdate = alarmToUpdate.getId();
			Calendar calendar = alarmToUpdate.getCalendar();
			// Set the spinner selection to the already selected value.
			tpAlarm.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
			tpAlarm.setCurrentMinute(calendar.get(Calendar.MINUTE));
		}

		// Get ActionBar.
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
	}

	// Initializing the GUI-components.
	private void initFields() {
		tpAlarm = (TimePicker) findViewById(R.id.timepicker_alarm);
		sAlarmType = (Spinner) findViewById(R.id.alarmTypeSpinner);
		tpAlarm.setIs24HourView(true);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.alarm_maker, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

	    case R.id.save_alarm:
	    	
	    	// Set the Calendar to the TimePickers selected values.
	    	Calendar calendar = Calendar.getInstance();
	    	calendar.set(Calendar.HOUR_OF_DAY, tpAlarm.getCurrentHour());
	    	calendar.set(Calendar.MINUTE, tpAlarm.getCurrentMinute());
	    	calendar.set(Calendar.SECOND, 0);
	    
			String alarmTypeString = String.valueOf(sAlarmType.getSelectedItem());
			
			// Create a new Alarm-object and set the values to it.
	    	Alarm alarm = new Alarm();
	    	alarm.setCalendar(calendar);
	    	alarm.setAlarmType(alarmTypeString);
			
			// If its an update.
			if(alarmToUpdate != null){
				alarm.setId(alarmIdToUpdate);
			}
			
			// Create a Intent and send the newly created to the AlarmClock-Activity.
			Intent result = new Intent();
			result.putExtra(AlarmClock.ALARM_RESULT, alarm);
			setResult(Activity.RESULT_OK, result);
			finish();
	        return true;
	        
	    default:
	        return super.onOptionsItemSelected(item);
        } 
	}
}