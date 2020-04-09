package ast.expression;

public class call extends Expression 
{   
	private TokenExpression func;   

	public call(TokenExpression id) 
	{ 
		super(id);
		this.func = id;
		this.expr = "CALL";
	}  

	public String eval() 
	{       
		return "";   
	}
}