package unb.cs2043.StudentAssistant;

import static org.junit.Assert.assertEquals;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Ignore;
import org.junit.Test;

import unb.cs2043.student_assistant.ClassTime;
import unb.cs2043.student_assistant.Course;
import unb.cs2043.student_assistant.Schedule;
import unb.cs2043.student_assistant.ScheduleArranger;
import unb.cs2043.student_assistant.Section;
import unb.cs2043.student_assistant.Permutations;

/**
 * This contains very basic tests for the algorithms.
 * @author frede
 */
public class ScheduleArrangerTest {
	
	private LocalTime time(int hr, int min) {
		return LocalTime.of(hr, min);
	}
	
	@Ignore("Test is ignored in general since this test is manual.")
	@Test
	//This is a temporary test method to see if algorithm somewhat works
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
		//Schedule with 6 courses, 3 sections each
		//No conflicts at all
		
		Schedule schedule2 = new Schedule("Crazy");
		
		for (int i=0; i<6; i++) {
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
		
		System.out.println("--Should return 4 schedules of 6 courses--");
		long startTime = System.nanoTime();
		results = ScheduleArranger.getBestSchedules(schedule2);
		long endTime = System.nanoTime();
		double duration = (endTime - startTime)/1000000000.0;
		printScheduleArray(results);
		System.out.println("Time: "+duration+"s");
	}
	
	private void printScheduleArray(Schedule[] array) {
		for (Schedule sc: array) {
			System.out.println(sc.getFormattedString());
		}
	}
	
	
	@Ignore("Test is ignored in general since this test is manual.")
	@Test
	public void testAlexBug() {
		ArrayList<String> TTh = new ArrayList<>();
		TTh.add("T"); TTh.add("Th");
		ArrayList<String> Th = new ArrayList<>();
		Th.add("Th");
		ArrayList<String> MWF = new ArrayList<>();
		MWF.add("M"); MWF.add("W"); MWF.add("F");
		ArrayList<String> M = new ArrayList<>();
		M.add("M");
		
		ClassTime time1 = new ClassTime("Lec", TTh, time(10,00), time(11,20));
		ClassTime time2 = new ClassTime("Lab", Th, time(14,30), time(16,20));
		Section sec1 = new Section("FR01A");
		sec1.add(time1); sec1.add(time2);
		
		ClassTime time3 = new ClassTime("Lec", TTh, time(13,00), time(14,20));
		ClassTime time4 = new ClassTime("Lab", M, time(13,30), time(15,20));
		Section sec2 = new Section("FR02A");
		sec2.add(time3); sec2.add(time4);
		
		Course CS2043 = new Course("CS2043");
		CS2043.add(sec1); CS2043.add(sec2);
		
		ClassTime time5 = new ClassTime("Lec", MWF, time(11,30), time(12,20));
		ClassTime time6 = new ClassTime("Tutorial", Th, time(12,30), time(13,20));
		Section sec3 = new Section("FR01A");
		sec3.add(time5); sec3.add(time6);
		
		Course ECE2214 = new Course("ECE2214");
		ECE2214.add(sec3);
		
		Schedule schedule = new Schedule("Schedule");
		schedule.add(CS2043);
		schedule.add(ECE2214);
		
		Schedule[] results = ScheduleArranger.getBestSchedules(schedule);
		printScheduleArray(results);
	}
	
	@Test
	public void testGetBestSchedulesV2() {
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
		Schedule[] results = ScheduleArranger.getBestSchedules(schedule, 2);
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
		results = ScheduleArranger.getBestSchedules(schedule, 2);
		printScheduleArray(results);
		
		//--------------------------------------------------------------
		
		time3.setEndTime(time(22,00));
		//Conflict
		System.out.println("--Sould have 2 schedules, 1 with 1 course, the other with 2 courses--");
		results = ScheduleArranger.getBestSchedules(schedule, 2);
		printScheduleArray(results);
		
		//--------------------------------------------------------------
		
		//Let's go crazy
		//Schedule with 6 courses, 3 sections each
		//No conflicts at all
		
		Schedule schedule2 = new Schedule("Crazy");
		
		for (int i=0; i<6; i++) {
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
		
		System.out.println("--Should return "+ScheduleArranger.NUM_BEST_SCHEDULES+" schedules of 6 courses--");
		long startTime = System.nanoTime();
		results = ScheduleArranger.getBestSchedules(schedule2, 2);
		long endTime = System.nanoTime();
		double duration = (endTime - startTime)/1000000000.0;
		printScheduleArray(results);
		System.out.println("Time: "+duration+"s");
		
		//--------------------------------------------------------------
		
		//Let's go crazy
		//Schedule with 6 courses, 3 sections each
		//Everything conflicts
		
		Schedule schedule3 = new Schedule("Crazy");
		
		for (int i=0; i<6; i++) {
			Course c = new Course("C"+i);
			
			for (int j=0; j<3; j++) {
				Section s = new Section("S"+j);
				
				ClassTime t = new ClassTime("Lab", days, time(5, 00), time(6, 00));
				
				s.add(t);
				c.add(s);
			}
			
			schedule3.add(c);
		}
		
		System.out.println("--Should return "+ScheduleArranger.NUM_BEST_SCHEDULES+" schedules of 1 courses each--");
		startTime = System.nanoTime();
		results = ScheduleArranger.getBestSchedules(schedule3, 2);
		endTime = System.nanoTime();
		duration = (endTime - startTime)/1000000000.0;
		printScheduleArray(results);
		System.out.println("Time: "+duration+"s");
	}
}
