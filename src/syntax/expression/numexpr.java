package syntax.expression;

import syntax.code.*;

//SPL-COMPILER
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

	public numexpr(Expression ex) 
	{ 
		super(ex);
		this.number = null;
		this.ex = ex;  
		this.expr = "NUMEXPR";
	}  

	public Line trans(File absFile)
	{			
		String temp = "TMPN" + this.getID();

		if(this.number != null)    
			absFile.add(new Line(temp + " = " + this.number.trans(absFile)));
		else
			absFile.add(new Line(temp + " = " + this.ex.trans(absFile)));

		return new Line(temp);
	}
}