package encoding;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class Main 
{
	//For Random Dataset
	//private static final String inputFile = "/home/cms500/workspace/dataset/Random/outputNameRandom.txt";
	//private static  String outputFile = "/home/cms500/workspace/dataset/Random/outputNumberRandom_1.txt";
	
	// For JHotDraw Dataset
	//private static final String inputFile = "/home/cms500/workspace/dataset/JHotDraw/outputNameJHotDraw.txt";
	//private static  String outputFile = "/home/cms500/workspace/dataset/JHotDraw/outputNumberJHotDraw_1.txt";
	
	
	// for JEdit Dataset
	private static final String inputFile = "/home/cms500/workspace/dataset/JEdit/outputNameJEdit.txt";
	private static  String outputFile = "/home/cms500/workspace/dataset/JEdit/outputNumberJEdit_1.txt";
	
	
	private static List<String> info;
    
	public static void main(String[] args) throws Exception 
	{
		Ranking rk = new Ranking();
		ArrayList<ArrayList<String>> subfile = splitfile();
		info = new ArrayList<String>();
		
		for(int i=0;i<subfile.size();i++)
		{
			info.add("File Name: " + outputFile);
			List<String> classnames = rk.getAllClass(subfile.get(i));
			List<String> methodName = rk.getAllmethod(subfile.get(i));
			readNameData(classnames,methodName,subfile.get(i));
			info.add("Number of Unique Method Call: "+methodName.size());
			if(i>=10)
 				outputFile = outputFile.substring(0, (outputFile.length()-6))+(i+2)+".txt";
 			else
 				outputFile = outputFile.substring(0, (outputFile.length()-5))+(i+2)+".txt";
			
			info.add("Number of Observation: "+subfile.get(i).size());
			info.add("\n");
			info.add("========================================================================================");
			info.add("\n");
		}
		
		System.out.println("Number of Subfile: "+subfile.size());
		
		infoWrite();
	}
	
	
	public static ArrayList<ArrayList<String>> splitfile() throws Exception
	{
		FileReader fr = new FileReader(inputFile);
		BufferedReader br = new BufferedReader(fr);
		
		ArrayList<ArrayList<String>>subfile = new ArrayList<ArrayList<String>>();
		String sCurrentLine;
		int i=0;
		ArrayList<String> temp=new ArrayList<String>();
		while ((sCurrentLine = br.readLine()) != null) 
		{
			if(i % 2500 == 0 && i > 0)
     		{
				subfile.add(temp);
				temp = new ArrayList<String>();
     		}
			
			temp.add(sCurrentLine);
			i++;
		}
		subfile.add(temp);
		br.close();
		return subfile;
		
	}
	
	
	
	
	public static void readNameData(List<String> classnames, List<String> methodName,ArrayList<String> rawdata) throws Exception
	{
		int max=0;
		ArrayList<ArrayList<Integer>> numberlist = new ArrayList<ArrayList<Integer>>();
		String sCurrentLine;

		for (int k=0;k<rawdata.size();k++) 
		{
			sCurrentLine = rawdata.get(k);
			ArrayList<Integer> oneList = new ArrayList<Integer>();
			String[] token = sCurrentLine.split(",");
			int[] encodvalue = returnNumericValue(token,classnames,methodName);
			for(int i=0;i<encodvalue.length;i++)
			{
				oneList.add(encodvalue[i]);
			}
			if(oneList.get(0)!=0)
			{
				numberlist.add(oneList);
				if(max < oneList.size())
					max=oneList.size();
			}
			
		}		
		printNumberOutput(numberlist,max);
	}
 
	public static int[] returnNumericValue(String[] token, List<String> classnames, List<String> methodName)
	{
		int t=0;
		int[] encodedvalue = new int[token.length];
		for(int i = 0; i<token.length;i++)
		{
			t=0;
			if(i==1)
			{
				for(int j=0;j<classnames.size();j++)
				{
					if(classnames.get(j).equals(token[i]))
					{
						t=j;
						break;
					}
				}
			}
			else
			{
				for(int j=0;j<methodName.size();j++)
				{
					if(methodName.get(j).equals(token[i]))
					{
						t=j;
						break;
					}
				}
			}
			
			encodedvalue[i] = t;
		}
		
		return encodedvalue;
		
	}
	
	public static void printNumberOutput(ArrayList<ArrayList<Integer>> numberlist, int max) throws Exception
	{
		
		BufferedWriter bw = fileCreation(outputFile);
		 try 
         {
         	for(int i=0;i<numberlist.size();i++)
         	{
         		
         		ArrayList<Integer> al = numberlist.get(i);
         		bw.write(al.get(0).toString());
         		for(int j=1;j<max;j++)
         		{
         			if(j<al.size())
         				bw.write(","+al.get(j));
         			else
         				bw.write(",0");
         		}
         		bw.newLine();
                //System.out.println("Successfully Completed");
			}
            
		} 
        catch (IOException e) 
        {
        	e.printStackTrace();
		}	//catch	
		 
		bw.close();
		info.add("Maximum Size of an observation:"+ max);
		
	}
	
	
	public static BufferedWriter fileCreation(String outputFile) throws Exception
	{
		File file = new File(outputFile);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		FileWriter fw = new FileWriter(file);
		BufferedWriter bw = new BufferedWriter(fw);
		
		return bw;
	}
	
	
	public static void infoWrite() throws Exception
	{
		//BufferedWriter bw = fileCreation("/home/cms500/workspace/dataset/Random/randomDatasetInfo.txt");
		
		//BufferedWriter bw = fileCreation("/home/cms500/workspace/dataset/JHotDraw/jhotdrawDatasetInfo.txt");
		
		BufferedWriter bw = fileCreation("/home/cms500/workspace/dataset/JEdit/jeditDatasetInfo.txt");
		for(int i=0;i<info.size();i++)
		{
			bw.write(info.get(i));
			bw.newLine();
		}
		bw.close();
	}
	
}


