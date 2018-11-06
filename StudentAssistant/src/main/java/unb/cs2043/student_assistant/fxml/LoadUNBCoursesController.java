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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Controller of the window that allows loading data from UNB website.
 * @author Frederic Verret
 */
public class LoadUNBCoursesController implements javafx.fxml.Initializable {
	
	private static ComboBoxChoice[][] choices;
	@FXML private StackPane container;
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
    	//Close window when pressing Escape
    	container.setOnKeyPressed(event -> {
			if (event.getCode() ==  KeyCode.ESCAPE) closeWindow(new ActionEvent());
			else if (event.getCode() ==  KeyCode.ENTER) loadData(null);
		});
    	
    	if (choices==null) {
    		//Waiting for choices to load...
    		setLoadingAnimation(true);
    	}
    	else {
    		initializeSelects();
    	}
    	
	}
    
    public void initializeSelects() {
		if (choices==null) {
			//Error occured while loading data
			closeWindow(new ActionEvent());
			App.showNotification("An error occured while loading the data.\n"
					+"Please make sure you are connected to the internet and try again.", AlertType.ERROR);
			return;
		};
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
    	
    	//System.out.println(termVal+" "+levelVal+" "+locationVal);
    	
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
				
				String msg = "An error occured while loading the data.\n"
						+"Please make sure you are connected to the internet and try again.";
				AlertType type = AlertType.ERROR;
				if (courseList!=null) {
					//Rename using choice labels
					String[] values = courseList.getName().split(" ");
					for (int i=0; i<values.length; i++) {
						values[i] = getChoiceLabel(values[i]);
					}
					courseList.setName(String.join(",\n", values));
					
					int numCourses = courseList.getSize();
					if (numCourses==0) {
						type = AlertType.INFORMATION;
						msg = "No courses are available for this selection.";
						courseList = null;
					}
					else {
						type = AlertType.INFORMATION;
						msg = "UNB Courses have been loaded successfully. \n("+courseList.getSize()+" courses loaded)";
					}
				}
				Alert alert = new Alert(type);
				alert.setContentText(msg);
				
				//Send courseList to main class
				App.UNBCourseList = courseList;
				
				closeWindow(new ActionEvent());
				alert.show();
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
    
    private static String getChoiceLabel(String value) {
		for (ComboBoxChoice[] array: choices) {
			for (ComboBoxChoice singleChoice: array) {
				if (singleChoice.getValue().equals(value)) {
					return singleChoice.getLabel();
				}
			}
		}
		return null;
	}
    
    @FXML
    private void closeWindow(MouseEvent event) {
    	closeWindow(new ActionEvent());
    }
    
    private void closeWindow(ActionEvent event) {
		Stage stage = (Stage)(cancelBtn.getScene().getWindow());
	    stage.close();
	}
}
