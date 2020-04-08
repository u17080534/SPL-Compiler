package ast.expression;
import lexer.Token.Tok;

public class type extends Expression 
{   
	private Tok type;

	public type(Tok type) 
	{ 
		this.type = type; 
	}  

	public String eval() 
	{       
		return "";
	}
}