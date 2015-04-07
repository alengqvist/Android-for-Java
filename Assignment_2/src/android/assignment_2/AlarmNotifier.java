package android.assignment_2;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.assignment_2.R;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;



public class AlarmNotifier extends Activity {

	public static final String INTENT_BROADCAST = "android.assignment_2.ALARM_BROADCAST";	// Broadcast variable.
	public static final String ALARM = "alarm";

	private MediaPlayer mediaPlayer;
	private AlarmDataSource datasource;
	private Alarm alarm;
	private TextView tAlarmTitle;
	private Button bSnooze;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_notifier);

		// Create instance of CountryDataSource and open the database.
        datasource = new AlarmDataSource(this);
        datasource.open();   
		
        // Get Intent Extras which is an Alarm object.
		alarm = (Alarm) AlarmNotifier.this.getIntent().getSerializableExtra(AlarmClock.ALARM);		
		
		// Initializing GUI-components.
		tAlarmTitle = (TextView) findViewById(R.id.alarm_title);
		bSnooze = (Button) findViewById(R.id.button_alarm_snooze);
		
		// Set GUI-components.
		bSnooze.setOnClickListener(new SnoozeClick());
		tAlarmTitle.setText(alarm.getAlarmType());
		
		// Create and start alarm sound.
		mediaPlayer = MediaPlayer.create(this, R.raw.kent_socker);
		mediaPlayer.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.alarm_notifier, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

        // Dismisses the alarm and deletes it from the database.
	    case R.id.button_alarm_dismiss:
			datasource.deleteAlarm(alarm);
	    	mediaPlayer.stop();
	    	finish();
	        return true;
	        
	    default:
	        return super.onOptionsItemSelected(item);
        } 
	}
	
	private class SnoozeClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			
			// Snooze one minute. Couldn't take a shorter time because of the inexact (see API 19) update.
	    	mediaPlayer.stop();
			Calendar snoozeCalendar = alarm.getCalendar();
			final long snoozeTime = System.currentTimeMillis() + 60000;
			snoozeCalendar.setTimeInMillis(snoozeTime);
			alarm.setCalendar(snoozeCalendar);

			datasource.updateAlarm(alarm);
			addAlarmToBroadCast(alarm);
			finish();
		}
		
	    // Create a PendingIntent in which the alarm is saved.
		private PendingIntent alarmToBroadCastMaker(Alarm alarm) {
			Intent intent = new Intent(INTENT_BROADCAST);
			intent.putExtra(ALARM, alarm);
			return PendingIntent.getBroadcast(AlarmNotifier.this, (int) alarm.getId(), intent, 0);
		}
	    
	    // Add an broadcast to the BroadCastReceiver.
		public void addAlarmToBroadCast(Alarm alarm) {
			
			// Schedule the snooze alarm.
			AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
			am.set(AlarmManager.RTC_WAKEUP, alarm.getTime(), alarmToBroadCastMaker(alarm));		
		}

	}
}


