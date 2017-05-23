import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.tartarus.snowball.ext.PorterStemmer;

import java.util.Scanner;
public class QueryRelevance {

	public static void queryRelevance(TreeMap<String,LinkedHashMap<String,Integer>> hs,HashMap<String,Integer> count,String query)
	{
		String another="y";
		final double d=1.0;
		final double e=0.8;
		final double f=0.15;
		Scanner Sc=new Scanner(System.in);
		LinkedHashMap<String,Double[]> al=new LinkedHashMap<>();
		Double queryVector[]=new Double[hs.size()];
		HashMap<String,Integer> anagram= new HashMap<>();
		Map<String,Double> output=new HashMap<>();
		Arrays.fill(queryVector, 0.0);
		int counter=0;
		for(String s: hs.keySet())
		{
			for(String p:hs.get(s).keySet())
			{
				if(al.containsKey(p))
				{
					Double arr[]=al.get(p);
					double value= (1+Math.log10((double)hs.get(s).get(p)))*(Math.log10((double)count.size()/(double)hs.get(s).size()));
					arr[counter]=value;
					al.put(p, arr);
				}
				else
				{
					Double arr[]=new Double[hs.size()];
					for(int i=0;i<arr.length;i++)
					{
						arr[i]=0.0;
					}
					double value= (1+Math.log10((double)hs.get(s).get(p)))*(Math.log10((double)count.size()/(double)hs.get(s).size()));
					arr[counter]=value;
					al.put(p, arr);
					
				}
			}
			counter++;
		}
	    PorterStemmer stemmer = new PorterStemmer();
		System.out.println();
		String a[]=query.split(" ");
		Double[] score=al.get("2");
		for(int i=0;i<a.length;i++)
		{
			stemmer.setCurrent(a[i]);
			stemmer.stem();
			a[i]=stemmer.getCurrent();
		}
		for(String s: a)
		{
			if(anagram.containsKey(s))
			{
				int x= anagram.get(s);
				x++;
				anagram.put(s, x);
			}
			else
			{
				anagram.put(s, 1);
			}
		}
		System.out.println(anagram);
		for(int i=0;i<a.length;i++)
		{
			if(hs.containsKey(a[i])){
			int cou=0;
			for(String s: hs.keySet())
			{
				if(s.equals(a[i]))
					break;
				cou++;
			}
			double value=0.0;
			if(anagram.containsKey(a[i]))
			{
				int x= anagram.get(a[i]);
			    value= (1+Math.log10(x))*(Math.log10(count.size()/(double)hs.get(a[i]).size()));
			}
			queryVector[cou]=value;}
		}
		while(another.equals("Y")||another.equals("y"))
		{
		for(String s: al.keySet())
		{
			Double x=CosineSimilarity.similarityScore(al.get(s), queryVector);
			output.put(s,x);
			
		}
		Set<Entry<String, Double>> set = output.entrySet();
		List<Entry<String, Double>> list = new ArrayList<Entry<String, Double>>(set);
		Collections.sort( list, new Comparator<Map.Entry<String, Double>>()
		{
			public int compare( Map.Entry<String, Double> o1, Map.Entry<String, Double> o2 )
			{
				return (o2.getValue()).compareTo( o1.getValue() );
			}
		} );
		int i=0;
		System.out.println("*************************************Query Relevance*******************************************");
		for(Map.Entry<String, Double> entry: list)
		{
			if(i>10)
				break;
			System.out.println("Rank: "+(i+1)+" \tDoc :"+entry.getKey()+" \tValue :"+entry.getValue());
			i++;
		}
		System.out.println("Do you want to continue??(y/n)");
		another = Sc.nextLine();
		
		System.out.println("Enter Relevant Documents: ");
		String rel= Sc.nextLine();
		String[] relRes=rel.split(" ");
		System.out.println("Enter Non-Relevant Documents: ");
		String nonRel=Sc.nextLine();
		String[] nonRelRes=nonRel.split(" ");
		Double sum1[]=Calculation(al,relRes,hs);
		Double sum2[]=Calculation(al,nonRelRes,hs);
		Double[] res1=div( e, relRes.length,sum1);
		Double[] res2=div( f, nonRelRes.length,sum2);
		for(int j=0;j<hs.size();j++)
		{
			queryVector[j]=queryVector[j]*d+res1[j]-res2[j];
		}
		}
		

	}
	public static Double[] Calculation(LinkedHashMap<String,Double[]> al,String[] relRes,TreeMap<String,LinkedHashMap<String,Integer>> hs)
	{
		Double sum1[]=new Double[hs.size()];
		
		Arrays.fill(sum1, 0.0);
		for(int j=0;j<relRes.length;j++)
		{
			for(int r=0;r<sum1.length;r++)
			{
				sum1[r]+=(al.get(relRes[j]))[r];
			}
		}
		return sum1;
	}
	
	public static Double[] div(double e,int relRes,Double[] sum1)
	{
		double res=e/(double)relRes;
		for(int i=0;i<sum1.length;i++)
		{
			sum1[i]*=res;
		}
		return sum1;
		
	}

}
