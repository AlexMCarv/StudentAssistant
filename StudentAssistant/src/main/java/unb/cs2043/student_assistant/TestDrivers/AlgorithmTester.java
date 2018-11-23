package unb.cs2043.student_assistant.TestDrivers;
/**@author Tye Shutty
AlgorithmTester tests the ability of the app to return optimal schedules.
Work in progress.
*/
import java.util.ArrayList;
import java.time.LocalTime;
import unb.cs2043.student_assistant.ClassTime;
import unb.cs2043.student_assistant.Course;
import unb.cs2043.student_assistant.Schedule;
import unb.cs2043.student_assistant.Section;
import unb.cs2043.student_assistant.ScheduleArranger;
import java.util.Arrays;
public class AlgorithmTester{
	public static void main(String[] args){
		ArrayList<Schedule> testSubjects = new ArrayList<Schedule>();
		ArrayList<ClassTime> classTimes = new ArrayList<ClassTime>();
		ArrayList<Section> sections = new ArrayList<Section>();
		ArrayList<Course> courses = new ArrayList<Course>();
//------------------First test should return all three Sections---------------------//
//Tests ability to return non conflicting courses
		testSubjects.add(new Schedule("First test should return all three Sections"));
		classTimes.add(new ClassTime("regular", new ArrayList<String>(Arrays.asList("Monday")),
		LocalTime.parse("10:00"), LocalTime.parse("11:20")));
		classTimes.add(new ClassTime("regular", new ArrayList<String>(Arrays.asList("Tuesday")),
		LocalTime.parse("10:00"), LocalTime.parse("11:20")));
		classTimes.add(new ClassTime("regular", new ArrayList<String>(Arrays.asList("Monday")),
		LocalTime.parse("13:00"), LocalTime.parse("14:20")));

//--------------Second test should return two of three Courses----------------//
//Tests ability to resolve conflict
		testSubjects.add(new Schedule("Second test should return two of three Courses"));
		classTimes.add(new ClassTime("special", new ArrayList<String>(Arrays.asList("Monday")),
		LocalTime.parse("13:00"), LocalTime.parse("14:20")));
//--------------Third test should return two of four Courses----------------//
//Tests ability to resolve conflict
		testSubjects.add(new Schedule("Third test should return two of four Courses"));
		classTimes.add(new ClassTime("regular", new ArrayList<String>(Arrays.asList("Monday")),
		LocalTime.parse("22:00"), LocalTime.parse("23:20")));
		classTimes.add(classTimes.get(0));
//--------------------Fourth test should return 5 Courses--------------------//
//Tests ability to pick right section out of past and future conflicts
	testSubjects.add(new Schedule("Fourth test should return 5 Courses"));
	String[] days = {"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
	Course manySections=new Course("course1 (many sections)");
	for(int x=0;x<5;x++){
		manySections.add(new Section("section"+x+"A"));
		manySections.getSection(x).add(new ClassTime("regular", new ArrayList<String>(Arrays.asList(days[x])),
		LocalTime.parse(10+x+":00"), LocalTime.parse(11+x+":"+20)));
	}
	testSubjects.get(3).add(manySections);
	ArrayList<ClassTime> classTimes2 = new ArrayList<ClassTime>();
	ArrayList<Section> sections2 = new ArrayList<Section>();
	ArrayList<Course> courses2 = new ArrayList<Course>();
	classTimes2.add(new ClassTime("regular", new ArrayList<String>(Arrays.asList("Thursday")),
	LocalTime.parse("13:00"), LocalTime.parse("14:20")));
	classTimes2.add(new ClassTime("regular", new ArrayList<String>(Arrays.asList("Monday")),
	LocalTime.parse("13:00"), LocalTime.parse("14:20")));
	classTimes2.add(new ClassTime("regular", new ArrayList<String>(Arrays.asList("Friday")),
	LocalTime.parse("13:00"), LocalTime.parse("14:20")));
	classTimes2.add(new ClassTime("regular", new ArrayList<String>(Arrays.asList("Tuesday")),
	LocalTime.parse("13:00"), LocalTime.parse("14:20")));
	for(int x=0;x<classTimes2.size();x++){
		sections2.add(new Section("section"+x));
		sections2.get(x).add(classTimes2.get(x));
		courses2.add(new Course("course"+x));
		courses2.get(x).add(sections2.get(x));
		testSubjects.get(3).add(courses2.get(x));
	}
//--------------------Fifth test should return 3 Courses--------------------//
//Tests Time Boundary
testSubjects.add(new Schedule("Fifth test should return 3 Courses"));
classTimes.add(new ClassTime("regular", new ArrayList<String>(Arrays.asList("Monday")),
LocalTime.parse("11:20"), LocalTime.parse("12:20")));
classTimes.add(new ClassTime("regular", new ArrayList<String>(Arrays.asList("Monday")),
LocalTime.parse("12:20"), LocalTime.parse("13:20")));
//------------------Sixth test --------------------------//
//a test solely about overlapping ClassTime
	testSubjects.add(new Schedule("Sixth test should return 1 Course, 6 possibilities"));
	ArrayList<ClassTime> classTimes3 = new ArrayList<ClassTime>();
	ArrayList<Section> sections3 = new ArrayList<Section>();
	ArrayList<Course> courses3 = new ArrayList<Course>();
	for(int x=0; x<6;x++){
		classTimes3.add(new ClassTime("regular", new ArrayList<String>(Arrays.asList(days[x])),
		LocalTime.parse("13:00"), LocalTime.parse("14:20")));
	}
	for(int x=0;x<classTimes3.size();x++){
		sections3.add(new Section("section"+x));
		for(int y=0; y<5;y++){
			sections3.get(x).add(classTimes3.get((x+y)%6));
		}
		courses3.add(new Course("course"+x));
		courses3.get(x).add(sections3.get(x));
		testSubjects.get(5).add(courses3.get(x));
	}
//------------------Seventh test --------------------------//
//a test solely about computing a large number of possibilities

	testSubjects.add(new Schedule("Seventh test should return 21 Courses, 1 possibilities"));
	ArrayList<ClassTime> classTimes4 = new ArrayList<ClassTime>();
	ArrayList<Section> sections4 = new ArrayList<Section>();
	ArrayList<Course> courses4 = new ArrayList<Course>();
	for(int y=0; y<7;y++){
		for(int x=0; x<10;x++){
			classTimes4.add(new ClassTime("regular", new ArrayList<String>(Arrays.asList(days[y])),
			LocalTime.parse("0"+x+":00"), LocalTime.parse("0"+x+":59")));
		}
		for(int x=10; x<24;x++){
			classTimes4.add(new ClassTime("regular", new ArrayList<String>(Arrays.asList(days[y])),
			LocalTime.parse(x+":00"), LocalTime.parse(x+":59")));
		}
	}
	for(int z=0;z<21;z++){
		for(int x=0;x<42;x++){
			sections4.add(new Section("section"+x));
			for(int y=0;y<4;y++){
				sections4.get(x).add(classTimes4.get((x+42*y)));
			}
		}
		courses4.add(new Course("course"+z));
		courses4.get(z).add(sections4.get(z));
		courses4.get(z).add(sections4.get(21+z));
		testSubjects.get(6).add(courses4.get(z));
	}


//---------------------General Test Construction----------------------//
		for(int x=0;x<classTimes.size();x++){
			sections.add(new Section("section"+x));
			sections.get(x).add(classTimes.get(x));
			courses.add(new Course("course"+x));
			courses.get(x).add(sections.get(x));
			if(x<3){
				testSubjects.get(0).add(courses.get(x));
			}
			if(x>0 && x<4){
				testSubjects.get(1).add(courses.get(x));
			}
			if(x>1 && x<6){
				testSubjects.get(2).add(courses.get(x));
			}
			if(x==0 || x>5){
				testSubjects.get(4).add(courses.get(x));
			}
		}
		testSubjects.get(2).getCourse(2).getSection(0).add(classTimes.get(0));
		testSubjects.get(2).getCourse(3).add(new Section("custom1"));
		testSubjects.get(2).getCourse(3).getSection(0).add(classTimes.get(2));

		for(int x=0;x<testSubjects.size();x++){
			System.out.println(testSubjects.get(x).getFormattedString());
			printScheduleArray(ScheduleArranger.getBestSchedules(testSubjects.get(x)));
		}

	}
	public static void printScheduleArray(Schedule[] array) {
		for (Schedule sc: array) {
			System.out.println(sc.getFormattedString());
		}
 	}
}
