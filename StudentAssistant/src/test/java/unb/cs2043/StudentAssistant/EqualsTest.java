package unb.cs2043.StudentAssistant;

import static org.junit.Assert.*;

import org.junit.Test;

import unb.cs2043.student_assistant.Course;
import unb.cs2043.student_assistant.Schedule;
import unb.cs2043.student_assistant.Section;

/**
 * Tests the equals() method of schedule.
 * @author frede
 */
public class EqualsTest {
	
	@Test
	public void ScheduleEqualsTest() {
		
		Schedule sc = new Schedule("1");
		Schedule sc2 = new Schedule("2");
		sc.add(new Course("test"));
		sc2.add(new Course("test"));
		
		assertEquals(false, sc==sc2);
		assertEquals(true, sc.equals(sc2));
		assertEquals(false, sc.copyCourses()==sc2.copyCourses());
		assertEquals(true, sc.copyCourses().equals(sc2.copyCourses()));
		
		Schedule sc3 = new Schedule("");
		assertEquals(false, sc.equals(sc3));
	}
	
	@Test
	public void CourseEqualsTest() {
		
		Course c1 = new Course("1");
		Course c2 = new Course("1");
		Course c3 = new Course("1");
		Section section = new Section("test");
		c1.add(section);
		c2.add(section);
		c3.add(new Section("test"));
		
		assertEquals(false, c1==c2);
		assertEquals(true, c1.equals(c2));
		assertEquals(false, c1.copySections()==c2.copySections());
		assertEquals(true, c1.copySections().equals(c2.copySections()));
		
		Course c4 = new Course("");
		assertEquals(false, c1.equals(c4));
	}
}
