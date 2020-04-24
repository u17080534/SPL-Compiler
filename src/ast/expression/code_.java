package ast.expression;

public class code_ extends Expression 
{   
	private Expression codeEx;   

	public code_() 
	{ 
		super();
		this.expr = "CODE_";
	}
	
	public code_(Expression e)
	{ 
		super(e);
		this.codeEx = e;
		this.expr = "CODE_";
	}  

	public String trans() 
	{       
		return this.codeEx.trans();   
	} 
}