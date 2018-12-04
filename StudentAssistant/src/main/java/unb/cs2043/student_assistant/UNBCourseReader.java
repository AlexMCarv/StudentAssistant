package unb.cs2043.student_assistant;

import unb.cs2043.student_assistant.fxml.ComboBoxChoice;
import java.io.ObjectOutputStream;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import org.apache.commons.logging.LogFactory;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

/**
 * Allows reading course information from the UNB course timetable website.
 * @author Frederic Verret
 */

public class UNBCourseReader {
	
	private static final String URL = "http://es.unb.ca/apps/timetable/";
	
	private String term;
	private String level;
	private String subject;
	private String city;
	
	public UNBCourseReader(String term, String level, String subject, String city) {
		this.term = term;
		this.level = level;
		this.subject = subject;
		this.city = city;
	}
	
	//Constructor overload without subject (assumes subject is ALL)
	public UNBCourseReader(String term, String level, String city) {
		this.term = term;
		this.level = level;
		this.subject = "ALL";
		this.city = city;
	}
	
//===== GETTERS AND SETTERS =====
	public String getTerm() {
		return term;
	}

	public String getLevel() {
		return level;
	}

	public String getSubject() {
		return subject;
	}

	public String getCity() {
		return city;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
//======= PUBLIC METHODS =======
	
	/**This method creates a file containing a Schedule object 
	which has all the courses from the specified term, year, etc.*/
	public boolean loadData() {
		Schedule courseList = new Schedule(term+" "+level+" "+city);
       	
		HtmlPage page = getHtmlPage(getUrl());
		
		if (page==null) {
			//Error loading page
			return false;
		}
        
        //Get the table with the results
        HtmlTable table = (HtmlTable)page.getElementById("course-list");
        
        //No courses are available for this selection, create file with empty course list.
        if (table==null) {
        	//Save to a file
            if(!writeToFile(courseList)) {
            	System.out.println("File creation failed!");
            	return false;
            }
        	return true;
        }
        
        //Get the number of rows of the table
        int numRows = table.getRowCount();
        //System.out.println("Number of rows: "+numRows+"\n");
        
        //Get rows
        List<HtmlTableRow> rows = table.getRows();
        
        
        //Regex Patterns
        Pattern courseRowPattern = 
        		Pattern.compile("(\\d{6})\\s"					//Course ID 		(6 digits)
				+ "(\\w{2,4}(?:\\/\\w{2,4})?\\*\\d{4})\\s"		//Course Name 		(Ex: CS2043)
				+ "([A-z]{2}\\w\\d[A-z](?:\\/[A-z])?)"			//Section 			(Ex: FR01A)
				+ "\\s(.*)\\t"									//Course Full Name 	(Ex: Digital Logic)
				+ "((?:M|T|W|Th|F)+)\\s"						//Days 				(Ex: MWF)
				+ "(\\d\\d:\\d\\d\\w\\w-\\d\\d:\\d\\d\\w\\w)"	//Time 				(Ex: 08:30AM-9:20AM)
				+ "(?:\\s+([A-Z]+\\d+)\\s)?");					//Location 			(Ex: HC13)
        /*(\d{6})\s(\w{2,4}(?:\/\w{2,4})?\*\d{4})\s([A-z]{2}\d\d[A-z](?:\/[A-z])?)\s(.*)\t.*\s((?:M|T|W|Th|F)+)\s
         * (\d\d:\d\d\w\w-\d\d:\d\d\w\w)(?:\s+([A-Z]+\d+)\s)?
         */
        
        //Courses with multiple locations
        Pattern multiLocPattern = 
        		Pattern.compile("(\\d{6})\\s"								//Course ID 		(6 digits)
				+ "(\\w{2,4}(?:\\/\\w{2,4})?\\*\\d{4})\\s"					//Course Name 		(Ex: CS2043)
				+ "([A-z]{2}\\w\\d[A-z](?:\\/[A-z])?)"						//Section 			(Ex: FR01A)
				+ "\\s(.*)\\t"												//Course Full Name 	(Ex: Digital Logic)
				+ "((?:M|T|W|Th|F)+)(?:\\s+((?:M|T|W|Th|F)+))?\\s"			//Days 				(Ex: MWF)
				+ "(\\d\\d:\\d\\d\\w\\w-\\d\\d:\\d\\d\\w\\w)"				//Time 				(Ex: 08:30AM-9:20AM)
				+ "(?:\\s+(\\d\\d:\\d\\d\\w\\w-\\d\\d:\\d\\d\\w\\w))?\\s"	//Times ctnd
				+ "([A-Z]+\\d+)(?:\\s+([A-Z]+\\d+))?");						//Locations 		(Ex: HC13)
        /*(\d{6})\s(\w{2,4}(?:\/\w{2,4})?\*\d{4})\s([A-z]{2}\d\d[A-z](?:\/[A-z])?)\s(.*)\t((?:M|T|W|Th|F)+)
         * (?:\s+((?:M|T|W|Th|F)+))?\s(\d\d:\d\d\w\w-\d\d:\d\d\w\w)(?:\s+(\d\d:\d\d\w\w-\d\d:\d\d\w\w))?\s([A-Z]+\d+)(?:\s+([A-Z]+\d+))?
         */
        
        //Lab courses have days, time, and location on next line (since they don't have any lecture)
        Pattern labCoursePattern = 
        		Pattern.compile("(\\d{6})\\s"					//Course ID 		(6 digits)
				+ "(\\w{2,4}(?:\\/\\w{2,4})?\\*\\d{4}).*"		//Course Name 		(Ex: CS2043)
				+ "([A-z]{2}\\w\\d[A-z](?:\\/[A-z])?)"			//Section 			(Ex: FR01A)
        		+ "\\s([^\\t]*)");								//Course Full Name 	(Ex: Digital Logic)
        //(\d{6})\s(\w{2,4}(?:\/\w{2,4})?\*\d{4}).*([A-z]{2}\d\d[A-z](?:\/[A-z])?)\s([^\t]*)
        
        //Lab courses with multiple locations
        Pattern multiLocLabCoursePattern =
        		Pattern.compile("(Lab|Tutorial)\\s"							//Type			(Ex: Lab)
        		+ "[ \\t]((?:M|T|W|Th|F)+)(?:\\s+((?:M|T|W|Th|F)+))?\\s"	//Days 			(Ex: MWF)
				+ "(\\d\\d:\\d\\d\\w\\w-\\d\\d:\\d\\d\\w\\w)"				//Time 			(Ex: 08:30AM-9:20AM)
				+ "(?:\\s+(\\d\\d:\\d\\d\\w\\w-\\d\\d:\\d\\d\\w\\w))?\\s"	//Times ctnd
				+ "([A-Z]+\\d+)(?:\\s+([A-Z]+\\d+))?");						//Locations 	(Ex: HC13)
        //Patern for extra class times (labs and tutorials)
        Pattern classTimeRowPattern = 
        		Pattern.compile("(Lab|Tutorial)\\s"				//Type			(Ex: Lab)
				+ "\\s(M|T|W|Th|F)+\\s"							//Days 			(Ex: MWF)
				+ "(\\d\\d:\\d\\d\\w\\w-\\d\\d:\\d\\d\\w\\w)"	//Time 			(Ex: 08:30AM-9:20AM)
				+ "(?:\\s+([A-Z]+\\d+)\\s)?");					//Location 		(Ex: HC13)
        
        Matcher m;
        String courseName="", section="", courseFullName="", type="", day="", time="", location="";
        Course courseObj = null;
        
        //Loop through each row		
        for (int i=1; i<rows.size(); i++) {
        	//Get current row
        	HtmlTableRow row = rows.get(i);
        	String rowText = getText(row, true);
        	
        	//Get next row (if not at the end)
        	HtmlTableRow nextRow = null;
        	String nextRowText = "";
        	if (i!=numRows-1) {
        		nextRow = rows.get(i+1);
        		nextRowText = getText(nextRow, false);
        	}
        	
        	//Flags for special cases
        	boolean sameCourse = false;
        	boolean labCourse = false;
        	boolean multiLocation = false;
        	
        	//Check if it is first row of a course section
        	if (row.getCell(0).asText().matches("\\d{6}")) {
        		
        		//Check if it is a lab course
        		if (row.getCell(5).asText().equals("") && i!=numRows-1 &&
        		nextRow.getCell(0).asText().matches("Lab|Tutorial")) {
        			labCourse = true;
        			//Match lab course
        			m = labCoursePattern.matcher(rowText);
        		}
        		//Check if it has multiple locations
        		else if (row.getCell(5).asText().matches(".*[\\r\\n]+.*")) {
        			multiLocation = true;
        			m = multiLocPattern.matcher(rowText);
        		}
        		else {
        			//Match normal course
        			m = courseRowPattern.matcher(rowText);
        		}
        		
    			if (m.find()) {
    				//Check if still same course (more than 1 section)
    				if (courseName.equals(row.getCells().get(1).asText())) {
    					sameCourse = true;
    				}
    				
    				//Get values for this course
    				courseName = row.getCells().get(1).asText();
    				section = m.group(3);
    				courseFullName = m.group(4);
    				if (labCourse) {
        				//Get values from next row
    					i++;
    					
    					//Check for rare case of lab course AND multiple locations
    					if (nextRow.getCell(2).asText().matches(".*[\\r\\n]+.*")) {
    						multiLocation = true;
    						m = multiLocLabCoursePattern.matcher(nextRowText);
    						
    		    			if (m.find()) {
    		    				//First one
    		    				type = m.group(1);
    		    				day = m.group(2);
    		    				time = m.group(4);
    		    				location = m.group(6);
    		    			}
    					}
    					else {
    						m = classTimeRowPattern.matcher(nextRowText);
    		    			if (m.find()) {
    		    				type = m.group(1);
    		    				day = m.group(2);
    		    				time = m.group(3);
    		    				location = m.group(4);
    		    			}
    					}
    				}
    				else if (multiLocation) {
    					//First one
    					type = "Lec";
	    				day = m.group(5);
	    				time = m.group(7);
	    				location = m.group(9);
    				}
    				else {
    					//Normal course
        				type = "Lec";
        				day = row.getCells().get(5).asText();
        				time = m.group(6);
        				location = m.group(7);
    				}
    				//If no location, set it as N/A
    				location = location==null?"N/A":location;
    				
    				//Create objects and add them to the list
    				ClassTime timeObj = createClassTime(type, day, time);
    				Section sectionObj = new Section(section);
    				sectionObj.add(timeObj);
    				
    				if (multiLocation) {
    					if (labCourse) {
    						//Second one (lab)
    						type = m.group(1);
    	    				day = m.group(3)==null?day:m.group(3);
    	    				time = m.group(5)==null?time:m.group(5);
    	    				location = m.group(7)==null?location:m.group(7);
    					}
    					else {
    						//Second one (normal)
        					type = "Lec";
    	    				day = m.group(6)==null?day:m.group(6);
    	    				time = m.group(8)==null?time:m.group(8);
    	    				location = m.group(10)==null?location:m.group(10);
    					}
    					timeObj = createClassTime(type, day, time);
	    				sectionObj.add(timeObj);
    				}
    				
    				if (sameCourse) {
        				//Add section to previous course (not creating a new course)
        				courseObj.add(sectionObj);
    				}
    				else {
    					//Create new course
        				courseObj = new Course(courseName.replace("*", ""));
        				courseObj.setFullName(courseFullName);
        				courseObj.add(sectionObj);
        				courseList.add(courseObj);
    				}
    				
    				
    				//Loop until find next course
    				boolean keepGoing = true;
    				for (int j=i+1; j<rows.size() && keepGoing; j++) {
    					row = rows.get(j);
    					rowText = getText(row, false);
    					if (row.getCell(0).asText().matches("\\d{6}")) {
    						//Found next course
    						i = j-1;
    						keepGoing = false;
    					}
    					else {
    						//Extra class time
    		    			m = classTimeRowPattern.matcher(rowText);
    		    			
    		    			if (m.find()) {
    		    				//Get values of this class time
    		    				type = m.group(1);
    		    				day = m.group(2);
    		    				time = m.group(3);
    		    				location = m.group(4);
    		    				//If no location, set it as N/A
    		    				location = location == null ? "N/A" : location;
    		    				
    		    				//Create classTime object and add it to the section
    		    				ClassTime otherTimeObj = createClassTime(type, day, time);
    		    				sectionObj.add(otherTimeObj);
    		    			}
    					}
    				}
    			}
        	}
        }
        
        //Compress adjacent sections
        courseList = compressAdjacentSections(courseList);
       
        //Compress all sections
        courseList = compressAllSections(courseList);
        
        //Save to a file
        if(!writeToFile(courseList)) {
        	System.out.println("File creation failed!");
        	return false;
        }
        
		return true;
	}
	
	public static Schedule readFile(String fileName) {
		
		File file = new File(fileName);
		
		//Check if file exists
		if (!file.exists()) {
			return null;
		}
		
		ObjectInputStream objectStream = null;
		try {
			objectStream = new ObjectInputStream(new FileInputStream(file));
		}
		catch (IOException e) {
			System.out.println(getClassName()+": Error finding file or Error opening stream");
			e.printStackTrace();
			return null;
		}
		
		//Read the course list from the file
		Schedule courseList = null;
		try {
			courseList = (Schedule) objectStream.readObject();
		}
		catch (Exception e) {
			System.out.println(getClassName()+": Error reading data");
			e.printStackTrace();
		}
		
		//Close the stream
		try {
			objectStream.close();
		}
		catch (IOException e) {
			System.out.println(getClassName()+": Error closing stream");
			e.printStackTrace();
		}
		
		return courseList;
	}
	
	public File getFile() {
		//The pattern for the fileName is:
		//UNBCourses_year_season_level_city.list
		String fileName = "UNBCourses"+"_"+term.replace("/", "_")+"_"+level+"_"+city+".list";
		File file = new File(fileName);
		return file;
	}
	
	public boolean deleteFile() {
		return getFile().delete();
	}
	
	
	public static ComboBoxChoice[][] getDropdownChoices() {
		
		ComboBoxChoice[][] choices = new ComboBoxChoice[3][];
		
		HtmlPage page = getHtmlPage(URL);
		if (page == null) return null;
		
		//Get Dropdowns (also called Selects)
		DomElement termElem = page.getElementById("term");
		DomElement levelElem = page.getElementById("level");
		DomElement locationElem = page.getElementById("location");
		int i=0;
		if (termElem==null || levelElem == null || locationElem == null) {
			//Could not find the dropdown on the page
			return null;
		}
		HtmlSelect termSelect = (HtmlSelect)termElem;
		HtmlSelect levelSelect = (HtmlSelect)levelElem;
		HtmlSelect locationSelect = (HtmlSelect)locationElem;
		
		HtmlSelect[] selects = {termSelect, levelSelect, locationSelect};
		i=0;
		for (HtmlSelect select: selects) {
			
			ArrayList<ComboBoxChoice> choiceArrayList = new ArrayList<>();
			
			List<HtmlOption> options = select.getOptions();
			for (HtmlOption option: options) {
				//Don't add options starting with a dash (They are just labels)
				if (option.getText().charAt(0)!='-') {
					ComboBoxChoice choice = new ComboBoxChoice(option.getText(), option.getValueAttribute());
					choiceArrayList.add(choice);
				}
			}
			
			ComboBoxChoice[] choiceArray = new ComboBoxChoice[choiceArrayList.size()];
			choices[i++] = choiceArrayList.toArray(choiceArray);
		}
		
		return choices;
	}
	
	
//======== PRIVATE METHODS =======
	
	private String getUrl() {
		return URL+"?term="+term+"&level="+level+
				"&subject="+subject+"&location="+city;
	}
	
	//Return the text in the row with a single space between columns
	private String getText(HtmlTableRow row, boolean removeProf) {
		String rowText = "";
		
		List<HtmlTableCell> cells = row.getCells();
		for(int i=0; i<cells.size(); i++) {
			if (!removeProf || i!=4) {
				rowText += cells.get(i).asText()+"\t";
			}
		}
		
		return rowText;
	}
	
	private static HtmlPage getHtmlPage(String url) {
		//Turn warnings off (Warnings are only useful when testing a website. We just want data so turn it off.)
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
        
        //Get page
        WebClient webClient = new WebClient();
		HtmlPage page = null;
		try {
			page = webClient.getPage(url);
		}
		catch (Exception e) {
			//Possible reasons: Could not connect to the internet, URL is not valid, ...
			System.out.println(getClassName()+": Could not open webpage");
			e.printStackTrace();
			webClient.close();
			return null;
		}
		
		//Find the search button and click it (Needed when searching for ALL courses)
        try {
        	HtmlInput submitButton = page.getFirstByXPath("/html/body//form//input[@type='submit' and @value='Search!']");
            page=submitButton.click();
        }
        catch (Exception e) {
        	//IOException or NullPointerException
        	System.out.println(getClassName()+": Error trying to press submit button.");
        	e.printStackTrace();
        	webClient.close();
        	return null;
        }
        
        webClient.close();
        return page;
	}
	
	private boolean writeToFile(Schedule courseList) {
		boolean result = true;
		
		//Create a file, putting the parameters in the file name 
		//It will overwrite if file with those parameters already exists
		File file = getFile();
		ObjectOutputStream objectStream = null;
		try {
			objectStream = new ObjectOutputStream(new FileOutputStream(file));
		}
		catch (IOException e) {
			System.out.println(getClassName()+": Error creating file or Error opening stream");
			e.printStackTrace();
			return false;
		}
		
		//Write the course list to the file
		try {
			objectStream.writeObject(courseList);
		}
		catch (IOException e) {
			System.out.println(getClassName()+": Error writing data");
			e.printStackTrace();
			
			//Try to delete the file:
			file.delete();
			result = false;
		}
		
		//Close the stream
		try {
			objectStream.close();
		}
		catch (IOException e) {
			System.out.println(getClassName()+": Error closing stream");
			e.printStackTrace();
			return false;
		}
		return result;
	}
	
	private static String getClassName() {
		return UNBCourseReader.class.getSimpleName();
	}
	
	private ClassTime createClassTime(String type, String days, String timeRange) {
		//Get days as a list
		ArrayList<String> dayList = getDaysAsList(days);
		
		//Reformat start+end times
		String[] times = timeRange.split("-");
		LocalTime startTime = convertToLocalTime(times[0]);
		LocalTime endTime = convertToLocalTime(times[1]);
		
		//Round times to nearest half-hour
		startTime = roundTo30(startTime);
		endTime = roundTo30(endTime);
		
		//Create object
		ClassTime timeObj = new ClassTime(type, dayList, startTime, endTime);
		return timeObj;
	}
	
	private ArrayList<String> getDaysAsList(String days) {
		//Split string using uppercase characters
		String[] dayArray = days.split("(?=\\p{Upper})");
		
		//Convert array into arraylist
		ArrayList<String> dayList = new ArrayList<>(); 
		dayList.addAll(Arrays.asList(dayArray));
		
		return dayList;
	}
	
	private LocalTime convertToLocalTime(String time) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mma");
		LocalTime date = LocalTime.parse(time, formatter);
		return date;
	}
	
