package unb.cs2043.StudentAssistant;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Hello world!
 *
 */
public class App extends Application
{

	@Override
	public void start(Stage primaryStage) throws Exception {
        
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Main_Window.fxml"));
        primaryStage.setTitle("Student Schedule Assistant");
        primaryStage.setScene(new Scene(root, 400, 350));
        primaryStage.show();
		
	}
	
    public static void main( String[] args ) {
    	launch(args);
    }

}
