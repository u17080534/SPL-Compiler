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
	{System.out.println(this.expr);       
		return new Line("GOTO %END%");   
	}
}