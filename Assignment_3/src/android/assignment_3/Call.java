package android.assignment_3;

import java.io.Serializable;

public class Call implements Serializable{

	private static final long serialVersionUID = 1L;

	private long id;
	private String number;

	public void setId(long id) {
		this.id = id;
	}

	public void setNumber(String number) {
		this.number = number;
	}
	
	public long getId(){
		return id;
	}

	public String getNumber() {
		return number;
	}

}
