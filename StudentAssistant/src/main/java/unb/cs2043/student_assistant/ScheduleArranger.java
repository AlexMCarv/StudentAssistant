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
		//Use a set to prevent duplicates in results
		TreeSet<Schedule> scheduleArrangements = new TreeSet<>();
		
		int numCourses = courseList.getSize();
		
		//Each course has an index (indicating what section to add)
		int[] indexes = new int[numCourses];
		//Stores the max number for each index (how many sections in each course)
		int[] maxIndexes = new int[numCourses];
		//Initialize all indexes to 0, and set max indexes
		for (int i=0; i<indexes.length; i++) {
			indexes[i] = 0;
			maxIndexes[i] = courseList.getCourse(i).getSize();
		}
		
		int numSchedules = 0;
		boolean done = false;
		while (!done) {
			Schedule currentSchedule = new Schedule("S"+numSchedules);
			
			for (int i=0; i<numCourses && !done; i++) {
				Course currentCourse = courseList.getCourse(i);
				Section sectionToAdd = currentCourse.getSection(indexes[i]);
				
				if (noConflictsBetween(currentSchedule, sectionToAdd)) {
					currentSchedule.add(currentCourse);
				}
				
			}
			
			if (!incrementAsCounter(indexes, maxIndexes)) {
				done = true;
			}
			
			scheduleArrangements.add(currentSchedule);
			numSchedules++;
		}
		
		
		return null;
	}
	
	
	//Return false if any section in schedule conflicts with section, true otherwise.
	private static boolean noConflictsBetween(Schedule schedule, Section section) {
		boolean noConflicts = true;
		
		for (int i=0; i<schedule.copyCourses().size() && noConflicts; i++) {
			Course currentCourse = schedule.getCourse(i);
			for (int j=0; j<currentCourse.copySections().size() && noConflicts; j++) {
				Section currentSection = currentCourse.getSection(j);
				noConflicts = !currentSection.conflictsWith(section);
			}
		}
		
		return noConflicts;
	}
	
	
	//Increments an array of integers in a fashion similar to a counter.
	private static boolean incrementAsCounter(int[] indexes, int[] maxIndexes) {
		//TODO
		return false;
	}
}
