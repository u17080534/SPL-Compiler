package syntax.expression;

import syntax.code.*;

public class calc__ extends Expression 
{   
	private Expression numexprEx;   

	public calc__(Expression e)
	{ 
		super(e);
		this.numexprEx = e; 
		this.expr = "CALC__";
	}  

	public Line trans(File absFile)
	{System.out.println(this.expr);       
		return new Line("");   
	}
}