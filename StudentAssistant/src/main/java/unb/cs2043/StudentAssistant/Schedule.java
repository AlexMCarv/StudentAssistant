package unb.cs2043.StudentAssistant;
//package naming convention https://docs.oracle.com/javase/tutorial/java/package/namingpkgs.html

import java.io.Serializable;
import java.util.ArrayList;
/**@author Tye Shutty*/
public class Schedule implements Serializable{
	private String name;
	private ArrayList<Course> courses;

	public Schedule(String name){
		this.name=name;
		courses = new ArrayList<Course>();
	}
	public ArrayList<Course> copyList(){
		return courses;
	}
	public Course getCourse(int index){
		if(courses.size()>=index){
			return courses.get(index);
		}
		return null;
	}
	public int getSize(){
		return courses.size();
	}
	public String getName(){
		return name;
	}
	public boolean setName(String name){
		this.name=name;
		return true;
	}
	public void add(Course one){
		if(courses.isEmpty() ||
		courses.get(courses.size()-1).getName().compareTo(one.getName())<=0){
			courses.add(one);
		}
		else{
			//inserts into the list to maintain order from
			// smallest to largest Course
			for(int x=0; x<courses.size();x++){
				if(courses.get(x).getName().compareTo(one.getName())>0){
					for(int y=courses.size();y>x;y--){
						courses.set(y,courses.get(y-1));
					}
					courses.add(x,one);
				}
			}
		}
	}
	public boolean remove(Course one){
		boolean deed=false;
		for(int x=0;x<courses.size();x++){
			if(courses.get(x).getName().compareTo(one.getName())==0){
				courses.remove(x);
				deed=true;
			}
		}
		return deed;
	}
	public boolean remove(int index){
		if(courses.size()>=index){
			courses.remove(index);
			return true;
		}
		return false;
	}
	public boolean replace(Course two){
		boolean deed=false;
		for(int x=0;x<courses.size();x++){
			if(courses.get(x).getName().compareTo(two.getName())==0){
				courses.add(x,two);
				deed=true;
			}
		}
		return deed;
	}
	public String toString(){
		String description=name;
		// System.out.println(courses.size());
		if(courses.size()>0){
			description+=":\n";
		}
		for(int x=0; x<courses.size();x++){
			description+=courses.get(x);
		}
		return description;
	}
}
