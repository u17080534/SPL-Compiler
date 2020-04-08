package ast.expression;

public class numexpr extends Expression 
{   
	private String literal;
	private Expression expr;    

	public numexpr(String literal) 
	{ 
		this.literal = literal;  
	}  

	public numexpr(Expression expr) 
	{ 
		this.expr = expr;  
	}  

	public String eval() 
	{       
		return "";   
	}
}