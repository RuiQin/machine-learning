package naivebayes;

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
	public static List<String> readFile(String filePath) {                
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
	

}
