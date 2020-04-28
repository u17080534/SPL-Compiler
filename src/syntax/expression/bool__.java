package syntax.expression;

import syntax.code.*;

public class bool__ extends Expression 
{   
	private Expression boolEx;   

	public bool__(Expression e)
	{ 
		super(e);
		this.boolEx = e; 
		this.expr = "BOOL__";
	}  

	public Line trans(File absFile)
	{System.out.println(this.expr);       
		return new Line("");   
	} 
}