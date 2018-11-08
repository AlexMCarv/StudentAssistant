package unb.cs2043.student_assistant;

import java.io.Serializable;
import java.util.ArrayList;
/**@author Tye Shutty*/
//Allan, this class is your responsibility, change it if you want, I
//just copied my methods from Course
//and found and replaced the ArrayList name and class name. -Tye
public class Section implements Serializable{
//-------Instance Variables--------//
	private String name;
	private ArrayList<ClassTime> classTimes;
//-------Constructor--------//
	public Section(String name){
		this.name=name;
		classTimes = new ArrayList<ClassTime>();
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
	public int indexOf(String a){
		for(int x=0;x<classTimes.size();x++){
			if(classTimes.get(x).getName().compareTo(a)==0){
				return x;
			}
		}
		return -1;
	}
	public int lastIndexOf(ClassTime a){
		return classTimes.lastIndexOf(a);
	}
	public boolean isEmpty(){
		return classTimes.isEmpty();
	}

	public String toString(){
		String description=name+":\n      ";
		if(classTimes.size()==0){
			description+="empty";
		}
		else{
			for(int x=0; x<classTimes.size();x++){
				//automatically calls toString on ClassTime object
				description+=classTimes.get(x);
				if(x+1<classTimes.size()){
					description+=",";
				}
			}
		}
		return description+"\n";
	}
//-----------Setters------------//
	public boolean setName(String name){
		this.name=name;
		return true;
	}
	public void add(ClassTime one){
		//edge cases
		if(classTimes.isEmpty() ||
		classTimes.get(classTimes.size()-1).getName().compareTo(one.getName())<=0){
			classTimes.add(one);
		}
		//general case
		else{
			//inserts into the list to maintain order from smallest to largest ClassTime
			int x=0;
			//compareTo returns the value of this object relative to the parameter
			while(classTimes.get(x).getName().compareTo(one.getName())<0){
				x++;
			}
			classTimes.add(x,one);
		}
	}
	public boolean remove(ClassTime one){
		return classTimes.remove(one);
	}
	public boolean remove(int index){
		return null!=classTimes.remove(index);
	}
	public boolean replace(ClassTime older, ClassTime newer){
		boolean deed=false;
		for(int x=0;x<classTimes.size();x++){
			if(classTimes.get(x).compareTo(older)==0){
				classTimes.set(x,newer);
				deed=true;
			}
		}
		return deed;
	}
	public boolean replace(String older, ClassTime newer){
		boolean deed=false;
		for(int x=0;x<classTimes.size();x++){
			if(classTimes.get(x).getName().compareTo(older)==0){
				classTimes.set(x,newer);
				deed=true;
			}
		}
		return deed;
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
}
