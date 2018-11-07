package unb.cs2043.student_assistant.fxml;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import unb.cs2043.student_assistant.App;
import unb.cs2043.student_assistant.Course;
import unb.cs2043.student_assistant.Section;

/**
 * Controller class for the AddEditSection.fxml 
 * @author Alexandre Carvalho
 */
public class AddEditSectionController implements javafx.fxml.Initializable {

	@FXML private StackPane container;
	@FXML private Button btnAdd;
	@FXML private Button btnCancel;
	@FXML private TextField txfName;
	@FXML private ComboBox<Course> cmbCourse;
	
	private Section sectionToEdit;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		btnAdd.setOnAction(this::addSection);
		btnCancel.setOnAction(this::closeWindow);
		
		//Close window when pressing Escape
		container.setOnKeyPressed(event -> {
			if (event.getCode() ==  KeyCode.ESCAPE) closeWindow(new ActionEvent());
		});
		
		cmbCourse.setItems(FXCollections.observableList(App.userSelection.copyList()));
		cmbCourse.setCellFactory(e -> new ComboBoxCourseCell());
		
	}
	
	public void setFocus(String elem) {
		if (elem.equals("ComboBox")) {
			cmbCourse.requestFocus();
		}
		else if (elem.equals(("TextField"))) {
			txfName.requestFocus();
		}
	}
	
	public void setCourseToAddTo(Course course) {
		cmbCourse.getSelectionModel().select(course);
	}
	public void setSectionToEdit(Section section) {
		this.sectionToEdit = section;
		txfName.setText(section.getName());
		btnAdd.setText("Modify");
	}
	
	private void addSection(ActionEvent event) {
		try {
			if (txfName.getText().trim().equals("")) {
				App.showNotification("Invalid Section name.", AlertType.ERROR);
				return;
			}
				
			if (cmbCourse.getSelectionModel().getSelectedItem() == null) {
				App.showNotification("Course not selected.", AlertType.ERROR);
				return;
			}
			
			String sectionName = txfName.getText();
			//Check if editing or adding
			if (sectionToEdit!=null) {
				sectionToEdit.setName(sectionName);
			}
			else {
				//Check if a section of this course already has that name
				if (cmbCourse.getSelectionModel().getSelectedItem().getSectionByName(sectionName) != null) {
					App.showNotification("Section "+sectionName+" already exists for this course.", AlertType.ERROR);
					return;
				}
				Section newSection = new Section(sectionName);
				Course course = cmbCourse.getSelectionModel().getSelectedItem();
				course.add(newSection);
			}
		
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
