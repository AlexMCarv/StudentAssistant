package unb.cs2043.student_assistant.fxml;

import javafx.scene.control.TreeCell;
import unb.cs2043.student_assistant.ClassTime;

/**
 * Controls how the objects are shown when added to a TreeView item 
 * @author Alexandre Carvalho
 */
public class TreeViewGenericCell <T> extends TreeCell<T>{
	
	@Override
	protected void updateItem(T item, boolean empty) {
		super.updateItem(item, empty);
		
		if (isEmpty()) {
			setText(null);
            
		} else {
			setText(item.toString());
			if (!(this.getItem() instanceof ClassTime)) {
				int colonPos = item.toString().indexOf(':');
				String text = item.toString().substring(0, colonPos);
				setText(text);
			}
		}
	}
}
