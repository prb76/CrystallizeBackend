package edu.cornell.softwareengineering.crystallize.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;

import edu.cornell.softwareengineering.crystallize.util.common.DynamoDBClient;

public class Query {
	public static String query(JSONObject parameters) throws Exception {
		String tableName;
		JSONArray query;
		JSONArray filters;
		
		try {
			tableName = parameters.getString("table");
			query = parameters.getJSONArray("query");
			filters = parameters.getJSONArray("filters");
		} catch (JSONException e) {
			throw new Exception("Parameter error inside Insert class");
		}
		
		AmazonDynamoDBClient dynamoDB = DynamoDBClient.getDynamoClient();
				
//		// Create scan request
//		HashMap<String, Condition> scanRequest = new HashMap<String, Condition>();
//		for(int queryIdx = 0; queryIdx < query.length(); queryIdx++) {
//			JSONObject queryItem = query.getJSONObject(queryIdx);
//			
//			ArrayList<AttributeValue> valueList = new ArrayList<AttributeValue>();
//			JSONArray jsonValueList = queryItem.getJSONArray("values");
//			for(int i = 0; i < jsonValueList.length(); i++) {
//				valueList.add(new AttributeValue().withS(jsonValueList.getString(i)));
//			}
//			
//			Condition condition = new Condition()
//					.withComparisonOperator(queryItem.getString("operator"))
//					.withAttributeValueList(valueList);
//			scanRequest.put(queryItem.getString("attribute"), condition);
//		}
		
		// Create list of attributes to retrieve
		ArrayList<String> attributesList = new ArrayList<String>();
		for(int i = 0; i < filters.length(); i++) {
			attributesList.add(filters.getString(i));
		}
		
		HashMap<String, AttributeValue> x = new HashMap<String, AttributeValue>();
		x.put(":grade", new AttributeValue().withS("B"));
		ScanRequest request = getScanRequest(tableName, query);
		
		if(!attributesList.isEmpty()) request.withAttributesToGet(attributesList);
		
		ScanResult result = dynamoDB.scan(request);
		
		List<Map<String, AttributeValue>> items = result.getItems();
		
    	return result.toString();
	}
	
	private static ScanRequest getScanRequest(String tableName, JSONArray query) throws JSONException {
		ScanRequest request = new ScanRequest(tableName);
		
		HashMap<String, AttributeValue> valueMap = new HashMap<String, AttributeValue>();
		String expression = "";
		
		for(int i = 0; i < query.length(); i++) {
			if(expression != "") expression += " AND ";
			
			JSONObject queryItem = query.getJSONObject(i);
			String attribute = queryItem.getString("attribute");
			String operator = queryItem.getString("operator");
			JSONArray values = queryItem.getJSONArray("values");
			
			expression += "(";
			for(int valueIndex = 0; valueIndex < values.length(); valueIndex++) {
				String value = values.getString(valueIndex);
				String valueKey = ":" + attribute + valueIndex;
				valueMap.put(valueKey, new AttributeValue().withS(value));
				
				expression += getExpression(attribute, valueKey, operator);
				expression += " OR ";
			}
			expression += ")";
		}
		
		request
			.withFilterExpression(expression)
			.withExpressionAttributeValues(valueMap);
		
		return request;
	}
	
	private static String getExpression(String attribute, String value, String operator) {
		//General Operators
		if (operator.equals(ComparisonOperator.EQ.toString())) 
			return "(" + attribute + " = " + value + ")";
		else if (operator.equals(ComparisonOperator.NE.toString()))
			return "(" + attribute + " <> " + value + ")";
		
		//Number Operators
		else if (operator.equals(ComparisonOperator.LT.toString())) 
			return "(" + attribute + " < " + value + ")";
		else if (operator.equals(ComparisonOperator.LE.toString()))
			return "(" + attribute + " <= " + value + ")";
		else if (operator.equals(ComparisonOperator.GT.toString()))
			return "(" + attribute + " >= " + value + ")";
		else if (operator.equals(ComparisonOperator.GE.toString()))
			return "(" + attribute + " > " + value + ")";
		
		//String Operators
		else if (operator.equals(ComparisonOperator.BEGINS_WITH.toString()))
			return "(begins_with(" + attribute + ", " + value + "))";
		else if (operator.equals(ComparisonOperator.NOT_NULL.toString()))
			return "(attribute_exists(" + attribute + "))";
		else if (operator.equals(ComparisonOperator.NULL.toString()))
			return "(attribute_not_exists(" + attribute + "))";
		
		//String & Set Operators
		else if (operator.equals(ComparisonOperator.CONTAINS.toString()))
			return "(contains(" + attribute + ", " + value + "))";

		//List Operators
		else if (operator.equals(ComparisonOperator.IN.toString()))
			return "(" + value + " IN " + attribute + ")";
		
		else return "";
	}
}