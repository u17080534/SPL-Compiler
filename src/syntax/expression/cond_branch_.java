package syntax.expression;

import syntax.code.*;

//SPL-COMPILER
public class cond_branch_ extends Expression 
{   
	private Expression codeEx;   

	public cond_branch_() 
	{ 
		super();
		this.expr = "COND_BRANCH_";
	} 

	public cond_branch_(Expression e) 
	{ 
		super(e);
		this.codeEx = e; 
		this.expr = "COND_BRANCH_";
	}  

	public Line trans(File absFile)
	{
		//System.out.println(this.expr);       
		return new Line("");   
	} 
}