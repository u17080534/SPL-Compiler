package ast.expression;

public class instr extends Expression 
{   
	private Expression expr;   

	public instr(Expression expr) 
	{ 
		this.expr = expr; 
	}  

	public String eval() 
	{       
		return this.expr.eval();   
	}
}