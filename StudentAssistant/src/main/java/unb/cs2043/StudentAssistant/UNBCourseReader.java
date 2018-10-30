package unb.cs2043.StudentAssistant;

//Imports
import java.util.logging.Level;
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
        Schedule courseList = new Schedule("Course List");
        
        //Loop through rows
        Pattern p; Matcher m;
        for (int i=1; i<rows.size(); i++) {
        	HtmlTableRow row = rows.get(i);
        	String rowText = row.asText();
        	
        	//System.out.println(rowText);
        	
        	//Check if first row of a course
        	if (row.getCell(0).asText().matches("\\d{6}")) {
				
        		p = Pattern.compile("(\\d{6}).*"						//Course ID 	(6 digits)
        				+ "(\\w{2,4}(?:\\/\\w{2,4})?\\*\\d{4}).*"		//Course Name 	(Ex: CS2043)
        				+ "([A-z]{2}\\d\\d[A-z]).*"						//Section 		(Ex: FR01A)
        				+ "\\s((?:M|T|W|Th|F)+)\\s.*"					//Days 			(Ex: MWF)
        				+ "(\\d\\d:\\d\\d\\w\\w-\\d\\d:\\d\\d\\w\\w).*"	//Time 			(Ex: 08:30AM-9:20AM)
        				+ "\\s([A-Z]+\\d+)\\s");						//Location 		(Ex: HC13)
    			m = p.matcher(rowText);
    			
    			if (m.find()) {
    				//Pull values of this course
    				String Course = row.getCells().get(1).asText();
    				String Section = m.group(3);
    				String Type = "Lec";
    				String Day = row.getCells().get(5).asText();
    				String Time = m.group(5);
    				String Location = m.group(6);
    				//System.out.println(Course+"\t"+Section+"\t"+Type+"\t\t"+Day+"\t"+Time+"\t"+Location);
    				
    				//Create course object and add it to the list
    				courseList.add(new Course());
    				
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
    								+ "(\\d\\d:\\d\\d\\w\\w-\\d\\d:\\d\\d\\w\\w).*"	//Time
    								+ "\\s([A-Z]+\\d+)\\s");						//Location 		(Ex: HC13)
    		    			m = p.matcher(rowText);
    		    			
    		    			if (m.find()) {
    		    				//Get values of this class time
    		    				Type = m.group(1);
    		    				Day = m.group(2);
    		    				Time = m.group(3);
    		    				Location = m.group(4);
    		    				
    		    				if (!Type.equals("Tutorial")) {
    		    					Type+="\t";
    		    				}
    		    				
    		    				System.out.println("\t\t\t"+Type+"\t"+Day+"\t"+Time+"\t"+Location);
    		    			}
    					}
    				}
    				
    			}
        	}
        	
        }
        
        webClient.close();
        
		return true;
	}
	
//======== PRIVATE METHODS =======
	
	//Builds the website url using the data.
	private String getUrl() {
		return url+"?term="+year+"/"+term+"&level="+level+
				"&subject="+subject+"&location="+location;
	}
	
	
}
