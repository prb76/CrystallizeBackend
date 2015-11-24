package edu.cornell.softwareengineering.crystallize.util;

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

public class Insert {
	public static String insert(JSONObject parameters) throws Exception {
		String tableName;
		String ID;
		JSONObject document;
		try {
			tableName = parameters.getString("table");
			document = parameters.getJSONObject("document");
			ID = parameters.getString("ID");
		} catch (JSONException e) {
			throw new Exception("Parameter error inside Insert class");
		}
		
		Item item = new Item().withPrimaryKey("ID", ID);
		
		JSONArray keys = document.names();
		for(int i = 0; i < keys.length(); i++) {
			String key = keys.getString(i);
			Object value = JSONObject.wrap(document.get(key));
			if(value instanceof JSONArray)
				item.withJSON(key, ((JSONArray) value).toString());
			else if(value instanceof JSONObject) 
				item.withJSON(key, ((JSONObject) value).toString());
			else if(value instanceof String)
				item.withString(key, (String) value);
			else if(value instanceof Double)
				item.withDouble(key, (Double) value);
			else if(value instanceof Integer)
				item.withInt(key, (Integer) value);
			else if(value instanceof Boolean)
				item.withBoolean(key, (Boolean) value);
			else
				item.withNull(key);
		}
		
		Table table = DynamoDBClient.getTable(tableName);
		
		PutItemOutcome result = table.putItem(item);
		
		JSONObject resultJSON = new JSONObject();
		resultJSON.put("ok", true);
		resultJSON.put("results", result);
		
    	return resultJSON.toString();
	}
	

}