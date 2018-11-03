package unb.cs2043.StudentAssistant;

import java.util.Scanner;
import java.util.ArrayList;
/**@author Tye Shutty*/
public class testDriver0{
	public static void main(String[] args){
		// Scanner Tye = new Scanner(System.in);
		Schedule one=new Schedule("Jane's Winter 2019");
		Course temp;
		Section temp1;
		ClassTime temp2;
		for(int x=0; x< 10; x++){
			temp2=new ClassTime("Time"+x*x*x);
			temp1=new Section("Section"+x*x);
			temp1.add(temp2);
			temp=new Course("Course"+x);
			temp.add(temp1);
			one.add(temp);

		}
		System.out.println(one);
	}
}
