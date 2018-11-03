package unb.cs2043.student_assistant;
import java.util.ArrayList;
import java.io.Serializable;

//this is an example of the code that could exist in a GUI
public class ScheduleDriver implements Serializable{

	private static String scheduleName;
	private static ArrayList<String> courseNames;
	private static ArrayList<ArrayList<String>> sectionNames;
	private static ArrayList<ArrayList<ArrayList<ClassTime>>> schedule;

	//replace with start method for GUI
	public static void main(String[] args){
		int start=0; int end=5;
		ClassTime time1=new ClassTime("placeholder");
		ArrayList<ClassTime> section1 = new ArrayList<ClassTime>();
		ArrayList<ArrayList<ClassTime>> course1 = new ArrayList<ArrayList<ClassTime>>();
		schedule=new ArrayList<ArrayList<ArrayList<ClassTime>>>();
		scheduleName = "Jane's Schedule";

	}
	//this is an example of the methods that could be made to change and retrieve information
	private static String formattedToString(){
		String description="Schedule="+scheduleName+":\n";
		//for classes
		for(int x=0; x<schedule.size();x++){
			description=description+"Course="+courseNames.get(x)+":\n";
			//for sections
			for(int y=0; y<schedule.get(x).size();y++){
				description=description+"Section="+sectionNames.get(x).get(y)+":\n";
				//for classtimes
				for(int z=0; z<schedule.get(x).get(y).size();z++){
					description=description+"ClassTimes="+schedule.get(x).get(y).get(z)+", ";
				}
				description+="\n";
			}
		}
		return description;
	}
}
