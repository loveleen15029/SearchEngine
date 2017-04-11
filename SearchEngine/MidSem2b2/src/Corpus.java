

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;
import pkg.Stemmer;

public class Corpus {
	TreeMap<String, ArrayList<Posting>> invertedIndex;
	static int counter = 0;
	public static TreeMap<String, ArrayList<String>> pathIndex;
	
	public Corpus() {
		Corpus.pathIndex = new TreeMap<String, ArrayList<String>>();
		this.invertedIndex = new TreeMap<String, ArrayList<Posting>>();
	}

	public TreeMap<String, ArrayList<Posting>> document_index() throws Exception
	{
		Stemmer stem = new Stemmer();
		File[] dir = new File("DataSet").listFiles();
		for (int i = 0; i < dir.length; i++)
		{
			String path_to_directory = "DataSet/";
			if (!dir[i].isFile()) {
				path_to_directory = path_to_directory+dir[i].getName();
				File[] dataFolder = new File(path_to_directory).listFiles();
				for(int j = 0; j < dataFolder.length ; j++)
				{
					if(!dataFolder[j].isFile())
					{
						File[] directories = new File(path_to_directory+"/" +dataFolder[j].getName()).listFiles();
						for(int k = 0 ; k < directories.length ; k++)
						{
							if(!directories[k].isFile())
							{
								File[] insetFolder = new File(path_to_directory+"/" +dataFolder[j].getName()+"/" +directories[k].getName()).listFiles();
								for(int l = 0 ; l < insetFolder.length ; l++)
								{
									String temp_path = path_to_directory+"/" +dataFolder[j].getName()+"/" +directories[k].getName();
									if(insetFolder[l].isFile() && !(insetFolder[l].getName().contains("topics.txt")) 
											&& (insetFolder[l].getName().contains(".txt.clean")) )
									{
										
										Scanner file = new Scanner(new File(temp_path+"/"+insetFolder[l].getName()));
										counter++;
										String data = file.nextLine().toLowerCase();
										data = data.replace("_", " ");
										data = data.replace("-", " ");
										ArrayList<String> patharray = new ArrayList<>();
										if(pathIndex.containsKey(data))
										{
											patharray = pathIndex.get(data);
											patharray.add(temp_path+"/"+insetFolder[l].getName());
										}
										else
										{
											patharray.add(temp_path+"/"+insetFolder[l].getName());
										}
										String fileName = temp_path+"/"+insetFolder[l].getName();
										pathIndex.put(data, patharray);
										while(file.hasNextLine())
										{
											String newStr = file.nextLine();
											String set[] = newStr.split(" ");
											for(String str : set)
											{
												str = str.trim();
												if(str.length()>0)
												{
													str = str.toLowerCase();
													str = stem.stem(str);
													String document_id = fileName;
													if(!invertedIndex.containsKey(str))
													{
														ArrayList<Posting> tempxy = new ArrayList<Posting>();
														Posting p = new Posting();
														p.docId = document_id;
														p.term_occurence = 1;
														tempxy.add(p);
														invertedIndex.put(str, tempxy);
													}
													else
													{
														ArrayList<Posting> postings = invertedIndex.get(str);
														boolean iscontain = false;
														for(int h = 0;h <postings.size() ;h++)
														{
															Posting p = postings.get(h);
															String docs = p.docId;
															if(docs.equals(document_id) )
															{
																p.term_occurence++;
																iscontain = true;
															}
															postings.set(h, p);
														}
														if(!iscontain)
														{
															Posting p = new Posting();
															p.docId = document_id;
															p.term_occurence = 1;
															postings.add(p);
														}
														invertedIndex.put(str, postings);
													}
													ArrayList<Posting> p1 = new ArrayList<>();
													p1 = invertedIndex.get(str);
													
												}
											}
										}
									}
								}
							}
						}
					}
				}

			}
		}
		return invertedIndex;
	}
}
