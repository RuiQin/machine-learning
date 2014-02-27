package regression;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * used for read data from the specified file path
 * 
 * @author rui
 *
 */
public class IOUtils {

	// non-instantiables class
	// just call the static method
	private IOUtils() {
	}
	
	/**
	 * read lines from the file
	 * 
	 * @param filePath
	 * @return
	 */
	public static List<String> linesFromFile(String filePath) {                
		List<String> records = new ArrayList<String>();                
		try {                        
			BufferedReader reader = new BufferedReader(new FileReader(filePath));                        
			String currentLine;                        
			while ((currentLine = reader.readLine()) != null) {                                
				records.add(currentLine);                        
			}                        
			reader.close();                
		} catch (Exception e) {                        
			e.printStackTrace();
		}                
		return records;        
	}
	
	/**
	 * write string to file
	 * @param fileName
	 * @param s
	 */
	public static void writeToFile(String fileName, String s){
		try{
			BufferedWriter out = new BufferedWriter(new FileWriter (fileName,true));
			out.write(s);
			out.newLine();
			out.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public static String ArraytoString(double[] weight){
		StringBuilder string = new StringBuilder();
		for(int i = 0; i < weight.length; i++){
			string.append(weight[i]+" ");			
		}
		return string.toString().trim();
	}
	
	public static String toStringFromI(double[] weight, int i){
		StringBuilder string = new StringBuilder();
		for(int j = i; j < weight.length; j++){
			string.append(weight[j]+" ");			
		}
		return string.toString().trim();
	}
}
