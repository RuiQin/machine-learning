package naivebayes;

import java.util.*;

public class Cluster {
	
	public String mClassId;
	
	/** document contained in this class */
	private List<Document> mDocuments;
	
	/** word count */
	private Map<String, Double> wordCounts;
	
	/** probability*/
	public Map<String,Double> bernoulliProbabilities;
	
	/** probability*/
	private Map<String,Double> multinominalProbabilities;
	
	/** vocabulary */
	private final List<String> voca;
	
	public Cluster(String classId){
		voca = NewsGroup.voc;
		mClassId = classId;
		mDocuments = new ArrayList<Document>();
		wordCounts = new HashMap<String, Double>();
		bernoulliProbabilities = new HashMap<String, Double>();
		multinominalProbabilities = new HashMap<String, Double>();
	}
	
	/** add document */
	public void addDocument(Document document) {
		mDocuments.add(document);
	}
	
	/** return the documents */
	public List<Document> getDocuments() {
		return mDocuments;
	}
	
	public String toString() {
		return mClassId;
	}
	
	/** calculate the word counts */
//	public void fillWordCounts() {
//		
//		for (Document document : mDocuments) {
//			Map<String, Integer> documentWordCount = document.getWordCount();
//			
//			for (String token : documentWordCount.keySet()) {
//				int count = documentWordCount.get(token);
//				
//				boolean contain = wordCounts.containsKey(token);
//				double wordCount = 0;
//				if (contain) {
//					wordCount = wordCounts.get(token);
//				}
//				
//				wordCounts.put(token, (wordCount + count));
//			}
//		}
//		
//	}
	
	public int getDocumentSize() {
		return mDocuments.size();
	}
	
	/** 
	 * calculate the probability 
	 * 
	 * before do testing, call this function
	 */
	public void calculateBernoulliProbability() {
		double denominator = mDocuments.size() + 20;
		
		for (int token = 1; token <= voca.size(); token++) {

			double numerator = 1.0;
			// calculate the probability
			for (Document document : mDocuments){
				boolean contain = document.getWordCount().containsKey(Integer.toString(token));
				if (contain) {
					numerator += 1;
				}
			}
			
			bernoulliProbabilities.put(Integer.toString(token), (numerator / denominator));
			
		}
	}
	
	/**
	 * calculate the multinomail probability
	 *
	 */
	//TODO
	// update the token id
	public void calculateMultinomialProbability() {
		double denominator = voca.size();
		for (String token : wordCounts.keySet()) {
			denominator += wordCounts.get(token);
		}

		for (int token = 1; token <= voca.size(); token++) {

			double numerator = 1.0;
			// calculate the probability
			boolean contain = wordCounts.containsKey(Integer.toString(token));
			if (contain) {
				numerator += wordCounts.get(token);
			}
			multinominalProbabilities.put(Integer.toString(token), (numerator / denominator));

		}

	}
}
