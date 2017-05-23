import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.tartarus.snowball.ext.PorterStemmer;

public class Invertedindex {

	final static double k=1.0;
	final static double b=0.5;
	public static  PorterStemmer stemmer = new PorterStemmer();
	


	public static boolean isInteger(String s) {
		try { 
			Integer.parseInt(s); 
		} catch(NumberFormatException e) { 
			return false; 
		} catch(NullPointerException e) {
			return false;
		}
		return true;
	}

	public static void main(String[] args) throws IOException {
		TreeMap<String,LinkedHashMap<String,Integer>> hs=new TreeMap<>();
	
		HashMap<String,Integer> count=new HashMap<>();
		File nF = new File("corpus.txt");
		double n=0;
		BufferedReader br = new BufferedReader(new FileReader(nF));	
		try {
			String line = br.readLine();
			LinkedHashMap<String,Integer> hs1;
			String temp="";
			while (line != null) {
				StringTokenizer st = new StringTokenizer(line," ");	
				while (st.hasMoreTokens()) {  
					String child1=st.nextToken();
					String child=child1.toLowerCase();
					if(child1.equals("#"))
					{
						if(st.hasMoreElements())
						{
							String child2=st.nextToken();
							temp=child2;
						}
						n++;
					}
					else
					{
						if(!isInteger(child1))
						{
							if(count.containsKey(temp))
							{
								int x=count.get(temp);
								x++;
								count.put(temp, x);
							}
							else
							{
								count.put(temp, 1);
							}
							if(hs.containsKey(child))
							{
								if(hs.get(child).containsKey(temp))
								{
									int x=hs.get(child).get(temp);
									x++;
									hs.get(child).put(temp,x);

								}
								else
								{
									hs.get(child1).put(temp, 1);
								}
							}
							else
							{
								hs1=new LinkedHashMap<String, Integer>();
								hs1.put(temp, 1);
								hs.put(child, hs1);
							}
						}
					}
				}
				line = br.readLine();
			}

		}catch (Exception e) {
			// TODO: handle exception
		}
		br.close();

		Scanner sc=new Scanner(System.in);
		while(true)
		{
			
			String query=sc.nextLine();
			StringTokenizer st = new StringTokenizer(query.toLowerCase()," ");
			ArrayList<String> resultSet=new ArrayList<>();
			HashSet<String> rs =new HashSet<>();
			HashSet<String> resultSet1=new HashSet<>();
			while (st.hasMoreTokens()) {  
				String child=st.nextToken();
				stemmer.setCurrent(child);
				stemmer.stem();
				child=stemmer.getCurrent();
				resultSet1.add(child);
				if(hs.containsKey(child))
				{
					for (String key : hs.get(child).keySet()) {
						resultSet.add(key);
					}
				}
			}
			rs.addAll(resultSet);
			resultSet.clear();
			resultSet.addAll(rs);
			double matrix[][]=new double[resultSet1.size()][resultSet.size()];
			double bmMatrix[][]=new double[resultSet1.size()][resultSet.size()];
			double avg=0.0;
			double sum=0.0;
			for(String s: count.keySet())
			{
				sum+=count.get(s);
			}
			avg=sum/count.size();
			StringTokenizer st1= new StringTokenizer(query.toLowerCase()," ");
			int i=0,j=0;
			while (st1.hasMoreTokens()) {  
				String child=st1.nextToken();
				stemmer.setCurrent(child);
				stemmer.stem();
			    child=stemmer.getCurrent();
				if(hs.containsKey(child))
				{
					if(hs.containsKey(child)){
						for (String s : resultSet ) {

							if(hs.get(child).containsKey(s))
							{

								matrix[i][j]=(double) ((1)+(hs.get(child).get(s))*Math.log10(n/(double)hs.get(child).size()));
								bmMatrix[i][j]=(Math.log10((n-hs.get(child).size()+0.5)/(hs.get(child).size()+0.5))*(hs.get(child).get(s)*(k+1))/((hs.get(child).get(s))+(k*(1-b+(b*count.get(s)/avg)))));

							}
							j++;
						}
						j=0;

					}
				}
				i++;
			}

			double a[]=new double[resultSet.size()];
			double res[]=new double[resultSet.size()];
			ArrayList<String> al=new ArrayList<>();
			al.addAll(resultSet);
			Map<String,Double> output1=new HashMap<>();
			Map<String,Double> outputBM=new HashMap<>();

			ArrayList<String> dummy=new ArrayList<>();
			dummy.addAll(resultSet);
			for(int i1=0;i1<resultSet.size();i1++)
			{
				for(j=0;j<resultSet1.size();j++)
				{
					a[i1]+=matrix[j][i1];
				}

				output1.put(resultSet.get(i1), a[i1]);

			}
			for(int i1=0;i1<resultSet.size();i1++)
			{
				for(j=0;j<resultSet1.size();j++)
				{
					res[i1]+=bmMatrix[j][i1];
				}

				outputBM.put(resultSet.get(i1), res[i1]);

			}
			Set<Entry<String, Double>> set = output1.entrySet();
			List<Entry<String, Double>> list = new ArrayList<Entry<String, Double>>(set);
			Collections.sort( list, new Comparator<Map.Entry<String, Double>>()
			{
				public int compare( Map.Entry<String, Double> o1, Map.Entry<String, Double> o2 )
				{
					return (o2.getValue()).compareTo( o1.getValue() );
				}
			} );
			Set<Entry<String, Double>> set1 = outputBM.entrySet();
			List<Entry<String, Double>> list1 = new ArrayList<Entry<String, Double>>(set1);
			Collections.sort( list1, new Comparator<Map.Entry<String, Double>>()
			{
				public int compare( Map.Entry<String, Double> o1, Map.Entry<String, Double> o2 )
				{
					return (o2.getValue()).compareTo( o1.getValue() );
				}
			} );
			int k=0;
			System.out.println("*************************************Tf-Idf Score*******************************************");
			for(Map.Entry<String, Double> entry: list)
			{
				if(k>10)
					break;
				//System.out.println("Rank: "+(k+1)+" \tDocId :"+entry.getKey()+" \tValue :"+entry.getValue());
				System.out.println("Rank"+(k+1)+":"+entry.getKey()+":"+entry.getValue());
				k++;
			}
			k=0;
			System.out.println("*************************************BM25 Score*******************************************");
			for(Map.Entry<String, Double> entry: list1)
			{
				if(k>10)
					break;
				//System.out.println("Rank: "+(k+1)+" \tDocId :"+entry.getKey()+" \tValue :"+entry.getValue());
				System.out.println("Rank"+(k+1)+":"+entry.getKey()+":"+entry.getValue());
				k++;
			}
			Arrays.sort(a);
			Collections.sort(al);
			for(int i1=0;i1<al.size();i1++)
			{
				double max=-4;
				int index=0;
				for(j=0;j<al.size();j++)
				{
					if(max<=a[j])
					{
						max=a[j];
						index=j;
					}
				}
				a[index]=-4;
			}
			StringTokenizer st2= new StringTokenizer(query.toLowerCase()," ");
			ArrayList<String> output=new ArrayList<>();
			HashMap<String,String> out=new HashMap<>();
			String res1="";
			while (st2.hasMoreTokens()) {  
				String child=st2.nextToken();
				stemmer.setCurrent(child);
				stemmer.stem();
				child=stemmer.getCurrent();
				double max=0;
				for(String s: hs.keySet() )
				{
					if(max<=similarityIndex.similarityIndex(child, s))
					{
						max=similarityIndex.similarityIndex(child, s);
						res1=s;
					}
				}
				if(max!=1.0)
					out.put(child,res1);
				output.add(res1);
			}
			if(out.size()!=0)
			{
				System.out.println("****************************************************");
				System.out.println("your corrected searched should be:");
				/*for(String s:output)
				{
				}*/
				System.out.println("****************************************************");
			}
			QueryRelevance.queryRelevance(hs,count,query);
			/*	for(String s: out.keySet())
	{
//		System.out.println(s+" ----->change this word to corresponding word for more accurate results -----> "+out.get(s));
	}
			 */	
		}
	}
}
