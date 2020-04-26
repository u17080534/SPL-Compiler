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
		Line l1 = this.literal.trans(absFile);

		String str = l1.toString();

		if(this.ex != null)
		{
			Line l2 = this.ex.trans(absFile);

			if(l2 != null)
				str = l2.toString();
		}

		return new Line(str);
	}
}