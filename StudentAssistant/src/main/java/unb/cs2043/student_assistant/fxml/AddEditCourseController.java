package unb.cs2043.student_assistant.fxml;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import unb.cs2043.student_assistant.App;
import unb.cs2043.student_assistant.Course;

/**
 * Controller class for the AddEditCourse.fxml 
 * @author Alexandre Carvalho
 */
public class AddEditCourseController implements javafx.fxml.Initializable {

	@FXML private AnchorPane anchorPane;
	@FXML private Button btnAdd;
	@FXML private Button btnCancel;
	@FXML private AutoCompleteTextField autoTxfName;
	private Label UNBCourseMsg;
	private Course courseToEdit;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		btnAdd.setOnAction(this::addCourse);
		btnCancel.setOnAction(this::closeWindow);
		
		//Close window when pressing Escape
		anchorPane.setOnKeyPressed(event -> {
			if (event.getCode() ==  KeyCode.ESCAPE) closeWindow(new ActionEvent());
		});
		
		initializeAutocompleteField();
	}
	
	public void setFocus() {
		autoTxfName.requestFocus();
	}
	
	public void setCourseToEdit(Course courseToEdit) {
		this.courseToEdit = courseToEdit;
		autoTxfName.setActive(false);
		autoTxfName.setText(courseToEdit.getName());
		btnAdd.setText("Modify");
	}
	
	//Add autocomplete textfield using UNBCourses
	private void initializeAutocompleteField() {
		
		Set<String> courseList = App.getUNBCourseNames();
		Set<String> courseFullNames = App.getUNBCourseFullNames();
		//If no UNBCourses loaded, no autocompletion (empty set)
		if (courseList==null) courseList = new TreeSet<>();
		
		autoTxfName = new AutoCompleteTextField(courseList, courseFullNames);
		autoTxfName.setLayoutX(12.0);
		autoTxfName.setLayoutY(35.0);
		autoTxfName.setPrefHeight(25.0);
		autoTxfName.setPrefWidth(400.0);
		anchorPane.getChildren().add(autoTxfName);
		
		initializeInfoLabel();
	}
	
	private void initializeInfoLabel() {
		//Create message label when input matches a UNB course
		UNBCourseMsg = new Label("Note: Entered course is a UNB course. All sections "
				+"and class times will be added automatically.");
		UNBCourseMsg.setLayoutX(12.0);
		UNBCourseMsg.setLayoutY(67.0);
		UNBCourseMsg.setMaxWidth(400.0);
		UNBCourseMsg.setWrapText(true);
		UNBCourseMsg.setVisible(false);
		anchorPane.getChildren().add(UNBCourseMsg);
		
		//Check if entered value matches a UNB Course
		autoTxfName.textProperty().addListener((observable, oldValue, newValue) -> {
			if (App.isUNBCourse(newValue) && courseToEdit==null) {
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
			if (autoTxfName.getText().trim().equals("")) {
				App.showNotification("Invalid Course name.", AlertType.ERROR);
				return;
			}
			
			String courseName = autoTxfName.getText();
			System.out.println("courseName: "+courseName);
			
			//Check if adding or editing
			if (courseToEdit!=null) {
				courseToEdit.setName(autoTxfName.getText());
			}
			else {
				//Check if a course already has that name
				if (App.userSelection.getCourseByName(courseName)!=null) {
					App.showNotification("Course "+courseName+" already in the list.", AlertType.ERROR);
					return;
				}
				Course newCourse;
				if (App.isUNBCourse(courseName)) {
					newCourse = App.UNBCourseList.getCourseByName(courseName);
				}
				else {
					newCourse = new Course(courseName);
				}
				App.userSelection.add(newCourse);
			}
			
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
