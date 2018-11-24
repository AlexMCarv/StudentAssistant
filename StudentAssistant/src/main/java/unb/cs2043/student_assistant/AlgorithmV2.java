package unb.cs2043.student_assistant;

import java.util.TreeSet;

/**
 * Class implementing the actual functionality of the algorithm (version 2).
 * @author frede
 */
public class AlgorithmV2 {
	
	private TreeSet<Schedule> schedules;
	private final Section[] sections;
	private final int[][] sectionConflictsMap;
	private final String[] courseNames;
	
	private int minBestScheduleSize;
	private final int numSections;
	private final int timeout;
	private long startTime;
	
	public AlgorithmV2 (Schedule courseList) {
		this.schedules = new TreeSet<Schedule>();
		this.minBestScheduleSize = 0;
		
		//Get total number of sections
		int numSections = 0;
		for (int i=0; i<courseList.getSize(); i++) {
			numSections += courseList.getCourse(i).getSize();
		}
		
		//Arrays containing all the sections and their associated course name.
		Section[] sections = new Section[numSections];
		String[] courseNames = new String[numSections];
		int sectionIndex = 0;
		for (int i=0; i<courseList.getSize(); i++) {
			Course currentCourse = courseList.getCourse(i);
			for (int j=0; j<currentCourse.getSize(); j++, sectionIndex++) {
				sections[sectionIndex] = currentCourse.getSection(j);
				courseNames[sectionIndex] = currentCourse.getName();
			}
		}
		
		//Matrix storing section conflicts (1=conflict, 0=no conflict)
		int[][] sectionConflictsMap = new int[numSections][numSections];
		for (int i=0; i<sections.length; i++) {
			//Section conflicts with itself
			sectionConflictsMap[i][i] = 1;
			
			for (int j=i+1; j<sections.length; j++) {
				/*Check if sections conflicts or sections are in same course 
				(sections in same course conflict since you can only have one section for each course)*/
				if (sections[i].conflictsWith(sections[j]) || courseNames[i].equals(courseNames[j])) {
					//Section i and section j conflicts with each other
					sectionConflictsMap[i][j] = 1;
					sectionConflictsMap[j][i] = 1;
				}
				else {
					//No conflict
					sectionConflictsMap[i][j] = 0;
				}
			}
		}
		
		this.sections = sections;
		this.sectionConflictsMap = sectionConflictsMap;
		this.courseNames = courseNames;
		this.numSections = sections.length;
		
		
		//Print sectionConflictsMap TODO: remove this
//		for (int i=0; i<sectionConflictsMap.length; i++) {
//			System.out.println(Arrays.toString(sectionConflictsMap[i]));
//		}
		System.out.println("Conflict%: "+getConflictPercent());
		
		timeout = ScheduleArranger.MAX_TIME;
	}
	
	public TreeSet<Schedule> findPossibilities() {
		startTime = System.nanoTime();
		for (int i=0; i<numSections && getRunningTime()<timeout; i++) {
			Schedule sc = new Schedule("S"+i);
			TreeSet<Integer> currentConflicts = new TreeSet<>();
			findPossibilitiesRec(i, sc, currentConflicts, 1);
		}
		
		return schedules;
	}
	
	private void findPossibilitiesRec(int index, Schedule scheduleInProgress, TreeSet<Integer> currentConflicts, int level) {
		if (getRunningTime()>timeout) {
			return;
		}
		
		Schedule tempSchedule = new Schedule(scheduleInProgress);
		TreeSet<Integer> tempConflicts = new TreeSet<>(currentConflicts);
		
		//Add current section
		Course tempCourse = new Course(courseNames[index]);
		tempCourse.add(sections[index]);
		tempSchedule.add(tempCourse);
				
		for (int i=0; i<sectionConflictsMap[index].length; i++) {
			if (sectionConflictsMap[index][i]==1) {
				tempConflicts.add(i);
			}
		}
		
		//TODO: remove debug prints
//		levelPrint("Index: "+index, level);
		
//		String tempCon = "";
//		tempCon += "tempConflicts: [";
//		for (Integer c: tempConflicts) {
//			tempCon += c+", ";
//		}
//		tempCon += "]";
//		levelPrint(tempCon, level);
		
		if (tempConflicts.size() >= numSections) {
			//Cannot add any more sections, schedule is complete
			schedules.add(tempSchedule);
			
			//TODO: remove
//			levelPrint("Adding: \n"+tempSchedule.getFormattedString(), level);
			
			//Update minBestScheduleSize
			if (schedules.size()>=ScheduleArranger.NUM_BEST_SCHEDULES && tempSchedule.getSize()>minBestScheduleSize) {
				minBestScheduleSize = ((Schedule)schedules.toArray()[ScheduleArranger.NUM_BEST_SCHEDULES-1]).getSize();
			}
			
			if (tempConflicts.size() > numSections) {
				//Should never happen
				System.out.println(" !!! tempConflicts.size() > numSections !!! ");
			}
		}
		//Check if schedule currently being built can potentialy end up in the top 6 best
		else if (tempSchedule.getSize()+numSections-tempConflicts.size()>minBestScheduleSize) {
			//Recurse with next non-conflicting section
			for (int i=0; i<numSections; i++) {
				if (sectionConflictsMap[index][i]==0 && !tempConflicts.contains(i)) {
					findPossibilitiesRec(i, tempSchedule, tempConflicts, level+1);
				}
			}
		}
	}
	
	@SuppressWarnings("unused")
	private void levelPrint(String msg, int level) {
		for (int i=0; i<level*2; i++) {
			System.out.print(" ");
		}
		System.out.println(msg);
	}
	
	/**
	 * Returns an integer representing the minimum complexity of the computation.
	 * @return An integer representing the minimum complexity of the computation.
	 * @throws ArithmeticException
	 */
	public long getMinComplexity() throws ArithmeticException {
		long result = 1;
		
		for (int i=0; i<sectionConflictsMap.length; i++) {
			result += factorial(getNumZerosIn(sectionConflictsMap[i]));
		}
		
		return result;
	}
	
	/**
	 * Returns an integer representing the maximum complexity of the computation.
	 * @return An integer representing the maximum complexity of the computation.
	 * @throws ArithmeticException
	 */
	public long getMaxComplexity() throws ArithmeticException {
		return factorial(numSections);
	}
	
	
	/**
	 * Returns the average complexity of the computation.
	 * @return The average complexity.
	 * @throws ArithmeticException
	 */
	public long getComplexity() throws ArithmeticException {
		return Math.floorDiv(Math.addExact(getMinComplexity(), getMaxComplexity()), 2);
	}
	
	/**
	 * Returns the number of 0 in an integer array.
	 * @param array The integer array.
	 * @return The number of 0 in the integer array.
	 */
	private int getNumZerosIn(int[] array) {
		int count = 0;
		for (int i:array) {
			if (i==0) count++;
		}
		return count;
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
		return complexity/107460000000000.0; //Estimate from tests
	}
	public double getTimeEstimate() {
		return getComplexity()/107460000000000.0; //Estimate from tests
	}
	
	public float getConflictPercent() {
		int numZeroes = 0;
		for (int i=0; i<numSections; i++) {
			numZeroes += getNumZerosIn(sectionConflictsMap[i]);
		}
		
		float result;
		if (numSections!=0) {
			result = 1.0f-(numZeroes*1.0f/(numSections*numSections));
		}
		else {
			result = 0;
		}
		return result;
	}
	
	private float getRunningTime() {
		return (System.nanoTime()-startTime)/1000000000;
	}
}
