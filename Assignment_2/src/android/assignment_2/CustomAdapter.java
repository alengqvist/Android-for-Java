package android.assignment_2;

import android.assignment_2.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;



public class CustomAdapter extends ArrayAdapter<Country> {

	private float fontSize;
	private int fontColor;
	
    private static class ViewHolder {
        TextView country;
        TextView year;
    }

	public CustomAdapter(Context context) {
        super(context, 0);
     }

     @Override
     public View getView(int position, View convertView, ViewGroup parent) {

    	 // Using a ViewHolder to speed things up. Basically just saves all the objects to solve the DRY-issue.
    	 ViewHolder viewHolder;
    	 if (convertView == null) {
             
    		 viewHolder = new ViewHolder();
             
             LayoutInflater inflater = LayoutInflater.from(getContext());
             convertView = inflater.inflate(R.layout.activity_my_countries_row, parent, false);
             viewHolder.country = (TextView) convertView.findViewById(R.id.country);
             viewHolder.year = (TextView) convertView.findViewById(R.id.visited);
             convertView.setTag(viewHolder);
          } else {
              viewHolder = (ViewHolder) convertView.getTag();
          }
    	 
		Country country = getItem(position);
		
		// Preferences.
		viewHolder.country.setTextSize(fontSize);
		viewHolder.country.setTextColor(fontColor);
		viewHolder.year.setTextColor(fontColor);

		viewHolder.country.setText(country.getCountry());
		viewHolder.year.setText(country.getYear());
        
        return convertView;
    }
 	
	public void setFontSize(float fontSize) {
		this.fontSize = fontSize;
	}

	public void setFontColor(int fontColor) {
		this.fontColor = fontColor;
	}
}