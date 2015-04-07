package android.assignment_2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;



public class AlarmDbHelper extends SQLiteOpenHelper {
	
	// Database declarations.
	public static final String ALARM_TABLE_NAME = "alarmclock";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_ALARM_TIME = "time";
	public static final String COLUMN_ALARM_TYPE = "alarm_type";
	public static final String COLUMN_ALARM_FLAG = "flag";

	private static final String DATABASE_NAME = "alarmclock.db";
	private static final int DATABASE_VERSION = 5;

    private static final String DATABASE_CREATE = "create table "+ ALARM_TABLE_NAME +" ("+ COLUMN_ID +" integer primary key autoincrement, "+ COLUMN_ALARM_TIME +" datetime not null, "+ COLUMN_ALARM_TYPE +" text not null, "+ COLUMN_ALARM_FLAG +" integer default 1);";
	
    
    // Call the super constructor from the implemented SQLiteOpenHelper.
	public AlarmDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Create the database when Class is created.
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    Log.w(CountryDbHelper.class.getName(), "Upgrading database from version " 
	    		+ oldVersion + " to " + newVersion 
	    		+ ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + ALARM_TABLE_NAME);
        onCreate(db);
	}
}