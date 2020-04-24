package ast.expression;
import lexer.Token.*;

public class var_ extends Expression 
{   
	private TokenExpression variable;   

	public var_(TokenExpression id) 
	{ 
		super(id);
		this.variable = id;
		this.expr = "VAR";
	}  

	public String trans() 
	{       
		return "";  
	}
}