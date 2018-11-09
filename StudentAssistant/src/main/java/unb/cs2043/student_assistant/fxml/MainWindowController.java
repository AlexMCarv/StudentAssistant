
package unb.cs2043.student_assistant.fxml;

import java.io.IOException;
import java.net.URL;
import java.util.List;
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
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
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
 * @author Frederic Verret
 */

public class MainWindowController implements javafx.fxml.Initializable {

	@FXML private VBox container;
	
	@FXML private TreeView<Object> treeCourseList;
	@FXML private Button btnAddCourse;
	@FXML private Button btnAddSection;
	@FXML private Button btnAddClassTime;
	@FXML private Button btnGenSchedule;
	@FXML private Label msgLabel;
	
	@FXML private ContextMenu contextMenu;
	@FXML private MenuItem menuEditCourse;
	@FXML private MenuItem menuEditSection;
	@FXML private MenuItem menuEditClassTime;
	@FXML private MenuItem menuAddCourse;
	@FXML private MenuItem menuAddSection;
	@FXML private MenuItem menuAddClassTime;
	@FXML private MenuItem menuDelete;
	
	private LoadUNBCoursesController LoadUNBController;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		initializeContextMenu();
		setKeyBindings();
		
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
            	//System.out.println("LOADED!");
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
	
	public boolean closeWindow() {
		boolean result = App.showConfirmDialog("Do you really want to exit?\nAll unsaved data will be lost.", AlertType.WARNING);
		if (result) {
			Stage stage = (Stage)container.getScene().getWindow();
		    stage.close();
		}
		return result;
	}
	
	private void initializeContextMenu() {
		contextMenu.setOnShowing(e -> {
			contextMenu.getItems().clear();
			TreeItem<Object> treeItem = treeCourseList.getSelectionModel().getSelectedItem();
			if (treeItem!=null) {
				String type = getObjectType(treeItem.getValue());
				if (type.equals("Course")) {
					contextMenu.getItems().addAll(menuEditCourse, menuAddSection, menuDelete);
				}
				else if (type.equals("Section")) {
					contextMenu.getItems().addAll(menuEditSection, menuAddClassTime, menuDelete);
				}
				else if (type.equals("ClassTime")) {
					contextMenu.getItems().addAll(menuEditClassTime, menuDelete);
				}
			}
			else {
				contextMenu.getItems().add(menuAddCourse);
			}
		});
		contextMenu.setOnHidden(e -> {
			contextMenu.getItems().clear();
			contextMenu.getItems().add(menuAddCourse);
		});
	}
	
	private void setKeyBindings() {
		//Keybindings
		final KeyCombination ctrlC = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN);
		final KeyCombination ctrlS = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
		final KeyCombination ctrlT = new KeyCodeCombination(KeyCode.T, KeyCombination.CONTROL_DOWN);
		final KeyCombination ctrlG = new KeyCodeCombination(KeyCode.G, KeyCombination.CONTROL_DOWN);
		container.setOnKeyReleased(event -> {
			if (ctrlC.match(event)) addCourse(null);
			else if (ctrlS.match(event)) addSection(null);
			else if (ctrlT.match(event)) addClassTime(null);
			else if (ctrlG.match(event)) genSchedule();
			else if (event.getCode() == KeyCode.DELETE) deleteItem(new ActionEvent());
		});
		container.setOnKeyPressed(event -> {if (event.getCode() ==  KeyCode.ESCAPE) closeWindow();});
		treeCourseList.setOnKeyPressed(event -> {if (event.getCode() ==  KeyCode.ESCAPE) closeWindow();});
		
