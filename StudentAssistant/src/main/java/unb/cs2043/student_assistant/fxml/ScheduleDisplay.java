package unb.cs2043.student_assistant.fxml;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.time.Duration;

import org.controlsfx.control.spreadsheet.GridBase;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType;
import org.controlsfx.control.spreadsheet.SpreadsheetView;
import org.controlsfx.samples.Utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import unb.cs2043.student_assistant.ClassTime;
import unb.cs2043.student_assistant.Course;
import unb.cs2043.student_assistant.Schedule;

/**
 * This class represents a schedule component based on SpreadsheetView of ControlFX
 * @author Alexandre Carvalho
 */
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
        populateGrid(grid);       
        
        //getFixedRows().add(0);
        //getColumns().get(0).setFixed(true);
        getStylesheets().add(Utils.class.getResource("/style/spreadsheet.css").toExternalForm());
        grid.setRowHeightCallback(new GridBase.MapBasedRowHeightFactory(generateRowHeight(25.0)));
        setEditable(false);
        setRowHeaderWidth(50);
		setPrefHeight(815); setMaxHeight(815); setMinHeight(815);
		setPrefWidth(750); setMaxWidth(750); setMinWidth(750);
		grid.setLocked(true);
		grid.setResizableRows(new BitSet(ROW_COUNT));
		lockColumns(true);
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
    private Map<Integer, Double> generateRowHeight(Double newHeight) {
        Map<Integer, Double> rowHeight = new HashMap<>();
        
        for(int i = 0; i < ROW_COUNT; i++)
        	rowHeight.put(i, newHeight);
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
			for(ClassTime time : course.getSection(0).copyClassTimes()) {
				List<Integer> columnIndex = getColumnIndex(time);
				int rowIndex = getRowIndex(time);
				for(Integer col : columnIndex) { 
					grid.setCellValue(rowIndex, col, course.getName() + "\n" + course.getSection(0) + " - " + time.getType());
					grid.getRows().get(rowIndex).get(col).getStyleClass().add("style" + (schedule.copyCourses().indexOf(course)%5));
					spanCell(grid, rowIndex, col, getDuration(time));
				}
			}
		}
	}
	
	/*
	 * This method extracts the column index based on the days of the ClassTime object
	 */
	private List<Integer> getColumnIndex(ClassTime time){
		List<Integer> columnIndex = new ArrayList<>();
		
		for(String day : time.copyDays()) {
			if(day.equals("Su"))
				columnIndex.add(0);
			if(day.equals("M"))
				columnIndex.add(1);
			if(day.equals("T"))
				columnIndex.add(2);
			if(day.equals("W"))
				columnIndex.add(3);
			if(day.equals("Th"))
				columnIndex.add(4);
			if(day.equals("F"))
				columnIndex.add(5);
			if(day.equals("Sa"))
				columnIndex.add(6);
		}
		return columnIndex;
	}
	
	/*
	 * This method returns the row index that corresponds to the class starting time
	 */
	private int getRowIndex(ClassTime time){
		LocalTime start = LocalTime.of(START_HOUR, START_MINUTE);
		int starting = (int)(Math.ceil((Duration.between(start, time.getStartTime()).toMinutes()/30.0)));
		return starting;
	}
	
	/*
	 * This method returns the duration of the class
	 */
	private int getDuration(ClassTime time){
		int duration = (int)(Math.ceil((Duration.between(time.getStartTime(), time.getEndTime()).toMinutes()/30.0)));
		return duration;
	}
	
	/*
	 * This method creates a span on the specified cell by the amount passed in
	 */
	private void spanCell(GridBase grid, int row, int col, int span) {
		grid.getRows().get(row).get(col).getStyleClass().add("span");
        grid.spanRow(span, row, col);
        //grid.spanColumn(row, col, span);
	}
	
	/*
	 * This method locks or unlock the resizing feature for columns
	 */
    public void lockColumns(boolean lock) {
    	for (int i = 0; i < COLUMN_COUNT; i++) {
    		this.getColumns().get(i).setResizable(!lock);
    	}
    }
}

