package unb.cs2043.student_assistant;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;



public class App extends Application
{

	public static Schedule userSelection;

	@Override
	public void start(Stage primaryStage) throws Exception {

		userSelection = new Schedule("My Schedule");
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainWindow.fxml"));
        primaryStage.setTitle("Student Schedule Assistant");
        primaryStage.setScene(new Scene(root, 400, 350));
        primaryStage.show();

	}

    public static void main( String[] args ) {
    	launch(args);
    }

}
