package android.assignment_3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;

public class CallHistoryReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {

		Bundle bundle = intent.getExtras();
		String state = bundle.getString(TelephonyManager.EXTRA_STATE);
		
		// If its a call (ringing) then create a Call object and set the number ringing and save it to the database.
		if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_RINGING)) {
			
			Call call = new Call();
			call.setNumber(bundle.getString(TelephonyManager.EXTRA_INCOMING_NUMBER));
			
			CallDataSource datasource = new CallDataSource(context);
			datasource.open();
			datasource.createCall(call);
			datasource.close();
		}
	}
}
