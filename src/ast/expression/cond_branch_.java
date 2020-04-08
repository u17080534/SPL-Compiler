package ast.expression;

public class cond_branch_ extends Expression 
{   
	private Expression codeEx;   

	public cond_branch_() 
	{ 
		
	} 

	public cond_branch_(Expression e) : Expression(e) 
	{ 
		this.codeEx = e; 
	}  

	public String eval() 
	{       
		// return (this.codeEx == NULL) ? "" : this.codeEx.eval();   
		return "";
	} 
}