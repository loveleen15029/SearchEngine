

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;
import java.util.TreeMap;

import com.textrazor.AnalysisException;
import com.textrazor.NetworkException;
import com.textrazor.TextRazor;
import com.textrazor.annotations.AnalyzedText;
import com.textrazor.annotations.Entity;

import info.debatty.java.stringsimilarity.NGram;


public class Simulate {
	public static String keYaPI = "be35432658d36869c4a1c81bc2b8d34c0ced989000322455bdc915a6";
	public static TreeMap<String, ArrayList<String>> indexonAnswers =  new TreeMap<String, ArrayList<String>>();
	public static TreeMap<String , ArrayList<String>> questionsTest = new TreeMap<String , ArrayList<String>>();
	public static double precisionValue = 0;
	public static double recallValue = 0;


	public static void main(String ar[])throws Exception
	{

		IndexCreation doc = new IndexCreation();
		indexonAnswers = doc.document_index();
		questionsTest = doc.testingAnswers;
			
			//		int counter = 0;
			//			NERFinder ner = new NERFinder();
			System.out.println("Enter Your Questions");
			Scanner scanner = new Scanner(System.in);
			String query = scanner.nextLine().toLowerCase();
			String questionsTokens[] = query.split("\\s+");			
			ArrayList<String> questionFormat=type(questionsTokens[0]+ " "+questionsTokens[1]);
			
			
			
			
			//System.out.println("Question wala"+entity_type);
			if(questionFormat == null || questionFormat.size() == 0)
			{
				System.out.println("Yes/No type Questions");
				scanner.close();
				return;
			}

			if(questionFormat.contains("Description"))
			{
				TreeMap<Double, String> dummyMap = new TreeMap<Double, String>(Collections.reverseOrder());
				ArrayList<String> tempData = new ArrayList<>();
				ArrayList<String> predictedData = new ArrayList<>();

				TreeMap<Double, String> descAnswers =  new TreeMap<>();
				for(String ques : questionsTokens)
				{
					if(ques.endsWith("'s"))
						ques = ques.substring(0, ques.length()-2);
					if(ques.endsWith("?"))
						ques = ques.substring(0, ques.length()-1);

					for(String valueKey : indexonAnswers.keySet())
					{
						int lock = 0;
						String keyTemp[] = valueKey.split(" ");
						for(String answers : keyTemp)
						{
							if(answers.equals(ques))
							{
								if(lock == 0)
								{
									for(String ans : indexonAnswers.get(valueKey))
									{
										double val=similarity_value(query, ans);
										if(ans.split(" ").length >=2 && val>=.7)
										{
											/*System.out.println(ans + "  " + val);
									System.out.println();*/
											descAnswers.put(val,ans);
										}
									}
									lock = 1;								
								}
							}
						}
					}
				}
				dummyMap.putAll(descAnswers);
				System.out.println("Rank            "+"    Answers");
				for(Double similarity : dummyMap.keySet())
				{
					System.out.println(similarity +"  "+descAnswers.get(similarity));
				}

				//precision recall calculation

				if(questionsTest.get(query) != null && questionsTest.get(query).size() !=0 && descAnswers.size() != 0)
				{
					for(Double y : descAnswers.keySet())
					{
						tempData.add(descAnswers.get(y));
					}
					predictedData.addAll(tempData);
					tempData.retainAll(questionsTest.get(query));
				}
				if(questionsTest.containsKey(query) && descAnswers.size() != 0)
				{
					precisionValue = (double)questionsTest.get(query).size()/(double)descAnswers.size();
				}


				if(tempData.size() != 0 && (double)questionsTest.get(query).size()!=0)
				{
					recallValue = (double)tempData.size()/(double)questionsTest.get(query).size();
				}
				System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
				System.out.println("Precision  : "+ precisionValue);
				System.out.println("Recall : "+recallValue);

				// NDCG Calculation
				System.out.println("NDCG : "+ Score.calculateNDCG(questionsTest.get(query),predictedData));
//				return;
			}
			else
			{
			for(String ques : questionsTokens)
			{
				if(ques.endsWith("'s"))
					ques = ques.substring(0, ques.length()-2);
				if(ques.endsWith("?"))
					ques = ques.substring(0, ques.length()-1);
				for(String val : indexonAnswers.keySet())
				{
					int lock = 0;
					String keyTemp[] = val.split(" ");
					for(String answers : keyTemp)
					{
						if(answers.equals(ques))
						{
							if(lock == 0)
							{
								//System.out.println(inverted_index.get(keyvalue));
								String soln="";
								for(String value:indexonAnswers.get(val))
								{
									soln=soln+" "+value;
								}
								test(keYaPI, soln, questionFormat);
//								NEROnline.test(keYaPI , soln ,questionFormat);
								lock = 1;								
							}

						}	
					}
					/*if(keyvalue.contains(qt))
					{
						System.out.println("\nKey is   :" + keyvalue);
						System.out.println(inverted_index.get(keyvalue));
						String str1="";
						for(String value:inverted_index.get(keyvalue))
						{
							str1=str1+" "+value;
						}
						NEROnline.test(API_KEY , str1 ,entity_type);
					}*/

				}
			}
			}
			/*for(String a : inverted_index.keySet())
			{
				//System.out.println(a);
				ArrayList<String> temp = inverted_index.get(a);
				for(String ans : temp)
				{
					//System.out.print(ans+" ");
					//ner.findNER(ans);
					tokens.add(ans);
					counter++;
				}
				//System.out.println("\n\n");
			}
			System.out.println(counter);*/
			//			ner.findNER("China");

			/*TestTextRazor t = new TestTextRazor();
//				t.testClassifier(API_KEY);
				t.testDictionary(API_KEY);
				t.testAnalysis(API_KEY);*/

			scanner.close();
		
	}
	
