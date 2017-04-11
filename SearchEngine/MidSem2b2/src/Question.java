

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import com.sun.xml.internal.bind.v2.model.util.ArrayInfoUtil;

import pkg.Stemmer;

public class Question {
		public static ArrayList<String> query(TreeMap<String, ArrayList<Posting>> inverted_index , String query , int N)
		{
			Stemmer strem = new Stemmer();
			String questionTokens[] = query.split(" ");
			SortedSet<String> docIds = new TreeSet<>();
			for(int i = 0; i < questionTokens.length ; i++)
			{
				ArrayList<Posting> postings = inverted_index.get(strem.stem(questionTokens[i]));
				if(postings != null)
				{
					for(Posting p : postings)
						docIds.add(p.docId);
				}
			}
			int k = 0;
			String data[] = new String[docIds.size()];
			Iterator it = docIds.iterator();
		      while (it.hasNext()) {
		         data[k++] = it.next().toString();
		      }
			
			double resultMatrix[][] = new double[questionTokens.length][data.length];
			for(int i = 0; i < questionTokens.length ; i++)
			{
				ArrayList<Posting> postings = inverted_index.get(strem.stem(questionTokens[i]));
				for(int j = 0 ; j < docIds.size(); j++)
				{
					if(postings != null)
					{
						int document_frequency = postings.size();
						double idf = Math.log10((N/(double)document_frequency));
						int tf = 0;
						for(Posting p : postings)
						{
							if(p.docId.equals(data[j]))
							{
								tf = p.term_occurence;
							}
						}
						resultMatrix[i][j] = 1 + (tf * idf);
					}
					else
					{
						resultMatrix[i][j] = 0;
					}
				}
			}
			
			double result[] = new double[docIds.size()];
			for(int i = 0; i < docIds.size() ; i++)
			{
				double tempData = 0;
				for(int j = 0 ; j < questionTokens.length; j++)
				{
					tempData = tempData + resultMatrix[j][i];
				}
				result[i] = tempData;
			}
			
			
	
			ArrayList<String> solutions = new ArrayList<>();
			String arg0 = "";
			double arg1 = 0;
			
			for(int i = 0; i < data.length ; i++)
			{
				if(result[i] > arg1)
				{
					arg1 = result[i];
					arg0 = data[i];
				}
				
			}
			solutions.add(arg0);
			solutions.add(Double.toString(arg1));
			return solutions;
		}
}
