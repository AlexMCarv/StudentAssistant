package unb.cs2043.student_assistant.fxml;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.controlsfx.glyphfont.GlyphFontRegistry;
import org.controlsfx.samples.HelloGlyphFont;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import unb.cs2043.student_assistant.Schedule;

/**
 * Controller class for the Schedule.fxml 
 * @author Alexandre Carvalho
 */
public class ScheduleController implements javafx.fxml.Initializable {
    
	static {GlyphFontRegistry.register("icomoon", HelloGlyphFont.class.getResourceAsStream("icomoon.ttf") , 16);}
	@FXML private VBox outerContainer;
	@FXML private StackPane container;
	@FXML private ToolBar toolbar;
	@FXML Label lblSchedule;
    private Schedule[] bestSchedules;
	private ScheduleDisplay[] displayList;
	private Button[] buttonList;
	private int visibleScheduleNum;
		
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		displayList = new ScheduleDisplay[bestSchedules.length];
		buttonList = new Button[bestSchedules.length];
		
		for(int i = 0; i < bestSchedules.length; i++) {
			displayList[i] = new ScheduleDisplay(bestSchedules[i]);
			displayList[i].setVisible(false);
			container.getChildren().add(displayList[i]);
			buttonList[i] = new Button("Schedule " + (i+1), new Glyph("FontAwesome", FontAwesome.Glyph.CALENDAR));
			buttonList[i].setOnAction(this::selectSchedule);
			toolbar.getItems().add(buttonList[i]);
		}
		
		if (displayList.length > 0)
			selectScheduleByNum(0);
		
		//Keybindings
		outerContainer.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
			KeyCode code = event.getCode();
			
			//Close window when pressing Escape
			if (code ==  KeyCode.ESCAPE) closeWindow();
			
			//Switch schedules using arrow keys
			else if ((code == KeyCode.RIGHT || code == KeyCode.DOWN) 
					&& visibleScheduleNum+1<buttonList.length) {
				selectScheduleByNum(visibleScheduleNum+1);
			}
			else if ((code == KeyCode.LEFT || code == KeyCode.UP) 
					&& visibleScheduleNum-1>=0) {
				selectScheduleByNum(visibleScheduleNum-1);
			}
		});
	
	}
		


	public void setBestSchedules(Schedule[] list) {
		bestSchedules = list;
	}
	
	public void selectSchedule(ActionEvent e) {
		Button source = (Button) e.getSource();
		for (int i=0; i< buttonList.length; i++) {
			if (source.equals(buttonList[i])) {
				selectScheduleByNum(i);
			}
		}
	}
	
	private void selectScheduleByNum(int scheduleNum) {
		int numCourses;
		String plural;
		for(int i = 0; i < buttonList.length; i++) {
			displayList[i].setVisible(false);
			if (i==scheduleNum) {
				numCourses = displayList[i].getNumCourses();
				plural = numCourses==1 ? "" : "s";
				
				displayList[i].setVisible(true);
				lblSchedule.setText("Schedule " + (i + 1)+ " - "+numCourses+" course" + plural);
				visibleScheduleNum = i;
			}				
		}
	}
	
	private void closeWindow() {
		Stage stage = (Stage)container.getScene().getWindow();
		stage.close();
	}
}
