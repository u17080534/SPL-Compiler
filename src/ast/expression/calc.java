package ast.expression;
import lexer.Token;

public class calc extends Expression 
{   
	private TokenExpression action;
	private Expression calc_Ex;   

	public calc(TokenExpression e1, Expression e2) 
	{ 
		super(e1, e2);
		this.action = e1;
		this.calc_Ex = e2; 
		this.expr = "CALC";
	}  

	public String trans() 
	{       
		return "";   
	} 
}