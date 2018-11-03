package unb.cs2043.student_assistant;

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

	public ArrayList<Section> copyList() throws nullPointerException{
		return ArrayList(sections);
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
		return sections.remove(index);
	}
	public boolean replace(Section older, Section newer){
		boolean deed=false;
		for(Section x : sections){
			if(sections.get(x).getName().compareTo(older.getName())==0){
				sections.add(x,newer);
				deed=true;
			}
		}
		return deed;
	}
	public boolean replace(String older, Section newer){
		boolean deed=false;
		for(Section x : sections){
			if(sections.get(x).getName().compareTo(older)==0){
				sections.add(x,newer);
				deed=true;
			}
		}
		return deed;
	}
	public boolean replace(int oldIndex, Section newer){
		if(oldIndex<sections.size()){
			set(oldIndex,newer);
			return true;
		}
		return false;
	}
	public void clear(){
		sections.clear();
	}
	public boolean contains(Section a){
		return sections.contains(a);
	}

	public String toString(){
		String description=name;
		// System.out.println(sections.size());
		if(sections.size()>0){
			description+=":\n";
		}
		for(int x=0; x<sections.size();x++){
			description+="   "+sections.get(x);
		}
		return description;
	}
}
