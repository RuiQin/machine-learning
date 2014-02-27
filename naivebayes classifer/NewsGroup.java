package naivebayes;

import java.util.*;
import java.util.Map.Entry;

public class NewsGroup {

	// define the data path
	private final String trainDataPath = "src/naivebayes/data/train.data";
	private final String trainLabelPath = "src/naivebayes/data/train.label";
	private final String vocabularyPath = "src/naivebayes/data/vocabulary.txt";
	private final String testDataPath = "src/naivebayes/data/test.data";
	private final String testLabelPath = "src/naivebayes/data/test.label";
	private final double trainDocSize = 11269.0;
	
	private final String englishstop = "src/naivebayes/data/englishstop.txt";
	private Set<Integer> englishStopPosition = new HashSet<Integer>();
	private int[][] confusion_matrix = new int[20][20];

	/**
	 * 
	 * @param confusionmatrix
	 */
	private static void printConfusionMatrix(int[][] confusionmatrix) {
		for (int i = 0; i < confusionmatrix.length; i++) {
			for (int j = 0; j < confusionmatrix[i].length; j ++) {
				System.out.print(Integer.toString(confusionmatrix[i][j]) + "    " );
			}

			System.out.println("");
		}
	}

	/**
	 * 
	 * @param vocabulary
	 * @param stopword
	 * @return
	 */
	public static Set<Integer> find_stop_words_position(List<String> vocabulary, List<String> stopword) {
		Set<Integer> stop_word_position = new HashSet<Integer>();
		for (int i = 0; i < vocabulary.size(); i++) {
			for (int j = 0; j < stopword.size(); j++) {
				if (vocabulary.get(i).toString().equals(stopword.get(j).toString())) {
					stop_word_position.add((i+1));
				}
			}
		}

		return stop_word_position;
	}

	/** voc */
	public static List<String> voc;

	public Map<String, String> documentClass;  //<docId,classId>
	public Map<String,ArrayList<String>> documentListClass;  // <classId,list<docId>>
	public Map<String, Document> documentsCount;  //<docId,<token,count>>

	public NewsGroup() {
		this(false);
	}

	public NewsGroup(boolean stopword) {
		// read vocabulary
		voc = IOUtils.readFile(vocabularyPath);
		if (stopword) {
			List<String> stopwords = IOUtils.readFile(englishstop);
			englishStopPosition = find_stop_words_position(voc, stopwords);
		}
	}

	/**
	 * read label file and data path in order to form the a list of cluster
	 * @param labelFile
	 */
	private Map<String, Cluster> readFile(String labelPath, String dataPath, String phase) {
		System.out.println("read the " + phase + " file ..... ");

		// read the label file
		System.out.println("Reading label from file " + phase + ".label ");
		List<String> classRecords = IOUtils.readFile(labelPath);
		documentClass = new HashMap<String, String>();
		documentListClass = new HashMap<String,ArrayList<String>>();

		for (int i = 0; i < classRecords.size(); i++) {
			String record = classRecords.get(i);
			documentClass.put((i + 1) + "", record);

			boolean contain = documentListClass.containsKey(record);
			if(!contain){
				documentListClass.put(record, new ArrayList<String>()) ;
			}
			documentListClass.get(record).add((i+1)+"");
		}

		// read the document
		System.out.println("Reading document from file " + phase + ".data ");
		List<String> dataRecords = IOUtils.readFile(dataPath);
		documentsCount = new HashMap<String, Document>();		

		for (String record : dataRecords) {
			String[] elements = record.split(" ");
			String documentID = elements[0];
			String wordID = elements[1];
			int wordCount = Integer.parseInt(elements[2]);

			boolean contain = documentsCount.containsKey(documentID);
			if (!contain) {
				documentsCount.put(documentID, new Document(documentID));
			}

			documentsCount.get(documentID).add(wordID, wordCount);
		}		

		// put the documents with the same cluster ID into the cluster
		System.out.println("put the documents with the same cluster ID into the cluster");
		Map<String, Cluster> clusters = new HashMap<String, Cluster>();
		for (String documentID : documentClass.keySet()) {
			String clusterID = documentClass.get(documentID);

			boolean contain = clusters.containsKey(clusterID);
			if (!contain) {
				clusters.put(clusterID, new Cluster(clusterID));
			}

			// put the document into cluster
			Document document = documentsCount.get(documentID);
			clusters.get(clusterID).addDocument(document);
		}

		// when in training phase, calculate the probability and frequence
		if (phase.equals("training")) {
			System.out.println("calculate the training probability");

			// generate the word count of cluster
			for (int index = 1; index <= 20; index++) {
				System.out.println("calculate the cluster "+ index);
				// fill the word count
				// clusters.get(clusterID).fillWordCounts();

				// calculate the bernoulli probability
				clusters.get(Integer.toString(index)).calculateBernoulliProbability();

			}

		}

		return clusters;
	}


