package syntax.expression;

import syntax.code.*;

//SPL-COMPILER
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
		String assn = this.literal.trans(absFile).toString(); //literal

		if(this.ex != null)
		{
			assn = "TMPA" + this.getID();
			absFile.add(new Line("TMPA" + this.getID() + " = " + this.ex.trans(absFile).toString())); //assign to temp var
		}

		return new Line(assn);
	}
}