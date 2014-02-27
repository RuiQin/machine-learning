package regression;

import java.util.*;

public abstract class Training {
	
	protected final double threshold = 0.0001;
	protected final double mLearningRate = 0.0001;
	
	public Training() {
		
	}
	
	/**
	 * 
	 * 
	 * @param instance
	 * @return a weight vector
	 */
	public abstract double[] train(List<Instance> trainingInstances, List<Instance> testingInstances);
} 
