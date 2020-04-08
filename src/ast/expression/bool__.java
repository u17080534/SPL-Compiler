package ast.expression;

public class bool__ extends Expression 
{   
	private Expression boolEx;   

	public bool__(Expression e) : Expression(e)
	{ 
		this.boolEx = e; 
	}  

	public String eval() 
	{       
		return this.boolEx.eval();   
	} 
}