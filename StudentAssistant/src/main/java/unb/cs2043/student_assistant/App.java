package unb.cs2043.student_assistant;

import java.util.Set;
import java.util.TreeSet;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;



public class App extends Application
{
	
	public static Schedule userSelection;
	public static Schedule UNBCourseList;
	//Need instance variable UNBCourseNames since used a lot for autocompletion.
	//(To avoid retrieving it from UNBCourseList every single time)
	public static Set<String> UNBCourseNames;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		UNBCourseList = null; UNBCourseNames = null;
		userSelection = new Schedule("My Schedule");
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainWindow.fxml"));
        primaryStage.setTitle("Student Schedule Assistant");
        primaryStage.setScene(new Scene(root, 400, 350));
        primaryStage.show();
	}
	
    public static void main( String[] args ) {
    	launch(args);
    }
    
    public static boolean isUNBCourse(String courseName) {
    	if (UNBCourseList==null) return false;
    	
    	courseName = courseName.toUpperCase();
    	return getUNBCourseNames().contains(courseName);
    }
    
    public static Set<String> getUNBCourseNames() {
    	if (UNBCourseList==null) return null;
    	
    	if (UNBCourseNames==null) {
    		UNBCourseNames = new TreeSet<>();
    		//Using a set to guarantee no duplicate
        	for (Course course: UNBCourseList.getCourses()) {
        		UNBCourseNames.add(course.getName());
        	}
    	}
    	
    	return UNBCourseNames;
    }

}
