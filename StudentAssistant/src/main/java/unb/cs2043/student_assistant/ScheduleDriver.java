package unb.cs2043.student_assistant;
import java.util.ArrayList;
import java.io.Serializable;
import java.util.Arrays;

//this is an example of the code that could exist in a GUI
//methods aren't necessary, just shows how to call ArrayListPlus, and they might help reduce duplicate code
//there are a lot more methods you can call because ArrayListPlus inherits from the ArrayList class
public class ScheduleDriver implements Serializable{

	private static ArrayListPlus<ArrayListPlus<ArrayListPlus<ClassTime>>> schedule;

	//throws IndexOutOfBoundsException
	private static ArrayListPlus<ClassTime> getSection(int course, int section){
			return schedule.get(course).get(section);
	}
	//throws IndexOutOfBoundsException
	private static ArrayListPlus<ArrayListPlus<ClassTime>> getCourse(int course){
			return schedule.get(course);
	}
	private static void insertClassTime(int course, int section, ClassTime fresh){
		schedule.get(course).get(section).add(fresh);
	}
	private static ArrayList<Integer> indexOfClass(ClassTime classTime1){
		boolean success=false;
		ArrayList<Integer> result=new ArrayList<Integer>();
		for(int x=0;x<schedule.size();x++){
			for(int y=0;y<schedule.get(x).size();y++){
				for(int z=0;z<schedule.get(x).get(y).size();z++){
					if(classTime1.equals(schedule.get(x).get(y).get(z))){
						result.addAll(Arrays.asList(Integer.valueOf(x),Integer.valueOf(y),Integer.valueOf(z)));
						success=true;
					}
				}
			}
		}
		if(success)
			return result;
		else
			return new ArrayList<Integer>(Arrays.asList(Integer.valueOf(-1),Integer.valueOf(-1),Integer.valueOf(-1)));
	}
	private static ArrayList<Integer> indexOfSection(String section1){
		for(int x=0;x<schedule.size();x++){
			for(int y=0;y<schedule.get(x).size();y++){
				if(section1.equals(schedule.get(x).get(y).getName())){
					return new ArrayList<Integer>(Arrays.asList(Integer.valueOf(x),Integer.valueOf(y)));
				}
			}
		}
		return new ArrayList<Integer>(Arrays.asList(Integer.valueOf(-1),Integer.valueOf(-1)));
	}
	private static int indexOfClass(String class1){
		for(int x=0;x<schedule.size();x++){
			if(class1.equals(schedule.get(x).getName())){
				return x;
			}
		}
		return -1;
	}
	private static String stringClassTimes(ArrayListPlus<ClassTime> section){
		String result="";
		for(int x=0;x<section.size();x++){
			result+=section.get(x)+", ";
		}
		return result;
	}
	private static String stringSections(ArrayListPlus<ArrayListPlus<ClassTime>> course){
		String result="";
		for(int x=0;x<course.size();x++){
			result+=course.get(x)+", ";
		}
		return result;
	}
	private static String stringCourses(){
		String result="";
		for(int x=0;x<schedule.size();x++){
			result+=schedule.get(x)+", ";
		}
		return result;
	}
	private static String stringEverything(){
		String description="Schedule="+schedule.getName()+":\n";
		//for classes
		for(int x=0; x<schedule.size();x++){
			description+="Course="+schedule.get(x).getName()+":\n";
			//for sections
			for(int y=0; y<schedule.get(x).size();y++){
				description=description+"Section="+schedule.get(x).get(y).getName()+":\nClass Time=";
				//for classtimes
				description+=stringClassTimes(schedule.get(x).get(y))+"\n";
			}
		}
		return description;
	}
	private static void makeSchedule(String name){
			schedule=new ArrayListPlus<ArrayListPlus<ArrayListPlus<ClassTime>>>(name);
	}

	//replace with start method for GUI
	public static void main(String[] args){
		//make schedule
		makeSchedule("Jane's Schedule");
		//make course
		ArrayListPlus<ArrayListPlus<ClassTime>> course1=new ArrayListPlus<ArrayListPlus<ClassTime>>("CS2043");
		//add course
		schedule.add(course1);
		//make section
		ArrayListPlus<ClassTime> section1=new ArrayListPlus<ClassTime>("1");
		//add section
		course1.add(section1);
		//make ClassTime
		ClassTime time1=new ClassTime("50");
		//add ClassTime
		section1.add(time1);
		System.out.println(stringEverything());
	}
}