	public static void test(String API,String str,ArrayList<String> entities) throws NetworkException, AnalysisException
	{
		HashMap<String, String> solutions = new HashMap<>();
		TextRazor client = new TextRazor(API);
		client.addExtractor("entities");
		client.addExtractor("words");
		
		
		AnalyzedText rspn = client.analyze(str);
		if(rspn != null && rspn.getResponse() != null && rspn.getResponse().getEntities() != null)
		{
			for (Entity sol : rspn.getResponse().getEntities()) {
				if(sol != null)
				{
//					System.out.println("Type : "+ entity.getType()+"\n");
						if(sol.getType()!=null)
						{
							for(String temp : entities)
							{
								if(sol.getType().contains(temp))
								{
									//System.out.println(entity.getEntityId());
									solutions.put(sol.getEntityId(), "");
								}
							}
						}
				}
			}
		}
		//System.out.println("##############");
		for(String strw :solutions.keySet())
		{
			System.out.println(strw);
		}
	}
	
	public static ArrayList<String>  type(String argument)
	{
		ArrayList<String> store = new ArrayList<>();
		String stringCollections[] = argument.split(" ");
		
		stringCollections[0] = stringCollections[0].toLowerCase();
		if(stringCollections[0].equals("is") || stringCollections[0].equals("am") || stringCollections[0].equals("are") || stringCollections[0].equals("do") || stringCollections[0].equals("did") || stringCollections[0].equals("has") || 
				stringCollections[0].equals("have") || stringCollections[0].equals("can") || stringCollections[0].equals("may") || stringCollections[0].equals("does") ||
				stringCollections[0].equals("was") || stringCollections[0].equals("were") ||stringCollections[0].equals("would") ||stringCollections[0].equals("should") )
			return store;
		
		switch (stringCollections[0]) {
		case "what":
			store.add("Description");
			return store;
		case "why":
			store.add("Description");
			return store;
		case "when":
			store.add("Date");
			store.add("Time");
			return store;

		case "where":
			store.add("Region");
			store.add("Country");
			store.add("City");
			store.add("Place");
			store.add("Stadium");
			return store;	
		case "which":
			store.add("Description");
			return store;
		case "whom":
			store.add("Agent");
			store.add("Scientist");
			store.add("Person");
			return store;
		case "who":
			store.add("Person");
			store.add("Agent");
			store.add("Scientist");
			return store;
		
		}

		switch (argument.toLowerCase()) {
		case "how many":
			store.add("Number");
			store.add("Duration");
			return store;

		case "how often":
			store.add("Number");
			store.add("Duration");
			return store;

		case "how much":
			store.add("Number");
			store.add("Duration");
			return store;
		}
		
		return store;
		
	}
	
	
	public static double similarity_value(String a , String b)
	{
		NGram similarityIndex = new NGram(2);
		double val=similarityIndex.distance(a, b);
		return val;
	}
	
	
}
