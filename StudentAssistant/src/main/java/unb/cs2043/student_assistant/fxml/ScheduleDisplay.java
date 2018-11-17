package unb.cs2043.student_assistant.fxml;
import java.time.LocalTime;
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
	
	private Schedule schedule; 
    /** Header at row 1 and other rows represent time ranging from 07:00AM to 22:00PM */
	private final int ROW_COUNT = 31;
	/** Header at column 1 and other columns represent the days of the week */
	private final int COLUMN_COUNT = 8;
    
	public ScheduleDisplay(Schedule schedule) {
		
		this.schedule = schedule;
        GridBase grid = new GridBase(ROW_COUNT, COLUMN_COUNT);
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
     * This method is used to create a map of rows with a specified new row height. To apply it to a grid call
     * grid.setRowHeightCallback(new GridBase.MapBasedRowHeightFactory(generateRowHeight(intArray, newDouble)));
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
    	String[] days = {"", "Sunday", "Monday", "Tueday", "Wednesday", "Thursday", "Friday", "Saturday"};
    	String[] time = new String[grid.getRowCount()];
    	LocalTime start = LocalTime.of(7, 0);
    	for (int i = 0; i < time.length; i++) {
    		time[i] = start.toString();
    		start = start.plusMinutes(30);
    	}
    	
    	
        ObservableList<ObservableList<SpreadsheetCell>> rows = FXCollections.observableArrayList();
        
        for (int row = 0; row < grid.getRowCount(); ++row) {
            final ObservableList<SpreadsheetCell> list = FXCollections.observableArrayList();

            for (int column = 0; column < grid.getColumnCount(); ++column) {
          		SpreadsheetCell cell;
            	if (row == 0) { 
            		cell = SpreadsheetCellType.STRING.createCell(row, column, 1, 1, days[column]);
            	} else if (column == 0) {
            		cell = SpreadsheetCellType.STRING.createCell(row, column, 1, 1, time[row]);
            	} else {
            		cell = SpreadsheetCellType.STRING.createCell(row, column, 1, 1,"value");
            	}
            	cell.getStyleClass().add("first-cell");
            	list.add(cell);
            }
            rows.add(list);
        }
        grid.setRows(rows);
    }
    
   
}