	private Schedule compressAdjacentSections(Schedule courseList) {
		Schedule compressedList = new Schedule(courseList.getName());
		
		Section compressedSection;
		
		for (int i=0; i<courseList.getSize(); i++) {
			Course currentCourse = courseList.getCourse(i);
			Course compressedCourse = new Course(courseList.getCourse(i).getName());
			compressedCourse.setFullName(currentCourse.getFullName());
			
			if (currentCourse.getSize()>1) {
				
				for (int j=0; j<currentCourse.getSize(); j++) {
					compressedSection = new Section(currentCourse.getSection(j));
					String start = currentCourse.getSection(j).getName();
					
					String end = "";
					boolean stop = false;
					int k;		
					for (k=j+1; k<currentCourse.getSize() && !stop; k++) {
						Section currentSection = currentCourse.getSection(k);
						if (compressedSection.sameClassTimes(currentCourse.getSection(k))) {
							end = getSectionNumber(currentSection.getName(), false, true);
							if (k==currentCourse.getSize()-1) {
								j = k;
							}
						}
						else {
							stop = true;
							j = k-1;
						}
					}
					
					if (!end.equals("")) {
						start = getSectionNumber(start, true, false);
						compressedSection.setName(start+"-"+end);
					}
					compressedCourse.add(compressedSection);
				}
				
				compressedList.add(compressedCourse);
			}
			else {
				compressedList.add(currentCourse);
			}
		}
		
		return compressedList;
	}
	
