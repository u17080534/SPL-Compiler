package ast.expression;

public class assign_ extends Expression 
{   
	private String literal;
	private Expression expr;   

	public assign_(String literal) 
	{ 
		this.literal = literal;  
	}  

	public assign_(Expression expr) 
	{ 
		this.expr = expr;  
	}  

	public String eval() 
	{       
		return "";   
	}
}