package syntax.expression;

import syntax.code.*;

public class calc_ extends Expression 
{   
	private Expression numexprEx, calc__Ex;   

	public calc_(Expression e1, Expression e2)
	{ 
		super(e1, e2);
		this.numexprEx = e1; 
		this.calc__Ex = e2; 
		this.expr = "CALC_";
	}  

	public Line trans(File absFile)
	{System.out.println(this.expr);       
		return new Line("");   
	} 
}