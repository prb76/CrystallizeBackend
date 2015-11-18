package edu.cornell.softwareengineering.crystallize.util;

import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import com.mongodb.util.JSON;

import edu.cornell.softwareengineering.crystallize.util.common.DynamoDBClient;

public class Delete {
	public static String delete(JSONObject parameters) throws Exception {
		String collection;
		JSONObject query;
		
		try {
			collection = parameters.getString("collection");
			query = parameters.getJSONObject("query");
		} catch (JSONException e) {
			throw new Exception("Parameter error inside Insert class");
		}
		
		DBCollection coll = DynamoDBClient.getCollection(collection);
		
    	// Deletes objects based on query
    	DBObject deleteQuery = (DBObject) JSON.parse(query.toString());

    	WriteResult result = coll.remove(deleteQuery);
    	return result.toString();
    	
    	//return "Successfully deleted object";
	}
	
//	// Converts an HTTP parameter mapping to a DBObject
//	private static QueryBuilder getParameterObject(Map<String, String[]> parameterMap) {
//		QueryBuilder parameterObj = new QueryBuilder();
//		
//		Iterator parameterIter = parameterMap.entrySet().iterator();
//		while(parameterIter.hasNext()) {
//			Map.Entry<String, String[]> pair = (Map.Entry<String, String[]>)parameterIter.next();
//			String key = pair.getKey();
//			String[] values = pair.getValue();
//			parameterObj.put(key).in(values);
//		}
//		return parameterObj;
//	}
}
