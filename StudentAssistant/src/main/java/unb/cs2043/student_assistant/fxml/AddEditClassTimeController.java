package unb.cs2043.student_assistant.fxml;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXTimePicker;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import unb.cs2043.student_assistant.App;
import unb.cs2043.student_assistant.Course;
import unb.cs2043.student_assistant.Section;

/**
 * Controller class for the AddEditClassTime.fxml 
 * @author Alexandre Carvalho
 */
public class AddEditClassTimeController implements javafx.fxml.Initializable {

	@FXML private ComboBox<Course> cmbCourse;
	@FXML private ComboBox<Section> cmbSection;
	@FXML private Button btnAdd;
	@FXML private Button btnCancel;
	@FXML private TextField txfUNB;
	@FXML private Button btnAddUNB;
	@FXML private JFXTimePicker timePickFrom;
	@FXML private JFXTimePicker timePickTo;
	@FXML private StackPane container;
	@FXML private CheckBox chkSun, chkMon, chkTue, chkWed, chkThu, chkFri, chkSat;
	@FXML private CheckBox chkLec, chkLab, chkTut, chkOth;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		btnCancel.setOnAction(this::closeWindow);
		
		cmbCourse.setItems(FXCollections.observableList(App.userSelection.copyList()));
		cmbCourse.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent actionEvent) {
		    	Course course = cmbCourse.getSelectionModel().getSelectedItem();
		    	cmbSection.setItems(FXCollections.observableList(course.copyList()));
		    }
		});
		
		//cmbCourse.setCellFactory(e -> new ComboBoxCourseCell());
	}

	
	private void closeWindow(ActionEvent event) {
		Stage stage = (Stage)container.getScene().getWindow();
	    stage.close();
	}
}
