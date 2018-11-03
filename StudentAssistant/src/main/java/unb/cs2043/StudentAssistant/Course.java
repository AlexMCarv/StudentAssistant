package unb.cs2043.StudentAssistant;

import java.io.Serializable;
import java.util.ArrayList;
/**@author Tye Shutty*/
public class Course implements Serializable{
	private String name;
	private ArrayList<Section> sections;
	public Course(String name){
		this.name=name;
		sections = new ArrayList<Section>();
	}
	public Section getSection(int index){
		if(sections.size()>=index){
			return sections.get(index);
		}
		return null;
	}
	public String getName(){
		return name;
	}
	public boolean setName(String name){
		this.name=name;
		return true;
	}
	public int getSize(){
		return sections.size();
	}
	public void add(Section one){
		if(sections.isEmpty() ||
		sections.get(sections.size()-1).getName().compareTo(one.getName())<=0){
			sections.add(one);
		}
		else{
			//inserts into the list to maintain order from
			// smallest to largest Section
			boolean added = false;
			for(int x=0; x<sections.size() && !added;x++){
				if(sections.get(x).getName().compareTo(one.getName())>0){
					/*This is not necessary, the add() method works as 
					 * an insert on its own (see documentation) -Fred*/ 
//					for(int y=sections.size();y>x;y--){
//						sections.set(y,sections.get(y-1));
//					}
					sections.add(x,one);
					//Your need to break out of the for loop otherwise you add it many times -Fred
					added = true;
				}
			}
		}
	}
	public boolean remove(Section one){
		boolean deed=false;
		for(int x=0;x<sections.size();x++){
			if(sections.get(x).getName().compareTo(one.getName())==0){
				sections.remove(x);
				deed=true;
			}
		}
		return deed;
	}
	public boolean remove(int index){
		if(sections.size()>=index){
			sections.remove(index);
			return true;
		}
		return false;
	}
	public boolean replace(Section two){
		boolean deed=false;
		for(int x=0;x<sections.size();x++){
			if(sections.get(x).getName().compareTo(two.getName())==0){
				sections.add(x,two);
				deed=true;
			}
		}
		return deed;
	}
	public String toString(){
		String description=name;
		// System.out.println(sections.size());
		if(sections.size()>0){
			description+=":\n";
		}
		for(int x=0; x<sections.size();x++){
			//automatically calls toString on Section object?
			description+="   "+sections.get(x);
		}
		return description;
	}
}
