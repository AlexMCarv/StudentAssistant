package unb.cs2043.student_assistant.fxml;

import java.util.ServiceLoader;

import org.controlsfx.control.spreadsheet.SpreadsheetView;

import fxsampler.FXSamplerConfiguration;
import fxsampler.SampleBase;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import unb.cs2043.student_assistant.fxml.HelloSpreadsheetView.SpreadsheetViewExample;

public class ScheduleExample extends Application {
	
	private SpreadsheetView spreadSheetView;
	private StackPane centerPane;
	
	@Override
	public void start(Stage primaryStage) {
        ServiceLoader<FXSamplerConfiguration> configurationServiceLoader = ServiceLoader.load(FXSamplerConfiguration.class);

        primaryStage.setTitle("Schedule Example");
        VBox root = new VBox();
        Scene scene = new Scene((Parent) getPanel(primaryStage), 800, 800);
        scene.getStylesheets().add(SampleBase.class.getResource("fxsampler.css").toExternalForm());
        for (FXSamplerConfiguration fxsamplerConfiguration : configurationServiceLoader) {
        	String stylesheet = fxsamplerConfiguration.getSceneStylesheet();
        	if (stylesheet != null) {
            	scene.getStylesheets().add(stylesheet);
        	}
        }
        primaryStage.setScene(scene);
        primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	public Node getPanel(Stage stage) {
        spreadSheetView = new ScheduleDisplay();
        centerPane = new StackPane(spreadSheetView);
        return centerPane;
    }
}