		btnAddCourse.setTooltip(new Tooltip("Ctrl+C"));
		btnAddSection.setTooltip(new Tooltip("Ctrl+S"));
		btnAddClassTime.setTooltip(new Tooltip("Ctrl+T"));
		btnGenSchedule.setTooltip(new Tooltip("Ctrl+G"));
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
			Stage stage = setStage(window, "Load UNB Data", 350, 260);
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
	@FXML private void addCourse(MouseEvent event) {
		if (btnAddCourse.isDisabled()) return;
		try {
			Parent window;
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AddEditCourse.fxml"));
			window = loader.load();
			AddEditCourseController controller = loader.<AddEditCourseController>getController();
			String title = "Add Course";
			
			//Potentially send data if editing
			TreeItem<Object> treeItem = treeCourseList.getSelectionModel().getSelectedItem();
			if (event==null && treeItem!=null && getObjectType(treeItem.getValue())=="Course") {
				//Send value of course to the window
				controller.setCourseToEdit((Course)treeItem.getValue());
				title = "Edit Course";
			}
			
			Stage stage = setStage(window, title, 425, 170);
			stage.show();
			controller.setFocus();
		} catch (IOException e) {
			e.printStackTrace();
			windowError();
		}
	}
	@FXML private void addSection(MouseEvent event) {
		if (btnAddSection.isDisabled()) return;
		try {
			Parent window;
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AddEditSection.fxml"));
			window = loader.load();
			AddEditSectionController controller = loader.<AddEditSectionController>getController();
			String title = "Add Section";
			
			String focus = "ComboBox";
			//Send data if editing
			TreeItem<Object> treeItem = treeCourseList.getSelectionModel().getSelectedItem();
			if (event==null && treeItem!=null) {
				String objType = getObjectType(treeItem.getValue());
				if (objType=="Course") {
					//Adding section to a course
					controller.setCourseToAddTo((Course)treeItem.getValue());
					focus = "TextField";
				}
				else if (objType=="Section") {
					//Editing section
					controller.setCourseToAddTo((Course)treeItem.getParent().getValue());
					controller.setSectionToEdit((Section)treeItem.getValue());
					title = "Edit Section";
					focus = "TextField";
				}
			}
			
			Stage stage = setStage(window, title, 425, 180);
			stage.show();
			controller.setFocus(focus);
		} catch (IOException e) {
			e.printStackTrace();
			windowError();
		}
	}
	@FXML private void addClassTime(MouseEvent event) {
		if (btnAddClassTime.isDisabled()) return;
		try {
			Parent window;
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AddEditClassTime.fxml"));
			window = loader.load();
			AddEditClassTimeController controller = loader.<AddEditClassTimeController>getController();
			String title = "Add Class Time";
			
			//Send data if editing
			TreeItem<Object> treeItem = treeCourseList.getSelectionModel().getSelectedItem();
			if (event==null && treeItem!=null) {
				String objType = getObjectType(treeItem.getValue());
				if (objType=="Section") {
					//Adding classtime to a section
					controller.setCourseToAddTo((Course)treeItem.getParent().getValue());
					controller.setSectionToAddTo((Section)treeItem.getValue());
				}
				else if (objType=="ClassTime") {
					//Editing a classtime
					controller.setCourseToAddTo((Course)treeItem.getParent().getParent().getValue());
					controller.setSectionToAddTo((Section)treeItem.getParent().getValue());
					controller.setClassTimeToEdit((ClassTime)treeItem.getValue());
					title = "Edit Class Time";
				}
			}
			
			Stage stage = setStage(window, title, 425, 500);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
			windowError();
		}
	}
	@FXML private void genSchedule() {
		if (btnGenSchedule.isDisabled()) return;
		openWindow("/fxml/Schedule.fxml", "Schedule", 1020, 680);
	}
	
