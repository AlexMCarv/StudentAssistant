package unb.cs2043.student_assistant.fxml;

import javafx.scene.control.ListCell;
import unb.cs2043.student_assistant.Course;

/**
 * Controls how the Course objects are shown in the pop-up list when added to a ComboBox item 
 * @author Alexandre Carvalho
 */
public class ComboBoxCourseCell extends ListCell<Course>{
	
	@Override
	public void updateItem(Course item, boolean empty) {
		super.updateItem(item, empty);
			
		if (isEmpty()) {
			setText(null);
	            
		} else {
			setText(item.getName());
		}
	}
}