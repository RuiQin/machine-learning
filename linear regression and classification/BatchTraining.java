package regression;

import java.util.List;


public class BatchTraining extends Training {

	public BatchTraining() {
		System.out.println("use batch training.........");
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
			weight[i] = 0;
		}
		
		boolean continueLoop = true;
		int iteration = 1;
		
		while(continueLoop){
			
			double[] gradient = new double[dimension];
			for(int index = 0; index < trainingInstances.size(); index++) {
				Instance instance = trainingInstances.get(index);
				double[] feature = instance.getFeatures();
				double u = DoubleOperation.multiply(feature, weight);
				double label = instance.getLabel();
				double[] g = DoubleOperation.multiply(feature, (u - label));
				gradient = DoubleOperation.plus(gradient,g);
			}
			double[] secondTerm = DoubleOperation.multiply(gradient, mLearningRate);
			weight = DoubleOperation.minus(weight, secondTerm);
			iteration++;
			
			double gradientNorm = DoubleOperation.norm(gradient);
			if (gradientNorm < threshold) {
				continueLoop = false;
				break;
			}
			double trainSSE = trainEvalutor.calculateSSE(weight);
			double testSSE = testEvalutor.calculateSSE(weight);
			String trainInfo = iteration + " " + trainSSE;
			String testInfo = iteration + " " + testSSE;
//			IOUtils.writeToFile("Batch_train.txt", trainInfo);
//			IOUtils.writeToFile("Batch_test.txt", testInfo);
			System.out.println("the " + iteration +"' iteration" + " training SSE is " + trainSSE);
			System.out.println("the " + iteration +"' iteration" + " test SSE is " + testSSE);

		}
		return weight;
	}
}
