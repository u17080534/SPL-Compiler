package ast.expression;

public class calc_ extends Expression 
{   
	private Expression numexprEx, calc__Ex;   

	public calc_(Expression e1, Expression e2) : Expression(e1, e2) 
	{ 
		this.numexprEx = e1; 
		this.calc__Ex = e2; 
	}  

	public String eval() 
	{       
		return this.numexprEx.eval() + this.calc__Ex.eval();   
	} 
}