	/** main entry point for the whole experiment */
	public void trainBernoulli() {		
		Map<String, Cluster> trainingDataSet = readFile(trainLabelPath, trainDataPath, "training");
		Map<String, Cluster> testingDataSet = readFile(testLabelPath, testDataPath, "testing");

		// do testing, if the predicted class and the gold class is not equal, increment one
		double error = 0;

		double documentSize = 0;

		System.out.println("evaluate the performance");
		for (int testKey = 1; testKey <= 20; testKey++) {
			Cluster testingCluster = testingDataSet.get(Integer.toString(testKey));
			System.out.println("Cluster: "+testingCluster.toString());

			for (Document document : testingCluster.getDocuments()) {
				documentSize += 1; 
				List<Double> classprobability = new ArrayList<Double>();
				Map<String, Integer> testDocumentWordCounts = document.getWordCount();

				for (int classindex = 1; classindex <= 20; classindex++) {
					// used for calculating the probability
					Cluster trainCluster = trainingDataSet.get(Integer.toString(classindex));
					// System.out.println(classindex + " " + trainCluster.mClassId);
					Map<String, Double> bernoulliProbability = trainCluster.bernoulliProbabilities;
					double probability = Math.log(trainCluster.getDocumentSize()/trainDocSize);
					for(int index = 1; index <= voc.size(); index++ ){

						// judge whether this word is stop word 
						boolean isStopwords = englishStopPosition.contains(index);
						if (isStopwords) continue;

						boolean contain = testDocumentWordCounts.containsKey(Integer.toString(index));						
						if (contain) {
							probability += Math.log(bernoulliProbability.get(Integer.toString(index)));
						} else {
							probability += Math.log(1 - bernoulliProbability.get(Integer.toString(index)));
						}

					}

					// put the probability into the map for the specific class
					classprobability.add(probability);
				}
				Object obj = Collections.max(classprobability);
				// System.out.println(classprobability);
				String predicte_class1 = obj.toString();
				// System.out.println(predicte_class1);
				// System.out.println(Double.parseDouble(predicte_class1));
				int index = classprobability.indexOf(Double.parseDouble(predicte_class1));
				// System.out.println(index);
				String predicte_class = Integer.toString(index + 1);


				confusion_matrix[testKey - 1][index] += 1;

				// System.out.println(document.docId + ": G, " + testKey + "; P: " + predicte_class);
				if (!predicte_class.equals(Integer.toString(testKey))) {
					error += 1;
				}

			}

			System.out.println("the error is " + error + "; the document size : " + documentSize);
		}

		System.out.println("the error is " + error + "; the document size : " + documentSize + " precision rate : " + (1 - error/documentSize));

		// print confusion matrix
		printConfusionMatrix(confusion_matrix);
	}

	private String getArgMaxKey(Map<String, Double> probabilities) {
		double maxValueInMap=(Collections.max(probabilities.values()));  // This will return max value in the Hashmap
		for (Entry<String, Double> entry : probabilities.entrySet()) {  // Itrate through hashmap
			if (entry.getValue()==maxValueInMap) {
				return entry.getKey();     // Print the key with max value
			}
		}

		return null;
	}

	public static void main(String[] args) {
		System.out.println("java NewsGroup true");
		
		// TODO
		boolean stopword = true;
		if (args.length > 0) {
			stopword = true;
		}
		
		NewsGroup resover = new NewsGroup(stopword);
		System.out.println("Training Bernoulli model");
		resover.trainBernoulli();
	}


}
