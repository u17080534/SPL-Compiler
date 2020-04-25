package syntax.expression;

import syntax.code.*;

public class assign_ extends Expression 
{   
	private TerminalExpression literal; //string
	private Expression ex; //VAR/NUMEXPR/BOOL

	public assign_(TerminalExpression literal) 
	{ 
		super(literal);
		this.literal = literal;
		this.ex = null;  
		this.expr = "ASSIGN_";
	}  

	public assign_(TerminalExpression literal, Expression e) 
	{ 
		super(literal, e);
		this.literal = literal;
		this.ex = e;  
		this.expr = "ASSIGN_";
	}  

	public Line trans(File absFile)
	{       
		Line line;

		if(this.ex != null)
			line = new Line(this.ex.trans(absFile).toString());  
		else
			line = new Line(this.ex.trans(absFile).toString());  

		return line;
	}
}