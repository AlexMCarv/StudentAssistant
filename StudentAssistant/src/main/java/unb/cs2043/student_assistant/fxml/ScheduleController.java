package unb.cs2043.student_assistant.fxml;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import unb.cs2043.student_assistant.Schedule;

public class ScheduleController implements javafx.fxml.Initializable {

	@FXML private AnchorPane anchorPane;
	private Schedule[] bestSchedules;
	private ScheduleDisplay schedule1;
	private ScheduleDisplay schedule2;
	private ScheduleDisplay schedule3;
	private ScheduleDisplay schedule4;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		schedule1 = new ScheduleDisplay(bestSchedules[0]);
		//schedule2 = new ScheduleDisplay(bestSchedules[1]);
		//schedule3 = new ScheduleDisplay(bestSchedules[2]);
		//schedule4 = new ScheduleDisplay(bestSchedules[3]);
		
		anchorPane.getChildren().addAll(schedule1);
	}

	public void setBestSchedules(Schedule[] list) {
		bestSchedules = list;
	}
}
