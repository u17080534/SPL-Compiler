package ast.expression;

public class TokenExpression extends Expression 
{   
	private String type;   
	private String value;   

	public TokenExpression(String type, String value)
	{
		super();
		this.type = type;
		this.value = value;

		this.expr = this.type + " '" + this.value + "'";
	}

	public String eval() 
	{       
		return "";
	}

	@Override
	public boolean isTerminal()
	{
		return true;
	} 
} 