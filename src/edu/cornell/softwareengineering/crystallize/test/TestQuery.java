package edu.cornell.softwareengineering.crystallize.test;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;

public class TestQuery {
	final static String queryURL = "http://localhost:8080/CrystallizeDynamoBackend/Query";
	
	public static void main(String[] args) throws JSONException, IOException {
		basicTest();
		multiValueSingleFieldTest();
		multiValueMultiFieldTest();
		testFilters();
		allFeatureTest();
		testPlayers2();
		//nestDocTest();
	}
	
	public static void basicTest() throws JSONException, IOException {
		JSONObject queryItem = new JSONObject();
		queryItem.put("attribute", "document.grade");
		queryItem.put("operator", "CONTAINS");
		queryItem.put("values", new JSONArray().put("A"));
		
		JSONObject parameters = new JSONObject();
		parameters.put("table", "Test");
		parameters.append("query", queryItem);
//		System.out.println(parameters.toString());
		
		HTTPConnection.excutePost(queryURL, parameters.toString());
	}

	public static void multiValueSingleFieldTest() throws JSONException, IOException {
		JSONObject queryItem = new JSONObject();
		queryItem.put("attribute", "document.grade");
		queryItem.put("operator", "CONTAINS");
		queryItem.put("values", new JSONArray().put("A").put("B"));
		
		JSONObject parameters = new JSONObject();
		parameters.put("table", "Test");
		parameters.append("query", queryItem);
//		System.out.println(parameters.toString());
		
		HTTPConnection.excutePost(queryURL, parameters.toString());
	}
	
	public static void multiValueMultiFieldTest() throws JSONException, IOException {
		JSONObject queryItem = new JSONObject();
		queryItem.put("attribute", "document.grade");
		queryItem.put("operator", "CONTAINS");
		queryItem.put("values", new JSONArray().put("A").put("B"));
		
		JSONObject queryItem2 = new JSONObject();
		queryItem2.put("attribute", "ID");
		queryItem2.put("operator", "EQ");
		queryItem2.put("values", new JSONArray().put("123"));
		
		JSONObject parameters = new JSONObject();
		parameters.put("table", "Test");
		parameters.append("query", queryItem);
		parameters.append("query", queryItem2);
//		System.out.println(parameters.toString());
		
		HTTPConnection.excutePost(queryURL, parameters.toString());
	}
	
	
	public static void nestDocTest() throws JSONException, IOException {
		JSONObject queryItem = new JSONObject();
		queryItem.put("attribute", "document.grade");
		queryItem.put("operator", "EQ");
		queryItem.put("values", new JSONArray().put("B-"));
		
		JSONObject parameters = new JSONObject();
		parameters.append("table", "Test");
		parameters.append("query", queryItem);
		System.out.println(parameters.toString());
		
		HTTPConnection.excutePost(queryURL, parameters.toString());
	}
	
	public static void testFilters() throws JSONException, IOException {
		JSONObject queryItem = new JSONObject();
		queryItem.put("attribute", "document.grade");
		queryItem.put("operator", "CONTAINS");
		queryItem.put("values", new JSONArray().put("A"));
		
		JSONObject parameters = new JSONObject();
		parameters.put("table", "Test");
		parameters.append("query", queryItem);
		parameters.put("filters", new JSONArray().put("document.grade").put("ID"));
		
		HTTPConnection.excutePost(queryURL, parameters.toString());
	}
	
	public static void allFeatureTest() throws JSONException, IOException {
		JSONObject queryItem = new JSONObject();
		queryItem.put("attribute", "document.grade");
		queryItem.put("operator", "CONTAINS");
		queryItem.put("values", new JSONArray().put("C").put("B"));
		
		JSONObject queryItem2 = new JSONObject();
		queryItem2.put("attribute", "ID");
		queryItem2.put("operator", "EQ");
		queryItem2.put("values", new JSONArray().put("123"));
		
		JSONObject parameters = new JSONObject();
		parameters.put("table", "Test");
		parameters.append("query", queryItem);
		parameters.append("query", queryItem2);
		parameters.put("filters", new JSONArray().put("document.grade").put("ID"));
		
		HTTPConnection.excutePost(queryURL, parameters.toString());
	}
	
	public static void testPlayers() throws JSONException, IOException {
		JSONObject query = new JSONObject();
		query.put("PlayerData.Reviews.Reviews.ItemReviewPlayerData.Phrase.Translation", "good morning");
		System.out.println(query.toString());
		
		JSONArray filters = new JSONArray();
		filters.put("PlayerData.PersonalData.Name");
		
		JSONObject parameters = new JSONObject();
		parameters.append("query", query.toString());
		parameters.append("collection", "TestInsert");
		parameters.append("filters", filters.toString());
		System.out.println(parameters.toString());
		
		HTTPConnection.excutePost(queryURL, parameters.toString());
	}

	public static void testPlayers2() throws JSONException, IOException {
		JSONObject query = new JSONObject();
		query.put("attribute", "document.PersonalData.PlayerName");
		query.put("operator", "EQ");
		query.put("values", new JSONArray().put("SimpleSheep"));
		
		System.out.println(query.toString());
		
		JSONArray filters = new JSONArray();
		filters.put("document.PersonalData");
		
		JSONObject parameters = new JSONObject();
		parameters.put("table", "Players");
		parameters.append("query", query);
		parameters.put("filters", filters);
		System.out.println(parameters.toString());
		
		HTTPConnection.excutePost(queryURL, parameters.toString());
	}
}

