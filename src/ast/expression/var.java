package ast.expression;
import lexer.Token.*;

public class var extends Expression 
{   
	private TokenExpression variable;   

	public var(TokenExpression id) 
	{ 
		super(id);
		this.variable = id;
		this.expr = "VAR";
	}  

	public String eval() 
	{       
		return "";  
	}
}