package unb.cs2043.StudentAssistant.fxml;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Controller class for the MainWindow.fxml 
 * @author Alexandre Carvalho
 */

public class MainWindowController implements javafx.fxml.Initializable {


	@FXML private Button btnAddCourse;
	@FXML private Button btnAddSection;
	@FXML private Button btnAddClassTime;
	@FXML private Button btnGenSchedule;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		btnAddCourse.setOnMouseClicked((event) -> {
			openWindow("/fxml/AddEditCourse.fxml", "Add/Edit Course", 425, 120);});
		btnAddSection.setOnMouseClicked((event) -> {
			openWindow("/fxml/AddEditSection.fxml", "Add/Edit Section", 425, 173);});
		btnAddClassTime.setOnMouseClicked((event) -> {
			openWindow("/fxml/AddEditClassTime.fxml", "Add/Edit Class Time", 425, 356);});
		btnGenSchedule.setOnMouseClicked((event) -> {
			openWindow("/fxml/Schedule.fxml", "Schedule", 1020, 680);});
		
	}
	
	
	private void formCloseWindow(ActionEvent event) {
		Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
	    stage.close();
	}
	

	private void openWindow(String path, String title, int width, int height) {

		try {
			
			Parent window;
			window = FXMLLoader.load(getClass().getResource(path));
			Scene scene = new Scene(window, width, height);
			Stage stage = new Stage();
			stage.setTitle(title);
			stage.setScene(scene);
			stage.show();
			
		} catch (IOException e) {}

	}
	
}
