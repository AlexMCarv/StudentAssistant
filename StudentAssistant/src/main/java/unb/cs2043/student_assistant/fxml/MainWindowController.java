package unb.cs2043.student_assistant.fxml;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import unb.cs2043.student_assistant.App;
import unb.cs2043.student_assistant.ClassTime;
import unb.cs2043.student_assistant.Course;
import unb.cs2043.student_assistant.Schedule;
import unb.cs2043.student_assistant.Section;
import unb.cs2043.student_assistant.UNBCourseReader;

/**
 * Controller class for the MainWindow.fxml 
 * @author Alexandre Carvalho
 */

public class MainWindowController implements javafx.fxml.Initializable {

	@FXML private VBox container;
	@FXML private TreeView<Object> treeCourseList;
	@FXML private Button btnAddCourse;
	@FXML private Button btnAddSection;
	@FXML private Button btnAddClassTime;
	@FXML private Button btnGenSchedule;
	@FXML private Label msgLabel;
	
	private LoadUNBCoursesController LoadUNBController;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		//Keybindings
		final KeyCombination ctrlC = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN);
		final KeyCombination ctrlS = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
		final KeyCombination ctrlT = new KeyCodeCombination(KeyCode.T, KeyCombination.CONTROL_DOWN);
		final KeyCombination ctrlG = new KeyCodeCombination(KeyCode.G, KeyCombination.CONTROL_DOWN);
		container.setOnKeyReleased(event -> {
			if (ctrlC.match(event)) addCourse();
			else if (ctrlS.match(event)) addSection();
			else if (ctrlT.match(event)) addClassTime();
			else if (ctrlG.match(event)) genSchedule();
		});
		container.setOnKeyPressed(event -> {
			if (event.getCode() ==  KeyCode.ESCAPE) {
				boolean result = App.showConfirmDialog("Do you really want to exit?\nAll progress will be lost.", AlertType.WARNING);
				if (result) closeWindow(new ActionEvent());
			}	
		});
		
		//Get UNB Choices (only once) in a separate thread to use in the UNB Load Data window
		ChoiceLoader service = new ChoiceLoader();
        service.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
            //*This runs after thread is finished
            	ComboBoxChoice[][] choices = (ComboBoxChoice[][])t.getSource().getValue();
            	LoadUNBCoursesController.setChoices(choices);
            	if (LoadUNBController!=null) {
            		LoadUNBController.initializeSelects();
            	}
            	System.out.println("LOADED!");
            }
        });
        service.start();
	}
	
	private class ChoiceLoader extends Service<ComboBoxChoice[][]> {
        protected Task<ComboBoxChoice[][]> createTask() {
            return new Task<ComboBoxChoice[][]>() {
                protected ComboBoxChoice[][] call() {
                	ComboBoxChoice[][] choices = null;
                	int i=0;
                	while (choices==null && i<3) {
                		//System.out.println("Load trial "+(++i));
                		choices = UNBCourseReader.getDropdownChoices();
                	}
                	return choices;
                }
            };
        }
    }
	
	@FXML
	//This is called when clicking on the Load UNB Data... menu item.
	private void loadUNBData() {
		//Can't use the openWindow method since I need a reference to the controller
		//to be able to call the method initializeSelects() when data is finished loading (see above).
		try {
			Parent window;
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoadUNBCourses.fxml"));
			window = loader.load();
			//Controller reference
			LoadUNBController = loader.<LoadUNBCoursesController>getController();
			Scene scene = new Scene(window, 350, 260);
			Stage stage = new Stage();
			stage.setTitle("Load UNB Data");
			stage.setMinWidth(350+20);
			stage.setMinHeight(260+47);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setScene(scene);
			stage.show();
			stage.setOnHidden(e -> {
				Schedule courseList = App.UNBCourseList;
				if (courseList!=null) {
					int numCourses = courseList.getSize();
					msgLabel.setText(numCourses+" UNB courses loaded for:\n"+courseList.getName()+".");
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
			windowError();
		}
	}
	
	//These methods are called when the corresponding button is pressed.
	@FXML private void addCourse() {openWindow("/fxml/AddEditCourse.fxml", "Add/Edit Course", 425, 170);}
	@FXML private void addSection() {openWindow("/fxml/AddEditSection.fxml", "Add/Edit Section", 425, 180);}
	@FXML private void addClassTime() {openWindow("/fxml/AddEditClassTime.fxml", "Add/Edit Class Time", 425, 360);}
	@FXML private void genSchedule() {openWindow("/fxml/Schedule.fxml", "Schedule", 1020, 680);}
	
	private void closeWindow(ActionEvent event) {
		Stage stage = (Stage)container.getScene().getWindow();
	    stage.close();
	}

	private void openWindow(String path, String title, int width, int height) {
		try {
			Parent window;
			FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
			window = loader.load();
			Scene scene = new Scene(window, width, height);
			Stage stage = new Stage();
			stage.setTitle(title);
			stage.setMinWidth(width+20);
			stage.setMinHeight(height+47);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setOnHiding(windowEvent -> createCourseList());
			stage.setScene(scene);
			stage.show();
			
		} catch (IOException e) {
			e.printStackTrace();
			windowError();
		}
	}
	
	
	private void createCourseList() {
		// Root Item
		TreeItem<Object> rootItem = new TreeItem<>(new Course("List"));
		rootItem.setExpanded(true);
		
		/* 
		for (Course course : App.userSelection.getCourseList()) {
			TreeItem<Object> courseCell = new TreeItem<>(course);
			rootItem.getChildren().add(courseCell);
			
			for (Section section: course.getSectionList()) {
				TreeItem<Object> sectionCell = new TreeItem<>(section);
				courseCell.getChildren().add(sectionCell);
				
				for (ClassTime time: section.getTimeList()) {
					TreeItem<Object> timeCell = new TreeItem<>(time);
					sectionCell.getChildren().add(timeCell);
				}
			}
		}*/
		
		
		// Populating Course List - DELETE AND UNCOMMENT CODE AT THE TOP after datatype classes receive a getList method
		for (int i = 0; i < App.userSelection.getSize(); i++) {
			Course course = App.userSelection.getCourse(i);
			TreeItem<Object> courseCell = new TreeItem<>(course);
			rootItem.getChildren().add(courseCell);
						
			for (int j = 0; j < course.getSize(); j++) {
				Section section = course.getSection(j);
				TreeItem<Object> sectionCell = new TreeItem<>(section);
				courseCell.getChildren().add(sectionCell);
				courseCell.setExpanded(true);
				
				for (int k = 0; k < section.getSize(); k++) {
					ClassTime time = section.getClassTime(k);
					TreeItem<Object> timeCell = new TreeItem<>(time);
					sectionCell.getChildren().add(timeCell);
				}
			}
		}
				
		//TreeView Setup
		treeCourseList.setCellFactory(e -> new TreeViewGenericCell());
		treeCourseList.setRoot(rootItem);
		treeCourseList.setShowRoot(false);
	}
	
	
	private void refresh() {createCourseList();}
	
	private void windowError() {
		App.showNotification("An error occured while trying to open the window.\nPlease try again.", AlertType.ERROR);
	}
}
