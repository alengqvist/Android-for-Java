package android.assignment_2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;



public class AlarmReceiver extends BroadcastReceiver {
	
	public static final String ALARM = "alarm";
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
        
		// Create alarm activity which acts as notifier.
		Intent alarmNotifierIntent = new Intent(context, AlarmNotifier.class);
		alarmNotifierIntent.putExtra(ALARM, intent.getSerializableExtra(ALARM));
		alarmNotifierIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(alarmNotifierIntent);
	}
}