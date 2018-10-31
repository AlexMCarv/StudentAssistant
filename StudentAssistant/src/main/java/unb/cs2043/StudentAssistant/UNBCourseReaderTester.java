package unb.cs2043.StudentAssistant;

/**
 * Used to test the UNBCourseReader class.
 * @author frede
 */
public class UNBCourseReaderTester {

	public static void main(String[] args) {
		UNBCourseReader reader = new UNBCourseReader("2018", "FA", "UG", "ECE", "FR");
		
		reader.loadData();
	}

}
