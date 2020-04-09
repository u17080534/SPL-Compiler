package ast.expression;

public class numexpr extends Expression 
{   
	private TokenExpression number;
	private Expression ex;    

	public numexpr(TokenExpression literal) 
	{ 
		super(literal);
		this.number = literal;
		this.expr = "NUMEXPR";
	}  

	public numexpr(Expression e) 
	{ 
		super(e);
		this.expr = "NUMEXPR";
		this.ex = e;  
	}  

	public String eval() 
	{       
		return "";   
	}
}