package unb.cs2043.StudentAssistant.fxml;

import unb.cs2043.StudentAssistant.Choice;
import unb.cs2043.StudentAssistant.Schedule;
import unb.cs2043.StudentAssistant.UNBCourseReader;

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
	
	@FXML private ProgressIndicator loading;
	@FXML private Label termLabel;
	@FXML private Label levelLabel;
    @FXML private Label locationLabel;
	@FXML private ComboBox<Choice> termSelect;
    @FXML private ComboBox<Choice> levelSelect;
    @FXML private ComboBox<Choice> locationSelect;
    @FXML private Button loadBtn;
    @FXML private Button cancelBtn;
	
    @Override
	public void initialize(URL location, ResourceBundle resources) {
    	setLoadingAnimation(true);
    	
    	//Initialize the selects on another thread
    	SelectInitializer service = new SelectInitializer();
        service.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
            	//Choose first choice as default
            	termSelect.getSelectionModel().select(0);
            	levelSelect.getSelectionModel().select(0);
            	locationSelect.getSelectionModel().select(0);
            	
            	setLoadingAnimation(false);
            }
        });
        service.start();
	}
    
    private class SelectInitializer extends Service<Void> {
        protected Task<Void> createTask() {
            return new Task<Void>() {
                protected Void call() {
                	initializeSelects();
                    return null;
                }
            };
        }
    }
    
	private void initializeSelects() {
		//Get choices
    	Choice[][] choices = UNBCourseReader.getDropdownChoices();
    	ObservableList<Choice> termChoices = FXCollections.observableArrayList(choices[0]);
    	ObservableList<Choice> levelChoices = FXCollections.observableArrayList(choices[1]);
    	ObservableList<Choice> locationChoices = FXCollections.observableArrayList(choices[2]);
    	
    	//Set Choices
    	termSelect.setItems(termChoices);
    	levelSelect.setItems(levelChoices);
    	locationSelect.setItems(locationChoices);
	}
    
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
				
				//todo: Send courseList to main controller
				System.out.println(courseList);
				
				//closeWindow();
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
