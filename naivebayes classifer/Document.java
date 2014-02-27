package naivebayes;

/**
 * define document class, it stores every word in this document and its word count 
 */

import java.util.*;

public class Document {
	public String docId;
	private Map<String, Integer> wordCount;
	
	public Document(){
		wordCount = new HashMap<String, Integer>();
	}
	
	public Document(String id){
		docId = id;
		wordCount = new HashMap<String, Integer>();
	}
	
	public Map<String, Integer> getWordCount() {
		return wordCount;
	}
	
	public String toString() {
		return docId;
	}
	
	/**
	 * add a record of (wordId,count) to document
	 * @param wordId
	 * @param count
	 */
	public void add(String wordId, int count){
		wordCount.put(wordId, count);
	}

	/**
	 * find the count of a word given the word's id
	 * @param id
	 * @return
	 */
	public int findCount(String id){
		boolean contain = wordCount.containsKey(id);
		
		int count = 0;
		if (contain) {
			count = wordCount.get(id);
		}
		
		return count;
	}
	
}
