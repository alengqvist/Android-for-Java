package android.assignment_2;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;



public class CountryDataSource {

	  // Database fields.
	  private SQLiteDatabase database;
	  private CountryDbHelper dbHelper;
	  private String[] allColumns = { CountryDbHelper.COLUMN_ID, CountryDbHelper.COLUMN_COUNTRY, CountryDbHelper.COLUMN_YEAR };

	  
	  // Create a instance of CountryDbHelper.
	  public CountryDataSource(Context context) {
	    dbHelper = new CountryDbHelper(context);
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
	  private Country cursorToCountry(Cursor cursor) {
		  Country country = new Country();
		  country.setId(cursor.getLong(0));
		  country.setCountry(cursor.getString(1));
		  country.setYear(cursor.getString(2));

		  return country;
	  }

	  
// C-R-R-U-D.
	  
	  // CREATE. 
	  public Country createCountry(Country country) {
		
		// Get values.
	    ContentValues values = new ContentValues();
	    values.put(CountryDbHelper.COLUMN_COUNTRY, country.getCountry());
	    values.put(CountryDbHelper.COLUMN_YEAR, country.getYear());
	    
	    // Insert into.
	    long insertId = database.insert(CountryDbHelper.COUNTRY_TABLE_NAME, null, values);

	    // Query.
	    Cursor cursor = database.query(CountryDbHelper.COUNTRY_TABLE_NAME,
	        allColumns, CountryDbHelper.COLUMN_ID + " = " + insertId, null,
	        null, null, null);
	    cursor.moveToFirst();
	    Country newCountry = cursorToCountry(cursor);
	    cursor.close();
	    
	    return newCountry;
	  }
	  
	  // RETRIEVE.
	  public Country getCountry(long countryId) {
		  
		  // Query.
		  String restrict = CountryDbHelper.COLUMN_ID + "=" + countryId;
		  Cursor cursor = database.query(true, CountryDbHelper.COUNTRY_TABLE_NAME, allColumns, restrict, 
		    		                      null, null, null, null, null);
		  
		  // If the id exists, create a new Country object and return it.
		  if (cursor != null && cursor.getCount() > 0) {
			  cursor.moveToFirst();
			  Country country = cursorToCountry(cursor);
			  return country;
		  }
		  cursor.close();
		  return null;
	  }
	  
	  // RETRIEVE ALL.
	  public List<Country> getAllCountries(String sortBy) {
		 
		
		// Create a ArrayList of countries.
	    List<Country> countries = new ArrayList<Country>();

	    // Query.
	    Cursor cursor = database.query(CountryDbHelper.COUNTRY_TABLE_NAME, allColumns, null, null, null, null, sortBy);

	    // For each row in the database create a new Country object and add it to the ArrayList.
	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	    	Country country = cursorToCountry(cursor);
	    	countries.add(country);
	      cursor.moveToNext();
	    }
	    cursor.close();
	    return countries;
	  }
	  
	  // UPDATE.
	  public boolean updateCountry(Country countryToUpdate) {
		  
		  // Get values.
		  ContentValues args = new ContentValues();
		  args.put(CountryDbHelper.COLUMN_YEAR, countryToUpdate.getYear());
		  args.put(CountryDbHelper.COLUMN_COUNTRY, countryToUpdate.getCountry());

		  // Specify the row Id.
		  String restrict = CountryDbHelper.COLUMN_ID + "=" + countryToUpdate.getId();
		  
		  return database.update(CountryDbHelper.COUNTRY_TABLE_NAME, args, restrict , null) > 0;
	  }

	  // DELETE.
	  public void deleteCountry(Country country) {
		
		// Get id to delete.
	    long id = country.getId();
	    System.out.println("Country deleted with id: " + id);
	    database.delete(CountryDbHelper.COUNTRY_TABLE_NAME, CountryDbHelper.COLUMN_ID + " = " + id, null);
	  }
} 