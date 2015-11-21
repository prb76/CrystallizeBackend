package edu.cornell.softwareengineering.crystallize.test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

public class XmlToJsonParser {
	    public static int PRETTY_PRINT_INDENT_FACTOR = 4;
	    public static String TEST_XML_STRING =
	        "<?xml version=\"1.0\" ?><test attrib=\"moretest\">Turn this to JSON</test>";
	    public static String readFile(String path, Charset encoding) throws IOException 
	    {
    		  byte[] encoded = Files.readAllBytes(Paths.get(path));
    		  return new String(encoded, encoding);
	    }
	    public static void writeFile(String path, String data) throws IOException 
	    {
	    	PrintStream ps = new PrintStream(path);
	    	ps.println(data); 
	    }
	    public static void main(String[] args) {
	        try {
	        	/*if(args.length != 2) {
	        		  System.err.println("Invalid command line, exactly one argument required");
	        		  System.exit(1);
	        	}*/
	        	args[0] = "C:\\Users\\Eileen Liu\\Desktop\\JMdict_e.xml";
	        	args[1] = "C:\\Users\\Eileen Liu\\Desktop\\JMdict_e.json";
	        	
	        	JSONObject xmlJSONObj = XML.toJSONObject(readFile(args[0], StandardCharsets.UTF_8));
	        	//JSONObject xmlJSONObj = XML.toJSONObject(TEST_XML_STRING);
	        	String jsonAsString = xmlJSONObj.toString();//xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);
	            writeFile(args[1], jsonAsString);
	            //System.out.println(jsonAsString);
	        } catch (JSONException je) {
	            System.out.println(je.toString());
	        } catch (IOException e){
	        	e.printStackTrace(); 
	        }
	    }
}
