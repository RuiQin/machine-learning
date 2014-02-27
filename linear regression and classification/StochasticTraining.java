package regression;

import java.util.Collections;
import java.util.List;

public class StochasticTraining extends Training {
	
	public StochasticTraining() {
		System.out.println("use Stochastic learning");
	}
	
	/**
	 * implement the train method of Training
	 */
	public double[] train(List<Instance> trainingInstances, List<Instance> testingInstances) {
		int dimension = trainingInstances.get(0).getFeatures().length;
		Evaluation trainEvalutor = new Evaluation(trainingInstances);
		Evaluation testEvalutor = new Evaluation(testingInstances);
		
		double[] weight = new double[dimension];
		for(int i = 0; i < dimension; i++){
			weight[i] = 9;
		}
		
		boolean continueLoop = true;
		int iteration = 1;
		int n = trainingInstances.size();
		
		while (continueLoop) {
			// shuffle the dataset
			Collections.shuffle(trainingInstances);
			
			for (int index = 0; index < trainingInstances.size(); index++) {
				Instance instance = trainingInstances.get(index);
				double[] feature = instance.getFeatures();
				double u = DoubleOperation.multiply(feature, weight);
				double label = instance.getLabel();
				double[] gradient = DoubleOperation.multiply(feature, (u - label));
				
				double gradientNorm = DoubleOperation.norm(gradient);
				
				
				double[] secondTerm = DoubleOperation.multiply(gradient, mLearningRate);
				weight = DoubleOperation.minus(weight, secondTerm);
				
				iteration += 1;
				
				// calculate the SSE
				
				double trainSSE = trainEvalutor.calculateSSE(weight);
				double testSSE = testEvalutor.calculateSSE(weight);
				String trainInfo = iteration + " " + trainSSE;
				String testInfo = iteration + " " + testSSE;
//				IOUtils.writeToFile("Stochastic_train.txt", trainInfo);
//				IOUtils.writeToFile("Stochastic_test.txt", testInfo);
				System.out.println("the " + iteration +"' iteration" + " training SSE is " + trainSSE);
				System.out.println("the " + iteration +"' iteration" + " test SSE is " + testSSE);
				
				if (gradientNorm < threshold) {
					continueLoop = false;
					break;
				}
			}
		}
		
		return weight;
	}

}
