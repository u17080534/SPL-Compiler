package ast.expression;
import lexer.Token.Tok;

public class io extends Expression 
{   
	private Tok action;   
	private Expression varEx;   

	public io(Tok e1, Expression e2) 
	{ 
		this.action = e1; 
		this.varEx = e2; 
	}  

	public String eval() 
	{       
		return this.action + this.varEx.eval();   
	} 
}