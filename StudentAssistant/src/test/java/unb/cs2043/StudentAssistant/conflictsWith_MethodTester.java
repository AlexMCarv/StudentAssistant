package unb.cs2043.StudentAssistant;

import static org.junit.Assert.*;

import java.time.LocalTime;
import java.util.ArrayList;

import org.junit.Test;

import unb.cs2043.student_assistant.ClassTime;

/**
 * This tester tests the conflictsWith() methods of both ClassTime and Section classes.
 * @author Frederic Verret
 */

public class conflictsWith_MethodTester {

	@Test
	public void testConflictsWithClassTime() {
		LocalTime start = LocalTime.of(21, 30);
		LocalTime end = LocalTime.of(22, 30);
		ArrayList<String> days = new ArrayList<>();
		days.add("M");
		ClassTime time1 = new ClassTime("Lab", days, start, end);
		ClassTime time2 = new ClassTime("Lab", days, start, end);
		//Same start and end time
		assertEquals(true, time1.conflictsWith(time2));
		
		LocalTime start3 = LocalTime.of(22, 30);
		LocalTime end3 = LocalTime.of(23, 30);
		ClassTime time3 = new ClassTime("Lab", days, start3, end3);
		//Border case
		assertEquals(false, time1.conflictsWith(time3));
		
		LocalTime start4 = LocalTime.of(20, 00);
		LocalTime end4 = LocalTime.of(23, 59);
		ClassTime time4 = new ClassTime("Lab", days, start4, end4);
		//Complete overlap
		assertEquals(true, time1.conflictsWith(time4));
		
		LocalTime start5 = LocalTime.of(18, 00);
		LocalTime end5 = LocalTime.of(22, 00);
		ClassTime time5 = new ClassTime("Lab", days, start5, end5);
		//Normal overlap
		assertEquals(true, time1.conflictsWith(time5));
		
		LocalTime start6 = LocalTime.of(8, 00);
		LocalTime end6 = LocalTime.of(10, 00);
		ClassTime time6 = new ClassTime("Lab", days, start6, end6);
		//Not conflicting
		assertEquals(false, time1.conflictsWith(time6));
		
		ArrayList<String> days2 = new ArrayList<>();
		days.add("T");
		ClassTime time7 = new ClassTime("Lab", days2, start, end);
		//Time conflicting but different days
		assertEquals(false, time1.conflictsWith(time7));
	}
	
	@Test
	public void testConflictsWithSection() {
		
		assertEquals(true, false);
	}
	

}
