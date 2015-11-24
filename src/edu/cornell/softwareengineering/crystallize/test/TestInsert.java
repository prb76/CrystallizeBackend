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
		//uploadDictionary();
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
			parameters.append("table", "Players");
			parameters.append("ID", ID);
			parameters.append("document", document.toString());

			System.out.println(parameters.toString());
			
		    HTTPConnection.excutePost(insertURL, parameters.toString());
		} finally {
		    br.close();
		}
	}
	
	public static void uploadDictionary() throws JSONException, IOException {
		String filename = "./data/JMdict_e.json";
		BufferedReader br = new BufferedReader(new FileReader(filename));
		try {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();

		    while (line != null) {
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
		    JSONObject obj = new JSONObject(sb.toString());
		    //System.out.println(JSONObject.getNames(obj));
		    JSONObject JMdict = obj.getJSONObject("JMdict");
		    JSONArray entries = JMdict.getJSONArray("entry");
		    for(int i = 25; i < 50; i++) {
		    	JSONObject entry = entries.getJSONObject(i);
		    	
		    	System.out.println(entry.toString());
		    	Object englishTranslations = entry.get("sense");
		    	if(englishTranslations instanceof String) {
		    		System.out.println("String: " + englishTranslations.toString());
		    	}
		    	else if(englishTranslations instanceof JSONArray) {
		    		System.out.println("Array of Strings: " + englishTranslations.toString());
		    	}
		    	else {
		    		System.out.println("Unknown object");
		    	}
		    	System.out.println();
//		    	
//		    	String englishStr = "";
//		    	JSONObject englishTranslations = entry.getJSONObject("sense").getJSONObject("gloss");
//		    	for(int j = 0; j < englishTranslations.length(); j++){
//		    		englishStr += englishTranslations.getString(j) + ", ";
//		    	}
//		    	
//		    	//Store item
//		    	JSONObject parameters = new JSONObject();
//				parameters.append("table", "Dictionary");
//		    	if(entry.has("k_ele")) {
//		    		JSONObject kanji = entry.getJSONObject("k_ele");
//					parameters.append("Kanji", kanji);
//		    	}
//		    	if(entry.has("r_ele")) {
//		    		JSONObject kana = entry.getJSONObject("r_ele");
//					parameters.append("Kana", kana);
//		    	}
//				parameters.append("English", englishTranslations);
//				//FIX THIS
//				System.out.println(parameters.toString());
//				
//			    HTTPConnection.excutePost(insertURL, parameters.toString());
		    }
		} finally {
		    br.close();
		}
	}	
}
