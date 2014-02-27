package regression;

public class DoubleOperation {

	private DoubleOperation() {
		
	}
	
	/**
	 * the value of w^{T} * x
	 * 
	 * @param feature
	 * @param weight
	 * @return
	 */
	public static double multiply(double[] feature, double[] weight) {
		double sum = 0.0;
		assert feature.length == weight.length;
		
		for (int index = 0; index < feature.length; index++) {
			sum += feature[index] * weight[index];
		}
		
		return sum;
	}
	
	public static double[] multiply(double[] feature, double constant) {
		int len = feature.length;
		double[] result = new double[len];
		for (int i = 0; i < len; i++) {
			result[i] = feature[i] * constant; 
		}
		return result;
	}
	
	public static double[] minus(double[] weight, double[] secondTerm) {
		assert weight.length == secondTerm.length;
		double[] result = new double[weight.length];
		for(int i = 0; i < weight.length; i++ ){
			result[i] = weight[i] - secondTerm[i];
		}
		return result;
	}
	
	public static double[] plus(double[] g1, double[] g2){
		assert g1.length == g2.length;
		double[] result = new double[g1.length];
		for(int i = 0; i < g1.length; i++){
			result[i] = g1[i] + g2[i];
		}
		return result;
	}
	
	public static double[] divide(double[] weight, double constant){
		int len = weight.length;
		double[] result = new double[len];
		for (int i = 0; i < len; i++) {
			result[i] = weight[i] / constant; 
		}
		return result;
	}
	/**
	 * two norm of a vector
	 * 
	 * @param weight
	 * @return
	 */
	public static double norm(double[] weight) {
		double sum = 0.0;
		for (double value : weight) {
			sum += value * value;
		}
		
		return Math.sqrt(sum);
	}
	
}
