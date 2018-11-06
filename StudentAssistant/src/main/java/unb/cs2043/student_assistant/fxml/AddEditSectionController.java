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
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		btnAdd.setOnAction(this::addSection);
		btnCancel.setOnAction(this::closeWindow);
		
		//Close window when pressing Escape
		container.setOnKeyPressed(event -> {
			if (event.getCode() ==  KeyCode.ESCAPE) closeWindow(new ActionEvent());
		});
		
		/*Delete after vinculating to getList from Schedule
		List<Course> list = new ArrayList<Course>();
		for (int i = 0; i < App.userSelection.getSize(); i++) {
			Course course = App.userSelection.getCourse(i);
			list.add(course);
		}	
		cmbCourse.setItems(FXCollections.observableList(list));
		*/
		
		cmbCourse.setItems(FXCollections.observableList(App.userSelection.copyList()));
		cmbCourse.setCellFactory(e -> new ComboBoxCourseCell());
		
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
