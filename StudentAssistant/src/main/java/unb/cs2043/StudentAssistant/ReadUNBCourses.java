package unb.cs2043.StudentAssistant;

/**
 * Reads data from UNB's course timetable website.
 * @author Frederic Verret
 */

//Regex
import java.util.regex.Pattern;
import java.util.regex.Matcher;

//HtmlUnit
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

import java.util.logging.Level;
import java.util.List;


public class ReadUNBCourses {
    public static void main(String[] args) throws Exception {
        WebClient webClient = new WebClient();
        
        //Turn warnings off (Warnings are only useful when testing a website. We just want data so turn it off.)
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
        
        //Parameters for website
        String year = "2018";	
        String term = "FA";		//(FA|WI|SM)
        String level = "UG";	//(UG|GR)
        String subject = "ECE";	//For all subjects, change this to anything that is not a course. Ex: 'ALL'
        String location = "FR";	//(FR|SJ|MO|...)
        
        //Get the web page
        //Example URL: http://es.unb.ca/apps/timetable/?term=2018/FA&level=UG&subject=CS&location=FR
        String url = "http://es.unb.ca/apps/timetable/?term="+year+"/"+term+"&level="+level+"&subject="+subject+"&location="+location;
        System.out.println(url);
        HtmlPage page = webClient.getPage(url);
        
        //Find the search button and click it (Only needed when searching for ALL courses
        HtmlInput submitButton = page.getFirstByXPath("/html/body//form//input[@type='submit' and @value='Search!']");
        page=submitButton.click();
        
        //Get the table with the results
        HtmlTable table = (HtmlTable)page.getElementById("course-list");
        
        //Get the number of rows of the table
        int numRows = table.getRowCount();
        System.out.println("Number of rows: "+numRows+"\n");
        
        //Get rows
        List<HtmlTableRow> rows = table.getRows();
        
        //Header
        System.out.println("Course\t\tSection\tType\t\tDays\tTime\t\tLocation");
        
        //Loop through rows
        Pattern p; Matcher m;
        String Course="", Section="", Type="", Day="", Time="", Location="";
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
        				+ "(\\d\\d:\\d\\d\\w\\w-\\d\\d:\\d\\d\\w\\w).*"	//Time 			(Ex: 08:30AM-9:20AM)
        				+ "\\s([A-Z]+\\d+)\\s");
        		
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
    				
    				//Check if still same course (new section)
    				//System.out.println(Course+" ?= "+row.getCells().get(1).asText());
    				if (Course.equals(row.getCells().get(1).asText())) {
    					//Same section
    					sameCourse = true;
    				}
    				
    				Course = row.getCells().get(1).asText();
    				Section = m.group(3);
    				
    				if (labCourse) {
        				//Get values from next row
    					i++;
        				
        				p = Pattern.compile("(Lab|Tutorial).*"					//Type
								+ "\\s(M|T|W|Th|F)+\\s.*"						//Days
								+ "(\\d\\d:\\d\\d\\w\\w-\\d\\d:\\d\\d\\w\\w).*"	//Time
								+ "\\s([A-Z]+\\d+)\\s");						//Location 		(Ex: HC13)
		    			m = p.matcher(nextRowText);
		    			
		    			if (m.find()) {
		    				Type = m.group(1);
		    				Day = m.group(2);
		    				Time = m.group(3);
		    				Location = m.group(4);
		    			}
    				}
    				else {
    					//Normal course
        				Type = "Lec";
        				Day = row.getCells().get(5).asText();
        				Time = m.group(5);
        				Location = m.group(6);
    				}
    				
    				if (sameCourse) {
    					System.out.println("\t\t"+Section+"\t"+Type+"\t\t"+Day+"\t"+Time+"\t"+Location);
    				}
    				else {
    					System.out.println(Course+"\t"+Section+"\t"+Type+"\t\t"+Day+"\t"+Time+"\t"+Location);
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
    								+ "(\\d\\d:\\d\\d\\w\\w-\\d\\d:\\d\\d\\w\\w).*"	//Time
    								+ "\\s([A-Z]+\\d+)\\s");						//Location 		(Ex: HC13)
    		    			m = p.matcher(rowText);
    		    			
    		    			if (m.find()) {
    		    				
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
    }
}