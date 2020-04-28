package syntax.expression;

import syntax.code.*;

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

	public Line trans(File absFile)
	{System.out.println(this.expr);       
		if(this.codeEx == null)
			return null;

		return this.codeEx.trans(absFile);   
	} 
}