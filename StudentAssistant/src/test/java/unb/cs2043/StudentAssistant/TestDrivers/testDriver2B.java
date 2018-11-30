package unb.cs2043.StudentAssistant.TestDrivers;

import unb.cs2043.student_assistant.Course;
import unb.cs2043.student_assistant.Schedule;
import unb.cs2043.student_assistant.Section;

import java.util.ArrayList;
/**@author Fredeic Verret*/
public class testDriver2B{
	public static void main(String[] args){
		Schedule one=new Schedule("Jane's Winter 2019");
		
		Course temp;
		Section temp1;
		
		//Add courses, sections, and classtimes to schedule one
		for(int x=0; x< 2; x++){
			temp1=new Section("Section"+x*x);
			temp=new Course("Course"+x);
			temp.add(temp1);
			one.add(temp);
		}
		
		//Printing schedule one
		System.out.println(one.getFormattedString());
		
		//Changing name of a course using the copied list
		ArrayList<Course> two=one.copyCourses();
		two.get(0).setName("Testing");
		
		//Printing schedule one again (course name has changed!)
		System.out.println(one.getFormattedString());
	}
}
