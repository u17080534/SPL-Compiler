package syntax.expression;

import syntax.code.*;

public class prog_ extends Expression 
{   
	private Expression ex;   

	public prog_()
	{
		super();
		this.expr = "PROG_";
	}
	
	public prog_(Expression e1)
	{ 
		super(e1);
		this.ex = e1; 
		this.expr = "PROG_";
	}  

	public Line trans(File absFile)
	{       
		if(this.ex == null)
			return null;      

		return this.ex.trans(absFile);         
	} 
} 