package unb.cs2043.student_assistant;

import java.util.Arrays;
import java.util.TreeSet;

/**
 * This class uses the main algorithms of this application to find the best schedule arrangements.
 * which finds the best schedule arrangement(s) given a set of courses.
 * @author Frederic Verret
 */
public class ScheduleArranger {
	
	/**Number of best schedules returned by the algorithm*/
	public static final int NUM_BEST_SCHEDULES = 6;
	
	/**Maximum computation time (seconds) before stopping*/
	public static final int MAX_TIME = 30;
	
	/**
	 * Uses one of two algorithms to find the best schedule arrangements.
	 * Determines which algorithm to use by calculating the complexity and time estimate of both.
	 * @param courseList The list of possible courses as a schedule.
	 * @return Array containing the NUM_BEST_SCHEDULES best schedule arrangements.
	 */
	public static Schedule[] getBestSchedules(Schedule courseList) {
		AlgorithmV1 alg1 = new AlgorithmV1(courseList);
		
		//Calculate time estimate of Alg1
		String alg1TimeEstimate = "";
		try {
			double time = Math.round(alg1.getTimeEstimate()*100.0)/100.0;
			alg1TimeEstimate = time+"";
		}
		catch (ArithmeticException e) {
			alg1TimeEstimate = "Too long.";
		}
		System.out.println("Time estimate (V1): "+alg1TimeEstimate);
		
		AlgorithmV2 alg2 = new AlgorithmV2(courseList);
		//Get %Conflict of Alg2
		float conflictPercent = alg2.getConflictPercent();
		
		//Determine which version to use
		int algVersion = conflictPercent<0.235?1:2;
		
		return getBestSchedules(courseList, algVersion, true);
	}
	
	
	/**
	 * Uses the algorithm version specified to find the best schedule arrangements.
	 * @param courseList The list of possible courses as a schedule.
	 * @param algVersion The version of the algorithm to use (1 or 2).
	 * @return Array containing the NUM_BEST_SCHEDULES best schedule arrangements.
	 */
	public static Schedule[] getBestSchedules(Schedule courseList, int algVersion, boolean showTime) {
		
		TreeSet<Schedule> scheduleArrangements = new TreeSet<>();
		long startTime=0;
		if (algVersion==1) {
			startTime = System.nanoTime();
			scheduleArrangements = new AlgorithmV1(courseList).findPossibilities();
		}
		else if (algVersion==2) {
			AlgorithmV2 alg2 = new AlgorithmV2(courseList);
			
			System.out.println("Conflict%: "+alg2.getConflictPercent());
			
			startTime = System.nanoTime();
			scheduleArrangements = alg2.findPossibilities();
		}
		
		//Convert the TreeSet to array
		Schedule[] results = new Schedule[scheduleArrangements.size()];
		results = scheduleArrangements.toArray(results);
		
		//Print # of schedule possibilities
		System.out.println("Possibilities: "+results.length);
		
		//Restrict to only NUM_BEST_SCHEDULES best schedules
		if (results.length>NUM_BEST_SCHEDULES) {
			Schedule[] bestResults = new Schedule[NUM_BEST_SCHEDULES];
			bestResults = Arrays.copyOfRange(results, 0, NUM_BEST_SCHEDULES);
			results = bestResults;
		}
		
		long endTime = System.nanoTime();
		double duration = (endTime - startTime)/1000000000.0;
		if (showTime) {
			System.out.println("Time taken (V"+algVersion+"): "+duration+"s");
		}
		
		return results;
	}
}
