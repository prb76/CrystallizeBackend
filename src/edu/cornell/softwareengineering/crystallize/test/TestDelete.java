package edu.cornell.softwareengineering.crystallize.test;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

public class TestDelete {
	final static String deleteURL = "http://localhost:8080/CrystallizeBackend/Delete";
	
	public static void main(String[] args) throws JSONException, IOException {
		basicTest();
	}
	
	public static void basicTest() throws JSONException, IOException {
		JSONObject query = new JSONObject();
		query.put("name.firstname", "peter");
		
		JSONObject parameters = new JSONObject();
		parameters.append("query", query.toString());
		parameters.append("collection", "TestInsert");
		System.out.println(parameters.toString());
		
		HTTPConnection.excutePost(deleteURL, parameters.toString());
	}
}
