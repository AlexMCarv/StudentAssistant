package unb.cs2043.student_assistant;
import java.util.ArrayList;
/**@author Tye Shutty*/
public class ArrayListPlus<E> extends ArrayList<E>{

	private String name;

	public ArrayListPlus(String name){
		this.name=name;
	}
	public ArrayListPlus(){
		this.name="";
	}
	public String getName(){
		return name;
	}
	public boolean setName(String name){
		this.name=name;
		return true;
	}
}
