package ast.expression;

public class assign_ extends Expression 
{   
	private TokenExpression literal;
	private Expression ex;   

	public assign_(TokenExpression literal) 
	{ 
		super(literal);
		this.literal = literal;
		this.expr = "ASSIGN_";
	}  

	public assign_(TokenExpression literal, Expression e) 
	{ 
		super(literal, e);
		this.literal = literal;
		this.ex = e;  
		this.expr = "ASSIGN_";
	}  

	public String eval() 
	{       
		return "";   
	}
}