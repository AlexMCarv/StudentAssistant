/**
 * 
 */
package unb.cs2043.student_assistant.fxml;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Controller for the progress window.
 * @author frede
 */
public class ProgressWindowController implements javafx.fxml.Initializable {
	
	@FXML private StackPane container;
	@FXML private ProgressBar progressBar;
	@FXML private Label label;
	
	private TimerTask task;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Create a Task.
		task = new TimerTask();
	}
	
	public void start(int timeout) {
		progressBar.setProgress(0);
		progressBar.progressProperty().unbind();
		progressBar.progressProperty().bind(task.progressProperty());
		label.textProperty().unbind();
		label.textProperty().bind(task.messageProperty());
		
		// Start the Task.
		task.setTimeout(timeout);
		new Thread(task).start();
	}
	
	public void stop() {
		task.cancel(true);
		progressBar.progressProperty().unbind();
		progressBar.setProgress(0);
		
		Stage stage = (Stage)container.getScene().getWindow();
	    stage.close();
	}
	
	private void setIndeterminate() {
		progressBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
	}
	
	private class TimerTask extends Task<Double> {

		private double startTime;
		private int timeout;
		
		private void setTimeout(int timeout) {this.timeout = timeout;}
		
		@Override
		protected Double call() throws Exception {
			startTime = System.nanoTime();
			while (!isCancelled()) {
				Thread.sleep(100);
				if (getRunningTime()<timeout) {
					updateProgress(getRunningTime(), timeout);
					updateMessage((int)(getRunningTime()/timeout*100)+"%");
				}
				else {
					setIndeterminate();
					updateMessage("?");
				}
			}
			return getRunningTime();
		}
		
		private double getRunningTime() {
			return (System.nanoTime()-startTime)/1000000000;
		}
	}
}

