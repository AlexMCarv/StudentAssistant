package unb.cs2043.StudentAssistant;

/**
 * Used to test the UNBCourseReader class.
 * @author frede
 */
public class UNBCourseReaderTester {

	public static void main(String[] args) {
		UNBCourseReader reader = new UNBCourseReader("2018", "FA", "UG", "ENGG", "FR");
		
		boolean success = reader.loadData();
		
		if (success) {
			//Try to read result from the created file
			Schedule courseList = UNBCourseReader.readFile("UNBCourses2018FAUGFR.list");
			System.out.println(courseList);
		}
		else {
			//Something went wrong!
			System.out.println("Oh no! Something went wrong!");
		}
		
	}

}
