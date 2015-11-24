package edu.cornell.softwareengineering.crystallize.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;

import edu.cornell.softwareengineering.crystallize.util.common.DynamoDBClient;

public class UploadDictionary {

	public static void main(String[] args) throws JSONException, IOException {
		uploadDictionary();
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

		    JSONObject JMdict = obj.getJSONObject("JMdict");
		    JSONArray entries = JMdict.getJSONArray("entry");
		    for(int i = 9000; i < entries.length(); i++) {
		    	JSONObject entry = entries.getJSONObject(i);
		    	
		    	System.out.println(entry.toString());

		    	// Create string of English translations, definitions separated by |
		    	String sensesString = "";
	    		Object sensesObject = JSONObject.wrap(entry.get("sense"));
	    		JSONArray senses;
	    		if(sensesObject instanceof JSONArray) {
	    			senses = (JSONArray) sensesObject;
	    		}
	    		else if(sensesObject instanceof JSONObject) {
	    			senses = new JSONArray().put((JSONObject) sensesObject);
	    		}
	    		else {
	    			senses = new JSONArray();
	    		}
		    	for(int senseIndex = 0; senseIndex < senses.length(); senseIndex++) {
		    		if(!sensesString.equals("")) sensesString += " | ";
		    		
		    		JSONObject sense = senses.getJSONObject(senseIndex);
		    		
		    		Object glossObject = JSONObject.wrap(sense.get("gloss"));
		    		JSONArray glosses;

		    		if(glossObject instanceof JSONArray) {
		    			glosses = (JSONArray) glossObject;
		    		}
		    		else if(glossObject instanceof String) {
		    			glosses = new JSONArray().put(glossObject);
		    		}
		    		else { 
		    			glosses = new JSONArray();
		    		}
			    	
			    	String senseString = "";
			    	for(int glossIndex = 0; glossIndex < glosses.length(); glossIndex++) {
			    		if(!senseString.equals("")) senseString += ", ";
			    		Object value = glosses.get(glossIndex);
			    		
			    		if(value instanceof String) {
			    			if(value != null) senseString += glosses.getString(glossIndex);
			    		}
			    		else {
			    			System.out.println(value.toString());
			    		}
			    	}
			    	sensesString += senseString;
		    	}

		    	// Create Map of Kana Objects
				JSONObject document = new JSONObject();
				List<Map<String, String>> kanaObjects = new ArrayList<Map<String, String>>();
		    	if(entry.has("r_ele")) {
		    		Object kanaObject = JSONObject.wrap(entry.get("r_ele"));
		    		JSONArray kana;
		    		
		    		if(kanaObject instanceof JSONArray) {
		    			kana = (JSONArray) kanaObject;
		    		}
		    		else if(kanaObject instanceof JSONObject) {
		    			kana = new JSONArray().put((JSONObject) kanaObject);
		    		}
		    		else {
		    			kana = new JSONArray();
		    		}
					document.put("Kana", kana);
					
					for(int j = 0; j < kana.length(); j++) {
						JSONObject newJSON = kana.getJSONObject(j);
						Map<String, String> newObj = new HashMap<String, String>();
						for(String name : JSONObject.getNames(newJSON)) {
							String val = newJSON.getString(name);
							System.out.println(new String(val.getBytes(), "UTF-8"));
							if(name.equals("reb")) newObj.put(name, newJSON.getString(name));							
						}
						kanaObjects.add(newObj);
					}
		    	}
		    	
				if(!sensesString.equals("")) document.put("English", sensesString);
		    	
		    	//Store item
				Item item = new Item().withPrimaryKey("WordID", String.valueOf(entry.getInt("ent_seq")));
				if(!sensesString.equals("")) item.withString("English", sensesString);
				if(!kanaObjects.isEmpty()) item.withList("Kana", kanaObjects);

				Table table = DynamoDBClient.getTable("Dictionary");
				
				PutItemOutcome result = table.putItem(item);
		    }
		} finally {
		    br.close();
		}
	}	
	
	public static String insertDictionary(JSONObject parameters) throws Exception {
		String tableName;
		String ID;
		JSONObject document;
		try {
			tableName = parameters.getString("table");
			document = parameters.getJSONObject("document");
			ID = parameters.getString("WordID");
		} catch (JSONException e) {
			throw new Exception("Parameter error inside Insert class");
		}
		
		Item item = new Item().withPrimaryKey("WordID", ID);
		
		JSONArray keys = document.names();
		for(int i = 0; i < keys.length(); i++) {
			String key = keys.getString(i);
			if(key == "English") {
				Object value = JSONObject.wrap(document.get(key));
				if(value instanceof String) item.withString("English", (String) value);
				else item.withNull("English");
			}
			else if(key == "Kana") {
				Object value = JSONObject.wrap(document.get(key));
				List<Map<String, String>> kanaObjects = new ArrayList<Map<String, String>>();
				
				if(value instanceof JSONArray) {
					JSONArray valueArray = (JSONArray) value;
					for(int j = 0; j < valueArray.length(); j++) {
						JSONObject newJSON = valueArray.getJSONObject(i);
						Map<String, String> newObj = new HashMap<String, String>();
						for(String name : JSONObject.getNames(newJSON)) {
							newObj.put(name, newJSON.getString(name));							
						}
						kanaObjects.add(newObj);
					}
				}
				else if(value instanceof JSONObject) { 
					JSONObject newJSON = (JSONObject) value;
					Map<String, String> newObj = new HashMap<String, String>();
					for(String name : JSONObject.getNames(newJSON)) {
						newObj.put(name, newJSON.getString(name));							
					}
					kanaObjects.add(newObj);
				}
				else { kanaObjects = null; }
				if(kanaObjects != null) item.withList("Kana", kanaObjects);
				else item.withNull("Kana");
			}
			else { item.withNull(key); }
		}
		
		Table table = DynamoDBClient.getTable(tableName);
		
		PutItemOutcome result = table.putItem(item);
		
		JSONObject resultJSON = new JSONObject();
		resultJSON.put("ok", true);
		resultJSON.put("results", result);
		
    	return resultJSON.toString();
	}

}
