package unb.cs2043.student_assistant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

/**
 * This class uses the main algorithms of this application to find the best schedule arrangements.
 * which finds the best schedule arrangement(s) given a set of courses.
 * @author Frederic Verret
 */
public class ScheduleArranger {
	
	/**Number of best schedules returned by the algorithm*/
	public static final int NUM_BEST_SCHEDULES = 6;
	
	/**
	 * Uses one of two algorithms to find the best schedule arrangements.
	 * Determines which algorithm to use by calculating the complexity and time estimate of both.
	 * @param courseList The list of possible courses as a schedule.
	 * @return Array containing the NUM_BEST_SCHEDULES best schedule arrangements.
	 */
	public static Schedule[] getBestSchedules(Schedule courseList) {
		
		AlgorithmV1 alg1 = new AlgorithmV1(courseList);
		
		//Calculate time estimate
		double time1 = 0;
		try {
			time1 = Math.round(alg1.getTimeEstimate()*100.0)/100.0;
			System.out.println("Time estimate (Algorithm1): "+time1+"s");
		}
		catch (ArithmeticException e) {
			throw new RuntimeException("Complexity (number of possible schedules) is too large.");
		}
		
		
		AlgorithmV2 alg2 = new AlgorithmV2(courseList);
		//Calculate complexity estimate
		long complexityMin = 0, complexityAvg=0;
		try {
			complexityMin = alg2.getMinComplexity();
			complexityAvg = alg2.getComplexity();
			System.out.println("ComplexityMin (Algorithm2): "+complexityMin+"s");
			System.out.println("ComplexityAvg (Algorithm2): "+complexityAvg+"s");
		}
		catch (ArithmeticException e) {
			throw new RuntimeException("Complexity (number of possible schedules) is too large.");
		}
		
		//Calculate time estimate
		double time = 0;
		try {
			time = Math.round(alg2.getTimeEstimate()*100.0)/100.0;
			System.out.println("Time estimate (Algorithm2): "+time+"s");
		}
		catch (ArithmeticException e) {
			throw new RuntimeException("Complexity (number of possible schedules) is too large.");
		}
		
		return getBestSchedules(courseList, 2);
	}
	
	
	/**
	 * Uses the algorithm version specified to find the best schedule arrangements.
	 * @param courseList The list of possible courses as a schedule.
	 * @param algVersion The version of the algorithm to use (1 or 2).
	 * @return Array containing the NUM_BEST_SCHEDULES best schedule arrangements.
	 */
	public static Schedule[] getBestSchedules(Schedule courseList, int algVersion) {
		
		TreeSet<Schedule> scheduleArrangements = new TreeSet<>();
		long startTime=0;
		if (algVersion==1) {
			scheduleArrangements = new AlgorithmV1(courseList).findPossibilities();
		}
		else if (algVersion==2) {
			AlgorithmV2 alg2 = new AlgorithmV2(courseList);
			
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
		System.out.println("Time: "+duration+"s");
		
		return results;
	}
}
