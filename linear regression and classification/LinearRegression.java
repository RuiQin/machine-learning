package regression;

import java.util.*;
import java.io.*;

/**
 * There are several steps of the experiment
 * 1. read the data according to the file path
 * 2. transform the data to a list of Instance
 * 3. use the specified algorithm to process the Instance
 * 4. output the result to a text file which is used for plotting
 * 
 * @author rui
 *
 */
public class LinearRegression {

	/** file path */
	private final String mTrainingFilePath;
	
	private final String mTestingFilePath;
	
	/** training method */
//	private final String[] trainingMethods = new String[] {"BatchTraining"};
	private final String[] trainingMethods = new String[] {"StochasticTraining", "BatchTraining"};
	
	public LinearRegression(String trainingFilePath, String testingFilePath) {
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
			double label = Double.parseDouble(elements[length - 1]);
			double[] feature = new double[length];
			feature[0] = 1.0;
			for (int index = 0; index < (length - 1); index++) {
				feature[index + 1] = Double.parseDouble(elements[index]);
			}
			
			Instance instance = new Instance(feature, label);
			instances.add(instance);
		}
		
		return instances;
	}
	
	
	public void doRegression() {
		
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
			String fweight = IOUtils.ArraytoString(finalweight);
			System.out.println("Weight is "+ fweight);
			double SSE = evaluator.calculateSSE(finalweight);
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
			Class trainingClass = Class.forName("Program1." + trainingMethod);
			Training trainer = (Training) trainingClass.newInstance();
			return trainer;
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}

	public static void main(String[] args){
		String trainingfilePath = "src/Program1/regression-train.csv";
		String testingfilePath = "src/Program1/regression-test.csv";
		LinearRegression lrSolver = new LinearRegression(trainingfilePath, testingfilePath);
		lrSolver.doRegression();
	}
}
