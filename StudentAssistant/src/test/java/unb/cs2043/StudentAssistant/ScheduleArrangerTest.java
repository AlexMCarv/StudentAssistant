package unb.cs2043.StudentAssistant;

import static org.junit.Assert.assertEquals;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import unb.cs2043.student_assistant.ClassTime;
import unb.cs2043.student_assistant.Course;
import unb.cs2043.student_assistant.Schedule;
import unb.cs2043.student_assistant.ScheduleArranger;
import unb.cs2043.student_assistant.Section;
import unb.cs2043.student_assistant.Permutations;

/**
 * This tests the component methods of the ScheduleArranger class (Everything EXCEPT the algorithm).
 * @author frede
 */
public class ScheduleArrangerTest {
	
	private LocalTime time(int hr, int min) {
		return LocalTime.of(hr, min);
	}
	
	@Test
	public void testNoConflictsBetween() {
		ArrayList<String> days = new ArrayList<>();
		days.add("M");
		ClassTime time1 = new ClassTime("Lab", days, time(21,30), time(22,30));
		Section sec1 = new Section("S1");
		sec1.add(time1);
		Course c1 = new Course("C1");
		c1.add(sec1);
		
		ClassTime time2 = new ClassTime("Lab", days, time(5, 00), time(7, 00));
		Section sec2 = new Section("S2");
		sec2.add(time2);
		Course c2 = new Course("C2");
		c2.add(sec2);
		
		Schedule schedule = new Schedule("Schedule");
		schedule.add(c1);
		schedule.add(c2);
		
		//Same section
		assertEquals(false, ScheduleArranger.noConflictsBetween(schedule, sec2));
		
		ClassTime time3 = new ClassTime("Lab", days, time(2, 00), time(3, 00));
		Section sec3 = new Section("S3");
		sec3.add(time3);
		
		//No conflict
		assertEquals(true, ScheduleArranger.noConflictsBetween(schedule, sec3));
		
		
		/*
		 * This test does not test many cases as this noConflictsBetween() 
		 * mostly relies on conflictsWith() method of Section class.
		 * (ie, as long as conflictsWith() works, this noConflictsBetween() should work)
		 */
	}
	
	
	@Test
	public void testIncrementAsCounter() {
		
		int[] indexes = {0, 0, 0};
		int[] maxIndexes = {2, 3, 1};
		
		int[][] steps = {
				{0, 0, 0},
				{1, 0, 0},
				{2, 0, 0},
				{0, 1, 0},
				{1, 1, 0},
				{2, 1, 0},
				{0, 2, 0},
				{1, 2, 0},
				{2, 2, 0},
				{0, 3, 0},
				{1, 3, 0},
				{2, 3, 0},
				{0, 0, 1},
				{1, 0, 1},
				{2, 0, 1},
				{0, 1, 1},
				{1, 1, 1},
				{2, 1, 1},
				{0, 2, 1},
				{1, 2, 1},
				{2, 2, 1},
				{0, 3, 1},
				{1, 3, 1},
				{2, 3, 1},	//Max
				{0, 0, 0}
		};
		
		for (int i=0; i<steps.length; i++) {
//			System.out.println(Arrays.toString(indexes));
			assertEquals(true, Arrays.equals(steps[i], indexes));
			ScheduleArranger.incrementAsCounter(indexes, maxIndexes);
		}
	}
	
	
	
	//This is a temporary test method to see if algorithm somewhat works
	@Test
	public void testGetBestSchedules() {
		ArrayList<String> days = new ArrayList<>();
		days.add("M");
		ClassTime time1 = new ClassTime("Lab", days, time(21,30), time(22,30));
		Section sec1 = new Section("S1");
		sec1.add(time1);
		Course c1 = new Course("C1");
		c1.add(sec1);
		
		ClassTime time2 = new ClassTime("Lab", days, time(5, 00), time(7, 00));
		Section sec2 = new Section("S2");
		sec2.add(time2);
		Course c2 = new Course("C2");
		c2.add(sec2);
		
		Schedule schedule = new Schedule("Schedule");
		schedule.add(c1);
		schedule.add(c2);
		
		//No conflicts
		System.out.println("--Sould have 1 schedule with 2 courses--");
		Schedule[] results = ScheduleArranger.getBestSchedules(schedule);
		printScheduleArray(results);
		
		//--------------------------------------------------------------
		
		ClassTime time3 = new ClassTime("Lab", days, time(4, 00), time(6, 00));
		Section sec3 = new Section("S3");
		sec3.add(time3);
		Course c3 = new Course("C3");
		c3.add(sec3);
		
		schedule.add(c3);
		
		//Conflict
		System.out.println("--Sould have 2 schedules with 2 courses--");
		results = ScheduleArranger.getBestSchedules(schedule);
		printScheduleArray(results);
		
		//--------------------------------------------------------------
		
		time3.setEndTime(time(22,00));
		//Conflict
		System.out.println("--Sould have 2 schedules, 1 with 1 course, the other with 2 courses--");
		results = ScheduleArranger.getBestSchedules(schedule);
		printScheduleArray(results);
		
		//--------------------------------------------------------------
		
		//Let's go crazy
		//Schedule with 7 courses, 3 sections each
		//No conflicts at all
		
		Schedule schedule2 = new Schedule("Crazy");
		
		for (int i=0; i<7; i++) {
			Course c = new Course("C"+i);
			
			for (int j=0; j<3; j++) {
				Section s = new Section("S"+j);
				
				//Use unique days to ensure there are aboslutely no conflicts detected.
				ArrayList<String> uniqueDays = new ArrayList<String>(Arrays.asList("D"+i+j));
				ClassTime t = new ClassTime("Lab", uniqueDays, time(5, 00), time(6, 00));
				
				s.add(t);
				c.add(s);
			}
			
			schedule2.add(c);
		}
		
		System.out.println("--Should return 4 schedules of 7 courses--");
		System.out.println("**(11022480 schedule combinations, Takes about 30sec)**\n");
		long startTime = System.nanoTime();
		results = ScheduleArranger.getBestSchedules(schedule2);
		long endTime = System.nanoTime();
		double duration = (endTime - startTime)/1000000000.0;
		printScheduleArray(results);
		System.out.println("Time: "+duration+"s");
	}
	
	public void printScheduleArray(Schedule[] array) {
		for (Schedule sc: array) {
			System.out.println(sc.getFormattedString());
		}
	}
}
