package regression;

import java.util.*;

/**
 * This is to implement voted perceptron algorithm
 * @author rui
 *
 */
public class VotedPerceptron extends Training{
	
	public VotedPerceptron(){
		System.out.println("Using voted perceptron learning...");
	}

	public double[] train(List<Instance> trainingInstances, List<Instance> testingInstances) {
		int dimension = trainingInstances.get(0).getFeatures().length;
		int trainSize= trainingInstances.size();
		int testSize = testingInstances.size();
		
		List<double[]> weights = new ArrayList<double[]>();
		List<Integer> c = new ArrayList<Integer>(); // c is w's survival time
		int epoches = 0;
		int n = 0;  // No. of update
		double[] weight = new double[dimension];
		int counter = 0;
		weights.add(weight);
		c.add(0);
		
		while(epoches < 100){
			int errorNum = 0;
			Collections.shuffle(trainingInstances);
			
			for(int m = 0; m < trainSize; m++){
				Instance instance = trainingInstances.get(m);
				double[] currentWeight = weights.get(n);
				double[] feature = instance.getFeatures();
				double u = DoubleOperation.multiply(feature, currentWeight);
				double label = instance.getLabel();
				
				if((label*u) <= 0){
					double[] change = DoubleOperation.multiply(feature, label);
					double[] newWeight = DoubleOperation.plus(currentWeight, change);
					weights.add(newWeight);
					n++;
					c.add(counter);
					counter = 1;
				} else { 
					counter += 1;
				}				
			}
			
			for(int i = 0; i < testSize; i++){
				
				Instance instance = testingInstances.get(i);
				double[] feature = instance.getFeatures();
				double label = instance.getLabel();
				double value = 0.0;
				for(int j = 0; j < weights.size(); j++){
					assert weights.size() == c.size();
					double wx = DoubleOperation.multiply(feature, weights.get(j));
					value += c.get(j)*sign(wx);
					
				}
				
				double predicateLabel = sign(value);
				if (predicateLabel != label) {
					errorNum += 1;
				}			
			}
			epoches++;
			System.out.println("Epoch: "+epoches+" Error's count is " + errorNum);
		}		
		
		for(int i = 0; i < testSize; i++){
			Instance instance = trainingInstances.get(i);
			double[] feature = instance.getFeatures();
			double label = instance.getLabel();
			double wx = DoubleOperation.multiply(feature, weights.get(n));
			double predictLabel = sign(wx);
			String features = IOUtils.toStringFromI(feature, 1);
			String testInfo = predictLabel + " " + features;
//			IOUtils.writeToFile("Voted_test.txt", testInfo);
		}
		return weights.get(n);
	}
	
	public static double sign(double x){
		if(x >= 0) return 1;
		else return -1;
	}
}
