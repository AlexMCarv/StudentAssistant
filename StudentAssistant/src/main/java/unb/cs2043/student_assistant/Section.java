package unb.cs2043.student_assistant;

import java.io.Serializable;
import java.util.ArrayList;
/**@author Tye Shutty and others*/
//Allan, this class is your responsibility, change it if you want, I
//just copied my methods from Course
//and found and replaced the ArrayList name and class name. -Tye
@SuppressWarnings("serial")
public class Section implements Serializable{
//-------Instance Variables--------//
	private String name;
	private ArrayList<ClassTime> classTimes;
//-------Constructor--------//
	public Section(String name){
		this.name=name;
		classTimes = new ArrayList<ClassTime>();
	}
	public Section(Section other) {
		this.name = other.name;
		classTimes = other.copyClassTimes();
	}
//--------Getters---------//
	public ArrayList<ClassTime> copyClassTimes(){
		return new ArrayList<ClassTime>(classTimes);
	}
	public ClassTime getClassTime(int index){
		if(classTimes.size()>index){
			return classTimes.get(index);
		}
		return null;
	}
	public String getName(){
		return name;
	}
	public int getSize(){
		return classTimes.size();
	}
	public boolean contains(ClassTime a){
		return classTimes.contains(a);
	}
	public int indexOf(ClassTime a){
		return classTimes.indexOf(a);
	}
	public int lastIndexOf(ClassTime a){
		return classTimes.lastIndexOf(a);
	}
	public boolean isEmpty(){
		return classTimes.isEmpty();
	}

	public String getFormattedString(){
		String description=name+":\n      ";
		if(classTimes.size()==0){
			description+="empty";
		}
		else{
			for(int x=0; x<classTimes.size();x++){
				//automatically calls toString on ClassTime object
				description+=classTimes.get(x);
				if(x+1<classTimes.size()){
					description+="\n      ";
				}
			}
		}
		return description+"\n";
	}
	public String toString() {
		return name;
	}
//-----------Setters------------//
	public boolean setName(String name){
		this.name=name;
		return true;
	}
	public void add(ClassTime one){
		classTimes.add(one);
	}
	public boolean remove(ClassTime one){
		return classTimes.remove(one);
	}
	public boolean remove(int index){
		return null!=classTimes.remove(index);
	}
	public boolean replace(int oldIndex, ClassTime newer){
		if(oldIndex<classTimes.size()){
			classTimes.set(oldIndex,newer);
			return true;
		}
		return false;
	}
	public void clear(){
		classTimes.clear();
	}
	
	//Checks if any ClassTime in this section conflicts with any ClassTime in the other section.
	public boolean conflictsWith(Section other) {
		boolean conflicting = false;
		
		for (int i=0; i<this.getSize() && !conflicting; i++) {
			for (int j=0; j<other.getSize() && !conflicting; j++) {
				conflicting = this.getClassTime(i).conflictsWith(other.getClassTime(j));
			}
		}
		
		return conflicting;
	}
	
	//Checks if both sections contains the same classTimes (same days & times)
	public boolean sameClassTimes(Section other) {
		boolean sameClassTimes = true;
		
		if (this.getSize()!=other.getSize()) {
			sameClassTimes = false;
		}
		
		for (int i=0; i<this.getSize() && sameClassTimes; i++) {
			if (!this.getClassTime(i).equivalent(other.getClassTime(i))) {
				sameClassTimes = false;
			}
		}
		
		return sameClassTimes;
	}
}
