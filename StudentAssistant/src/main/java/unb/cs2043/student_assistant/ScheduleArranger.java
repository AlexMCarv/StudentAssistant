package unb.cs2043.student_assistant;

import java.util.TreeSet;

/**
 * This class contains the main algorithm of this application 
 * which finds the best schedule arrangement(s) given a set of courses.
 * @author Frederic Verret
 */
public class ScheduleArranger {
	
	//The array returned contains (at most) 4 schedule objects
	public static Schedule[] getBestSchedules(Schedule courseList) {
		//Use a set to prevent duplicates
		TreeSet<Schedule> scheduleArrangements = new TreeSet<>();
		
		int numCourses = courseList.getSize();
		
		//Each course has an index (indicating what section to add)
		int[] indexes = new int[numCourses];
		
		//Initialize all indexes to 0
		for (int i=0; i<numCourses; i++) {
			indexes[i] = 0;
		}
		
		boolean done = false;
		int numSchedules = 0;
		while (!done) {
			Schedule currentSchedule = new Schedule("S"+numSchedules);
			
			for (int i=0; i<numCourses && !done; i++) {
				
			}
			
			numSchedules++;
		}
		
		
		return null;
	}
}
