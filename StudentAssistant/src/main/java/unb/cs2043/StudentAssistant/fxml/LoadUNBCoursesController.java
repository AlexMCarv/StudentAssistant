package unb.cs2043.StudentAssistant.fxml;

import unb.cs2043.StudentAssistant.Choice;
import unb.cs2043.StudentAssistant.UNBCourseReader;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;

/**
 * Controller of the window that allows loading data from UNB website.
 * @author Frederic Verret
 */
public class LoadUNBCoursesController implements javafx.fxml.Initializable {
	
	@FXML private ComboBox<Choice> termSelect;
    @FXML private ComboBox<Choice> levelSelect;
    @FXML private ComboBox<Choice> locationSelect;
    @FXML private Button loadBtn;
    @FXML private Button cancelBtn;
	
    @Override
	public void initialize(URL location, ResourceBundle resources) {
		
    	//Get choices
    	Choice[][] choices = UNBCourseReader.getDropdownChoices();
    	ObservableList<Choice> termChoices = FXCollections.observableArrayList(choices[0]);
    	ObservableList<Choice> levelChoices = FXCollections.observableArrayList(choices[1]);
    	ObservableList<Choice> locationChoices = FXCollections.observableArrayList(choices[2]);
    	
    	//Set Choices
    	termSelect.setItems(termChoices);
    	levelSelect.setItems(levelChoices);
    	locationSelect.setItems(locationChoices);
    	
    	//Choose first choice as default
    	termSelect.getSelectionModel().select(0);
    	levelSelect.getSelectionModel().select(0);
    	locationSelect.getSelectionModel().select(0);
	}
    
    @FXML
    private void closeWindow(MouseEvent event) {
    	closeWindow();
    }

    @FXML
    private void loadData(MouseEvent event) {
    	//Get selected choice
    	String termVal = termSelect.getSelectionModel().getSelectedItem().getValue();
    	String levelVal = levelSelect.getSelectionModel().getSelectedItem().getValue();
    	String locationVal = locationSelect.getSelectionModel().getSelectedItem().getValue();
    	
    	System.out.println("term: "+termVal+"\nlevel: "+levelVal+"\nlocation: "+locationVal);
    	
    	//Send values to main controller
    	
    	//Close window
//    	closeWindow();
    }
    
    private void closeWindow() {
    	cancelBtn.getScene().getWindow().hide();
    }
}
