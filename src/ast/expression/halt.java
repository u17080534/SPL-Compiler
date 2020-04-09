package ast.expression;

public class halt extends Expression 
{   
	public halt() 
	{ 
		super();
		this.expr = "HALT";
	}  

	public String eval() 
	{       
		return "";   
	}
}