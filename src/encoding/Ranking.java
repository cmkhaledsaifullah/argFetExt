package encoding;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class Ranking {
	private static final String classFile = "/home/cms500/workspace/allJavaClass.txt";
	


	public ArrayList<String> getAllmethod(ArrayList<String> rawdata) throws Exception 
	{
		
		ArrayList<String> methodList = new ArrayList<String>();
		methodList.add("");
		String sCurrentLine;
		
		for (int i=0;i<rawdata.size();i++) 
		{
			sCurrentLine = rawdata.get(i);
			String[] token = sCurrentLine.split(",");
			int flag=0;
			for(int j=0;j<methodList.size();j++)
			{
				if(token[0].equals(methodList.get(j)))
				{
					flag=1;
					break;
				}
			}
			if(flag==0)
				methodList.add(token[0]);
			
		}
		
		return methodList;

	}
	
	
	public ArrayList<String> getAllClass(ArrayList<String> rawdata) throws Exception 
	{
		
		
		ArrayList<String> classList = new ArrayList<String>();
		classList.add("");
		String sCurrentLine;
		
		for (int i=0;i<rawdata.size();i++) 
		{
			sCurrentLine = rawdata.get(i);
			String[] token = sCurrentLine.split(",");
			int flag=0;
			for(int j=0;j<classList.size();j++)
			{
				if(token[1].equals(classList.get(j)))
				{
					flag=1;
					break;
				}
			}
			if(flag==0)
				classList.add(token[1]);
			
		}
		return classList;

	}
	
	
	public static List<String> getallClasses()
	{
		List<String> classname=new ArrayList<String>();
		BufferedReader br = null;
		FileReader fr = null;

		try 
		{
			fr = new FileReader(classFile);
			br = new BufferedReader(fr);

			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) 
			{
				classname.add(sCurrentLine);
			}

		} 
		catch (IOException e) 
		{

			e.printStackTrace();

		} 
		finally 
		{

			try 
			{

				if (br != null)
					br.close();

				if (fr != null)
					fr.close();

			} 
			catch (IOException ex) 
			{

				ex.printStackTrace();

			}

		}
		return classname;
		
	}
    
	
	public static List<String> getallMethods(List<String> classnames)
	{
		List<String> methodName = new ArrayList<String>();
		methodName.add("");
		for(String classname: classnames)
		{
			try
			{
				Class c = Class.forName(classname);
		        Method[] m = c.getDeclaredMethods();
		        for (int i = 0; i < m.length; i++)
		            methodName.add(m[i].getName());
			}
			catch(Exception E)
			{}
		}
		return methodName;
	}

}
