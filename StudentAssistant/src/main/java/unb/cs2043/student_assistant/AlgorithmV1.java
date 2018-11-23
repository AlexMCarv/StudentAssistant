package unb.cs2043.student_assistant;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * Class implementing the actual functionality of the algorithm (version 1).
 * @author frede
 */
public class AlgorithmV1 {
	
	private final Schedule courseList;
	private TreeSet<Schedule> scheduleArrangements;
	
	public AlgorithmV1(Schedule courseList) {
		this.scheduleArrangements = new TreeSet<>();
		this.courseList = courseList;
	}
	
	
	public TreeSet<Schedule> findPossibilities() {
		int numCourses = courseList.getSize();
		
		//Each course has an index (indicating what section to add)
		int[] indexes = new int[numCourses];
		//Stores the max number for each index (how many sections in each course)
		int[] maxIndexes = new int[numCourses];
		//Stores numbers 0 to numCourses-1 (used for permutations)
		Integer[] courseNums = new Integer[numCourses];
		//Initialize integer arrays
		for (int i=0; i<indexes.length; i++) {
			indexes[i] = 0;
			maxIndexes[i] = courseList.getCourse(i).getSize()-1;
			courseNums[i] = i;
		}
		
		
		int numSchedules = 0;
		boolean done = false;
		//Go through all section combinations (Average#OfSectionsInCourses^#OfCourses possibilities)
		while (!done) {
			
			//Go throug all permutations of the courses (#OfCourses! possibilities)
			Permutations<Integer> perm = new Permutations<Integer>(courseNums);
		    while(perm.hasNext()){
		    	numSchedules++;
		    	
		    	Integer[] courseIndexes = perm.next();
		    	
		    	Schedule currentSchedule = new Schedule("S"+numSchedules);
		    	
				for (int i:courseIndexes) {
					Course currentCourse = courseList.getCourse(i);
					Section sectionToAdd = currentCourse.getSection(indexes[i]);
					
					if (noConflictsBetween(currentSchedule, sectionToAdd)) {
						//Add section to currentSchedule (need a new course to hold it)
						Course courseToAdd = new Course(currentCourse.getName());
						courseToAdd.add(sectionToAdd);
						currentSchedule.add(courseToAdd);
					}
				}
				
				//Make sure not a subset of a schedule already in the list
				if (notSubset(scheduleArrangements, currentSchedule)) {
					scheduleArrangements.add(currentSchedule);
					
					//Check if currentSchedule is a superset of a schedule in the list
					List<Schedule> subSchedules = findSubsets(scheduleArrangements, currentSchedule);
					for (Schedule subSc: subSchedules) {
						scheduleArrangements.remove(subSc);
					}
				}
				
		    }
			
			if (incrementAsCounter(indexes, maxIndexes)) {
				done = true;
			}	
		}
		
		return scheduleArrangements;
	}
	
	
	/**
	 * Returns an integer representing the complexity of the computation.
	 * @param schedule The schedule to compute for.
	 * @return An integer representing the complexity of the computation.
	 * @throws ArithmeticException
	 */
	public long getComplexity() throws ArithmeticException {
		long result = 1;
		
		for (int i=0; i<courseList.getSize(); i++) {
			Course currentCourse = courseList.getCourse(i);
			result *= currentCourse.getSize();
		}
		result--;
		
		result *= factorial(courseList.getSize());
		
		if (result<0) {
			throw new ArithmeticException("Number is too large.");
		}
		
		return result;
	}
	
	
	/**
	 * Returns n!.
	 * @param n
	 * @return n!
	 * @throws ArithmeticException
	 */
	public static long factorial(int n) throws ArithmeticException {  
		long fact=1;
		for(int i=1; i<=n; i++){
			fact=fact*i;
		}
		
		if (fact<0) {
			throw new ArithmeticException("Number is too large.");
		}
		
		return fact;
	}
	
	
	/**
	 * Returns A time estimate in seconds of the computation time.
	 * @param complexity
	 * @return A time estimate in seconds of the computation time.
	 */
	public double getTimeEstimate(long complexity) {
		return complexity/400000.0; //Estimate from tests
	}
	public double getTimeEstimate() {
		return getComplexity()/400000.0; //Estimate from tests
	}
	
	
	/**
	 * Return false if any section in schedule conflicts with section, true otherwise.
	 * @param schedule
	 * @param section
	 * @return False if any section in schedule conflicts with section, true otherwise.
	 */
	public static boolean noConflictsBetween(Schedule schedule, Section section) {
		boolean noConflicts = true;
		
		for (int i=0; i<schedule.getSize() && noConflicts; i++) {
			Course currentCourse = schedule.getCourse(i);
			for (int j=0; j<currentCourse.getSize() && noConflicts; j++) {
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
	
	
	/**
	 * Returns true if subSchedule is a sub schedule of any schedule inside the list schedules.
	 * @param schedules	List of schedules
	 * @param subSchedule Possible subschedule
	 * @return True if subSchedule is a sub schedule of any schedule inside the list schedules.
	 */
	private static boolean notSubset(TreeSet<Schedule> schedules, Schedule subSchedule) {
		for (Schedule sc : schedules) {
			if (subSchedule.isSubsetOf(sc)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Returns a list of schedules in the schedules list that are subsets of superSchedule.
	 * @param schedules The list of schedules.
	 * @param superSchedule The superSchedule.
	 * @return A list of schedules that are subsets of superSchedule.
	 */
	public static List<Schedule> findSubsets(TreeSet<Schedule> schedules, Schedule superSchedule) {
		ArrayList<Schedule> subSchedules = new ArrayList<>();
		for (Schedule sc : schedules) {
			if (sc.isSubsetOf(superSchedule)) {
				subSchedules.add(sc);
			}
		}
		return subSchedules;
	}
}
