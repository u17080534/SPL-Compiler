package ast.expression;
import lexer.Token;

public class bool extends Expression 
{   
	private TokenExpression action; // T F ID EQ LT GT NOT AND OR
	private Expression e1;   
	private Expression e2;   

	public bool(TokenExpression action) 
	{ 
		super(action);
		this.action = action;
		this.expr = "BOOL";
	}

	public bool(TokenExpression action, Expression e1) 
	{ 
		super(action);
		this.action = action;
		this.expr = "BOOL";
		this.e1 = e1; 
	}

	public bool(TokenExpression action, Expression e1, Expression e2) 
	{ 
		super(action);
		this.action = action;
		this.expr = "BOOL";
		this.e1 = e1; 
		this.e2 = e2; 
	}  

	public String eval() 
	{       
		return "";   
	} 
}