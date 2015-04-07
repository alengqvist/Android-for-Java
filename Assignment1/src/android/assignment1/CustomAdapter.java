package android.assignment1;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomAdapter extends ArrayAdapter<Country> {
	
	private ArrayList<Country> mCountries;
	
    private static class ViewHolder {
        TextView country;
        TextView year;
    }

	public CustomAdapter(Context context, ArrayList<Country> countries) {
        super(context, 0, countries);
        this.mCountries = countries;
     }

     @Override
     public View getView(int position, View convertView, ViewGroup parent) {

    	 // Using a ViewHolder to speed things up. Basically just saves all the objects to solve the DRY-issue.
    	 ViewHolder viewHolder;
    	 if (convertView == null) {
             
    		 viewHolder = new ViewHolder();
             
             LayoutInflater inflater = LayoutInflater.from(getContext());
             convertView = inflater.inflate(R.layout.list_item, parent, false);
             viewHolder.country = (TextView) convertView.findViewById(R.id.country);
             viewHolder.year = (TextView) convertView.findViewById(R.id.visited);
             convertView.setTag(viewHolder);
          } else {
              viewHolder = (ViewHolder) convertView.getTag();
          }

		Country country = mCountries.get(position);
		
		viewHolder.country.setText(country.getCountry());
		viewHolder.year.setText(country.getYear());
        
        return convertView;
    }
}