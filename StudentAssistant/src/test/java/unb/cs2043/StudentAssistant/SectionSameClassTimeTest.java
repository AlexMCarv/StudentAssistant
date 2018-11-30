/**
 * 
 */
package unb.cs2043.StudentAssistant;

import static org.junit.Assert.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import unb.cs2043.student_assistant.ClassTime;
import unb.cs2043.student_assistant.Section;

/**
 * Test for sameClassTimes() method of Section and equivalent() method of ClassTime.
 * @author frede
 */
public class SectionSameClassTimeTest {
	
	private LocalTime time(int hr, int min) {
		return LocalTime.of(hr, min);
	}
	
	/**
	 * Test method for {@link unb.cs2043.student_assistant.Section#sameClassTimes}.
	 * Test method for {@link unb.cs2043.student_assistant.ClassTime#equivalent}.
	 */
	@Test
	public void testSameClassTimes() {
		
		ClassTime t1 = new ClassTime("Lab", new ArrayList<String>(Arrays.asList("M")), time(12,00), time(14,00));
		ClassTime t2 = new ClassTime("Lab", new ArrayList<String>(Arrays.asList("M")), time(12,00), time(14,00));
		
		//Different day
		ClassTime t3 = new ClassTime("Lab", new ArrayList<String>(Arrays.asList("M","W")), time(12,00), time(14,00));
		//Different time
		ClassTime t4 = new ClassTime("Lab", new ArrayList<String>(Arrays.asList("M")), time(12,30), time(14,00));
		//Different type
		ClassTime t5 = new ClassTime("Lecture", new ArrayList<String>(Arrays.asList("M")), time(12,00), time(14,00));
		
		assertTrue(t1.equivalent(t2));
		
		assertFalse(t1.equivalent(t3));
		assertFalse(t1.equivalent(t4));
		assertFalse(t1.equivalent(t5));
		
		Section s1 = new Section("FR01A");
		s1.add(t1);
		
		Section s2 = new Section("FR02A");
		s2.add(t2);
		
		Section s3 = new Section("FR03A");
		s3.add(t3);
		
		Section s4 = new Section("FR04A");
		s4.add(t4);
		
		Section s5 = new Section("FR05A");
		s5.add(t5);
		
		assertTrue(s1.sameClassTimes(s2));
		
		assertFalse(s1.sameClassTimes(s3));
		assertFalse(s1.sameClassTimes(s4));
		assertFalse(s1.sameClassTimes(s5));
		
		Section s6 = new Section("FR06A");
		s6.add(t1);
		s6.add(t3);
		
		assertFalse(s1.sameClassTimes(s6));
		assertFalse(s6.sameClassTimes(s1));
	}

}
