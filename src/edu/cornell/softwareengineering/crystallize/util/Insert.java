package edu.cornell.softwareengineering.crystallize.util;

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
		
		Item item = new Item()
			.withPrimaryKey("ID", ID)
			.withJSON("document", document.toString());
			//.withJSON("document", document.toString());
		
		Table table = DynamoDBClient.getTable(tableName);

		
		
		PutItemOutcome result = table.putItem(item);
		
    	return result.toString();
	}
}