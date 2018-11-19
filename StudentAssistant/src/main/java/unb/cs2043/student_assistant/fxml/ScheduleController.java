package unb.cs2043.student_assistant.fxml;

import java.net.URL;
import java.util.ResourceBundle;

import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.controlsfx.glyphfont.GlyphFontRegistry;
import org.controlsfx.samples.HelloGlyphFont;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.AnchorPane;
import unb.cs2043.student_assistant.Schedule;

public class ScheduleController implements javafx.fxml.Initializable {
    
	static {GlyphFontRegistry.register("icomoon", HelloGlyphFont.class.getResourceAsStream("icomoon.ttf") , 16);}
	@FXML private AnchorPane anchorPane;
	@FXML private ToolBar toolbar;
	private Schedule[] bestSchedules;
	private ScheduleDisplay[] displayList;
	private Button[] buttonList;
		
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		displayList = new ScheduleDisplay[bestSchedules.length];
		buttonList = new Button[bestSchedules.length];
		
		for(int i = 0; i < bestSchedules.length; i++) {
			displayList[i] = new ScheduleDisplay(bestSchedules[i]);
			anchorPane.getChildren().add(displayList[i]);
			buttonList[i] = new Button("Schedule " + (i+1), new Glyph("FontAwesome", FontAwesome.Glyph.CALENDAR));
			buttonList[i].setOnAction(this::selectSchedule);
			toolbar.getItems().add(buttonList[i]);
		}
		
	}

	public void setBestSchedules(Schedule[] list) {
		bestSchedules = list;
	}
	
	public void selectSchedule(ActionEvent e) {
		
		Button source = (Button) e.getSource();
		for(int i = 0; i < buttonList.length; i++) {
			displayList[i].setVisible(false);
			if (source.equals(buttonList[i]))
				displayList[i].setVisible(true);
		}
	}
}
