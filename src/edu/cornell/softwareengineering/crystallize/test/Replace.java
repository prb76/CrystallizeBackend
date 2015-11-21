package edu.cornell.softwareengineering.crystallize.test;

import java.io.*;

public class Replace
{
	static String[] toReplace = new String[]{"&adj",
    		"&adv",
    		"&on",
    		"&aux",
    		"&n",
    		"&v5r",
    		"&vs",
    		"&m",
    		"&v5k",
    		"&v2m",
    		"&v1",
    		"&v2r",
    		"&v5u",
    		"&v2t",
    		"&v2h",
    		"&cop",
    		"&v2a",
    		"&v2y",
    		"&v2d",
    		"&v2k",
    		"&v2g",
    		"&v2b",
    		"&v2s",
    		"&v2z",
    		"&v2n",
    		"&v2w",
};
 public static void main(String args[])
     {
     try
         {
         File file = new File("C:\\Users\\Eileen Liu\\Desktop\\JMdict_e.json");
         BufferedReader reader = new BufferedReader(new FileReader(file));
         String line = "", oldtext = "";
         while((line = reader.readLine()) != null)
             {
             oldtext += line + "\r\n";
         }
         reader.close();
         // replace a word in a file
         //String newtext = oldtext.replaceAll("drink", "Love");
        
         //To replace a line in a file
         //String newtext = oldtext.replaceAll("This is test string 20000", "blah blah blah");
         String newtext = oldtext;
     	 for(int i = 0; i<toReplace.length; i++){
            if (newtext.contains(toReplace[i])){
                newtext = newtext.replace(toReplace[i], toReplace[i]+"-");
                System.out.print("replaced");
            }
            if (newtext.contains(toReplace[i]+"-;"))
                newtext = newtext.replace(toReplace[i]+"-;", toReplace[i]+";");
    	 }
         
         FileWriter writer = new FileWriter("file.txt");
         writer.write(newtext);
         writer.close();
     }
     catch (IOException ioe)
         {
         ioe.printStackTrace();
     }
 }
}