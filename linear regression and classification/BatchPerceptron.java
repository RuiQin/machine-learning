package regression;

import java.util.List;

/*
 * This is to implement batch perceptron algorithm
 */

public class BatchPerceptron extends Training{
	
	public BatchPerceptron(){
		System.out.println("Using batch perceptron learning...");
	}

	public double[] train(List<Instance> trainingInstances, List<Instance> testingInstances) {
		int dimension = trainingInstances.get(0).getFeatures().length;
		int trainSize= trainingInstances.size();
		int testSize = testingInstances.size();
		
		double[] weight = new double[dimension];
		boolean continueLoop = true;
		int iteration = 0;
		
		while(continueLoop){
			int errorNum = 0;
			double[] delta = new double [dimension];
			for(int m = 0; m < trainSize; m++){
				Instance instance = trainingInstances.get(m);
				double[] feature = instance.getFeatures();
				double label = instance.getLabel();
				double u = DoubleOperation.multiply(feature, weight);
				double[] secondTerm = DoubleOperation.multiply(feature, label);
				if((label * u) <= 0)
					delta = DoubleOperation.minus(delta, secondTerm);
			}
			delta = DoubleOperation.divide(delta, trainSize);
			double[] addLearningRate = DoubleOperation.multiply(delta, 1);
			weight = DoubleOperation.minus(weight, addLearningRate);
			iteration++;
			
			double deltaNorm = DoubleOperation.norm(delta);
			if(deltaNorm < threshold)
				continueLoop = false;
			
			for(int i = 0; i < testSize; i++){
				Instance instance = trainingInstances.get(i);
				double[] feature = instance.getFeatures();
				double label = instance.getLabel();
				double wx = DoubleOperation.multiply(feature, weight);
				double predictLabel = sign(wx);
				if(predictLabel != label) 
					errorNum++;
			}
			System.out.println("iteration: "+iteration+" Error's count is " + errorNum);
			
		}
		
		for(int i = 0; i < testSize; i++){
			Instance instance = trainingInstances.get(i);
			double[] feature = instance.getFeatures();
			double label = instance.getLabel();
			double wx = DoubleOperation.multiply(feature, weight);
			double predictLabel = sign(wx);

			String features = IOUtils.toStringFromI(feature, 1);
			String testInfo = predictLabel + " " + features;
//			IOUtils.writeToFile("Batch_perceptron_test.txt", testInfo);

		}
		
		return weight;
	}
	
	public static double sign(double x){
		if(x < 0) return -1;
		else if(x == 0) return 0;
		else return 1;
	}
}
