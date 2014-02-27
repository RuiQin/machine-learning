package regression;

/**
 * Representation of the instance read from the dataset
 * Hence, the dataset is just a list of Instance
 * List<@Instace> dataset = ArrayList<@Instace>();
 * 
 * @author rui
 *
 */
public class Instance {

	/** features, for regression, add 1 in front of every feature as a bias feature */
	private final double[] mFeatures;
	
	/** target or label, in regression, it is a numeric value, in classification, it is just a integer */
	private final double mLabel;
	
	public Instance(double[] features, double label) {
		mFeatures = features;
		mLabel = label;
	}
	
	public double[] getFeatures() {
		return mFeatures;
	}
	
	public double getLabel() {
		return mLabel;
	}
	
	/**
	 * 
	 * @return a string representation of the instance, concatenation of features and label
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		// feature first
		for (double value : mFeatures) {
			sb.append(value + " ");
		}
		sb.append("=====> ");
		// label second, features and label are separated by =====>
		sb.append(mLabel);
		
		return sb.toString().trim();   // trim all the whitespace before the string or after the string
	}
}
