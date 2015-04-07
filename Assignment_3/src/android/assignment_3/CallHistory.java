package android.assignment_3;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ListView;



public class CallHistory extends Activity {

	private CallDataSource datasource;
	private CallAdapter adapter;
	private ListView listView;
	
	private static final int CALL	= 1;									// Request code for updating.
	private static final int SMS	= 2;									// Request code for creating.
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_call_history);
		
		// Create instance of CountryDataSource and open the database.
        datasource = new CallDataSource(this);
        datasource.open();       
        
        // Create a AlarmAdapter in which all alarms is displayed.
		adapter = new CallAdapter(this);
		listView = (ListView) findViewById(R.id.callhistory_list);
		listView.setEmptyView(findViewById(R.id.emptyList));
		
		reloadAdapter();
		
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
	
	// The context menu when user long click on one of the list items. 
    @Override  
    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {  
    	super.onCreateContextMenu(menu, v, menuInfo);  
    	
    	// Setup the context menu and its context selections.
		if (v.getId() == R.id.callhistory_list) {
			menu.setHeaderTitle(R.string.title_call_history_context);
			menu.add(0, CALL, 0, R.string.title_call);
			menu.add(0, SMS, 0, R.string.title_sms);
		}
    }
	
    // Selections in the context menu. Notice: I didn't add any Telephony(hasSystemFeature)-controls on this one.
    // If so, I would have added some if-statements inside of this method to check for those features.
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
		Call call = adapter.getItem(info.position);
		String number = call.getNumber();

        switch (item.getItemId()) {

        // If user press Call in the context menu start a DIAL intent.
        case CALL:
        	startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+number)));
	        return true;

        // If user press Message in the context menu create a AppChooser to send a message.
        case SMS: 
        	Intent smsIntent = new Intent(Intent.ACTION_SEND);
        	smsIntent.setType("text/plain");
        	smsIntent.putExtra(Intent.EXTRA_TEXT, number);
    		startActivity(Intent.createChooser(smsIntent, getResources().getString(R.string.share)));
    		return true;
	        
		default:
			return super.onContextItemSelected(item);
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
	
    // Refreshing method for the adapter when something have been created, updated o deleted.
    private void reloadAdapter() {
    	adapter.clear();
    	adapter.addAll(datasource.getAllCalls());
		listView.setAdapter(adapter);
	}
}
