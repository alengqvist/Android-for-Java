package android.assignment_3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;



public class CallAdapter extends ArrayAdapter<Call> {
	 
    private static class ViewHolder {
        TextView number;
    }

	public CallAdapter(Context context) {
        super(context, 0);
     }

     @Override
     public View getView(int position, View convertView, ViewGroup parent) {

    	 // Using a ViewHolder to speed things up. Basically just saves all the objects to solve the DRY-issue.
    	 ViewHolder viewHolder;
    	 if (convertView == null) {
             
    		 viewHolder = new ViewHolder();
             
             LayoutInflater inflater = LayoutInflater.from(getContext());
             convertView = inflater.inflate(R.layout.activity_call_history_row, parent, false);
             viewHolder.number = (TextView) convertView.findViewById(R.id.number);
             convertView.setTag(viewHolder);
          } else {
              viewHolder = (ViewHolder) convertView.getTag();
          }
    	 
		viewHolder.number.setText(getItem(position).getNumber());
        
        return convertView;
    }
}