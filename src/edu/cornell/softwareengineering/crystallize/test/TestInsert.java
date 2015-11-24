package edu.cornell.softwareengineering.crystallize.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TestInsert {
	final static String insertURL = "http://localhost:8080/CrystallizeDynamoBackend/Insert";

	public static void main(String[] args) throws JSONException, IOException {		
		basicTest();
		//uploadPlayer("54321", "./data/ConcernedSheep.json");
	}
	
	public static void basicTest() throws JSONException, IOException {
		JSONObject document = new JSONObject();
		document.put("grade", "A+");
		System.out.println(document.toString());
		
		JSONObject parameters = new JSONObject();
		parameters.put("document", document);
		parameters.put("table", "Test");
		parameters.put("ID", "123");
		System.out.println(parameters.toString());
		
		HTTPConnection.excutePost(insertURL, parameters.toString());
	}
	
//	public static void complexTest() throws JSONException, IOException {
//		JSONObject name = new JSONObject();
//		name.put("firstname", "peter");
//		name.put("lastname", "baker");
//		
//		JSONArray grades = new JSONArray();
//		grades.put("A-");
//		grades.put("C+");
//		grades.put("B+");
//		grades.put("B-");
//		System.out.println(grades.toString());
//		
//		JSONObject document = new JSONObject();
//		document.put("name", name);
//		document.put("grades", grades);
//		System.out.println(document.toString());
//		
//		JSONObject parameters = new JSONObject();
//		parameters.append("document", document);
//		parameters.append("collection", "TestInsert");
//		System.out.println(parameters.toString());
//		
//		HTTPConnection.excutePost(insertURL, parameters.toString());
//	}
//	
//	public static void playerTest(String filename) throws JSONException, IOException {
//		BufferedReader br = new BufferedReader(new FileReader(filename));
//		try {
//		    StringBuilder sb = new StringBuilder();
//		    String line = br.readLine();
//
//		    while (line != null) {
//		        sb.append(line);
//		        sb.append(System.lineSeparator());
//		        line = br.readLine();
//		    }
//		    JSONObject player = new JSONObject(sb.toString());
//		    
//		    JSONObject parameters = new JSONObject();
//			parameters.append("document", player);
//			parameters.append("collection", "TestInsert");
//			System.out.println(parameters.toString());
//		    
//		    HTTPConnection.excutePost(insertURL, parameters.toString());
//		} finally {
//		    br.close();
//		}
//	}
	
	public static void uploadPlayer(String ID, String fileName) throws JSONException, IOException {
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		try {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();

		    while (line != null) {
		        sb.append(line);
		        System.out.println(line);
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
		    JSONObject document = new JSONObject(sb.toString());
		    
	    	//Store item
	    	JSONObject parameters = new JSONObject();
			parameters.put("table", "Players");
			parameters.put("ID", ID);
			parameters.put("document", document);

			System.out.println(parameters.toString());
			
		    HTTPConnection.excutePost(insertURL, parameters.toString());
		} finally {
		    br.close();
		}
	}

}