	private Schedule compressAllSections(Schedule courseList) {
		Schedule compressedList = new Schedule(courseList.getName());
		
		Section compressedSection;
		for (int i=0; i<courseList.getSize(); i++) {
			Course currentCourse = courseList.getCourse(i);
			Course compressedCourse = new Course(courseList.getCourse(i).getName());
			compressedCourse.setFullName(currentCourse.getFullName());
			
			if (currentCourse.getSize()>1) {
				ArrayList<Integer> addedSections = new ArrayList<>(currentCourse.getSize());
				for (int j=0; j<currentCourse.getSize() && !addedSections.contains(j); j++) {
					compressedSection = new Section(currentCourse.getSection(j));
					String currName = currentCourse.getSection(j).getName();
					String newName = getSectionNumber(currName, true, false);
					
					addedSections.add(j);
					for (int k=j+1; k<currentCourse.getSize(); k++) {
						Section currentSection = currentCourse.getSection(k);
						if (compressedSection.sameClassTimes(currentCourse.getSection(k))) {
							currName = currentSection.getName();
							newName += ","+getSectionNumber(currName, false, false);
							addedSections.add(k);
						}
					}
					
					if (newName.length()>currName.length()-1) {
						newName += getSectionLetter(currName);
						compressedSection.setName(newName);
					}
					
					compressedCourse.add(compressedSection);
				}
				
				compressedList.add(compressedCourse);
			}
			else {
				compressedList.add(currentCourse);
			}
		}
		
		return compressedList;
	}
	
