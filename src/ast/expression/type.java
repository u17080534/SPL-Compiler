package ast.expression;
import lexer.*;
import lexer.Token;

public class type extends Expression 
{   
	private TokenExpression typex;

	public type(TokenExpression tok) 
	{ 
		super(tok);
		this.typex = tok;
		this.expr = "TYPE";
	}  

	public String eval() 
	{       
		return "";
	}
}