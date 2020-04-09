package ast.expression;

public class proc extends Expression 
{   
	private TokenExpression proced;   
	private Expression progEx;   

	public proc(String e1, Expression e2) 
	{ 
		super(new TokenExpression("proc", e1), e2);
		this.proced = (TokenExpression) this.descendents.get(0);
		this.expr = "PROC";
		this.progEx = e2; 
	}  

	public String eval() 
	{       
		return this.id + this.progEx.eval();   
	} 
}