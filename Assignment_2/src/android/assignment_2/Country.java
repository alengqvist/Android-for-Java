package android.assignment_2;

import java.io.Serializable;

public class Country implements Serializable{

	private static final long serialVersionUID = 1L;

	private long id;
	private String country;
	private String year;

	
	public void setId(long id){
		this.id = id;
	}
	
	public void setCountry(String country){
		this.country = country;
	}
	
	public void setYear(String year){
		this.year = year;
	}
	
	public long getId(){
		return id;
	}
	
	public String getCountry(){
		return country;
	}
	
	public String getYear(){
		return year;
	}
	
	public String toString(){
		return country +" "+ year;
	}
}