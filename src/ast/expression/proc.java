package ast.expression;

public class proc extends Expression 
{   
	private TokenExpression proced;   
	private Expression progEx;   

	public proc(TokenExpression e1, Expression e2) 
	{ 
		super(e1, e2);
		this.proced = e1;
		this.progEx = e2; 
		this.expr = "PROC";
	}  

	public String trans() 
	{       
		return this.id + this.progEx.trans();   
	} 
}