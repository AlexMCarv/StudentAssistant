package unb.cs2043.student_assistant.fxml;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * This class is a TextField which implements an "autocomplete" functionality, based on a supplied list of entries.
 * @author Caleb Brinkman
 */
public class AutoCompleteTextField extends TextField
{
	/** The existing autocomplete entries. */
	private final SortedSet<String> entries;
	/** The popup used to select an entry. */
	private ContextMenu entriesPopup;
	/**Determines if the autocomplete is active or not. */
	private boolean active;

	/** Construct a new AutoCompleteTextField. */
	public AutoCompleteTextField(Set<String> displayValues, Set<String> useValues) {
		super();
		active = true;
		entries = new TreeSet<>();
		entries.addAll(displayValues);
		
		TreeSet<String> useValuesTree = new TreeSet<>();
		useValuesTree.addAll(useValues);
		
		entriesPopup = new ContextMenu();
		textProperty().addListener(new ChangeListener<String>()
		{
			@Override
			public void changed(ObservableValue<? extends String> observableValue, String s, String s2) {
				if (getText().length() == 0 || !active) {
					entriesPopup.hide();
				} 
				else {
					LinkedList<String> searchResult = new LinkedList<>();
					
					String value = getText().toUpperCase();
					searchResult.addAll(entries.subSet(value, value + Character.MAX_VALUE));
					
					LinkedList<String> useValuesList = new LinkedList<>();
					useValuesList.addAll(useValuesTree.subSet(value, value + Character.MAX_VALUE));
					
					if (entries.size() > 0) {
						populatePopup(searchResult, useValuesList);
						if (!entriesPopup.isShowing()) {
							entriesPopup.show(AutoCompleteTextField.this, Side.BOTTOM, 0, 0);
						}
					}
					else {
						entriesPopup.hide();
					}
				}
			}
		});

		focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean aBoolean2) {
				entriesPopup.hide();
			}
		});

	}

	/**
	 * Get the existing set of autocomplete entries.
	 * @return The existing autocomplete entries.
	 */
	public SortedSet<String> getEntries() { return entries; }
	
	/**
	 * Populate the entry set with the given search results.	Display is limited to 10 entries, for performance.
	 * @param searchResult The set of matching strings.
	 */
	private void populatePopup(List<String> searchResult, List<String> useValuesList) {
		List<CustomMenuItem> menuItems = new LinkedList<>();
		// If you'd like more entries, modify this line.
		int maxEntries = 10;
		int count = Math.min(searchResult.size(), maxEntries);
		for (int i = 0; i < count; i++) {
			final String result = searchResult.get(i);
			final String useValue = useValuesList.get(i);
			Label entryLabel = new Label(useValue);
			CustomMenuItem item = new CustomMenuItem(entryLabel, true);
			item.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent actionEvent) {
					setText(result);
					entriesPopup.hide();
				}
			});
			menuItems.add(item);
		}
		entriesPopup.getItems().clear();
		entriesPopup.getItems().addAll(menuItems);

	}
	
	/**
	 * Changes the active state.
	 * @param newActive The new active value.
	 */
	public void setActive(boolean newActive) {
		active = newActive;
	}
}