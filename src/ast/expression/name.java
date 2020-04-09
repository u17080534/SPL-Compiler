package ast.expression;

public class name extends Expression 
{   
	private TokenExpression variable;   

	public name(TokenExpression id) 
	{ 
		super(id);
		this.variable = id;
		this.expr = "NAME";
	}  

	public String eval() 
	{       
		return "";   
	}
}