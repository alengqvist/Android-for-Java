package android.assignment_2;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;



public class AlarmDataSource {

	  // Database fields.
	  private SQLiteDatabase database;
	  private AlarmDbHelper dbHelper;
	  private String[] allColumns = { AlarmDbHelper.COLUMN_ID, AlarmDbHelper.COLUMN_ALARM_TIME, AlarmDbHelper.COLUMN_ALARM_TYPE, AlarmDbHelper.COLUMN_ALARM_FLAG };

	  
	  // Create a instance of AlarmDbHelper.
	  public AlarmDataSource(Context context) {
	    dbHelper = new AlarmDbHelper(context);
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
	  private Alarm cursorToAlarm(Cursor cursor) {
		  Alarm alarm = new Alarm();
		  alarm.setId(cursor.getLong(0));
		  
		  Calendar calendar = Calendar.getInstance();
		  calendar.setTimeInMillis(cursor.getLong(1));
		  
		  alarm.setCalendar(calendar);
		  alarm.setAlarmType(cursor.getString(2));
		  alarm.setFlag(cursor.getInt(3) == 1 ? true : false);
		  return alarm;
	  }

	  
// C-R-R-U-D.
	  
	  // CREATE. 
	  public Alarm createAlarm(Alarm alarm) {
		
		// Get values.
	    ContentValues values = new ContentValues();
	    values.put(AlarmDbHelper.COLUMN_ALARM_FLAG, alarm.getFlag());
	    values.put(AlarmDbHelper.COLUMN_ALARM_TYPE, alarm.getAlarmType());
	    values.put(AlarmDbHelper.COLUMN_ALARM_TIME, alarm.getTime());
	    
	    // Insert into.
	    long insertId = database.insert(AlarmDbHelper.ALARM_TABLE_NAME, null, values);

	    // Query.
	    Cursor cursor = database.query(AlarmDbHelper.ALARM_TABLE_NAME,
	        allColumns, AlarmDbHelper.COLUMN_ID + " = " + insertId, null,
	        null, null, null);
	    cursor.moveToFirst();
	    Alarm newAlarm = cursorToAlarm(cursor);
	    cursor.close();
	    
	    return newAlarm;
	  }
	  
	  // RETRIEVE.
	  public Alarm getAlarm(long alarmId) {
		  
		  // Query.
		  String restrict = AlarmDbHelper.COLUMN_ID + "=" + alarmId;
		  Cursor cursor = database.query(true, AlarmDbHelper.ALARM_TABLE_NAME, allColumns, restrict, 
		    		                      null, null, null, null, null);
		  
		  // If the id exists, create a new Alarm object and return it.
		  if (cursor != null && cursor.getCount() > 0) {
			  cursor.moveToFirst();
			  Alarm alarm = cursorToAlarm(cursor);
			  return alarm;
		  }
		  cursor.close();
		  return null;
	  }
	  
	  // RETRIEVE ALL.
	  public List<Alarm> getAllAlarms() {
		 
		
		// Create a ArrayList of alarms.
	    List<Alarm> alarms = new ArrayList<Alarm>();

	    // Query.
	    Cursor cursor = database.query(AlarmDbHelper.ALARM_TABLE_NAME, allColumns, null, null, null, null, AlarmDbHelper.COLUMN_ALARM_TIME+" ASC");

	    // For each row in the database create a new Alarm object and add it to the ArrayList.
	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	    	Alarm alarm = cursorToAlarm(cursor);
	    	alarms.add(alarm);
	      cursor.moveToNext();
	    }
	    cursor.close();
	    return alarms;
	  }
	  
	  // UPDATE.
	  public boolean updateAlarm(Alarm alarmToUpdate) {
		  
		  // Get values.
		  ContentValues args = new ContentValues();
		  args.put(AlarmDbHelper.COLUMN_ALARM_FLAG, alarmToUpdate.getFlag());
		  args.put(AlarmDbHelper.COLUMN_ALARM_TYPE, alarmToUpdate.getAlarmType());
		  args.put(AlarmDbHelper.COLUMN_ALARM_TIME, alarmToUpdate.getTime());

		  // Specify the row Id.
		  String restrict = AlarmDbHelper.COLUMN_ID + "=" + alarmToUpdate.getId();
		  
		  return database.update(AlarmDbHelper.ALARM_TABLE_NAME, args, restrict , null) > 0;
	  }

	  // DELETE.
	  public void deleteAlarm(Alarm alarm) {
		
		// Get id to delete.
	    long id = alarm.getId();
	    System.out.println("Alarm deleted with id: " + id);
	    database.delete(AlarmDbHelper.ALARM_TABLE_NAME, AlarmDbHelper.COLUMN_ID + " = " + id, null);
	  }
} 