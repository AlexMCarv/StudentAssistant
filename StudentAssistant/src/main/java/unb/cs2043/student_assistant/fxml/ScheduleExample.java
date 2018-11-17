package unb.cs2043.student_assistant.fxml;

import java.util.ServiceLoader;

import org.controlsfx.control.spreadsheet.GridBase;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType;
import org.controlsfx.control.spreadsheet.SpreadsheetView;

import fxsampler.FXSamplerConfiguration;
import fxsampler.SampleBase;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import unb.cs2043.student_assistant.fxml.HelloSpreadsheetView.SpreadsheetViewExample;

public class ScheduleExample extends Application {
	
	private SpreadsheetView spreadSheetView1;
	private SpreadsheetView spreadSheetView2;
	private HBox centerPane;
	
	@Override
	public void start(Stage primaryStage) {
        ServiceLoader<FXSamplerConfiguration> configurationServiceLoader = ServiceLoader.load(FXSamplerConfiguration.class);

        primaryStage.setTitle("Schedule Example");
        VBox root = new VBox();
        root.getChildren().addAll(getPanel(primaryStage));
        Scene scene = new Scene(root, 800, 800);
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
        spreadSheetView1 = new ScheduleDisplay();
        int rowCount = 30;
        int columnCount = 8;
        GridBase grid = new GridBase(rowCount, columnCount);
        ObservableList<ObservableList<SpreadsheetCell>> rows = FXCollections.observableArrayList();
        for (int row = 0; row < grid.getRowCount(); ++row) {
            final ObservableList<SpreadsheetCell> list = FXCollections.observableArrayList();
            for (int column = 0; column < grid.getColumnCount(); ++column) {
                list.add(SpreadsheetCellType.STRING.createCell(row, column, 1, 1,"value"));
            }
            rows.add(list);
        }
       
        SpreadsheetCell cell = SpreadsheetCellType.STRING.createCell(0, 0, 2, 1,"value");
        //I add them in the area covered by the span.
        rows.get(0).set(0, cell);
        rows.get(1).set(0, cell);
        grid.setRows(rows);

               
        grid.setRows(rows);
        
        spreadSheetView2 = new SpreadsheetView(grid);
        spreadSheetView2.setShowRowHeader(true);
        spreadSheetView2.setShowColumnHeader(true);
        centerPane = new HBox(spreadSheetView1, spreadSheetView2);
        return centerPane;
    }
}
