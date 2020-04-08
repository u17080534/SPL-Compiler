package ast.expression;
import lexer.Token.Tok;

public class bool extends Expression 
{   
	private Tok action; // T F ID EQ LT GT NOT AND OR
	private Expression e1;   
	private Expression e2;   

	public bool(Tok action) 
	{ 
		this.action = action;
		// this.e1 = NULL; 
		// this.e2 = NULL; 
	}

	public bool(Tok action, Expression e1) 
	{ 
		this.action = action;
		this.e1 = e1; 
		// this.e2 = NULL; 
	}

	public bool(Tok action, Expression e1, Expression e2) 
	{ 
		this.action = action;
		this.e1 = e1; 
		this.e2 = e2; 
	}  

	public String eval() 
	{       
		return "";   
	} 
}