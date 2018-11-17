package unb.cs2043.StudentAssistant;

import static org.junit.Assert.*;

import java.time.LocalTime;
import java.util.ArrayList;

import org.junit.Test;

import unb.cs2043.student_assistant.ClassTime;
import unb.cs2043.student_assistant.Course;
import unb.cs2043.student_assistant.Schedule;
import unb.cs2043.student_assistant.ScheduleArranger;
import unb.cs2043.student_assistant.Section;

/**
 * This tester tests the conflictsWith() methods of both ClassTime and Section classes, 
 * as well as the noConflictsBetween() method of ScheduleArranger class.
 * @author Frederic Verret
 */
public class ConflictsWithTest {
	
	private LocalTime time(int hr, int min) {
		return LocalTime.of(hr, min);
	}
	
	@Test
	public void testConflictsWithClassTime() {
		//Same start and end time
		ArrayList<String> days = new ArrayList<>();
		days.add("M");
		ClassTime time1 = new ClassTime("Lab", days, time(21,30), time(22,30));
		ClassTime time2 = new ClassTime("Lab", days, time(21,30), time(22,30));
		assertEquals(true, time1.conflictsWith(time2));
		
		//Border case
		ClassTime time3 = new ClassTime("Lab", days, time(22,30), time(23,30));
		assertEquals(false, time1.conflictsWith(time3));
		
		//Complete overlap
		ClassTime time4 = new ClassTime("Lab", days, time(20, 00), time(23, 59));
		assertEquals(true, time1.conflictsWith(time4));
		
		//Normal overlap
		ClassTime time5 = new ClassTime("Lab", days, time(18, 00), time(22, 00));
		assertEquals(true, time1.conflictsWith(time5));
		
		//Not conflicting
		ClassTime time6 = new ClassTime("Lab", days, time(8, 00), time(10, 00));
		assertEquals(false, time1.conflictsWith(time6));
		
		//Time conflicting but different days
		ArrayList<String> days2 = new ArrayList<>();
		days.add("T");
		ClassTime time7 = new ClassTime("Lab", days2, time(21,30), time(22,30));
		assertEquals(false, time1.conflictsWith(time7));
	}
	
	@Test
	public void testConflictsWithSection() {
		//1-1 conflict
		ArrayList<String> days = new ArrayList<>();
		days.add("M");
		ClassTime time1 = new ClassTime("Lab", days, time(21,30), time(22,30));
		ClassTime time2 = new ClassTime("Lab", days, time(21,30), time(22,30));
		Section sec1 = new Section("S1");
		sec1.add(time1);
		Section sec2 = new Section("S2");
		sec2.add(time2);
		assertEquals(true, sec1.conflictsWith(sec2));
		
		//2-2 conflict
		ClassTime time3 = new ClassTime("Lab", days, time(8, 00), time(10, 00));
		sec1.add(time3);
		sec2.add(time3);
		assertEquals(true, sec1.conflictsWith(sec2));
		
		//2-2 No conflict
		ClassTime time4 = new ClassTime("Lab", days, time(5, 00), time(7, 00));
		ClassTime time5 = new ClassTime("Lab", days, time(12, 00), time(13, 00));
		Section sec3 = new Section("S3");
		sec3.add(time1);
		sec3.add(time4);
		Section sec4 = new Section("S4");
		sec4.add(time3);
		sec4.add(time5);
		assertEquals(false, sec3.conflictsWith(sec4));
		
		//One section conflicts internally (but not with the other section)
		Section sec5 = new Section("S5");
		sec5.add(time1);
		sec5.add(time2);
		assertEquals(false, sec5.conflictsWith(sec4));
		
	}
	
}
