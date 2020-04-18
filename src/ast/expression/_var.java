package ast.expression;
import lexer.Token.*;

public class _var extends Expression 
{   
	private TokenExpression variable;   

	public _var(TokenExpression id) 
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