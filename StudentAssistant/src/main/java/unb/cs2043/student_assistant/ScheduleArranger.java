package unb.cs2043.student_assistant;

import java.util.TreeSet;

/**
 * This class contains the main algorithm of this application 
 * which finds the best schedule arrangement(s) given a set of courses.
 * @author Frederic Verret
 */
public class ScheduleArranger {
	
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
					//Add section to currentSchedule (need a new course to hold it)
					Course courseToAdd = new Course(currentCourse.getName());
					courseToAdd.add(sectionToAdd);
					currentSchedule.add(courseToAdd);
				}
			}
			
			if (incrementAsCounter(indexes, maxIndexes)) {
				done = true;
			}
			
			scheduleArrangements.add(currentSchedule);
			numSchedules++;
		}
		
		//Convert the TreeSet to array
		Schedule[] results = null;
		results = scheduleArrangements.toArray(results);
		
		//TODO: Remove this debug print
		for (Schedule sc: results) {
			System.out.println(sc);
		}
		
		return results;
	}
	
	
	/**
	 * Return false if any section in schedule conflicts with section, true otherwise.
	 * @param schedule
	 * @param section
	 * @return False if any section in schedule conflicts with section, true otherwise.
	 */
	public static boolean noConflictsBetween(Schedule schedule, Section section) {
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
	
	
	/**
	 * Increments an array of integers in a fashion similar to a counter.
	 * @param indexes
	 * @param maxIndexes
	 * @return False when reached max value of last index (counter is reset).
	 */
	public static boolean incrementAsCounter(int[] indexes, int[] maxIndexes) {
		return incrementRecursive(0, indexes, maxIndexes);
	}
	
	
	/**
	 * Recursive method used by incrementAsCounter.
	 * @param i
	 * @param indexes
	 * @param maxIndexes
	 * @return False when reached max value of last index (counter is reset).
	 */
	private static boolean incrementRecursive(int i, int[] indexes, int[] maxIndexes) {
		boolean reachedMax = false;
		
		if (indexes[i]<maxIndexes[i]) {
			//Increment normally
			indexes[i]++;
		}
		else {
			//Reached max of this index, reset this index
			indexes[i] = 0;
			
			//Check if this is last index
			if (i<indexes.length-1) {
				//Not last index, increment next.
				reachedMax = incrementRecursive(i+1, indexes, maxIndexes);
			}
			else {
				//Last index has reached max value (counter has completely reset)
				reachedMax = true;
			}
		}
		
		return reachedMax;
	}
}
