package unb.cs2043.StudentAssistant;

import static org.junit.Assert.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;

import org.junit.Test;

import unb.cs2043.student_assistant.ClassTime;
import unb.cs2043.student_assistant.Course;
import unb.cs2043.student_assistant.Schedule;
import unb.cs2043.student_assistant.Section;

public class ScheduleSubsetTest {

	private LocalTime time(int hr, int min) {
		return LocalTime.of(hr, min);
	}
	
	@Test
	public void test() {
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
		
		
		ClassTime time3 = new ClassTime("Lab", days, time(4, 00), time(6, 00));
		Section sec3 = new Section("S3");
		sec3.add(time3);
		Course c3 = new Course("C3");
		c3.add(sec3);
		
		Schedule superSchedule = new Schedule(schedule);
		superSchedule.add(c3);
		
		//True - Subset
		assertTrue(schedule.isSubsetOf(superSchedule));
		
		//False - Superset
		assertFalse(superSchedule.isSubsetOf(schedule));
		
		//False - Same schedule
		assertFalse(schedule.isSubsetOf(schedule));
	}

}
