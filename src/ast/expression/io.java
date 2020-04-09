package ast.expression;
import lexer.Token;

public class io extends Expression 
{   
	private TokenExpression action;   
	private Expression varEx;   

	public io(TokenExpression e1, Expression e2) 
	{ 
		super(e1, e2);
		this.action = e1;
		this.varEx = e2; 
		this.expr = "IO";
	}  

	public String eval() 
	{       
		return this.action + this.varEx.eval();   
	} 
}