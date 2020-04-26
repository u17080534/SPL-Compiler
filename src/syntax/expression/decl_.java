package syntax.expression;

import syntax.code.*;

public class decl_ extends Expression 
{   
	private Expression declEx;   

	public decl_() 
	{ 
		super();
		this.expr = "DECL_";
	}  

	public decl_(Expression e1) 
	{ 
		super(e1);
		this.expr = "DECL_";
		this.declEx = e1; 
	}  

	public Line trans(File absFile)
	{   
		if(this.expr == null)
			return null;

		return this.declEx.trans(absFile);   
	} 
}