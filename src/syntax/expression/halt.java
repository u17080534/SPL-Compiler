package syntax.expression;

import syntax.code.*;

public class halt extends Expression 
{   
	public halt() 
	{ 
		super();
		this.expr = "HALT";
	}  

	public Line trans(File absFile)
	{       
		return new Line("GOTO %END%");   
	}
}