package unb.cs2043.student_assistant;
import java.io.File;
import javafx.stage.Window;
import javafx.stage.FileChooser;

// Uses JavaFX to display a new window prompting the user to select a file to load.
public class FileSelect {
	private File file;
	private FileChooser fileChooser = new FileChooser();
		
	public FileSelect (Window window) {
		fileChooser.setTitle("Select File");
		file = fileChooser.showOpenDialog(window);
	}
	
	// Get methods
	public File getFile() {
		return file;
	}

}
