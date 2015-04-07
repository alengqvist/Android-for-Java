package android.assignment_3;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;



public class CallDataSource {

	  // Database fields.
	  private SQLiteDatabase database;
	  private CallDbHelper dbHelper;
	  private String[] allColumns = { CallDbHelper.COLUMN_ID, CallDbHelper.COLUMN_NUMBER };

	  
	  // Create a instance of CountryDbHelper.
	  public CallDataSource(Context context) {
	    dbHelper = new CallDbHelper(context);
	  }
	  
	  // Open the database.
	  public void open() throws SQLException {
	    database = dbHelper.getWritableDatabase();
	  }

	  // Close the database.
	  public void close() {
	    dbHelper.close();
	  }
	  
	  // Cursor for point out and set each column.
	  private Call cursorToCall(Cursor cursor) {
		  Call call = new Call();
		  call.setId(cursor.getLong(0));
		  call.setNumber(cursor.getString(1));

		  return call;
	  }

	  
// C-R-R-U-D.
	  
	  // CREATE. 
	  public Call createCall(Call call) {
		
		// Get values.
	    ContentValues values = new ContentValues();
	    values.put(CallDbHelper.COLUMN_NUMBER, call.getNumber());
	    
	    // Insert into.
	    long insertId = database.insert(CallDbHelper.CALL_TABLE_NAME, null, values);

	    // Query.
	    Cursor cursor = database.query(CallDbHelper.CALL_TABLE_NAME,
	        allColumns, CallDbHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
	    cursor.moveToFirst();
	    Call newCall = cursorToCall(cursor);
	    cursor.close();
	    
	    return newCall;
	  }
	  
	  // RETRIEVE ALL.
	  public List<Call> getAllCalls() {
		 
		
		// Create a ArrayList of calls.
	    List<Call> calls = new ArrayList<Call>();

	    // Query.
	    Cursor cursor = database.query(CallDbHelper.CALL_TABLE_NAME, allColumns, null, null, null, null, null);

	    // For each row in the database create a new Call object and add it to the ArrayList.
	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	    	Call call = cursorToCall(cursor);
	    	calls.add(call);
	      cursor.moveToNext();
	    }
	    cursor.close();
	    return calls;
	  }
}