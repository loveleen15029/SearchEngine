import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.StringTokenizer;
public class Invertedindex {
	static HashMap<String,HashMap<String,Integer>> invertedindex=new HashMap<>();
	static HashSet<String> set=new HashSet<>();
	static HashSet<String> set1=new HashSet<>();
	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		Scanner scanner=new Scanner(System.in);
		
		ArrayList<String> arrayList=new ArrayList<>();
		ArrayList<String> result=new ArrayList<>();
		HashMap<String,String> solution=new HashMap<>();
		int tf=0,idf=1,count=0,total_docs=3;
		String res="";
		int k=1;
		while(k<=total_docs){
			String docid="docid"+""+k+".txt";
			BufferedReader reader = new BufferedReader(new FileReader(docid));	
			try {
				String l = reader.readLine();
				while (l != null) {
					StringTokenizer tokenId = function3(l);
					createIndex(tokenId,docid);
					l = reader.readLine();
				}
		}catch (Exception e) {
		}
			k++;
		}
		String queryString=scanner.nextLine();
		StringTokenizer tokenId =  function3(queryString.toLowerCase());
		while (tokenId.hasMoreTokens()) {  
			String element=tokenId.nextToken();
			set1.add(element);
			if(invertedindex.containsKey(element))
			{
				for (String setKey : invertedindex.get(element).keySet()) {
					set.add(setKey);
					}
			}
		}
		scanner.close();
		double mat[][]=new double[set1.size()][set.size()];
		StringTokenizer tokenId1=  function3(queryString.toLowerCase());
		int i=0,j=0;
		createMatrix(tokenId1, mat, i, j, total_docs);
        double maxArray[]=new double[set.size()];
        arrayList.addAll(set);
		for(i=0;i<set.size();i++)
		{
			for(j=0;j<set1.size();j++)
			{
				maxArray[i]+=mat[j][i];
			}
		}
		for(i=0;i<arrayList.size();i++)
		{
			double maxEleement=-2;
			int pos=0;
			for(j=0;j<arrayList.size();j++)
			{
				if(maxEleement<=maxArray[j])
				{
                  maxEleement=maxArray[j];
                  pos=j;
				}
			}
			System.out.println(i+1+" "+arrayList.get(pos));
			maxArray[pos]=-1;
			maxEleement=-2;
		}
		StringTokenizer tokens2=  function3(queryString.toLowerCase());
		while (tokens2.hasMoreTokens()) {  
		  String element=tokens2.nextToken();
		  double maxElement=0;
		  for(String value: invertedindex.keySet() )
	    	{
			  if(maxElement<=function1(element, value))
			  {
				  maxElement=function1(element, value);
				  res=value;
			  }
		    }
		  if(maxElement!=1.0)
		  {
		  solution.put(element,res);
		  }
		  result.add(res);
	}
	if(solution.size()!=0)
	{
		System.out.println("--------------------------------------------------------------------------------------");
		System.out.println("Suggestions:");
		for(String s:result)
		{
			System.out.print(s+" ");
		}
		System.out.println(" ");
		System.out.println("--------------------------------------------------------------------------------------");
	}
	for(String s: solution.keySet())
	{
		System.out.println(s+" ----->"+solution.get(s));
	}
}
	public static double function1(String val1, String val2) {
	    String one = val1, two = val2;
	    if (val1.length() < val2.length()) {
	      one = val2; two = val1;
	    }
	    int longerLength = one.length();
	    if (longerLength == 0) { return 1.0;  }
	    return (longerLength - function2(one, two)) / (double) longerLength;
	  }
	public static int function2(String val1, String val2) {
		int[] values = new int[val2.length() + 1];
	    val1 = val1.toLowerCase();
	    val2 = val2.toLowerCase();
	    for (int i = 0; i <= val1.length(); i++) {
	      int storedResult = i;
	      for (int j = 0; j <= val2.length(); j++) {
	        if (i == 0)
	          values[j] = j;
	        else {
	          if (j > 0) {
	            int newValue = values[j - 1];
	            if (val1.charAt(i - 1) != val2.charAt(j - 1))
	              newValue = Math.min(Math.min(newValue, storedResult),
	                  values[j]) + 1;
	            values[j - 1] = storedResult;
	            storedResult = newValue;
	          }
	        }
	      }
	      if (i > 0)
	        values[val2.length()] = storedResult;
	    }
	    return values[val2.length()];
	  }
	public static StringTokenizer function3(String line)
	{
		return new StringTokenizer(line," !@#$%^&*()_-+={}[]:;'<,>.?/~`\"");
		
	}
	public static void createIndex(StringTokenizer tokenId,String docid)
	{
		while (tokenId.hasMoreTokens()) {  
			String element1=tokenId.nextToken();
			String element=element1.toLowerCase();
			HashMap<String,Integer> secondaryIndex=new HashMap<>();
			if(!invertedindex.containsKey(element))
			{
				invertedindex.put(element, secondaryIndex);
			}
			
            if(invertedindex.containsKey(element))
            {
                if(invertedindex.get(element).get(docid) != null) 
            	invertedindex.get(element).put(docid,invertedindex.get(element).get(docid)+1);
            	else
            	invertedindex.get(element).put(docid,1);
            }
		}
	}
	public static void createMatrix(StringTokenizer tokenId1,double mat[][],int i,int j,int total_docs)
	{
		while (tokenId1.hasMoreTokens()) {  
			String element=tokenId1.nextToken();
			if(invertedindex.containsKey(element))
			{
				for (String setKey : invertedindex.get(element).keySet()) {
					if(invertedindex.containsKey(element)){
					for (String string1 : set ) {
					    if(invertedindex.get(element).containsKey(string1))
                        {
                            mat[i][j]=(double) ((1)+(invertedindex.get(element).get(string1))*Math.log(total_docs/invertedindex.get(element).size()));
                            j++;
                        }
					}
					j=0;
					
					}
				}
			}
			i++;
		}
		
	}
	
}
