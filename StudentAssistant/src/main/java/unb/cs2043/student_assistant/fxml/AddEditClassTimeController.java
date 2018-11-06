package unb.cs2043.student_assistant.fxml;

import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXTimePicker;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.LocalTimeStringConverter;
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
	@FXML private Spinner<LocalTime> spinnerFrom, spinnerTo;
	@FXML private StackPane container;
	@FXML private CheckBox chkSun, chkMon, chkTue, chkWed, chkThu, chkFri, chkSat;
	@FXML private RadioButton rbtnLec, rbtnLab, rbtnTut, rbtnOth;
	@FXML private Label lblTimeError;
	final ToggleGroup group = new ToggleGroup();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		btnCancel.setOnAction(this::closeWindow);
		
		rbtnLec.setToggleGroup(group);
		rbtnLab.setToggleGroup(group);
		rbtnTut.setToggleGroup(group);
		rbtnOth.setToggleGroup(group);
		
		spinnerFrom.setValueFactory(new TimeSpinnerValueFactory());
		spinnerTo.setValueFactory(new TimeSpinnerValueFactory());
		spinnerTo.valueProperty().addListener((obs, oldValue, newValue) -> {
			if (spinnerFrom.getValue().compareTo(spinnerTo.getValue()) >= 0) {
				lblTimeError.setText("* End time must be greater than start time.");
			} else {
				lblTimeError.setText("");
			}
		});
		
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
