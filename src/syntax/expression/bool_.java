package syntax.expression;

import syntax.code.*;

public class bool_ extends Expression 
{   
	private Expression boolEx, bool__Ex;   

	public bool_(Expression e1, Expression e2)
	{ 
		super(e1, e2);
		this.boolEx = e1; 
		this.bool__Ex = e2; 
		this.expr = "BOOL_";
	}  

	public Line trans(File absFile)
	{       
		System.out.println(this.expr);
		return new Line("");   
	} 
}