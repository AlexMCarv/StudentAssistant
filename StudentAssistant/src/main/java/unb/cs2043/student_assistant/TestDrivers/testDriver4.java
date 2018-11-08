package unb.cs2043.student_assistant.TestDrivers;

import unb.cs2043.student_assistant.ClassTime;
import unb.cs2043.student_assistant.Course;
import unb.cs2043.student_assistant.Schedule;
import unb.cs2043.student_assistant.Section;
import java.util.ArrayList;
import java.util.Scanner;
/**
* WARNING: Doesn't work with current ClassTime class
* Reads textfile(use redirection <), creates Schedule, runs all functions on that Schedule and its Classes
* Textfile test case format:
* Schedule name
* 3              <- number of courses
* CS2043
* 2				<- num of sections
* 1A
* 3				<- num of classTimes
* placeholder1	<- classTime names
* placeholder2
* placeholder3
* 1B			<-repeat
* Could also use to test Section and ClassTime
* @author Tye Shutty
*/
public class testDriver4{
	public static void main(String[] args){
		ArrayList<Section> sections=new ArrayList<Section>();
		ArrayList<ClassTime> classTimes=new ArrayList<ClassTime>();
		Scanner Tye = new Scanner(System.in);
		Schedule one=new Schedule(Tye.nextLine());
		int numCourses=Tye.nextInt();
		Tye.nextLine();
		for(int x=0;x<numCourses;x++){
			Course temp=new Course(Tye.next());
			Tye.nextLine();
			one.add(temp);
			int index=one.indexOf(temp);
			int numSections=Tye.nextInt();
			Tye.nextLine();
			for(int y=0;y<numSections;y++){
				Section temp2=new Section(Tye.next());
				Tye.nextLine();
				one.getCourse(index).add(temp2);
				int index2=one.getCourse(index).indexOf(temp2);
				int numTimes=Tye.nextInt();
				Tye.nextLine();
				for(int z=0;z<numTimes;z++){
					one.getCourse(index).getSection(index2).add(new ClassTime(Tye.next()));
					Tye.nextLine();
				}
			}

		}
		Course course0=one.getCourse(0);
		Course courseMid=one.getCourse(one.getSize()/2);
		Course courseMax=one.getCourse(one.getSize()-1);
		Section section0=courseMax.getSection(0);
		Section sectionMid=courseMax.getSection(courseMax.getSize()/2);
		Section sectionMax=courseMax.getSection(courseMax.getSize()-1);

		//Tests schedule
		System.out.println("----TESTING SCHEDULE CLASS:----\n"+one.getSize());
		System.out.println(one);
		System.out.println(one.getCourseByName(course0.getName()));
		System.out.println(one.getCourseByName(courseMid.getName()));
		System.out.println(one.getCourseByName(courseMax.getName()));
		System.out.println(one.indexOf(course0)+", "+one.indexOf(courseMid)+", "+one.indexOf(courseMax));
		System.out.println(one.indexOf(course0.getName())+", "+one.indexOf(courseMid.getName())+", "+one.indexOf(courseMax.getName()));
		System.out.println(one.lastIndexOf(course0)+", "+one.lastIndexOf(courseMid)+", "+one.lastIndexOf(courseMax));
		if(one.contains(course0))
			System.out.println("one contains "+course0.getName());
		if(one.contains(courseMid))
			System.out.println("one contains "+courseMid.getName());
		if(one.contains(courseMax))
			System.out.println("one contains "+courseMax.getName());
		Course five =new Course("Course5");
		if(!one.contains(five))
			System.out.println("one doesn't contain 5");
		if(one.isEmpty())
			System.out.println("empty schedule");
		else
			System.out.println("nonempty schedule");
		one.setName("Jerry's Schedule");
		one.remove(0);
		one.remove(courseMid);
		System.out.println(one);
		one.replace(courseMax,course0);
		System.out.println(one);
		one.replace("CS2043",courseMid);
		System.out.println(one);
		one.replace(one.getSize()/2,courseMax);
		System.out.println(one);
		one.clear();
		System.out.println(one);

		//Tests Course
		System.out.println("----TESTING COURSE CLASS:----\n"+courseMax.getSize());
		System.out.println(courseMax);
		System.out.println(courseMax.getSectionByName(section0.getName()));
		System.out.println(courseMax.getSectionByName(sectionMid.getName()));
		System.out.println(courseMax.getSectionByName(sectionMax.getName()));
		System.out.println(courseMax.indexOf(section0)+", "+courseMax.indexOf(sectionMid)+", "+courseMax.indexOf(sectionMax));
		System.out.println(courseMax.indexOf(section0.getName())+", "+courseMax.indexOf(sectionMid.getName())+", "+courseMax.indexOf(sectionMax.getName()));
		System.out.println(courseMax.lastIndexOf(section0)+", "+courseMax.lastIndexOf(sectionMid)+", "+courseMax.lastIndexOf(sectionMax));
		if(courseMax.contains(section0))
			System.out.println("courseMax contains "+section0.getName());
		if(courseMax.contains(sectionMid))
			System.out.println("courseMax contains "+sectionMid.getName());
		if(courseMax.contains(sectionMax))
			System.out.println("courseMax contains "+sectionMax.getName());
		Section six =new Section("section6");
		if(!courseMax.contains(six))
			System.out.println("one doesn't contain 6");
		if(courseMax.isEmpty())
			System.out.println("empty course");
		else
			System.out.println("nonempty course");
		courseMax.setName("Jerry's Course");
		courseMax.remove(0);
		courseMax.remove(sectionMid);
		System.out.println(courseMax);
		courseMax.replace(sectionMax,section0);
		System.out.println(courseMax);
		courseMax.replace("1A",sectionMid);
		System.out.println(courseMax);
		courseMax.replace(courseMax.getSize()/2,sectionMax);
		System.out.println(courseMax);
		courseMax.clear();
		System.out.println(courseMax);
	}
}
