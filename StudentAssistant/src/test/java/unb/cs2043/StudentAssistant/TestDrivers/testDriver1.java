package unb.cs2043.StudentAssistant.TestDrivers;

import unb.cs2043.student_assistant.ClassTime;
import unb.cs2043.student_assistant.Course;
import unb.cs2043.student_assistant.Schedule;
import unb.cs2043.student_assistant.Section;
import java.io.*;


//tests serialize
/** @author Tye Shutty */
public class testDriver1{
	public static void main(String[] args){
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
		serialize(one);
		Schedule two =deserialize("test");
		System.out.println(two);
	}
	public static void serialize(Schedule a){
		FileOutputStream gary;
		try{
			gary=new FileOutputStream("test.list");
			ObjectOutputStream smithe=new ObjectOutputStream(gary);
			smithe.writeObject(a);
			smithe.close();
			gary.close();
		}
		catch(FileNotFoundException e){
			e.printStackTrace();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	public static Schedule deserialize(String filename){
		Schedule a=null;
		try{
			FileInputStream hugo=new FileInputStream(filename);
			ObjectInputStream dara=new ObjectInputStream(hugo);
			a=(Schedule)dara.readObject();
		}
		catch(FileNotFoundException e){
			e.printStackTrace();
		}
		catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		return a;
	}
}
