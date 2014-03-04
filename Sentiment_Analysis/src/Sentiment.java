import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.Pattern;

import snowball.SnowballStemmer;
import snowball.ext.englishStemmer;



public class Sentiment 
{

	public static void main(String[] args) throws FileNotFoundException 
	{


		FileInputStream fis = null;
		BufferedReader reader = null;
		HashMap<String,Integer> bucket=new HashMap<String,Integer>();
		int count=0;
		int total_tweets=0;
		double e=2.71828182846;
		String http_regex="(http:[A-z0-9./~%\"]+)";
		String tweet_regex="[\".!,&$+%':_;\\]\\[\\*\\/()\\?=|-]";

		//adding smileys to the bucket
		bucket.put(":)", count++);
		bucket.put(":D",count++);
		bucket.put(":-)",count++);
		bucket.put(";)", count++);
		bucket.put(":P",count++);
		bucket.put(":(", count++);
		bucket.put(":-(",count++);

		HashMap<String,String> stopWords=new HashMap<String,String>();
		SnowballStemmer stemmer= new englishStemmer();
		try {

			System.out.println("Reading in the stop words..");
			fis = new FileInputStream("C:/Dushyant/Artificial Intelligence/TweetsCorpus/stopwords.txt");
			reader = new BufferedReader(new InputStreamReader(fis));
			String line = reader.readLine();
			String words="";
			while(line != null)
			{
				words+=line;
				line=reader.readLine();
			}
			String []array_stop_words=words.split(",");

			for(int i=0;i<array_stop_words.length;i++)
			{
				array_stop_words[i]=array_stop_words[i].trim();
				array_stop_words[i]=array_stop_words[i].substring(1, array_stop_words[i].length()-1);
				if(!stopWords.containsKey(array_stop_words[i]))
				{
					stopWords.put(array_stop_words[i],array_stop_words[i]);
				}
			}
			fis.close();
			reader.close();

			System.out.println("reading the positive tweets..");

			fis =new FileInputStream("C:/Dushyant/Artificial Intelligence/TweetsCorpus/twitter_positive_full");
			reader = new BufferedReader(new InputStreamReader(fis));

			line = reader.readLine();
			while(line != null)
			{
				total_tweets++;
				line=line.replaceAll("\t"," ");
				String []keywords=line.split(" ");

				for(int i=0;i<keywords.length;i++)
				{

					if(Pattern.matches(http_regex,keywords[i]))
						continue;

					keywords[i]=keywords[i].replaceAll(tweet_regex,"").toLowerCase();

					if(keywords[i].length()==0)
						continue;

					if(keywords[i].charAt(0)=='@' || keywords[i].charAt(0)=='#')
					{
						continue;
					}

					if(Pattern.matches("[0-9]+",keywords[i]))
						continue;

					stemmer.setCurrent(keywords[i]);
					stemmer.stem();
					keywords[i]=stemmer.getCurrent();
					if(!(bucket.containsKey(keywords[i])  || stopWords.containsKey(keywords[i])))
					{
						bucket.put(keywords[i],count++);
					}
				}

				line = reader.readLine();
			}
			fis.close();
			reader.close();
			System.out.println("reading the negative tweets..");

			fis =new FileInputStream("C:/Dushyant/Artificial Intelligence/TweetsCorpus/twitter_negative_full");
			reader = new BufferedReader(new InputStreamReader(fis));

			line = reader.readLine();
			while(line != null)
			{
				total_tweets++;
				line=line.replaceAll("\t"," ");
				String []keywords=line.split(" ");

				for(int i=0;i<keywords.length;i++)
				{

					if(Pattern.matches(http_regex,keywords[i]))
						continue;

					keywords[i]=keywords[i].replaceAll(tweet_regex,"").toLowerCase();

					if(keywords[i].length()==0)
						continue;

					if(keywords[i].charAt(0)=='@' || keywords[i].charAt(0)=='#')
						continue;

					if(Pattern.matches("[0-9]+",keywords[i]))
						continue;

					stemmer.setCurrent(keywords[i]);
					stemmer.stem();
					keywords[i]=stemmer.getCurrent();

					if(!(bucket.containsKey(keywords[i])  || stopWords.containsKey(keywords[i])))
					{
						bucket.put(keywords[i],count++);
					}
				}

				line = reader.readLine();
			}
			fis.close();
			reader.close();
			System.out.println("reading the neutral tweets..");

			fis =new FileInputStream("C:/Dushyant/Artificial Intelligence/TweetsCorpus/twitter_objective_full");
			reader = new BufferedReader(new InputStreamReader(fis));

			line = reader.readLine();
			while(line != null)
			{
				total_tweets++;
				line=line.replaceAll("\t"," ");
				String []keywords=line.split(" ");

				for(int i=0;i<keywords.length;i++)
				{

					if(Pattern.matches(http_regex,keywords[i]))
						continue;

					keywords[i]=keywords[i].replaceAll(tweet_regex,"").toLowerCase();

					if(keywords[i].length()==0)
						continue;

					if(keywords[i].charAt(0)=='@' || keywords[i].charAt(0)=='#')
						continue;

					if(Pattern.matches("[0-9]+",keywords[i]))
						continue;

					stemmer.setCurrent(keywords[i]);
					stemmer.stem();
					keywords[i]=stemmer.getCurrent();

					if(!(bucket.containsKey(keywords[i])  || stopWords.containsKey(keywords[i])))
					{
						bucket.put(keywords[i],count++);
					}
				}

				line = reader.readLine();
			}
			fis.close();
			reader.close();
			System.out.println("Dictionary size:"+count);
			System.out.println("creating the feature vectors aka truth table");
			int [][]truth_table=new int[total_tweets][count+1]; //+1 for the classification output
			/* Road ahead as of now 
			 * creating the truth table
			 * making sure when we read in the files again the vectors are made properly
			 * laying down the architecture for the neural network
			 * (count) inputs and 3 output neurons
			 * modularizing in the end
			 * */
			int row=0;

			fis =new FileInputStream("C:/Dushyant/Artificial Intelligence/TweetsCorpus/twitter_positive");
			reader = new BufferedReader(new InputStreamReader(fis));

			line = reader.readLine();
			boolean createVector=false;
			total_tweets=0;

			while(line != null)
			{
				total_tweets++;
				line=line.replaceAll("\t"," ");
				String []keywords=line.split(" ");

				for(int i=0;i<keywords.length;i++)
				{

					if(Pattern.matches(http_regex,keywords[i]))
						continue;

					keywords[i]=keywords[i].replaceAll(tweet_regex,"").toLowerCase();

					if(keywords[i].length()==0)
						continue;

					if(keywords[i].charAt(0)=='@' || keywords[i].charAt(0)=='#')
					{
						continue;
					}

					if(Pattern.matches("[0-9]+",keywords[i]))
						continue;

					stemmer.setCurrent(keywords[i]);
					stemmer.stem();
					keywords[i]=stemmer.getCurrent();

					if(!stopWords.containsKey(keywords[i]))
					{
						truth_table[row][bucket.get(keywords[i])]=1; 
						createVector=true;
					}
				}

				line = reader.readLine();
				if(createVector)
				{
					truth_table[row][count]=1;
					row++;
					createVector=false;
				}

			}
			fis.close();
			reader.close();

			createVector=false;
			fis =new FileInputStream("C:/Dushyant/Artificial Intelligence/TweetsCorpus/twitter_objective");
			reader = new BufferedReader(new InputStreamReader(fis));

			line = reader.readLine();
			while(line != null)
			{
				total_tweets++;
				line=line.replaceAll("\t"," ");
				String []keywords=line.split(" ");

				for(int i=0;i<keywords.length;i++)
				{

					if(Pattern.matches(http_regex,keywords[i]))
						continue;

					keywords[i]=keywords[i].replaceAll(tweet_regex,"").toLowerCase();

					if(keywords[i].length()==0)
						continue;

					if(keywords[i].charAt(0)=='@' || keywords[i].charAt(0)=='#')
					{
						continue;
					}

					if(Pattern.matches("[0-9]+",keywords[i]))
						continue;

					stemmer.setCurrent(keywords[i]);
					stemmer.stem();
					keywords[i]=stemmer.getCurrent();

					if(!stopWords.containsKey(keywords[i]))
					{
						truth_table[row][bucket.get(keywords[i])]=1; 
						createVector=true;
					}
				}

				line = reader.readLine();
				if(createVector)
				{
					truth_table[row][count]=3;
					row++;
					createVector=false;
				}
			}
			fis.close();
			reader.close();

			createVector=false;
			fis =new FileInputStream("C:/Dushyant/Artificial Intelligence/TweetsCorpus/twitter_negative");
			reader = new BufferedReader(new InputStreamReader(fis));

			line = reader.readLine();
			while(line != null)
			{
				total_tweets++;
				line=line.replaceAll("\t"," ");
				String []keywords=line.split(" ");

				for(int i=0;i<keywords.length;i++)
				{

					if(Pattern.matches(http_regex,keywords[i]))
						continue;

					keywords[i]=keywords[i].replaceAll(tweet_regex,"").toLowerCase();

					if(keywords[i].length()==0)
						continue;

					if(keywords[i].charAt(0)=='@' || keywords[i].charAt(0)=='#')
					{
						continue;
					}

					if(Pattern.matches("[0-9]+",keywords[i]))
						continue;

					stemmer.setCurrent(keywords[i]);
					stemmer.stem();
					keywords[i]=stemmer.getCurrent();

					if(!stopWords.containsKey(keywords[i]))
					{
						truth_table[row][bucket.get(keywords[i])]=1; 
						createVector=true;
					}

				}

				line = reader.readLine();
				if(createVector)
				{
					truth_table[row][count]=2;
					row++;
					createVector=false;
				}
			}
			fis.close();
			reader.close();

			System.out.println("Creating the neural network architecture now:Enter configuration details");

			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			int hidden_layers;
			System.out.println("Enter the no. of hidden layers you want");
			hidden_layers=Integer.parseInt(br.readLine());

			int no_neurons;
			System.out.println("Enter the no of neurons per hidden layer");
			no_neurons=Integer.parseInt(br.readLine());

			double learning_rate;
			System.out.println("Enter the starting learning rate");
			learning_rate=Double.parseDouble(br.readLine());

			double momentum;
			System.out.println("Enter the starting momentum");
			momentum=Double.parseDouble(br.readLine());

			//input layer neurons+ last layer+ hidden layer
			int total_links=count*no_neurons + no_neurons*3 + (hidden_layers-1)*no_neurons*no_neurons;
			int link_tracker=0;
			//generating the link weights randomly for the first iteration 
			Double[] links=new Double[total_links];

			for(int i=0;i<total_links;i++)
			{
				links[i]=Math.random();
			}

			Layer []layers=new Layer[hidden_layers+2];
			//assigning the link weights to the input layer

			layers[0]=new Layer();
			layers[0].neurons=new Neuron[no_neurons];
			for(int i=0;i<no_neurons;i++)
			{
				layers[0].neurons[i]=new Neuron();
				layers[0].neurons[i].to=new Link[count];
				for(int j=0;j<count;j++)
				{
					layers[0].neurons[i].to[j]=new Link();
					layers[0].neurons[i].to[j].weight=links[link_tracker++];
				}
			}

			//assigning weights to other hidden layer neurons
			for(int i=1;i<hidden_layers;i++)
			{
				layers[i]=new Layer();
				layers[i].neurons=new Neuron[no_neurons];
				for(int j=0;j<no_neurons;j++)
				{
					layers[i].neurons[j]=new Neuron();
					layers[i].neurons[j].to=new Link[no_neurons];
					for(int k=0;k<no_neurons;k++)
					{
						layers[i].neurons[j].to[k]=new Link();
						layers[i].neurons[j].to[k].weight=links[link_tracker++];
					}
				}
			}

			layers[hidden_layers]=new Layer();
			layers[hidden_layers].neurons=new Neuron[3];
			//assigning weights to last year
			for(int i=0;i<3;i++)
			{
				layers[hidden_layers].neurons[i]= new Neuron();
				layers[hidden_layers].neurons[i].to=new Link[no_neurons];
				for(int j=0;j<no_neurons;j++)
				{
					layers[hidden_layers].neurons[i].to[j]=new Link();
					layers[hidden_layers].neurons[i].to[j].weight=links[link_tracker++];
				}
			}
			//done with the initializing of weights

			Random rnd=new Random(); 
			int run_count=0;
			int check=0;
			outer:while(true)
			{	 
				run_count++;
				for(row=0;row < total_tweets;row++)
				{
					for(int i=0;i<=hidden_layers;i++)
					{
						if(i==0) // the first layer of the network connected to inputs
						{
							double net=0;
							for(int n=0;n<no_neurons;n++)
							{
								for(int j=0;j<count;j++)
								{
									net+=truth_table[row][j]*layers[i].neurons[n].to[j].weight;
								}
								double op=(double)1/(1+Math.pow(e,-net)); //sigmoidal function as the activation function

								layers[i].neurons[n].output=op;
							}
						}
						else if(i==hidden_layers) //the last layer of the network
						{
							double net=0;
							for(int n=0;n<3;n++)
							{
								for(int j=0;j<no_neurons;j++)
								{
									net+=layers[i-1].neurons[j].output*layers[i].neurons[n].to[j].weight;
								}
								double op=(double)1/(1+Math.pow(e,-net));        //sigmoidal function as the activation function
								
								layers[i].neurons[n].output=op;
							} 
						}
						else
						{
							double net=0;
							for(int n=0;n<no_neurons;n++)
							{
								for(int j=0;j<no_neurons;j++)
								{
									net+=layers[i-1].neurons[j].output*layers[i].neurons[n].to[j].weight;
								}
								double op=(double)1/(1+Math.pow(e,-net));        //sigmoidal function as the activation function

								layers[i].neurons[n].output=op;
							} 
						}
					}
					/*
					 * Here for learning what we are forcing is that the o/p neuron 0 should be on +ve positive tweet
					 * o/p neuron 1 for -ve tweets
					 * o/p neuron 2 for objective tweets
					 */
					int t;
					double o;
					double E=0.0;
					for(int i=0;i<3;i++)
					{
						if(i==0 && truth_table[row][count]==1) 
						{
							t=1;
						}
						else if(i==1 && truth_table[row][count]==2)
						{
							t=1;
						}
						else if(i==2 && truth_table[row][count]==3)
						{
							t=1;
						}
						else
						{
							t=0;
						}
						o=layers[hidden_layers].neurons[i].output;
						layers[hidden_layers].neurons[i].error=(t-o)*o*(1-o);
						E=E+Math.abs((t-o));
					}
					//calculating the total sum squared error
					E=Math.pow(E,2);
					E=E/2;
					E=E*100;
					E=Math.round(E);
					E=E/100;
					System.out.println(E+" "+run_count+" "+check);
					if(E <= 0.06)
					{
						check++;
						if(check==total_tweets)
						{
							System.out.println("We are done with "+run_count+" run(s)");
							break outer;
						}
						else
						{
							continue;
						}
					}
					else
					{
						/**
						 * backpropagate for online learning
						 */
						check=0;
						for(int i=hidden_layers;i>=0;i--)
						{
							if(i==hidden_layers) //the last layer with three single neurons
							{
								for(int j=0;j<3;j++)
								{	
									for(int n=0;n<no_neurons;n++)
									{
										layers[i].neurons[j].to[n].weight+=learning_rate*layers[i].neurons[j].error*layers[i-1].neurons[n].output + momentum*layers[i].neurons[j].to[n].delta_weight;
										layers[i].neurons[j].to[n].delta_weight=learning_rate*layers[i].neurons[j].error*layers[i-1].neurons[n].output;
									}
								}	
							}
							else if(i==0)  //the first layer
							{
								for(int n=0;n<no_neurons;n++) //neurons for the layer whose incoming weights are to be corrected
								{
									o=layers[i].neurons[n].output;
									for(int j=0;j<layers[i].neurons[n].to.length;j++) //links coming towards the neuron whose weights are to be corrected
									{
										for(int k=0;k<layers[i+1].neurons.length;k++)
										{
											//calculating the error from the links going outward and the next layers error
											layers[i].neurons[n].error+=layers[i+1].neurons[k].to[n].weight*layers[i+1].neurons[k].error;
										}
										layers[i].neurons[n].error*=(1-o)*o;
										layers[i].neurons[n].to[j].weight+=learning_rate*layers[i].neurons[n].error*truth_table[row][j] + momentum*layers[i].neurons[n].to[j].delta_weight; 
										layers[i].neurons[n].to[j].delta_weight=learning_rate*layers[i].neurons[n].error*truth_table[row][j];
									}

								}
							}
							else  //the case otherwise
							{
								for(int n=0;n<no_neurons;n++) //neurons for the layer whose incoming weights are to be corrected
								{
									o=layers[i].neurons[n].output;
									for(int j=0;j<layers[i].neurons[n].to.length;j++) //links coming towards the neuron whose weights are to be corrected
									{
										for(int k=0;k<layers[i+1].neurons.length;k++)
										{
											//calculating the error from the links going outward and the next layers error
											layers[i].neurons[n].error+=layers[i+1].neurons[k].to[n].weight*layers[i+1].neurons[k].error;
										}
										layers[i].neurons[n].error*=(1-o)*o;
										layers[i].neurons[n].to[j].weight+=learning_rate*layers[i].neurons[n].error*layers[i-1].neurons[j].output +  momentum*layers[i].neurons[n].to[j].delta_weight;
										layers[i].neurons[n].to[j].delta_weight=learning_rate*layers[i].neurons[n].error*layers[i-1].neurons[j].output;  
									}

								}
							}
						}
						row=rnd.nextInt(total_tweets);
					}
				}
			} 

			System.out.println("Now that the network has converged , lets make efforts to generalize it"); 
			int [][]truth_table_test=new int[total_tweets][count+1];
			row=0;
			//reading in the positive test tweets
			fis =new FileInputStream("C:/Dushyant/Artificial Intelligence/TweetsCorpus/twitter_negative_test");
			reader = new BufferedReader(new InputStreamReader(fis));

			line = reader.readLine();
			createVector=false;
			total_tweets=0;

			while(line != null)
			{
				total_tweets++;
				line=line.replaceAll("\t"," ");
				String []keywords=line.split(" ");

				for(int i=0;i<keywords.length;i++)
				{

					if(Pattern.matches(http_regex,keywords[i]))
						continue;

					keywords[i]=keywords[i].replaceAll(tweet_regex,"").toLowerCase();

					if(keywords[i].length()==0)
						continue;

					if(keywords[i].charAt(0)=='@' || keywords[i].charAt(0)=='#')
					{
						continue;
					}

					if(Pattern.matches("[0-9]+",keywords[i]))
						continue;

					stemmer.setCurrent(keywords[i]);
					stemmer.stem();
					keywords[i]=stemmer.getCurrent();

					if(!stopWords.containsKey(keywords[i]))
					{
						truth_table_test[row][bucket.get(keywords[i])]=1; 
						createVector=true;
					}
				}

				line = reader.readLine();
				if(createVector)
				{
					truth_table_test[row][count]=2;
					row++;
					createVector=false;
				}

			}
			fis.close();
			reader.close();

			//reading in the negative test tweets
			fis =new FileInputStream("C:/Dushyant/Artificial Intelligence/TweetsCorpus/twitter_positive_test");
			reader = new BufferedReader(new InputStreamReader(fis));

			line = reader.readLine();
			createVector=false;

			while(line != null)
			{
				total_tweets++;
				line=line.replaceAll("\t"," ");
				String []keywords=line.split(" ");

				for(int i=0;i<keywords.length;i++)
				{

					if(Pattern.matches(http_regex,keywords[i]))
						continue;

					keywords[i]=keywords[i].replaceAll(tweet_regex,"").toLowerCase();

					if(keywords[i].length()==0)
						continue;

					if(keywords[i].charAt(0)=='@' || keywords[i].charAt(0)=='#')
					{
						continue;
					}

					if(Pattern.matches("[0-9]+",keywords[i]))
						continue;

					stemmer.setCurrent(keywords[i]);
					stemmer.stem();
					keywords[i]=stemmer.getCurrent();

					if(!stopWords.containsKey(keywords[i]))
					{
						truth_table_test[row][bucket.get(keywords[i])]=1; 
						createVector=true;
					}
				}

				line = reader.readLine();
				if(createVector)
				{
					truth_table_test[row][count]=1;
					row++;
					createVector=false;
				}

			}
			fis.close();
			reader.close();

			fis =new FileInputStream("C:/Dushyant/Artificial Intelligence/TweetsCorpus/twitter_objective_test");
			reader = new BufferedReader(new InputStreamReader(fis));

			line = reader.readLine();
			createVector=false;

			while(line != null)
			{
				total_tweets++;
				line=line.replaceAll("\t"," ");
				String []keywords=line.split(" ");

				for(int i=0;i<keywords.length;i++)
				{

					if(Pattern.matches(http_regex,keywords[i]))
						continue;

					keywords[i]=keywords[i].replaceAll(tweet_regex,"").toLowerCase();

					if(keywords[i].length()==0)
						continue;

					if(keywords[i].charAt(0)=='@' || keywords[i].charAt(0)=='#')
					{
						continue;
					}

					if(Pattern.matches("[0-9]+",keywords[i]))
						continue;

					stemmer.setCurrent(keywords[i]);
					stemmer.stem();
					keywords[i]=stemmer.getCurrent();

					if(!stopWords.containsKey(keywords[i]))
					{
						truth_table_test[row][bucket.get(keywords[i])]=1; 
						createVector=true;
					}
				}

				line = reader.readLine();
				if(createVector)
				{
					truth_table_test[row][count]=3;
					row++;
					createVector=false;
				}

			}
			fis.close();
			reader.close();

			//lets check out the %accuracy for the test tweets
			int error_count=0;
			for(row=0;row < total_tweets;row++)
			{
				for(int i=0;i<=hidden_layers;i++)
				{
					if(i==0) // the first layer of the network connected to inputs
					{
						double net=0;
						for(int n=0;n<no_neurons;n++)
						{
							for(int j=0;j<count;j++)
							{
								net+=truth_table_test[row][j]*layers[i].neurons[n].to[j].weight;
							}
							double op=(double)1/(1+Math.pow(e,-net));        //sigmoidal function as the activation function
							layers[i].neurons[n].output=op;
						}
					}
					else if(i==hidden_layers) //the last layer of the network
					{
						double net=0;
						for(int n=0;n<3;n++)
						{
							for(int j=0;j<no_neurons;j++)
							{
								net+=layers[i-1].neurons[j].output*layers[i].neurons[n].to[j].weight;
							}
							double op=(double)1/(1+Math.pow(e,-net));        //sigmoidal function as the activation function
							layers[i].neurons[n].output=op;
						} 
					}
					else
					{
						double net=0;
						for(int n=0;n<no_neurons;n++)
						{
							for(int j=0;j<no_neurons;j++)
							{
								net+=layers[i-1].neurons[j].output*layers[i].neurons[n].to[j].weight;
							}
							double op=(double)1/(1+Math.pow(e,-net));        //sigmoidal function as the activation function
							layers[i].neurons[n].output=op;
						} 
					}
				}
				/*
				 * Here for learning what we are forcing is that the o/p neuron 0 should be on +ve positive tweet
				 * o/p neuron 1 for -ve tweets
				 * o/p neuron 2 for objective tweets
				 */
				double o;
				double max=0.0;
				for(int i=0;i<3;i++)
				{
					o=layers[hidden_layers].neurons[i].output;
					if(o>max)
					{
						max=o;
					}
				}
				for(int i=0;i<3;i++)
				{
					o=layers[hidden_layers].neurons[i].output;  
					System.out.println(i+" "+o +" "+max+" "+truth_table_test[row][count]);
					if(i==0 && truth_table_test[row][count]==1) 
					{
						if(o==max)
						{
							break;
						}
						else
						{	
							error_count++;
							break;
						}
					}
					else if(i==1 && truth_table_test[row][count]==2)
					{
						if(o==max)
						{
							break;
						}
						else
						{	
							error_count++;
							break;
						}
					}
					else if(i==2 && truth_table_test[row][count]==3)
					{
						if(o==max)
						{
							break;
						}
						else
						{	
							error_count++;
							break;
						}
					}
				}

			}
			System.out.print("% accuracy is:");
			System.out.println((1.00-((double)error_count/(double)total_tweets))*100);
		} 
		catch(FileNotFoundException f)
		{
			System.out.println("File not found, enter the file path correctly");
		}
		catch(IOException io)
		{
			System.out.println(io.getMessage());
		}

	}

}
