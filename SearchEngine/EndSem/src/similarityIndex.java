
public class similarityIndex {
	public static double similarityIndex(String str, String str2) {
	    String lng = str, diff = str2;
	    if (str.length() < str2.length()) {
	      lng = str2; diff = str;
	    }
	    int longerLength = lng.length();
	    if (longerLength == 0) { return 1.0;  }
	    
	    return (longerLength - similarityValue.similarityValue(lng, diff)) / (double) longerLength;

	  }
	
}
