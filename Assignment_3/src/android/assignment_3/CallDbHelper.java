package android.assignment_3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;



public class CallDbHelper extends SQLiteOpenHelper {
	
	// Database declarations.
	public static final String CALL_TABLE_NAME = "callhistory";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NUMBER = "number";

	private static final String DATABASE_NAME = "callhistory.db";
	private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = "create table "+ CALL_TABLE_NAME +" ("+ COLUMN_ID +" integer primary key autoincrement, "+ COLUMN_NUMBER +" text not null);";
	
    
    // Call the super constructor from the implemented SQLiteOpenHelper.
	public CallDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Create the database when Class is created.
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    Log.w(CallDbHelper.class.getName(), "Upgrading database from version " 
	    		+ oldVersion + " to " + newVersion 
	    		+ ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + CALL_TABLE_NAME);
        onCreate(db);
	}
}