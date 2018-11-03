package unb.cs2043.StudentAssistant.fxml;

/**
 * Class representing a choice used in a comboBoc (has a label and value)
 * @author frede
 */
public class Choice {
	private String label;
	private String value;
	
	public Choice(String label, String value) {
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
