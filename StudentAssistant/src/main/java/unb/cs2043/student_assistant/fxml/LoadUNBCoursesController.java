package unb.cs2043.student_assistant.fxml;

import unb.cs2043.student_assistant.App;
import unb.cs2043.student_assistant.Schedule;
import unb.cs2043.student_assistant.UNBCourseReader;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.input.MouseEvent;

/**
 * Controller of the window that allows loading data from UNB website.
 * @author Frederic Verret
 */
public class LoadUNBCoursesController implements javafx.fxml.Initializable {
	
	private static ComboBoxChoice[][] choices;
	@FXML private ProgressIndicator loading;
	@FXML private Label termLabel;
	@FXML private Label levelLabel;
    @FXML private Label locationLabel;
	@FXML private ComboBox<ComboBoxChoice> termSelect;
    @FXML private ComboBox<ComboBoxChoice> levelSelect;
    @FXML private ComboBox<ComboBoxChoice> locationSelect;
    @FXML private Button loadBtn;
    @FXML private Button cancelBtn;

//====== PUBLIC METHODS ======
    
    @Override
	public void initialize(URL location, ResourceBundle resources) {
    	
    	if (choices==null) {
    		//Waiting for choices to load...
    		setLoadingAnimation(true);
    	}
    	else {
    		initializeSelects();
    	}
    	
	}
    
    public void initializeSelects() {
		if (choices==null) return;
    	ObservableList<ComboBoxChoice> termChoices = FXCollections.observableArrayList(choices[0]);
    	ObservableList<ComboBoxChoice> levelChoices = FXCollections.observableArrayList(choices[1]);
    	ObservableList<ComboBoxChoice> locationChoices = FXCollections.observableArrayList(choices[2]);
    	
    	//Set Choices
    	termSelect.setItems(termChoices);
    	levelSelect.setItems(levelChoices);
    	locationSelect.setItems(locationChoices);
    	
    	//Choose first choice as default
    	termSelect.getSelectionModel().select(0);
    	levelSelect.getSelectionModel().select(0);
    	locationSelect.getSelectionModel().select(0);
    	
    	setLoadingAnimation(false);
	}
    	
	public static void setChoices(ComboBoxChoice[][] loadedChoices) {
		choices = loadedChoices;
	}
	
//====== PRIVATE METHODS ======
    
	private void setLoadingAnimation(boolean active) {
		//Loading animation
		loading.setVisible(active);
		//Labels and selects
		termLabel.setVisible(!active);
		levelLabel.setVisible(!active);
		locationLabel.setVisible(!active);
		termSelect.setVisible(!active);
		levelSelect.setVisible(!active);
		locationSelect.setVisible(!active);
		//Load button
		loadBtn.setDisable(active);
	}

    @FXML
    private void loadData(MouseEvent event) {
    	setLoadingAnimation(true);
    	
    	//Get selected choice
    	String termVal = termSelect.getSelectionModel().getSelectedItem().getValue();
    	String levelVal = levelSelect.getSelectionModel().getSelectedItem().getValue();
    	String locationVal = locationSelect.getSelectionModel().getSelectedItem().getValue();
    	
    	//Load the data on another thread
    	DataLoader service = new DataLoader();
    	service.setTerm(termVal);
    	service.setLevel(levelVal);
    	service.setLocation(locationVal);
        service.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
            	setLoadingAnimation(false);
            	
            	File file = (File) t.getSource().getValue();
				Schedule courseList = UNBCourseReader.readFile(file.getName());
				
				//Send courseList to main class
				App.UNBCourseList = courseList;
				
				closeWindow();
            }
        });
        service.start();
    	
    }
    
    private static class DataLoader extends Service<File> {
    	private String term;
    	private String level;
    	private String location;
        public void setTerm(String term) {this.term = term;}
		public void setLevel(String level) {this.level = level;}
		public void setLocation(String location) {this.location = location;}
		protected Task<File> createTask() {
            return new Task<File>() {
                protected File call() {
                    UNBCourseReader reader = new UNBCourseReader(term, level, location);
                    File file = reader.getFile();
                    if (!file.exists()) {
                    	reader.loadData();
                    }
                    return file;
                }
            };
        }
    }
    
    @FXML
    private void closeWindow(MouseEvent event) {
    	closeWindow();
    }
    
    private void closeWindow() {
    	cancelBtn.getScene().getWindow().hide();
    }
}
