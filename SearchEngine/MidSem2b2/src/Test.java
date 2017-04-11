

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;



public class Test {
	
	public static TreeMap<String, ArrayList<String>> inverted_index =  new TreeMap<String, ArrayList<String>>();
	public static void main(String ar[])throws Exception
	{
		Scanner scanner =new Scanner(System.in);
		String question= scanner.nextLine();
				dataStream(question);
				scanner.close();
	}
	
	public static void dataStream(String question)
	{
		Corpus access = new Corpus();
		TreeMap <String, ArrayList<Posting>> documentInvertedIndex = new TreeMap<>(); 
		try {
			documentInvertedIndex = access.document_index();
		} catch (Exception e) {
			e.printStackTrace();
		}
		ArrayList<String> output = new ArrayList<>();
		output = Question.query(documentInvertedIndex, question , Corpus.counter);
		System.out.println(output.get(0)+" has highest score "+output.get(1));
		if(Corpus.pathIndex.get(gettingKey(question)) != null)
		
			{
				System.out.println("Precision : "+(Corpus.pathIndex.get(gettingKey(question).trim()).size()/(double)Corpus.counter));
				List<String> real = new ArrayList<>();
				real.addAll(Corpus.pathIndex.keySet());
				System.out.println(real.size()+"nsven");
				System.out.println("NDCG : "+ Score.calculateNDCG(Corpus.pathIndex.get(gettingKey(question)),real));
			}
		else
		{
			System.out.println("Precision : 0");
			System.out.println("NDCG score : 0");
		}
			
		System.out.println("Recall : 1");
		
		
	}
	
	public static String gettingKey(String question)
	{
		String key ="";
		String questionTokens[] = question.split("\\s+");
		for(String ques : questionTokens)
		{
			if(ques.endsWith("'s"))
				ques = ques.substring(0, ques.length()-2);
			if(ques.endsWith("?"))
				ques = ques.substring(0, ques.length()-1);
			
			for(String val : inverted_index.keySet())
			{
				int lock = 0;
				String dataSet[] = val.split(" ");
				for(String x : dataSet)
				{
					if(x.equals(ques))
					{
						if(lock == 0)
						{
							key = val;
							lock = 1;								
						}
					}
				}
			}
		}
		return key;
	}
}
