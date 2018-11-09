package unb.cs2043.student_assistant;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**@author Allan Boutilier **/

public class ClassTime implements Serializable {

	private ArrayList<String> days;
	private String type;
	private LocalTime startTime;
	private LocalTime endTime;

	public ClassTime(String type, ArrayList<String> daysIn, LocalTime startTimeIn, LocalTime endTimeIn ) {
		this.type = type;
		this.days = daysIn;
		this.startTime = startTimeIn;
		this.endTime = endTimeIn;
	}

	/**********Getters*********/
	public String getDays(){
		String s = "";
		for(int i=0; i<days.size(); i++) {
			s+=days.get(i);
		}
		return s;
	}
	
	public LocalTime getStartTime() {
		return startTime;
	}
	
	public LocalTime getEndTime() {
		return endTime;
	}

	public String getType() {
		return type;
	}

	public String toString() {
		return getType() + " " + getDays() + " " + getStartTime().toString() + "-" + getEndTime().toString();
	}

	/**********Setters*********/
	public void setDays(ArrayList<String> newDaysIn) {
		boolean check=true;
		for(int i=0; i<newDaysIn.size(); i++) {
			if(!isValidDay(days.get(i))) {
				check = false;
			}
		}
	
		if(check == true) {
			days = newDaysIn;
		}
	}
	

	public void setStartTime(LocalTime newStartTime) {
			startTime = newStartTime;
	}

	public void setEndTime(LocalTime newEndTime) {
			endTime = newEndTime;
	}

	
	/*********Check if Valid**************/
	public boolean isValidDay(String dayIn) {
//		System.out.println("day of week: "+ dayIn);
		if (dayIn.toLowerCase().equals("m")||
			dayIn.toLowerCase().equals("t")||
			dayIn.toLowerCase().equals("w")||
			dayIn.toLowerCase().equals("th")||
			dayIn.toLowerCase().equals("f")||
			dayIn.toLowerCase().equals("sa")||
			dayIn.toLowerCase().equals("su")) {
			return true;
		}
	
		else {
			return false;
		}
	}
	
	/*	 
	public boolean isValidTime(String start, String end) {
		int startHour=0;
		int startMin=0;
		int endHour=0;
		int endMin=0;
	
	
		if(start.length()!= 5){
			if(start.length()!=4){
				if(end.length()!=4){
					if(end.length()!= 5) {
						return false;
					}
				}
			}
		}
	
		if( start.length() == 5) {
		
			try {
				startHour = Integer.parseInt(start.substring(0,2));
				startMin = Integer.parseInt(start.substring(start.length()-2));
		
				if(startHour >=13||startMin>=60) {
		
					return false;
				}
		
			}
		
			catch (NumberFormatException e) {
				return false;
			}
		}
	
		if(start.length() == 4) {
		
		
			try {
				startHour = Integer.parseInt(start.substring(0,1));
				startMin = Integer.parseInt(start.substring(start.length()-2));
				if(startHour >=13||startMin>=60) {
					return false;
				}
			}
		
			catch (NumberFormatException e) {
				return false;
			}
		}
	
	
		if( end.length() == 5) {
			try {
				endHour = Integer.parseInt(end.substring(0,2));
				endMin = Integer.parseInt(end.substring(end.length()-2));
				if(endHour >=13||endMin>=60) {
					return false;
				}
			}
		
			catch (NumberFormatException e) {
				return false;
			}
		}
	
		if(end.length() == 4) {
		
		
			try {
				endHour = Integer.parseInt(end.substring(0,1));
				endMin = Integer.parseInt(end.substring(end.length()-2));
				if(endHour >=13||endMin>=60) {
					return false;
				}
			}
		
			catch (NumberFormatException e) {
				return false;
			}
		}
		
		return true;
	}

	public boolean isValidClassTime() {
		boolean check=true;
		for(int i=0; i<days.size(); i++) {
			if(!isValidDay(days.get(i))) {
	
				check = false;
			}
		}
	
		if (check&&isValidTime(startTime, endTime)){
			return true;
		}
	
		else {
			return false;
		}
	}*/
}
