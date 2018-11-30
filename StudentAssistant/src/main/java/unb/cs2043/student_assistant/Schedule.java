package unb.cs2043.student_assistant;

//package naming convention https://docs.oracle.com/javase/tutorial/java/package/namingpkgs.html
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicLong;
/**@author Tye Shutty
* Schedule allows for performing most default operations of ArrayList.
* Lists can only be of type Course
* Saves course name
*/
@SuppressWarnings("serial")
public class Schedule implements Serializable, Comparable<Schedule>{
//-------Instance Variables--------//
	private static final AtomicLong NEXT_ID = new AtomicLong(0);
	private final long id = NEXT_ID.getAndIncrement();
	private String name;
	private ArrayList<Course> courses;
//-------Constructor--------//
	public Schedule(String name){
		this.name=name;
		courses = new ArrayList<Course>();
	}
	//Alternate constructor
	public Schedule(Schedule otherSchedule){
		this.name=otherSchedule.getName();
		courses = otherSchedule.copyCourses();
	}
//--------Getters---------//
	public ArrayList<Course> copyCourses(){
		return new ArrayList<Course>(courses);
	}
	public Course getCourse(int index){
		if(courses.size()>index){
			return courses.get(index);
		}
		return null;
	}
	public Course getCourseByName(String courseName) {
		courseName = courseName.toLowerCase();
		Course result = null;
		for (int i=0; i<courses.size() && result==null; i++) {
			Course current = courses.get(i);
			if (current.getName().toLowerCase().equals(courseName)) {
				result = current;
			}
		}
		return result;
	}
	public String getName(){
		return name;
	}
	public int getSize(){
		return courses.size();
	}
	public boolean contains(Course a){
		return courses.contains(a);
	}
	public int indexOf(Course a){
		return courses.indexOf(a);
	}
	public int indexOf(String a){
		for(int x=0;x<courses.size();x++){
			if(courses.get(x).getName().compareTo(a)==0){
				return x;
			}
		}
		return -1;
	}
	public int lastIndexOf(Course a){
		return courses.lastIndexOf(a);
	}
	public boolean isEmpty(){
		return courses.isEmpty();
	}
	public String getFormattedString(){
		String description=name+":\n";
		if(courses.size()==0){
			description+="empty\n";
		}
		else{
			for(int x=0; x<courses.size();x++){
				description+=courses.get(x).getFormattedString();
			}
		}
		return description;
	}
	public String toString(){
		return name;
	}
//-----------Setters------------//
	public boolean setName(String name){
		this.name=name;
		return true;
	}
	public void add(Course one){
		//edge cases
		if(courses.isEmpty() ||
		courses.get(courses.size()-1).getName().compareTo(one.getName())<=0){
			courses.add(one);
		}
		//general case
		else{
			//inserts into the list to maintain order from smallest to largest Course
			int x=0;
			//compareTo returns the value of this object relative to the parameter
			while(courses.get(x).getName().compareTo(one.getName())<0){
				x++;
			}
			courses.add(x,one);
		}
	}
	public boolean remove(Course one){
		return courses.remove(one);
	}
	public boolean remove(int index){
		return null!=courses.remove(index);
	}
	public boolean replace(Course older, Course newer)
	{
		boolean deed=false;
		for(int x=0;x<courses.size();x++){
			if(courses.get(x).getName().compareTo(older.getName())==0){
				courses.set(x,newer);
				deed=true;
			}
		}
		return deed;
	}
	public boolean replace(String older, Course newer){
		boolean deed=false;
		for(int x=0;x<courses.size();x++){
			if(courses.get(x).getName().compareTo(older)==0){
				courses.set(x,newer);
				deed=true;
			}
		}
		return deed;
	}
	public boolean replace(int oldIndex, Course newer){
		if(oldIndex<courses.size()){
			courses.set(oldIndex,newer);
			return true;
		}
		return false;
	}
	public void clear(){
		courses.clear();
	}
	
	
	/**
	 * Schedules are equal if, and only if, they contain the same courses.
	 * (according to equals() method of courses)
	 * (regardless of the name of the schedules)
	 */
	@Override
	public boolean equals(Object obj){
		boolean result;
		
		if (obj instanceof Schedule) {
			Schedule sc = (Schedule) obj;
			
			if (this.getSize() != sc.getSize()) {
				//False if don't have same number of courses
				result = false;
			}
			else {
				boolean sameCourses = true;
				for (int i=0; i<this.getSize() && sameCourses; i++) {
					sameCourses = sc.getCourse(i).equals(this.getCourse(i));
				}
				result = sameCourses;
			}
		} 
		else {
			result = false;
		}
		
		return result;
	}
	
	
	@Override
	/**
	 * Only returns 0 if they are equal (see equals() method above)
	 * Otherwise, sort by number of courses (most courses first)
	 * (If same number of courses, return 1)
	 */
	public int compareTo(Schedule other) {
		int result;
		if (this.equals(other)) {
			result = 0;
		}
		else {
			result = other.getSize() - this.getSize();
			if (result==0) {
				//Use unique id when have same number of courses
				result = other.id - this.id > 0 ? 1:-1;
			}
		}
		
		return  result;
	}
	
	
	/**
	 * Returns true if all courses in this schedule are present in the other schedule.
	 * Note: Returns false if this and other are equal.
	 * @param other Other schedule.
	 * @return True if all courses in this schedule are present in the other schedule.
	 */
	public boolean isSubsetOf(Schedule other) {
		int index=Collections.indexOfSubList(other.copyCourses(), courses);
		
		return index>=0 && !this.equals(other) ? true : false;
	}
}
