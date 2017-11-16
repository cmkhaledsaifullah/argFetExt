package argument_recommendation.actions;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;


public class FeatureExtraction 
{
	BufferedWriter bw= null;
	public FeatureExtraction() throws IOException
	{
		//For Random Dataset
		//File file = new File("/home/cms500/workspace/dataset/Random/outputNameRandom.txt");
		
		//For JHotDraw Dataset
		//File file = new File("/home/cms500/workspace/dataset/Random/outputNameRandom.txt");
		
		
		//For JEdit Dataset
		File file = new File("/home/cms500/workspace/dataset/Random/outputNameRandom.txt");
		
		
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		FileWriter fw = new FileWriter(file);
		bw = new BufferedWriter(fw);
	}
	
	public void run() throws IOException, Exception
	{
		//Navigating Workspace
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		
		// Get all projects in the workspace
        IProject[] projects = root.getProjects();
        
        for (IProject project : projects) 
        {
        	// check if we have a Java project
        	
            if (project.isNatureEnabled("org.eclipse.jdt.core.javanature")) 
            {
            	//bw.write("Working in project " + project.getName());
            	//bw.newLine();
            	
            	//Checking the Projects are Random?
            	//if(!project.getName().equals("jhotdraw60b1") ||!project.getName().equals("jEdit"))
            	//Checking the Project is JHotDraw?
            	//if(project.getName().equals("jhotdraw60b1"))
            	//Checking the Project is JEdit?
            	if(project.getName().equals("jEdit"))
            	{
            		// Get all package in the package
	                IPackageFragment[] packages = JavaCore.create(project).getPackageFragments();
	                for (IPackageFragment mypackage : packages) 
	                {
	                	
	                    if (mypackage.getKind() == IPackageFragmentRoot.K_SOURCE) 
	                    {
	                        for (ICompilationUnit unit : mypackage.getCompilationUnits()) 
	                        {
	                        	final ASTParser parser = ASTParser.newParser(AST.JLS3); 
	                        	parser.setKind(ASTParser.K_COMPILATION_UNIT);
	                        	parser.setSource(unit);
	                        	parser.setResolveBindings(true); // we need bindings later on
	                        	CompilationUnit parse = (CompilationUnit) parser.createAST(null);
	                        	
	                        	//The method search function
	                        	method_search(parse);
	                        } // for
	                    } //	if (mypackage.getKind() == IPackageFragmentRoot.K_SOURCE)
	                }// for all package
            	}//	if(project.getName().equals("jhotdraw60b1"))		
            }	// if (project.isNatureEnabled("org.eclipse.jdt.core.javanature"))
        }	// for all projects
        bw.close();
        System.out.println("Process Completed!!!!");
	}	// public void run()
	
	
	public void method_search(CompilationUnit parse) throws IOException
	{
		//List of all receiver for a class
		final List<String> recievername = new ArrayList<String>();
		
		// list of all method for each receiver
		final List<List<String>> methodName = new ArrayList<List<String>>();
		
		parse.accept(new ASTVisitor() {
		// Visit all method one by one
		public boolean visit(MethodDeclaration node) 
		{
			Block block = node.getBody();
			if(block != null)
			{			
				block.accept(new ASTVisitor() 
				{
					//visit every method invoked in a method.
					public boolean visit(MethodInvocation node) 
					{
						int count=0;
			            Expression expression = node.getExpression();
			            if (expression != null) 
			            {
			            	//System.out.println("Expr: " + expression.toString());
			                ITypeBinding typeBinding = expression.resolveTypeBinding();
			                if (typeBinding != null) 
			                {
			                	//System.out.println("package: " + typeBinding.getPackage().getName());
			                	//if the package is javax.swing
			                	try
			                	{
				                    //if(typeBinding.getPackage().getName().equals("javax.swing"))
				                    //{
				                    	int flag=0;
				                    	for(int i=0;i<recievername.size();i++)
				                    	{
				                    		if(recievername.get(i).equals(expression.toString()))
				                    		{
				                    			flag=i;
				                    			break;
				                    		}
				                    	}
				                    	
				                    	// when the reciever is seen for first time
				                    	if(flag == 0)
				                    	{
				                    		if(expression instanceof SimpleName)
				                    		{			               
				                    			recievername.add(expression.toString());
				                    			flag= recievername.size()-1;
				                    			List<String> temp =new ArrayList<String>();
				                    			methodName.add(temp);
				                    		}
				                    	}
				                    	
				                    	//when reviever is not first time seen
				                    	if(flag != 0)
				                    	{
				                    		try 
					                        {
					                        	bw.write(node.getName().getFullyQualifiedName()+","+typeBinding.getQualifiedName()+",");
					                        	List<String> methodHistory = methodName.get(flag);
					                        	for(int i=0;i<methodHistory.size();i++)
					                        	{
					                        		String temp=methodHistory.get(i);
													bw.write(temp+",");
												}
					                        	methodHistory.add(node.getName().getFullyQualifiedName());
					                        	methodName.set(flag, methodHistory);
						                        bw.newLine();
						                        //System.out.println("Successfully Completed");
											} 
					                        catch (IOException e) 
					                        {
					                        	e.printStackTrace();
											}	//catch	
				                    		
				                    	}
				                        
				
				                    //}	//if (typeBinding.getPackage().getName().equals("javax.swing"))
			                	}
			                	catch(Exception ex)
			                	{
			                		System.out.println(ex.getMessage());
			                		//System.out.println("package: " + typeBinding.getPackage().getName());
			                		System.out.println("Expression: " + expression.toString());
			                		System.out.println("Expression: " + node.toString());
			                	}
			                    
			                }	//if (typeBinding != null) 
			                
			            }	//if (expression != null) 
			            
			            return true;
			            
			            }	//visit(MethodInvocation node)
				});	//block.accpet
			}
			return true;
			
		}
		
		}); //cu.accept
		
		
	}
}
