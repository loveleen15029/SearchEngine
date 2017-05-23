
public class similarityValue {
 
	public static int similarityValue(String s1, String s2) {
	    s1 = s1.toLowerCase();
	    s2 = s2.toLowerCase();
	    int[] amt = new int[s2.length() + 1];
	    for (int i = 0; i <= s1.length(); i++) {
	      int lastValue = i;
	      for (int j = 0; j <= s2.length(); j++) {
	        if (i == 0)
	          amt[j] = j;
	        else {
	          if (j > 0) {
	            int newValue = amt[j - 1];
	            if (s1.charAt(i - 1) != s2.charAt(j - 1))
	              newValue = Math.min(Math.min(newValue, lastValue),
	                  amt[j]) + 1;
	            amt[j - 1] = lastValue;
	            lastValue = newValue;
	          }
	        }
	      }
	      if (i > 0)
	        amt[s2.length()] = lastValue;
	    }
	    return amt[s2.length()];
	  }
	
}
