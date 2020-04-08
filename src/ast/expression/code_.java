package ast.expression;

public class code_ extends Expression 
{   
	private Expression codeEx;   

	public code_() 
	{ 

	}
	
	public code_(Expression e) : Expression(e) 
	{ 
		this.codeEx = e;
	}  

	public String eval() 
	{       
		return this.codeEx.eval();   
	} 
}