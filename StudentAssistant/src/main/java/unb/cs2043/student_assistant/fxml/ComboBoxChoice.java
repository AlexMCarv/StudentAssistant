package unb.cs2043.student_assistant.fxml;

/**
 * Class representing a choice used in a comboBox (has a label and value)
 * @author frede
 */
public class ComboBoxChoice {
	private String label;
	private String value;
	
	public ComboBoxChoice(String label, String value) {
		this.label = label;
		this.value = value;
	}

	public String getLabel() {
		return label;
	}

	public String getValue() {
		return value;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public String toFormattedString() {
	return "label: "+label+"\tvalue: "+value;
	}
	
	public String toString() {
		return label;
	}
}
