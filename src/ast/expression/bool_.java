package ast.expression;

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

	public String trans() 
	{       
		return this.boolEx.trans() + this.bool__Ex.trans();   
	} 
}