

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;

public class IndexCreation {
	TreeMap<String, ArrayList<String>> indexing;
	TreeMap<String , ArrayList<String>> testingAnswers;
	public IndexCreation() {
		this.indexing = new TreeMap<String, ArrayList<String>>();
		this.testingAnswers = new TreeMap<String , ArrayList<String>>();
	}
	
	public boolean questionLevel(String str)
	{
		if(str.equals("null") || str.equals("hard") || str.equals("easy") || str.equals("medium"))
			return true;
		return false;
	}

	public boolean isValidQuestions(String answer)
	{
		if(answer.trim().length() == 0)
			return true;
		answer = answer.toLowerCase();
		if((answer.equals("yes") || answer.equals("no") || answer.equals("yes.")|| answer.equals("no.")))
			return true;
		return false;
	}

	public TreeMap<String, ArrayList<String>> document_index()
	{
		try{
			File[] directories = new File("DataSet").listFiles();
			for (int k = 0; k < directories.length; k++) {
				String path = "DataSet/";
				if (!directories[k].isFile()) {
					//System.out.println(folders[i].getName());
					path = path+directories[k].getName();
					File[] folders = new File(path).listFiles();
					for(int j = 0; j < folders.length ; j++)
					{
						if(folders[j].isFile())
						{
							//System.out.println("----"+dataFolder[j].getName());
							Scanner file = new Scanner(new File(path+"/question_answer_pairs.txt"));
							file.nextLine();
							while(file.hasNextLine())
							{
								
								String line = file.nextLine().toLowerCase();
								String tokens[] = line.split("\\s+");
								String query= "";							
								if(!line.contains("?"))
									continue;
								int count = 1;
								while(!tokens[count].contains("?"))
								{
									query = query + tokens[count]+" ";
									count++;
								}
								query += tokens[count];
								String soln = "";
								++count;
								while( !questionLevel(tokens[count]))
								{
									soln += tokens[count];
									soln += " ";
									++count;
								}
								soln = soln.trim();
								if(!isValidQuestions(soln))
								{
									if(soln != null && soln.length() !=0 && soln.endsWith("."))
										soln = soln.substring(0, soln.length() - 1);									
									//System.out.print(counter++ +" "+tokens[0]+" ");
									//System.out.println(answer);

									ArrayList<String> arrayTemporary = new ArrayList<>();
									tokens[0] = tokens[0].replace("_"," ");
									if(indexing.containsKey(tokens[0]))
									{
										arrayTemporary = indexing.get(tokens[0]);
										if(!arrayTemporary.contains(soln))
										{
											arrayTemporary.add(soln);
											indexing.put(tokens[0], arrayTemporary);											
										}
									}
									else
									{
										arrayTemporary.add(soln);
										indexing.put(tokens[0], arrayTemporary);
									}
									
									ArrayList<String> testAns = new ArrayList<>();
									if(testingAnswers.containsKey(query))
									{
										testAns = testingAnswers.get(query);
										testAns.add(soln);
									}
									else
										testAns.add(soln);
									
									testingAnswers.put(query.toLowerCase(), testAns);
								}
							}
							file.close();
						}
					}
					//System.out.println("---------------------------------------------");
				}	
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return indexing;

	}


}
