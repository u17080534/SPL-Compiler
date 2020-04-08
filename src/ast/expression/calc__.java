package ast.expression;

public class calc__ extends Expression 
{   
	private Expression numexprEx;   

	public calc__(Expression e) : Expression(e) 
	{ 
		this.numexprEx = e; 
	}  

	public String eval() 
	{       
		return this.numexprEx.eval();   
	}
}