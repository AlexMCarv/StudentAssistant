package unb.cs2043.student_assistant;

import java.io.Serializable;
import java.util.ArrayList;

/**@author Allan Boutilier **/

public class ClassTime{
	
private ArrayList<String> days;
private String startTime;
private String endTime;


public ClassTime(ArrayList<String> daysIn, String startTimeIn, String endTimeIn ) {
	this.days = daysIn;
	this.startTime = startTimeIn;
	this.endTime = endTimeIn;
}


/**********Getters*********/
public String getDays(){
	String s = "";
	for(int i=0; i<days.size(); i++) {
		s+=days.get(i) + (" ");
	}
	return s;
}

public String getStartTime() {
	return startTime;
}

public String getEndTime() {
	return endTime;
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

public void setStartTime(String newStartTime) {
	if (isValidTime(newStartTime, endTime)) {
		startTime = newStartTime;
	}

}

public void setEndTime(String newEndTime) {
	if (isValidTime(startTime, newEndTime)) {
		endTime = newEndTime;
	}
}


/*********Check if Valid**************/

public boolean isValidDay(String dayIn) {
	System.out.println("day of week: "+ dayIn);
	if (dayIn.toLowerCase().equals("monday")||
		dayIn.toLowerCase().equals("tuesday")||
		dayIn.toLowerCase().equals("wednesday")||
		dayIn.toLowerCase().equals("thursday")||
		dayIn.toLowerCase().equals("friday")||
		dayIn.toLowerCase().equals("saturday")||
		dayIn.toLowerCase().equals("sunday")) {


		return true;
	}

	else {
		return false;
	}
}

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
}
}
