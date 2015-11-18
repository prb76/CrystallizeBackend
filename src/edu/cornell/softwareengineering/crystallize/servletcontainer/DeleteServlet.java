package edu.cornell.softwareengineering.crystallize.servletcontainer;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.cornell.softwareengineering.crystallize.util.Delete;
import edu.cornell.softwareengineering.crystallize.util.common.ParameterParser;

/**
 * Servlet implementation class Delete
 */
public class DeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public DeleteServlet() {
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
			
			String result = Delete.delete(refinedParams);	
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
		
		// check collection parameter
		if(parameters.has("collection")) {
			JSONArray collectionArray = parameters.getJSONArray("collection");
			if(collectionArray.length() == 1) {
				refined.put("collection", collectionArray.get(0));
			}
			else if(collectionArray.length() > 1) { throw new Exception("Parameters 'collection' has more than one value"); }
			else { throw new Exception("Parameters 'collection' is empty"); }
		}
		else { throw new Exception("Parameter 'collection' missing"); }
		
		// check query parameter
		if(parameters.has("query")) {
			JSONArray queryArray = parameters.getJSONArray("query");
			if(queryArray.length() == 1) {
				refined.put("query", new JSONObject(queryArray.getString(0)));
			}
			else if (queryArray.length() > 1) {
				throw new Exception("Parameter 'query' has too many values");
			}
			else { throw new Exception("Parameter 'query' is empty"); }		}
		else { throw new Exception("Parameter 'query' missing"); }
		
		return refined;
	}
	
}
