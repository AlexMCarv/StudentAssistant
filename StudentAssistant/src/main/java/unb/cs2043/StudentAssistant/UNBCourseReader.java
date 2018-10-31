package unb.cs2043.StudentAssistant;

//Imports
import java.util.logging.Level;
import java.io.ObjectOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.MalformedURLException;
import java.util.List;

//Regex
import java.util.regex.Pattern;
import java.util.regex.Matcher;

//HtmlUnit
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

/**
 * Allows reading course information from the UNB course timetable website 
 * @author Frederic Verret
 */

public class UNBCourseReader {
	
	private final String url = "http://es.unb.ca/apps/timetable/";
	
	private String year;
	private String term;
	private String level;
	private String subject;
	private String location;
	
	public UNBCourseReader(String year, String term, 
			String level, String subject, String location) {
		this.year = year;
		this.term = term;
		this.level = level;
		this.subject = subject;
		this.location = location;
	}
	
	//Constructor overload without subject. Assumes subject is ALL.
	public UNBCourseReader(String year, String term, 
			String level, String location) {
		this.year = year;
		this.term = term;
		this.level = level;
		this.subject = "ALL";
		this.location = location;
	}
	
//===== GETTERS AND SETTERS =====
	public String getYear() {
		return year;
	}

	public String getTerm() {
		return term;
	}

	public String getLevel() {
		return level;
	}

	public String getSubject() {
		return subject;
	}

	public String getLocation() {
		return location;
	}

