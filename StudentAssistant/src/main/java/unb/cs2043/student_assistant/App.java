package unb.cs2043.student_assistant;

import java.io.File;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import unb.cs2043.student_assistant.fxml.MainWindowController;
/**
 * Main driver for GUI application
 * @author Alexandre Carvalho
 * @author Frederic Verret
 */
public class App extends Application
{
	
	public static Schedule userSelection;
	public static Schedule UNBCourseList;
	//Need instance variable UNBCourseNames since used a lot for autocompletion.
	//(To avoid retrieving it from UNBCourseList every single time)
	private static Set<String> UNBCourseNames;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		UNBCourseList = null; UNBCourseNames = null;
		userSelection = new Schedule("My Schedule");
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainWindow.fxml"));
		Parent root = loader.load();
		MainWindowController controller = loader.<MainWindowController>getController();
        primaryStage.setTitle("Student Schedule Assistant");
        primaryStage.setScene(new Scene(root, 525, 360));
        primaryStage.setMinWidth(530+20);
        primaryStage.setMinHeight(360+47);
        primaryStage.setOnCloseRequest(e -> {
        	if (!controller.closeWindow()) {
        		e.consume();
        	}
        });
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
        	for (Course course: UNBCourseList.copyCourses()) {
        		UNBCourseNames.add(course.getName());
        	}
    	}
    	
    	return UNBCourseNames;
    }
    
    public static boolean showConfirmDialog(String content, AlertType alertType) {
        final Alert alert = new Alert(alertType);
        alert.setContentText(content);
        
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
        
        //Deactivate Defaultbehavior for yes-Button:
        //Button yesButton = (Button) alert.getDialogPane().lookupButton( ButtonType.YES );
        //yesButton.setDefaultButton( false );
        
        final Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.YES;
    }
    
    public static void showNotification(String content, AlertType alertType) {
        final Alert alert = new Alert(alertType);
        alert.setContentText(content);
        alert.show();
    }

}
