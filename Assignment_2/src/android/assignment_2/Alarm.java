package android.assignment_2;

import java.io.Serializable;
import java.util.Calendar;
import android.text.format.DateFormat;



public class Alarm implements Serializable{

	private static final long serialVersionUID = 1L;
	private static final String TIME_FORMAT = "HH:mm";
	
	private long id;
	private Calendar calendar;
	private String alarmType;
	private boolean flag;

	
	public void setId(long id){
		this.id = id;
	}
	
	public void setCalendar(Calendar calendar){
		
	    // If todays set time is passed. Set to tomorrow.
		Calendar calenderNow = Calendar.getInstance();
		if(calendar.compareTo(calenderNow) <= 0){
			calendar.add(Calendar.DATE, 1);
		}
		this.calendar = calendar;
	}
	
	public void setAlarmType(String alarmType){
		this.alarmType = alarmType;
	}
	
	public void setFlag(boolean flag){
		this.flag = flag;
	}
	
	public long getId(){
		return id;
	}
	
	public Calendar getCalendar(){
		return calendar;
	}
	
	public long getTime(){
		return calendar.getTimeInMillis();
	}
	
	public String getAlarmType(){
		return alarmType;
	}
	
	public boolean getFlag(){
		return flag;
	}
	
	public String getTimeAsString(){
		return (String) DateFormat.format(TIME_FORMAT, calendar.getTime());
	}
}