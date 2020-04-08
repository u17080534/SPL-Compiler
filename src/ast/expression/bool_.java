package ast.expression;

public class bool_ extends Expression 
{   
	private Expression boolEx, bool__Ex;   

	public bool_(Expression e1, Expression e2) : Expression(e1, e2)
	{ 
		this.boolEx = e1; 
		this.bool__Ex = e2; 
	}  

	public String eval() 
	{       
		return this.boolEx.eval() + this.bool__Ex.eval();   
	} 
}