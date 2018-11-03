package unb.cs2043.student_assistant;

import java.io.Serializable;
import java.util.ArrayList;
/**@author Tye Shutty*/
//Allan, this class is your responsibility,
//I just copied my methods from Course and pasted them here. -Tye
public class Section implements Serializable{

	private String name;
	private ArrayList<ClassTime> classTimes;

	public Section(String name){
		this.name=name;
		classTimes = new ArrayList<ClassTime>();
	}

	public ArrayList<ClassTime> copyList(){
		return classTimes;
	}
	public ClassTime getClassTime(int index){
		if(classTimes.size()>=index){
			return classTimes.get(index);
		}
		return null;
	}
	public String getName(){
		return name;
	}
	public boolean setName(String two){
		name=two;
		return true;
	}

	public int getSize(){
		return classTimes.size();
	}

	public void add(Section one){
		//edge cases
		if(classTimes.isEmpty() ||
		classTimes.get(classTimes.size()-1).getName().compareTo(one.getName())<=0){
			classTimes.add(one);
		}
		//general case
		else{
			//inserts into the list to maintain order from smallest to largest Section
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
		return classTimes.remove(index);
	}
	public boolean set(ClassTime two){
		boolean deed=false;
		for(int x=0;x<classTimes.size();x++){
			if(classTimes.get(x).compareTo(two)==0){
				classTimes.add(x,two);
				deed=true;
			}
		}
		return deed;
	}
	public String toString(){
		String description=name;
		// System.out.println(classTimes.size());
		if(classTimes.size()>0){
			description+=":\n";
		}
		for(int x=0; x<classTimes.size();x++){
			//automatically calls toString on ClassTime object?
			description+="      "+classTimes.get(x);
			if(x+1<classTimes.size()){
				description+=",\n";
			}
		}
		return description+"\n";
	}
}
