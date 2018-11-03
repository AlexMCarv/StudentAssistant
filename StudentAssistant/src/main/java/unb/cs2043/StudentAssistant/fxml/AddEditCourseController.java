package unb.cs2043.StudentAssistant.fxml;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import unb.cs2043.StudentAssistant.App;
import unb.cs2043.StudentAssistant.Course;

/**
 * Controller class for the AddEditCourse.fxml 
 * @author Alexandre Carvalho
 */
public class AddEditCourseController implements javafx.fxml.Initializable {

	@FXML private Button btnAdd;
	@FXML private Button btnCancel;
	@FXML private TextField txfName;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		btnAdd.setOnAction(this::addCourse);
		btnCancel.setOnAction(this::closeWindow);
	}

	
	private void addCourse(ActionEvent event) {
		try {
			if (txfName.getText().trim().equals("")) 
				throw new Exception("Invalid Course name.");
			Course newCourse = new Course(txfName.getText());
			App.userSelection.add(newCourse);
			closeWindow(event);
			
		} catch (Exception e) {
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}
	}
	
	
	private void closeWindow(ActionEvent event) {
		Stage stage = (Stage)(btnCancel.getScene().getWindow());
	    stage.close();
	}
	
}
