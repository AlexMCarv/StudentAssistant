package unb.cs2043.student_assistant.fxml;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import unb.cs2043.student_assistant.App;
import unb.cs2043.student_assistant.Course;

/**
 * Controller class for the AddEditCourse.fxml 
 * @author Alexandre Carvalho
 */
public class AddEditCourseController implements javafx.fxml.Initializable {

	@FXML private AnchorPane pane;
	@FXML private Button btnAdd;
	@FXML private Button btnCancel;
	@FXML private AutoCompleteTextField autoTxfName;
	private Label UNBCourseMsg;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		btnAdd.setOnAction(this::addCourse);
		btnCancel.setOnAction(this::closeWindow);
		
		initializeAutocompleteField();
	}
	
	//Add autocomplete textfield using UNBCourses
	private void initializeAutocompleteField() {
		
		Set<String> courseList = App.getUNBCourseNames();
		//If no UNBCourses loaded, no autocompletion (empty set)
		if (courseList==null) courseList = new TreeSet<>();
		
		autoTxfName = new AutoCompleteTextField(courseList);
		autoTxfName.setLayoutX(12.0);
		autoTxfName.setLayoutY(35.0);
		autoTxfName.setPrefHeight(25.0);
		autoTxfName.setPrefWidth(400.0);
		pane.getChildren().add(autoTxfName);
		
		//Create message label when input matches a UNB course
		UNBCourseMsg = new Label("Note: Entered course is a UNB course. All sections "
				+"and class times will be added automatically.");
		UNBCourseMsg.setLayoutX(12.0);
		UNBCourseMsg.setLayoutY(67.0);
		UNBCourseMsg.setMaxWidth(400.0);
		UNBCourseMsg.setWrapText(true);
		UNBCourseMsg.setVisible(false);
		pane.getChildren().add(UNBCourseMsg);
		
		//Check if entered value matches a UNB Course
		autoTxfName.textProperty().addListener((observable, oldValue, newValue) -> {
			if (App.isUNBCourse(newValue)) {
				//Show message
				UNBCourseMsg.setVisible(true);
			}
			else {
				//Hide message
				UNBCourseMsg.setVisible(false);
			}
		});
	}
	
	private void addCourse(ActionEvent event) {
		try {
			if (autoTxfName.getText().trim().equals("")) 
				throw new Exception("Invalid Course name.");
			String inputVal = autoTxfName.getText();
			Course newCourse;
			if (App.isUNBCourse(inputVal)) {
				newCourse = App.UNBCourseList.getCourseByName(inputVal);
			}
			else {
				newCourse = new Course(autoTxfName.getText());
			}
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
