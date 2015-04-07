package android.assignment_3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;



/*
 * Adapter for each forecast to be representing in a list.
 */
class WeatherAdapter extends ArrayAdapter<WeatherForecast> {
	
	private class ViewHolder {
		TextView date;
		TextView time;
		TextView temperature;
		TextView precipitation;
		ImageView icon;
	}

	public WeatherAdapter(Context context) {
		super(context, R.layout.activity_weather_row);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		
	   	 // Using a ViewHolder to speed things up. Basically just saves all the objects to solve the DRY-issue.
		ViewHolder viewHolder;
	   	 if (convertView == null) {
	            
   		 	viewHolder = new ViewHolder();
   		 	
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.activity_weather_row, parent, false);
            viewHolder.date = (TextView)convertView.findViewById(R.id.date);
            viewHolder.time = (TextView)convertView.findViewById(R.id.time);
            viewHolder.temperature = (TextView)convertView.findViewById(R.id.temperature);
            viewHolder.precipitation = (TextView)convertView.findViewById(R.id.precipitation);
            viewHolder.icon = (ImageView)convertView.findViewById(R.id.icon);
            viewHolder.date.setText(this.getItem(position).getStartMMDD());

            convertView.setTag(viewHolder);
            
         } else {
             viewHolder = (ViewHolder) convertView.getTag();
         }
	   	 
	   	 
	   	 WeatherForecast forecast = this.getItem(position);
	   
	   	 viewHolder.date.setText(forecast.getStartMMDD());
	   	 viewHolder.time.setText(String.valueOf(forecast.getStartHHMM() + "-\n" + forecast.getEndHHMM()));
	   	 viewHolder.temperature.setText(Integer.toString(forecast.getTemp())+" °C");
	   	 viewHolder.precipitation.setText(String.valueOf(forecast.getRain())+" mm");
	   	 viewHolder.icon.setImageResource(getResourceId("icon_", forecast.getWeatherCode(), forecast.getPeriodCode(), convertView));
		 
	   	 return convertView;
	}
	
	// Gets which weather icon to represent the forecast by concatenating the iconPath-string a bit odd could just use a switch case instead, I know..
	private int getResourceId(String iconPath, int weatherCode, int periodCode, View convertView) {
		 	
		if (weatherCode <= 9){
		    iconPath = iconPath+"0"+(Integer.toString(weatherCode));
		}
		else {
		    iconPath = iconPath+(Integer.toString(weatherCode));
		}
		
		// Trying to get the image.
		int resourceId = convertView.getResources().getIdentifier(iconPath, "drawable", WeatherActivity.PACKAGE_NAME);

		// If the image doesn't exist set the periodcode.
		if (resourceId == 0){
			
			if (periodCode == 2 || periodCode == 1){
			
			    iconPath = iconPath +"d";
			}
			else {
			    iconPath = iconPath +"n";
			}
			resourceId = convertView.getResources().getIdentifier(iconPath, "drawable", WeatherActivity.PACKAGE_NAME);
			return resourceId;
		}
		
		return resourceId;
	}	
}
