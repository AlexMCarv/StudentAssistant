package unb.cs2043.student_assistant.fxml;
import java.util.HashMap;
import java.util.Map;

import org.controlsfx.control.spreadsheet.GridBase;
import org.controlsfx.control.spreadsheet.Picker;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType;
import org.controlsfx.control.spreadsheet.SpreadsheetView;
import org.controlsfx.samples.Utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

public class ScheduleDisplay extends SpreadsheetView {
	
	public ScheduleDisplay() {
        int rowCount = 31; //Will be re-calculated after if incorrect.
        int columnCount = 8;

        GridBase grid = new GridBase(rowCount, columnCount);
        grid.setRowHeightCallback(new GridBase.MapBasedRowHeightFactory(generateRowHeight()));
        buildGrid(grid);

        setGrid(grid);

        generatePickers();

        getFixedRows().add(0);
        getColumns().get(0).setFixed(true);
        getStylesheets().add(Utils.class.getResource("spreadsheetSample.css").toExternalForm());
	}
	
	 /**
     * Add some pickers into the SpreadsheetView in order to give some
     * information.
     */
    private void generatePickers() {
        getRowPickers().put(0, new Picker() {

            @Override
            public void onClick() {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("This row contains several fictive companies. "
                        + "The cells are not editable.\n"
                        + "A custom tooltip is applied for the first cell.");
                alert.show();
            }
        });

        getRowPickers().put(1, new Picker() {

            @Override
            public void onClick() {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("This row contains cells that can only show a list.");
                alert.show();
            }
        });

        getRowPickers().put(2, new Picker() {

            @Override
            public void onClick() {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("This row contains cells that display some dates.");
                alert.show();
            }
        });

        getRowPickers().put(3, new Picker() {

            @Override
            public void onClick() {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("This row contains some Images displaying logos of the companies.");
                alert.show();
            }
        });

        getRowPickers().put(4, new Picker() {

            @Override
            public void onClick() {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("This row contains Double editable cells. "
                        + "Except for ControlsFX compagny where it's a String.");
                alert.show();
            }
        });
        getRowPickers().put(5, new Picker("picker-label", "picker-label-exclamation") {

            @Override
            public void onClick() {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("This row contains Double editable cells with "
                        + "a special format (%). Some cells also have "
                        + "a little icon next to their value.");
                alert.show();
            }
        });

        getColumnPickers().put(0, new Picker("picker-label", "picker-label-security") {

            @Override
            public void onClick() {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Each cell of this column (except for the "
                        + "separator in the middle) has a particular css "
                        + "class for changing its color.\n");
                alert.show();
            }
        });
    }

    /**
     * Specify a custom row height.
     *
     * @return
     */
    private Map<Integer, Double> generateRowHeight() {
        Map<Integer, Double> rowHeight = new HashMap<>();
        rowHeight.put(1, 100.0);
        return rowHeight;
    }
    
    /**
     * Build the grid.
     *
     * @param grid
     */
    private void buildGrid(GridBase grid) {
        ObservableList<ObservableList<SpreadsheetCell>> rows = FXCollections.observableArrayList();

        int rowIndex = 0;

        for (int i = rowIndex; i < rowIndex + 1000; ++i) {
            final ObservableList<SpreadsheetCell> randomRow = FXCollections.observableArrayList();

            SpreadsheetCell cell = SpreadsheetCellType.STRING.createCell(i, 0, 1, 1, "Random " + (i + 1));
            cell.getStyleClass().add("first-cell");
            randomRow.add(cell);

            rows.add(randomRow);
        }
        grid.setRows(rows);
    }


}
