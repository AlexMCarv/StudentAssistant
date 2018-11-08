package unb.cs2043.student_assistant.TestDrivers;

import unb.cs2043.student_assistant.Schedule;
import unb.cs2043.student_assistant.UNBCourseReader;
import unb.cs2043.student_assistant.fxml.ComboBoxChoice;

/**
 * Used to test the UNBCourseReader class.
 * @author frede
 */
public class UNBCourseReaderTester {

	public static void main(String[] args) {
		UNBCourseReader reader = new UNBCourseReader("2018/FA", "UG", "CS", "FR");
		
		boolean success = reader.loadData();
		
		if (success) {
			//Try to read result from the created file
			String fileName = reader.getFile().getName();
			System.out.println("fileName: "+fileName);
			Schedule courseList = UNBCourseReader.readFile(fileName);
			System.out.println(courseList.getFormattedString());
			
			//Delete file
			if (!reader.deleteFile()) {
				System.out.println("Could not delete file.");
			}
		}
		else {
			System.out.println("Oh no! Something went wrong!");
		}
		
//		ComboBoxChoice[][] choices = UNBCourseReader.getDropdownChoices();
//		print2DChoiceArray(choices);
	}
	
	private static void print2DChoiceArray(ComboBoxChoice[][] array) {
		int i=0;
		for (ComboBoxChoice[] arrayLv2: array) {
			System.out.println(i+++":");
			
			for (ComboBoxChoice obj: arrayLv2) {
				System.out.println("\t"+obj.toFormattedString());
			}
		}
	}
	
	private static void print3DStringArray(String[][][] array) {
		int i=0;
		for (String[][] arrayLv2: array) {
			System.out.println(i+++":");
			
			for (String[] arrayLv3: arrayLv2) {
				
				int j=0;
				for (String value: arrayLv3) {
					String header = "Text:";
					if (j++==1) header = "Value:";
					System.out.println("\t"+header);
					System.out.println("\t\t"+value);
				}
			}
		}
	}
}
