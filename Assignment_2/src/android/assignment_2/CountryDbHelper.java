package android.assignment_2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;



public class CountryDbHelper extends SQLiteOpenHelper {
	
	// Database declarations.
	public static final String COUNTRY_TABLE_NAME = "mycountries";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_COUNTRY = "country";
	public static final String COLUMN_YEAR = "year";

	private static final String DATABASE_NAME = "mycountries.db";
	private static final int DATABASE_VERSION = 6;

    private static final String DATABASE_CREATE = "create table "+ COUNTRY_TABLE_NAME +" ("+ COLUMN_ID +" integer primary key autoincrement, "+ COLUMN_COUNTRY +" text not null, "+ COLUMN_YEAR +" text not null);";
	
    
    // Call the super constructor from the implemented SQLiteOpenHelper.
	public CountryDbHelper(Context context) {
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
        db.execSQL("DROP TABLE IF EXISTS " + COUNTRY_TABLE_NAME);
        onCreate(db);
	}
}