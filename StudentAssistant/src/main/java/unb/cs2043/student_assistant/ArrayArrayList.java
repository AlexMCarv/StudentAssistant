package unb.cs2043.student_assistant;
import java.util.ArrayList;
/**@author Tye Shutty*/
//MyArrayList<MyArrayList<ClassTime>> aka courses
//MyArrayList<MyArrayList<MyArrayList<ClassTime>>> aka schedule
//The best practice is to do the same thing with regular
//ArrayLists in whatever class calls this one
public class ArrayArrayList<E> extends ArrayList<E>{

	private String name;

	public ArrayArrayList(String name){
		this.name=name;
	}
	public String getName(){
		return name;
	}
	public boolean setName(String name){
		this.name=name;
		return true;
	}
	public boolean add(ArrayArrayList<E> one){
		//edge cases
		if(isEmpty())
			super.add((E)one);
		else if (((ArrayArrayList<E>)(get(size()-1))).name.compareTo(one.getName())<=0)
			super.add((E)one);
		//general case
		else{
			//inserts into the list to maintain order from smallest to largest Section
			int x=0;
			//compareTo returns the value of this object relative to the parameter
			while(((ArrayArrayList<E>)(get(x))).name.compareTo(one.getName())<0){
				x++;
			}
			add(x,(E)one);
		}
		return true;
	}
}
