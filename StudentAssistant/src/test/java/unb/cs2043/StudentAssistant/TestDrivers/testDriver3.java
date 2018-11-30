package unb.cs2043.StudentAssistant.TestDrivers;

import unb.cs2043.student_assistant.ClassTime;
import unb.cs2043.student_assistant.Course;
import unb.cs2043.student_assistant.Schedule;
import unb.cs2043.student_assistant.Section;

import java.util.ArrayList;
/**@author Tye Shutty*/
public class testDriver3{
	public static void main(String[] args){
		Schedule one=new Schedule("Jane's Winter 2019");
		Schedule two=new Schedule("empty");
		Course temp;
		Section temp1;
		ClassTime temp2;
		for(int x=0; x< 1; x++){
			temp1=new Section("Section"+x*x);
			for(int y=0; y<10;y++){
				temp2=new ClassTime("Time"+y*y*y);
				temp1.add(temp2);
			}
			temp=new Course("Course"+x);
			temp.add(temp1);
			one.add(temp);

		}
		System.out.println(one);
		if(two.getCourse(0)==null){
			System.out.println("2 is empty");
		}
		if(one.getCourse(0)==null){
			System.out.println("1 is empty");
		}
		else
			System.out.println("1 is not empty");
		Course three=one.getCourseByName("Course0");
		System.out.println(three);
		Course four=one.getCourseByName("Coursez");
		System.out.println(four);
		if(one.contains(three)){
			System.out.println("one contains three");
		}
		Course five =new Course("Course5");
		if(!one.contains(five)){
			System.out.println("one doesn't contain 5");
		}
		System.out.println(one.indexOf(five)+", "+one.indexOf(three)+", "+one.indexOf("Course0"));
		

	}
}
