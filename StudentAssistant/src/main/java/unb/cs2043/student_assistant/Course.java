package unb.cs2043.student_assistant;

import java.io.Serializable;
import java.util.ArrayList;
/**@author Tye Shutty and others
* Course allows for performing most default operations of ArrayList.
* Lists can only be of type Section
* Saves course name
*/
@SuppressWarnings("serial")
public class Course implements Serializable{
//-------Instance Variables--------//
	private String name;
	private String fullName;
	private ArrayList<Section> sections;
//-------Constructor--------//
	public Course(String name){
		this.name=name;
		sections = new ArrayList<Section>();
	}
//--------Getters---------//
	public ArrayList<Section> copySections(){
		return new ArrayList<Section>(sections);
	}
	public Section getSection(int index){
		if(sections.size()>index){
			return sections.get(index);
		}
		return null;
	}
	public Section getSectionByName(String sectionName) {
		Section result = null;
		for (int i=0; i<sections.size() && result==null; i++) {
			Section current = sections.get(i);
			if (current.getName().equals(sectionName)) {
				result = current;
			}
		}
		return result;
	}
	public String getName(){
		return name;
	}
	public String getFullName(){
		return fullName;
	}
	public int getSize(){
		return sections.size();
	}
	public boolean contains(Section a){
		return sections.contains(a);
	}
	public int indexOf(Section a){
		return sections.indexOf(a);
	}
	public int indexOf(String a){
		for(int x=0;x<sections.size();x++){
			if(sections.get(x).getName().compareTo(a)==0){
				return x;
			}
		}
		return -1;
	}
	public int lastIndexOf(Section a){
		return sections.lastIndexOf(a);
	}
	public boolean isEmpty(){
		return sections.isEmpty();
	}
	public String getFormattedString(){
		String description=name+" ("+fullName+"):\n";
		if(sections.size()==0){
			description+="   empty\n";
		}
		else{
			for(int x=0; x<sections.size();x++){
				description+="   "+sections.get(x).getFormattedString();
			}
		}
		return description;
	}
	public String toString() {
		return name;
	}
//-----------Setters------------//
	public boolean setName(String name){
		this.name=name;
		return true;
	}
	public boolean setFullName(String fullName){
		this.fullName=fullName;
		return true;
	}
	public void add(Section one){
		//edge cases
		if(sections.isEmpty() ||
		sections.get(sections.size()-1).getName().compareTo(one.getName())<=0){
			sections.add(one);
		}
		//general case
		else{
			//inserts into the list to maintain order from smallest to largest Section
			int x=0;
			//compareTo returns the value of this object relative to the parameter
			while(sections.get(x).getName().compareTo(one.getName())<0){
				x++;
			}
			sections.add(x,one);
		}
	}
	public boolean remove(Section one){
		return sections.remove(one);
	}
	public boolean remove(int index){
		return null!=sections.remove(index);
	}
	public boolean replace(Section older, Section newer)
	{
		boolean deed=false;
		for(int x=0;x<sections.size();x++){
			if(sections.get(x).getName().compareTo(older.getName())==0){
				sections.set(x,newer);
				deed=true;
			}
		}
		return deed;
	}
	public boolean replace(String older, Section newer){
		boolean deed=false;
		for(int x=0;x<sections.size();x++){
			if(sections.get(x).getName().compareTo(older)==0){
				sections.set(x,newer);
				deed=true;
			}
		}
		return deed;
	}
	public boolean replace(int oldIndex, Section newer){
		if(oldIndex<sections.size()){
			sections.set(oldIndex,newer);
			return true;
		}
		return false;
	}
	public void clear(){
		sections.clear();
	}
	
	/**
	 * Courses are equal if they have the same name and same sections.
	 */
	@Override
	public boolean equals(Object obj){
		boolean result;
		
		if (obj instanceof Course) {
			Course course = (Course) obj;
			
			boolean sameSections = this.copySections().equals(course.copySections());
			boolean sameName = this.getName().equals(course.getName());
			result = sameSections && sameName;
		} 
		else {
			result = false;
		}
		
		return result;
	}
}
