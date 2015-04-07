package android.assignment1;

import java.io.Serializable;

public class Country implements Serializable{

	private static final long serialVersionUID = 1L;

	private String country;
	private String year;
	
	public Country(){
	}
	
	public void setCountry(String country){
		this.country = country;
	}
	
	public void setYear(String year){
		this.year = year;
	}
	
	public String getCountry(){
		return country;
	}
	
	public String getYear(){
		return year;
	}
}