	private String getSectionNumber(String sectionName, boolean addStart, boolean addEnd) {
		String result = null;
		
		String pattern = "\\d\\d(?:.*\\d\\d)*";
		pattern = addStart? ".*"+pattern : pattern;
		pattern = addEnd? pattern+".*" : pattern;
		
		Pattern sectionNumber = Pattern.compile(pattern);
        Matcher m = sectionNumber.matcher(sectionName);
        
        if (m.find()) {
        	result = m.group();
        }
        else {
        	pattern = "\\w\\d(?:.*\\w\\d)*";
        	pattern = addStart? ".*"+pattern : pattern;
        	pattern = addEnd? pattern+".*" : pattern;
        	
        	sectionNumber = Pattern.compile(pattern);
        	m = sectionNumber.matcher(sectionName);
        	
        	if (m.find()) {
        		result = m.group();
        	}
        }
        
        return result;
	}
	
	private String getSectionLetter(String sectionName) {
		String result = null;
		
		String pattern = "\\d\\d(?:.*\\d\\d)*(.*)";
		
		Pattern sectionNumber = Pattern.compile(pattern);
        Matcher m = sectionNumber.matcher(sectionName);
        
        if (m.find()) {
        	result = m.group(1);
        }
        else {
        	pattern = "\\w\\d(?:.*\\w\\d)*(.*)";
        	
        	sectionNumber = Pattern.compile(pattern);
        	m = sectionNumber.matcher(sectionName);
        	
        	if (m.find()) {
        		result = m.group(1);
        	}
        }
        
        return result;
	}
	
	private LocalTime roundTo30(LocalTime time) {
		int mod = time.getMinute() % 30;
        int addMin = mod < 15 ? -mod : 30-mod;
        return time.plusMinutes(addMin);
	}
}
