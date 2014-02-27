package regression;

import java.util.*;

/**
 * calculate the SSE result
 * 
 * @author rui
 *
 */
public class Evaluation {

	/**
	 * 
	 */
	private final List<Instance> mInstances;
	
	public Evaluation(List<Instance> instances) {
		mInstances = instances;
	}
	
	/**
	 * 
	 * @param weight
	 * @return
	 */
	public double calculateSSE(double[] weight) {
		double sse = 0;
		for(Instance instance : mInstances){
			double[] feature = instance.getFeatures();
			double u = DoubleOperation.multiply(feature, weight);
			double label = instance.getLabel();
			sse += (label - u) * (label - u);		
		}
		return sse / 2;
	}
	
}
