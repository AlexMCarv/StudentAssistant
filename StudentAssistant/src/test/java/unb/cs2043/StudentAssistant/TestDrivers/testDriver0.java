package unb.cs2043.StudentAssistant.TestDrivers;

import unb.cs2043.student_assistant.ClassTime;
import unb.cs2043.student_assistant.Course;
import unb.cs2043.student_assistant.Schedule;
import unb.cs2043.student_assistant.Section;
/**@author Tye Shutty*/
public class testDriver0{
	public static void main(String[] args){
		Schedule one=new Schedule("Jane's Winter 2019");
		Course temp;
		Section temp1;
		ClassTime temp2=null;
		for(int x=0; x< 10; x++){
//out of date constructor			temp2=new ClassTime("Time"+x*x*x);
			temp1=new Section("Section"+x*x);
			temp1.add(temp2);
			temp=new Course("Course"+x);
			temp.add(temp1);
			one.add(temp);

		}
		System.out.println(one);
	}
}
