package syntax.expression;

import syntax.code.*;

public class numexpr extends Expression 
{   
	private TerminalExpression number;
	private Expression ex;    

	public numexpr(TerminalExpression literal) 
	{ 
		super(literal);
		this.number = literal;
		this.ex = null;  
		this.expr = "NUMEXPR";
	}  

	public numexpr(Expression e) 
	{ 
		super(e);
		this.ex = e;  
		this.expr = "NUMEXPR";
	}  

	public Line trans(File absFile)
	{       
		return new Line("");      
	}
}