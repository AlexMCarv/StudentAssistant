package unb.cs2043.StudentAssistant;

import static org.junit.Assert.*;

import java.time.LocalTime;
import java.util.ArrayList;
import org.junit.Test;
import unb.cs2043.student_assistant.ClassTime;

public class ClassTimeTest {

	@Test
	public void testGetDays() {
		LocalTime start = LocalTime.of(21, 30);
		LocalTime end = LocalTime.of(22, 30);
		ArrayList<String> days = new ArrayList<>();
		days.add("Su");
		days.add("M");
		days.add("Th");
		ClassTime time = new ClassTime("Lec", days, start, end);
		assertEquals("SuMTh", time.getDays());
		
		days.remove("M");
		assertEquals("SuTh", time.getDays());
	}

	@Test
	public void testGetStartTime() {
		LocalTime start = LocalTime.of(21, 30);
		LocalTime end = LocalTime.of(22, 30);
		ArrayList<String> days = new ArrayList<>();
		days.add("Su");
		ClassTime time = new ClassTime("Lec", days, start, end);
		
		assertEquals(LocalTime.of(21, 30), time.getStartTime());
		assertEquals("21:30", time.getStartTime().toString());
	}

	@Test
	public void testGetEndTime() {
		LocalTime start = LocalTime.of(21, 30);
		LocalTime end = LocalTime.of(22, 30);
		ArrayList<String> days = new ArrayList<>();
		days.add("Su");
		ClassTime time = new ClassTime("Lec", days, start, end);
		
		assertEquals(LocalTime.of(22, 30), time.getEndTime());
		assertEquals("22:30", time.getEndTime().toString());
	}

	@Test
	public void testGetType() {
		LocalTime start = LocalTime.of(21, 30);
		LocalTime end = LocalTime.of(22, 30);
		ArrayList<String> days = new ArrayList<>();
		days.add("Su");
		ClassTime time = new ClassTime("Lec", days, start, end);
				
		assertEquals("Lec", time.getType());
	}

	@Test
	public void testToString() {
		LocalTime start = LocalTime.of(21, 30);
		LocalTime end = LocalTime.of(22, 30);
		ArrayList<String> days = new ArrayList<>();
		days.add("Su");
		days.add("M");
		days.add("Th");
		ClassTime time = new ClassTime("Lec", days, start, end);
				
		assertEquals("Lec SuMTh 21:30-22:30", time.toString());
	}

	@Test
	public void testSetDays() {
		LocalTime start = LocalTime.of(21, 30);
		LocalTime end = LocalTime.of(22, 30);
		ArrayList<String> days = new ArrayList<>();
		days.add("Su");
		days.add("M");
		ClassTime time = new ClassTime("Lec", days, start, end);
		
		ArrayList<String> newDays = new ArrayList<>();
		newDays.add("T");
		newDays.add("Th");
		time.setDays(newDays);
		
		assertEquals("TTh", time.getDays());
	}

	@Test
	public void testSetStartTime() {
		LocalTime start = LocalTime.of(21, 30);
		LocalTime end = LocalTime.of(22, 30);
		ArrayList<String> days = new ArrayList<>();
		days.add("Su");
		ClassTime time = new ClassTime("Lec", days, start, end);
		
		LocalTime newTime = LocalTime.of(21, 45);
		time.setStartTime(newTime);
		
		assertEquals("21:45", time.getStartTime().toString());
	}

	@Test
	public void testSetEndTime() {
		LocalTime start = LocalTime.of(21, 30);
		LocalTime end = LocalTime.of(22, 30);
		ArrayList<String> days = new ArrayList<>();
		days.add("Su");
		ClassTime time = new ClassTime("Lec", days, start, end);
		
		LocalTime newTime = LocalTime.of(22, 55);
		time.setEndTime(newTime);
		
		assertEquals("22:55", time.getEndTime().toString());
	}
}
