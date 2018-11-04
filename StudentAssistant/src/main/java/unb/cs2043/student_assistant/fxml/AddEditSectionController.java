package unb.cs2043.student_assistant.fxml;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import unb.cs2043.student_assistant.App;
import unb.cs2043.student_assistant.Course;
import unb.cs2043.student_assistant.Section;

/**
 * Controller class for the AddEditSection.fxml 
 * @author Alexandre Carvalho
 */
public class AddEditSectionController implements javafx.fxml.Initializable {

	@FXML private Button btnAdd;
	@FXML private Button btnCancel;
	@FXML private TextField txfName;
	@FXML private ComboBox<Course> cmbCourse;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		btnAdd.setOnAction(this::addSection);
		btnCancel.setOnAction(this::closeWindow);
		
		//Delete after vinculating to getList from Schedule
		List<Course> list = new ArrayList<Course>();
		for (int i = 0; i < App.userSelection.getSize(); i++) {
			Course course = App.userSelection.getCourse(i);
			list.add(course);
		}	
		cmbCourse.setItems(FXCollections.observableList(list));
		//End
		
		//cmbCourse.setItems(FXCollections.observableList(App.userSelection.getCourseList()));
		cmbCourse.setCellFactory(e -> new ComboBoxCourseCell());
		
	}

	
	private void addSection(ActionEvent event) {
		try {
			if (txfName.getText().trim().equals("")) 
				throw new Exception("Invalid Section name.");
			if (cmbCourse.getSelectionModel().getSelectedItem() == null) 
				throw new Exception("Course not selected.");
			Section newSection = new Section(txfName.getText());
			Course course = cmbCourse.getSelectionModel().getSelectedItem();
			course.add(newSection);
					
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		closeWindow(event);
	}
		
		
	private void closeWindow(ActionEvent event) {
		Stage stage = (Stage)(btnCancel.getScene().getWindow());
	    stage.close();
	}
}
