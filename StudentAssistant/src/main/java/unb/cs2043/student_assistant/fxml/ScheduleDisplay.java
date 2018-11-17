package unb.cs2043.student_assistant.fxml;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.controlsfx.control.spreadsheet.GridBase;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType;
import org.controlsfx.control.spreadsheet.SpreadsheetView;
import org.controlsfx.samples.Utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import unb.cs2043.student_assistant.Schedule;

public class ScheduleDisplay extends SpreadsheetView {
	
	private List<Schedule> scheduleList; 
    /** Header at row 1 and other rows represent time ranging from 07:00AM to 22:00PM */
	private final int ROW_COUNT = 31;
	/** Header at column 1 and other columns represent the days of the week */
	private final int COLUMN_COUNT = 8;
    
	public ScheduleDisplay(List<Schedule> scheduleList) {
		
		this.scheduleList = scheduleList;
        GridBase grid = new GridBase(ROW_COUNT, COLUMN_COUNT);
        grid.setRowHeightCallback(new GridBase.MapBasedRowHeightFactory(generateRowHeight(0, 100.0)));
        buildGrid(grid);

        setGrid(grid);

        getFixedRows().add(0);
        getColumns().get(0).setFixed(true);
        getStylesheets().add(Utils.class.getResource("spreadsheetSample.css").toExternalForm());
	}
	
	/*
	 * Utility Methods
	 */
	
    /**
     * This method is used to create a map of rows with a specified new row height
     * @param rowIndex Index of the row where the height is to be modified
     * @param newHeight New height of the row
     * @return a Map of rowIndex as key and Height as values
     */
    private Map<Integer, Double> generateRowHeight(int[] rowIndex, Double newHeight) {
        Map<Integer, Double> rowHeight = new HashMap<>();
        
        for(int row : rowIndex)
        	rowHeight.put(rowIndex[row], newHeight);
        return rowHeight;
    }
    
    /**
     * Build the grid.
     *
     * @param grid
     */
    private void buildGrid(GridBase grid) {
        
        ObservableList<ObservableList<SpreadsheetCell>> rows = FXCollections.observableArrayList();
        for (int row = 0; row < grid.getRowCount(); ++row) {
            final ObservableList<SpreadsheetCell> list = FXCollections.observableArrayList();
            for (int column = 0; column < grid.getColumnCount(); ++column) {
            	SpreadsheetCell cell = SpreadsheetCellType.STRING.createCell(row, column, 1, 1,"value");
            	cell.getStyleClass().add("first-cell");
            	list.add(cell);
            }
            rows.add(list);
        }
        grid.setRows(rows);
    }
}
