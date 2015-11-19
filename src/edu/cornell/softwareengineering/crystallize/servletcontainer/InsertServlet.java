package edu.cornell.softwareengineering.crystallize.servletcontainer;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.cornell.softwareengineering.crystallize.util.Insert;
import edu.cornell.softwareengineering.crystallize.util.common.DynamoDBClient;
import edu.cornell.softwareengineering.crystallize.util.common.ParameterParser;

/**
 * Servlet implementation class Insert
 */
public class InsertServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public InsertServlet() {
    	super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		JSONObject parameters;
		try {
			parameters = ParameterParser.getParameterObject(request);
			out.append(parameters.toString() + "\n");

			JSONObject refinedParams = refineParameters(parameters);
			out.append(refinedParams.toString() + "\n");
			
			//DynamoDBClient.addTable(refinedParams.getString("table"));
			
			String result = Insert.insert(refinedParams);	
			out.append(result);
			
		} catch (Exception e) {
			out.append(e.getMessage());
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private JSONObject refineParameters(JSONObject parameters) throws Exception {
		JSONObject refined = new JSONObject();
		if(parameters.length() == 0) throw new Exception("No parameters found");
		
		// check table parameter
		if(parameters.has("table")) {
			JSONArray tableArray = parameters.getJSONArray("table");
			if(tableArray.length() == 1) {
				refined.put("table", tableArray.getString(0));
			}
			else if(tableArray.length() > 1) { throw new Exception("Parameters 'table' has more than one value"); }
			else { throw new Exception("Parameters 'table' is empty"); }
		}
		else { throw new Exception("Parameter 'table' missing"); }
		
		// check ID parameter
		if(parameters.has("ID")) {
			JSONArray IDArray = parameters.getJSONArray("ID");
			if(IDArray.length() == 1) {
				refined.put("ID", IDArray.getString(0));
			}
			else if(IDArray.length() > 1) { throw new Exception("Parameters 'ID' has more than one value"); }
			else { throw new Exception("Parameters 'ID' is empty"); }
		}
		else { throw new Exception("Parameter 'ID' missing"); }
		
		// check document parameter
		if(parameters.has("document")) {
			JSONArray documentArray = parameters.getJSONArray("document");
			if(documentArray.length() == 1) {
				refined.put("document", new JSONObject(documentArray.getString(0)));
			}
			else if(documentArray.length() > 1) { throw new Exception("Parameters 'ID' has more than one value"); }
			else { throw new Exception("Parameters 'document' is empty"); }		}
		else { throw new Exception("Parameter 'document' missing"); }
		
		return refined;
	}

}
