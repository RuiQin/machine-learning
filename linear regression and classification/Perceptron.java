package regression;

import java.util.ArrayList;
import java.util.List;

/**
 * This file implement two perceptron algorithms:
 * batch perceptron and voted perceptron
 * Perceptron is uasful in classifying filed
 * especially for discrete data
 * @author rui
 *
 */
public class Perceptron {
	/** file path */
	private final String mTrainingFilePath;
	
	private final String mTestingFilePath;
	
	/** training method */
	private final String[] trainingMethods = new String[] {"VotedPerceptron"}; //VotedPerceptron,BatchPerceptron
	
	public Perceptron(String trainingFilePath, String testingFilePath) {
		mTrainingFilePath = trainingFilePath;
		mTestingFilePath = testingFilePath;
	}
	
	/**
	 * generate the dataset
	 * 
	 * @param filePath
	 * @return
	 */
	private List<Instance> generateDataSet(String filePath) {
		//
		// 1. read the data according to the file path
		//
		List<String> records = IOUtils.linesFromFile(filePath);

		//
		// 2. transform the data to a list of Instance
		//
		
		// convert records to instances
		// assume that the last index is the label
		List<Instance> instances = new ArrayList<Instance>();
		for (String record : records) {
			String[] elements = record.split(",");
			int length = elements.length;
			double label = Double.parseDouble(elements[0]);
			double[] feature = new double[length];
			feature[0] = 1.0;
			for (int index = 1; index < length ; index++) {
				feature[index] = Double.parseDouble(elements[index]);
			}
			
			Instance instance = new Instance(feature, label);
			instances.add(instance);
		}
		
		return instances;
	}
	
	public void doClassification(){
		List<Instance> trainingInstances = generateDataSet(mTrainingFilePath);
		List<Instance> testingInstances = generateDataSet(mTestingFilePath);
		
		//
		// use defined training method to do training
		//
		// 
		// 3. use the specified algorithm to process the Instance
		//
		Evaluation evaluator = new Evaluation(testingInstances);
		for (String trainingMethod : trainingMethods) {
			Training trainer = createTrainingMethod(trainingMethod);
			double[] finalweight = trainer.train(trainingInstances,testingInstances);
			System.out.println(IOUtils.ArraytoString(finalweight));
		}
	}
	
	/**
	 * create training method
	 * 
	 * @param trainingMethod
	 * @return
	 */
	private Training createTrainingMethod(String trainingMethod) {
		if (trainingMethod == null) {
			throw new RuntimeException("training method not specified");
		}
		
		try {
			Class trainingClass = Class.forName("regression." + trainingMethod);
			Training trainer = (Training) trainingClass.newInstance();
			return trainer;
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}

	public static void main(String[] args){
		String trainingfilePath = "src/Program1/iris-twoclass.csv"; //dataset twogaussian.csv/iris-twoclass.csv
		String testingfilePath = "src/Program1/iris-twoclass.csv";
		Perceptron perceptronSolver = new Perceptron(trainingfilePath, testingfilePath);
		perceptronSolver.doClassification();
	}
}