	//These methods are called when corresponding context menu item its clicked
	@FXML private void menuCourseClicked(ActionEvent event) {addCourse(null);}
	@FXML private void menuSectionClicked(ActionEvent event) {addSection(null);}
	@FXML private void menuClassTimeClicked(ActionEvent event) {addClassTime(null);}
	@FXML private void deleteItem(ActionEvent event) {
		TreeItem<Object> selectedItem = treeCourseList.getSelectionModel().getSelectedItem();
		if (selectedItem!=null) {
			Object item = selectedItem.getValue();
			String type = getObjectType(item);
			if (type.equals("Course")) {
				Course course = (Course)item;
				if (App.showConfirmDialog("Do you really want to delete "+course.getName()+"?", AlertType.WARNING)) {
					App.userSelection.remove(course);
				}
			}
			else if (type.equals("Section")) {
				Section section = (Section)item;
				List<Course> courses = App.userSelection.copyCourses();
				for (Course course: courses) {
					if (course.contains(section) && 
					App.showConfirmDialog("Do you really want to delete "+section.getName()+"?", AlertType.WARNING)) {
						course.remove(section);
					}
				}
			}
			else if (type.equals("ClassTime")) {
				ClassTime classTime = (ClassTime)item;
				List<Course> courses = App.userSelection.copyCourses();
				for (Course course: courses) {
					List<Section> sections = course.copySections();
					for (Section section: sections) {
						if (section.contains(classTime) && 
						App.showConfirmDialog("Do you really want to delete "+classTime.toString()+"?", AlertType.WARNING)) {
							section.remove((ClassTime)item);
						}
					}
				}
			}
			treeCourseList.getSelectionModel().clearSelection();
			refresh();
		}
	}
	
	private void resetButtons() {
		btnAddSection.setDisable(true);
		btnAddClassTime.setDisable(true);
	}

	private void openWindow(String path, String title, int width, int height) {
		try {
			Parent window;
			FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
			window = loader.load();
			Stage stage = setStage(window, title, width, height);
			stage.show();
			
		} catch (IOException e) {
			e.printStackTrace();
			windowError();
		}
	}
	
	private Stage setStage(Parent window, String title, int width, int height) {
		Scene scene = new Scene(window, width, height);
		Stage stage = new Stage();
		stage.setTitle(title);
		stage.setMinWidth(width+20);
		stage.setMinHeight(height+47);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setOnHiding(windowEvent -> createCourseList());
		stage.setScene(scene);
		return stage;
	}
	
	private void createCourseList() {
		resetButtons();
		
		// Root Item
		TreeItem<Object> rootItem = new TreeItem<>(new Course("List"));
		rootItem.setExpanded(true);
		
		for (Course course : App.userSelection.copyCourses()) {
			TreeItem<Object> courseCell = new TreeItem<>(course);
			courseCell.setExpanded(true);
			rootItem.getChildren().add(courseCell);
			btnAddSection.setDisable(false);
			btnGenSchedule.setDisable(false);
			
			for (Section section: course.copySections()) {
				TreeItem<Object> sectionCell = new TreeItem<>(section);
				sectionCell.setExpanded(true);
				courseCell.getChildren().add(sectionCell);
				btnAddClassTime.setDisable(false);
				
				for (ClassTime time: section.copyClassTimes()) {
					TreeItem<Object> timeCell = new TreeItem<>(time);
					timeCell.setExpanded(true);
					sectionCell.getChildren().add(timeCell);
				}
			}
		}
		
		//TreeView Setup
		treeCourseList.setCellFactory(e -> new TreeViewGenericCell());
		treeCourseList.setRoot(rootItem);
		treeCourseList.setShowRoot(false);
	}
	
	private String getObjectType(Object item) {
		String type = null;
		try {
			Course course = (Course)item;
			type = "Course";
		}
		catch (Exception e1) {
			try {
				Section section = (Section)item;
				type = "Section";
			}
			catch (Exception e2) {
				try {
					ClassTime classtime = (ClassTime)item;
					type = "ClassTime";
				}
				catch (Exception e3) {
					//Should not come here
				}
			}
		}
		return type;
	}
	
	private void refresh() {createCourseList();}
	
	private void windowError() {
		App.showNotification("An error occured while trying to open the window.\nPlease try again.", AlertType.ERROR);
	}
}
