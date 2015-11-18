package edu.cornell.softwareengineering.crystallize.util;

import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import edu.cornell.softwareengineering.crystallize.util.common.DynamoDBClient;

public class Query {
	public static String query(JSONObject parameters) throws Exception {
		String collection;
		JSONObject query;
		JSONObject filters;
		
		try {
			collection = parameters.getString("collection");
			query = parameters.getJSONObject("query");
			filters = parameters.getJSONObject("filters");
		} catch (JSONException e) {
			throw new Exception("Parameter error inside Insert class");
		}
		
		DBCollection coll = DynamoDBClient.getCollection(collection);
		
		// Query
    	DBObject queryObj = (DBObject) JSON.parse(query.toString());
    	DBObject filtersObj = (DBObject) JSON.parse(filters.toString());
    	DBCursor cursor = coll.find(queryObj, filtersObj);
    	String result;
    	if (cursor.length() > 0) {
    		result = cursor.toArray().toString();
    	}
    	else {
    		result = "No results";
    	}
		return result;
	}
	
//	// For testing purposes
//	public static String query(String collection) {
//		Map<String, String[]> queryTerms = new HashMap<String, String[]>();
//		String[] values = {"prateek"};
//		queryTerms.put("name", values);
//		
//		return queryTerms(collection, queryTerms);
//	}
	
//	private static DBObject getFilters(JSONArray filters) {
//		DBObject filterObj = new BasicDBObject();
//		
//		for(int i = 0; i < filters.length(); i++) {
//			try {
//				filterObj.put((String) filters.get(i), new Integer(1));
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				return null;
//			}
//		}
//		
//		return filterObj;
//	}
//	
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
