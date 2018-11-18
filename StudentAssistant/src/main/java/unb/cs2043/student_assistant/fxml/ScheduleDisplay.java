package unb.cs2043.student_assistant.fxml;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static java.time.temporal.ChronoUnit.MINUTES;

import java.time.Duration;

import org.controlsfx.control.spreadsheet.GridBase;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType;
import org.controlsfx.control.spreadsheet.SpreadsheetColumn;
import org.controlsfx.control.spreadsheet.SpreadsheetView;
import org.controlsfx.samples.Utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import unb.cs2043.student_assistant.ClassTime;
import unb.cs2043.student_assistant.Course;
import unb.cs2043.student_assistant.Schedule;
import unb.cs2043.student_assistant.Section;

public class ScheduleDisplay extends SpreadsheetView {
	
	private final int START_HOUR = 7;
	private final int START_MINUTE = 0;
	private Schedule schedule; 
    /** Header at row 1 and other rows represent time ranging from 07:00AM to 22:00PM */
	private final int ROW_COUNT = 31;
	/** Header at column 1 and other columns represent the days of the week */
	private final int COLUMN_COUNT = 7;
    
	public ScheduleDisplay(Schedule schedule) {
		
		this.schedule = schedule;
        GridBase grid = new GridBase(ROW_COUNT, COLUMN_COUNT);
        setHeaders(grid);
        buildGrid(grid);
        setGrid(grid);
        
        ArrayList<String> days = new ArrayList<>();
		days.add("M");
		days.add("Th");
		ClassTime time1 = new ClassTime("Lab", days, LocalTime.of(11,30), LocalTime.of(14,30));
		Section sec1 = new Section("S1");
		sec1.add(time1);
		Course c1 = new Course("C1");
		c1.add(sec1);
		
		ClassTime time2 = new ClassTime("Lab", days, LocalTime.of(8, 30), LocalTime.of(9, 20));
		Section sec2 = new Section("S2");
		sec2.add(time2);
		Course c2 = new Course("C2");
		c2.add(sec2);
		
		schedule.add(c1);
		schedule.add(c2);
        populateGrid(grid);       
        
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
        ObservableList<ObservableList<SpreadsheetCell>> rows = FXCollections.observableArrayList();
        
        for (int row = 0; row < grid.getRowCount(); ++row) {
            final ObservableList<SpreadsheetCell> list = FXCollections.observableArrayList();

            for (int column = 0; column < grid.getColumnCount(); ++column) {
          		
          		SpreadsheetCell cell = SpreadsheetCellType.STRING.createCell(row, column, 1, 1,"");
            	cell.getStyleClass().add("first-cell");
            	list.add(cell);
            	
            }
            rows.add(list);
        }
        grid.setRows(rows);
        System.out.println(grid.getColumnHeaders().size());
        for (String text : grid.getColumnHeaders())
        	System.out.println(text);
        
    }
    
    public void setHeaders(GridBase grid) {
    	String[] days = {"Sunday", "Monday", "Tueday", "Wednesday", "Thursday", "Friday", "Saturday"};
    	String[] time = new String[grid.getRowCount()];
    	LocalTime start = LocalTime.of(START_HOUR, START_MINUTE);
    	for (int i = 0; i < time.length; i++) {
    		time[i] = start.toString();
    		start = start.plusMinutes(30);
    	}
    	
    	grid.getColumnHeaders().setAll(days);
    	grid.getRowHeaders().setAll(time);
    }
 
    
	private void populateGrid(GridBase grid) {
		
		for(Course course : schedule.copyCourses()) {
			ClassTime time = course.getSection(0).getClassTime(0);
			List<Integer> columnIndex = getColumnIndex(time);
			List<Integer> rowIndex = getRowIndex(time);
			for (Integer row : rowIndex) {
				for(Integer col : columnIndex) 
					grid.setCellValue(row, col, course.getName());
			}
		}
	}
	
	/*
	 * This method extracts the column index based on the days of the ClassTime object
	 */
	private List<Integer> getColumnIndex(ClassTime time){
		List<Integer> columnIndex = new ArrayList<>();
		
		for(String day : time.copyDays()) {
			if(day == "Su")
				columnIndex.add(1);
			if(day == "M")
				columnIndex.add(2);
			if(day == "T")
				columnIndex.add(3);
			if(day == "W")
				columnIndex.add(4);
			if(day == "Th")
				columnIndex.add(5);
			if(day == "F")
				columnIndex.add(6);
			if(day == "Sa")
				columnIndex.add(7);
		}
		return columnIndex;
	}
	
	/*
	 * This method extracts the column index based on the days of the ClassTime object
	 */
	private List<Integer> getRowIndex(ClassTime time){
		List<Integer> rowIndex = new ArrayList<>();
		
		LocalTime start = LocalTime.of(START_HOUR, START_MINUTE);
		int starting = (int)(Math.ceil((Duration.between(start, time.getStartTime()).toMinutes()/30.0)));
		rowIndex.add(starting);
		
		int duration = (int)(Math.ceil((Duration.between(time.getStartTime(), time.getEndTime()).toMinutes()/30.0)));
		for (int i = 1; i < duration; i++)
			rowIndex.add(starting + i);
		
		return rowIndex;
	}
}

