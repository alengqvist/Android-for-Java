package android.assignment_2;

import android.assignment_2.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;



public class AlarmAdapter extends ArrayAdapter<Alarm> {
	
    private static class ViewHolder {
        TextView time;
		TextView alarm_type;
    }

	public AlarmAdapter(Context context) {
        super(context, 0);
     }

     @Override
     public View getView(int position, View convertView, ViewGroup parent) {

    	 // Using a ViewHolder to speed things up. Basically just saves all the objects to solve the DRY-issue.
    	 ViewHolder viewHolder;
    	 if (convertView == null) {
             
    		 viewHolder = new ViewHolder();
             
             LayoutInflater inflater = LayoutInflater.from(getContext());
             convertView = inflater.inflate(R.layout.activity_alarm_clock_row, parent, false);
             viewHolder.time = (TextView) convertView.findViewById(R.id.time);
             viewHolder.alarm_type = (TextView) convertView.findViewById(R.id.alarm_type);
             convertView.setTag(viewHolder);
          } else {
              viewHolder = (ViewHolder) convertView.getTag();
          }
    	 
		Alarm alarm = getItem(position);
		viewHolder.time.setText(alarm.getTimeAsString());
		viewHolder.alarm_type.setText(alarm.getAlarmType());
        
        return convertView;
    }
}