	public void setYear(String year) {
		this.year = year;
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

	public void setLocation(String location) {
		this.location = location;
	}
	
//======= PUBLIC METHODS =======
	
	/*This method creates a file containing a Schedule object 
	which has all the courses from the specified term, year, etc.*/
	public boolean loadData() {
		//Turn warnings off (Warnings are only useful when testing a website. We just want data so turn it off.)
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
        
        //Get page
        WebClient webClient = new WebClient();
		String address = getUrl();
		HtmlPage page;
		try {
			page = webClient.getPage(address);
		}
		catch (Exception e) {
			//Possible reasons: Could not connect to the internet, URL is not valid, ...
			webClient.close();
			return false;
		}
		
		//Find the search button and click it (Needed when searching for ALL courses)
        try {
        	HtmlInput submitButton = page.getFirstByXPath("/html/body//form//input[@type='submit' and @value='Search!']");
            page=submitButton.click();
        }
        catch (Exception e) {
        	//IOException or NullPointerException
        	webClient.close();
        	return false;
        }
        
        //Get the table with the results
        HtmlTable table = (HtmlTable)page.getElementById("course-list");
        
        //Get the number of rows of the table
        int numRows = table.getRowCount();
        //System.out.println("Number of rows: "+numRows+"\n");
        
        //Get rows
        List<HtmlTableRow> rows = table.getRows();
        
        //Schedule
        Schedule courseList = new Schedule("UNB Course List: "+year+" "+term+" "+level+" "+location);
        
        
        
        
        //Loop through rows
        Pattern p; Matcher m;
        String Course="", Section="", Type="", Day="", Time="", Location="";
        Course course = null;
        for (int i=1; i<rows.size(); i++) {
        	//Get current row
        	HtmlTableRow row = rows.get(i);
        	String rowText = row.asText();
        	
        	//Get next row (if not at the end)
        	HtmlTableRow nextRow = null;
        	String nextRowText = "";
        	if (i!=numRows-1) {
        		nextRow = rows.get(i+1);
        		nextRowText = nextRow.asText();
        	}
        	
        	//Flags for special cases
        	boolean sameCourse = false;
        	boolean labCourse = false;
        	
        	//Check if it is first row of a course section
        	if (row.getCell(0).asText().matches("\\d{6}")) {
				
        		p = Pattern.compile("(\\d{6}).*"						//Course ID 	(6 digits)
        				+ "(\\w{2,4}(?:\\/\\w{2,4})?\\*\\d{4}).*"		//Course Name 	(Ex: CS2043)
        				+ "([A-z]{2}\\d\\d[A-z]).*"						//Section 		(Ex: FR01A)
        				+ "\\s((?:M|T|W|Th|F)+)\\s.*"					//Days 			(Ex: MWF)
        				+ "(\\d\\d:\\d\\d\\w\\w-\\d\\d:\\d\\d\\w\\w)"	//Time 			(Ex: 08:30AM-9:20AM)
        				+ "(?:.*\\s([A-Z]+\\d+)\\s)?");					//Location 		(Ex: HC13)
        		//Check if it is a lab course
        		if (row.getCell(5).asText().equals("") && i!=numRows-1 &&
        		nextRow.getCell(0).asText().matches("Lab|Tutorial")) {
        			labCourse = true;
        			
        			//Lab courses have days, time, and location on next line (since they don't have any lecture)
        			p = Pattern.compile("(\\d{6}).*"						//Course ID 	(6 digits)
            				+ "(\\w{2,4}(?:\\/\\w{2,4})?\\*\\d{4}).*"		//Course Name 	(Ex: CS2043)
            				+ "([A-z]{2}\\d\\d[A-z]).*");					//Section 		(Ex: FR01A)
        			
        		}
        		m = p.matcher(rowText);
    			
    			if (m.find()) {
    				
    				//Check if still same course (more than 1 section)
    				if (Course.equals(row.getCells().get(1).asText())) {
    					sameCourse = true;
    				}
    				
    				//Pull values of this course
    				Course = row.getCells().get(1).asText();
    				Section = m.group(3);
    				
    				if (labCourse) {
        				//Get values from next row
    					i++;
        				
        				p = Pattern.compile("(Lab|Tutorial).*"					//Type
								+ "\\s(M|T|W|Th|F)+\\s.*"						//Days
								+ "(\\d\\d:\\d\\d\\w\\w-\\d\\d:\\d\\d\\w\\w)"	//Time
								+ "(?:.*\\s([A-Z]+\\d+)\\s)?");					//Location 		(Ex: HC13)
		    			m = p.matcher(nextRowText);
		    			
		    			if (m.find()) {
		    				Type = m.group(1);
		    				Day = m.group(2);
		    				Time = m.group(3);
		    				Location = m.group(4);
		    				//If no location, set it as N/A
		    				Location = Location==null?"N/A":Location;
		    			}
    				}
    				else {
    					//Normal course
        				Type = "Lec";
        				Day = row.getCells().get(5).asText();
        				Time = m.group(5);
        				Location = m.group(6);
        				//If no location, set it as N/A
	    				Location = Location==null?"N/A":Location;
    				}
    				
    				//Create objects and add them to the list
    				ClassTime time = new ClassTime(Type+" "+Day+" "+Time+" "+Location);
    				Section section = new Section(Section);
    				section.add(time);
    				if (sameCourse) {
        				//Add section to previous course (not creating a new course)
        				course.add(section);
    				}
    				else {
    					//Create new course
        				course = new Course(Course);
        				course.add(section);
        				courseList.add(course);
    				}
    				
    				//Loop until find next course
    				boolean keepGoing = true;
    				for (int j=i+1; j<rows.size() && keepGoing; j++) {
    					row = rows.get(j);
    					rowText = row.asText();
    					
    					if (row.getCell(0).asText().matches("\\d{6}")) {
    						//Found next course
    						i = j-1;
    						keepGoing = false;
    					}
    					else {
    						//Extra class time
    						p = Pattern.compile("(Lab|Tutorial).*"					//Type
    								+ "\\s(M|T|W|Th|F)+\\s.*"						//Days
    								+ "(\\d\\d:\\d\\d\\w\\w-\\d\\d:\\d\\d\\w\\w)"	//Time
    								+ "(?:.*\\s([A-Z]+\\d+)\\s)?");					//Location 		(Ex: HC13)
    		    			m = p.matcher(rowText);
    		    			
    		    			if (m.find()) {
    		    				//Get values of this class time
    		    				Type = m.group(1);
    		    				Day = m.group(2);
    		    				Time = m.group(3);
    		    				Location = m.group(4);
    		    				//If no location, set it as N/A
    		    				Location = Location==null?"N/A":Location;
    		    				
    		    				//Create classTIme object and add it to the section
    		    				ClassTime other = new ClassTime(Type+" "+Day+" "+Time+" "+Location);
    		    				section.add(other);
    		    			}
    					}
    				}
    			}
        	}
        }
        
        webClient.close();
        
        //System.out.println(courseList);
        
        //Save to a file
        if(!writeToFile(courseList)) {
        	//File creation failed!
        	return false;
        }
        
		return true;
	}
	
	public static Schedule readFile(String fileName) {
		
		File file = new File(fileName);
		ObjectInputStream objectStream = null;
		try {
			objectStream = new ObjectInputStream(new FileInputStream(file));
		}
		catch (IOException e) {
			//Error finding file or Error opening stream
			return null;
		}
		
		//Read the course list from the file
		Schedule courseList = null;
		try {
			courseList = (Schedule) objectStream.readObject();
		}
		catch (Exception e) {
			//Error reading data
		}
		
		//Close the stream
		try {
			objectStream.close();
		}
		catch (IOException e) {
			//Error closing stream
		}
		
		return courseList;
	}
	
//======== PRIVATE METHODS =======
	
	//Builds the website url using the data.
	private String getUrl() {
		return url+"?term="+year+"/"+term+"&level="+level+
				"&subject="+subject+"&location="+location;
	}
	
	private boolean writeToFile(Schedule courseList) {
		boolean result = true;
		
		//Create a file, putting the parameters in the file name 
		//so it will overwrite if file with those parameters already exists
		String fileName = "UNBCourses"+year+term+level+location+".list";
		File file = new File(fileName);
		ObjectOutputStream objectStream = null;
		try {
			objectStream = new ObjectOutputStream(new FileOutputStream(file));
		}
		catch (IOException e) {
			//Error creating file or Error opening stream
			return false;
		}
		
		//Write the course list to the file
		try {
			objectStream.writeObject(courseList);
		}
		catch (IOException e) {
			//Error writing data
			
			//Try to delete the file:
			file.delete();
			result = false;
		}
		
		//Close the stream
		try {
			objectStream.close();
		}
		catch (IOException e) {
			//Error closing stream
			return false;
		}
		return result;
	}
}
