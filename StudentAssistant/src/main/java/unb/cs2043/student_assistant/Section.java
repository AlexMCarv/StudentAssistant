package unb.cs2043.student_assistant;

import java.io.Serializable;
import java.util.ArrayList;
/**@author Tye Shutty*/
public class Section implements Serializable{
	private String name;
	private ArrayList<ClassTime> classTimes;
	public Section(String name){
		this.name=name;
		classTimes = new ArrayList<ClassTime>();
	}
	public void add(ClassTime one){
		if(classTimes.isEmpty() ||
		classTimes.get(classTimes.size()-1).compareTo(one)<=0){
			classTimes.add(one);
		}
		else{
			//inserts into the list to maintain order from
			// smallest to largest ClassTime
			boolean added = false;
			for(int x=0; x<classTimes.size() && !added;x++){
				if(classTimes.get(x).compareTo(one)>0){
					/*This is not necessary, the add() method works as 
					 * an insert on its own (see documentation) -Fred*/ 
					//for(int y=classTimes.size()-1;y>x;y--){
					//	classTimes.set(y,classTimes.get(y-1));
					//}
					classTimes.add(x,one);
					//Your need to break out of the for loop otherwise you add it many times -Fred
					added = true;
				}
			}
		}
	}
	public boolean replace(ClassTime two){
		boolean deed=false;
		for(int x=0;x<classTimes.size();x++){
			if(classTimes.get(x).compareTo(two)==0){
				classTimes.add(x,two);
				deed=true;
			}
		}
		return deed;
	}
	public boolean remove(ClassTime one){
		boolean deed=false;
		for(int x=0;x<classTimes.size();x++){
			if(classTimes.get(x).compareTo(one)==0){
				classTimes.remove(x);
				deed=true;
			}
		}
		return deed;
	}
	public boolean remove(int index){
		if(classTimes.size()>=index){
			classTimes.remove(index);
			return true;
		}
		return false;
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
	public ClassTime getClassTime(int index){
		if(classTimes.size()>=index){
			return classTimes.get(index);
		}
		return null;
	}
	public int getSize(){
		return classTimes.size();
	}
	public String getName(){
		return name;
	}
	public boolean setName(String two){
		name=two;
		return true;
	}
}
