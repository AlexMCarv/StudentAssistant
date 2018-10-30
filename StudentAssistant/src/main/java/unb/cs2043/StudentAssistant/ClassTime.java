package unb.cs2043.StudentAssistant;

/**@author Tye Shutty*/
//This is a skeleton class for testing, you can change it
public class ClassTime implements Comparable{
	private String name;
	public ClassTime(String name){
		this.name=name;
	}
	public String toString(){
		// System.out.println(2);
		return name;
	}
	public int compareTo(Object two){
		if(((ClassTime)two).getName().compareTo(name)>0){
			return 1;
		}
		else if(((ClassTime)two).getName().compareTo(name)<0){
			return -1;
		}
		else{
			return 0;
		}
	}
	public String getName(){
		return name;
	}
}
