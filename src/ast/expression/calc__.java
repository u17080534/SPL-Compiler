package ast.expression;

public class calc__ extends Expression 
{   
	private Expression numexprEx;   

	public calc__(Expression e)
	{ 
		super(e);
		this.numexprEx = e; 
		this.expr = "CALC__";
	}  

	public String eval() 
	{       
		return this.numexprEx.eval();   
	}
}