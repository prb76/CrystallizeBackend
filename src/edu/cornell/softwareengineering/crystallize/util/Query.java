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
		
		// Create list of attributes to retrieve
		String filterString = "";
		for(int i = 0; i < filters.length(); i++) {
			if(!filterString.equals("")) filterString += ", ";
			filterString += filters.getString(i);
		}
		
		ScanRequest request = getScanRequest(tableName, query);
		
		if(!filterString.equals("")) request.setProjectionExpression(filterString);
		
		ScanResult result = dynamoDB.scan(request);
		
		List<Map<String, AttributeValue>> items = result.getItems();
		
		JSONObject resultJSON = new JSONObject();
		resultJSON.put("ok", true);
		resultJSON.put("results", items);
		
    	return resultJSON.toString();
	}
	
	private static ScanRequest getScanRequest(String tableName, JSONArray query) throws JSONException {
		ScanRequest request = new ScanRequest(tableName);
		
		HashMap<String, AttributeValue> valueMap = new HashMap<String, AttributeValue>();
		String expression = "";
		int valueKeyID = 0;
		
		for(int i = 0; i < query.length(); i++) {
			if(expression != "") expression += " AND ";
			
			JSONObject queryItem = query.getJSONObject(i);
			String attribute = queryItem.getString("attribute");
			String operator = queryItem.getString("op");
			JSONArray values = queryItem.getJSONArray("values");
			
			expression += "(";
			for(int valueIndex = 0; valueIndex < values.length(); valueIndex++) {
				if(valueIndex > 0) expression += " OR ";
				String value = values.getString(valueIndex);
				String valueKey = ":value" + (valueKeyID++);
				valueMap.put(valueKey, new AttributeValue().withS(value));
				
				expression += getExpression(attribute, valueKey, operator);
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
			return attribute + " = " + value;
		else if (operator.equals(ComparisonOperator.NE.toString()))
			return attribute + " <> " + value;
		
		//Number Operators
		else if (operator.equals(ComparisonOperator.LT.toString())) 
			return attribute + " < " + value;
		else if (operator.equals(ComparisonOperator.LE.toString()))
			return attribute + " <= " + value;
		else if (operator.equals(ComparisonOperator.GT.toString()))
			return attribute + " >= " + value;
		else if (operator.equals(ComparisonOperator.GE.toString()))
			return attribute + " > " + value;
		
		//String Operators
		else if (operator.equals(ComparisonOperator.BEGINS_WITH.toString()))
			return "begins_with(" + attribute + ", " + value + ")";
		else if (operator.equals(ComparisonOperator.NOT_NULL.toString()))
			return "attribute_exists(" + attribute + ")";
		else if (operator.equals(ComparisonOperator.NULL.toString()))
			return "attribute_not_exists(" + attribute + ")";
		
		//String & Set Operators
		else if (operator.equals(ComparisonOperator.CONTAINS.toString()))
			return "contains(" + attribute + ", " + value + ")";

		//List Operators
		else if (operator.equals(ComparisonOperator.IN.toString()))
			return value + " IN " + attribute;
		
		else return "";
	}
}