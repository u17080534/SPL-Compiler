package syntax.expression;

import syntax.code.*;
import lexer.Token;

public class io extends Expression 
{   
	private TerminalExpression action;   
	private Expression varEx;   

	public io(TerminalExpression e1, Expression e2) 
	{ 
		super(e1, e2);
		this.action = e1;
		this.varEx = e2; 
		this.expr = "IO";
	}  

	public Line trans(File absFile)
	{       
		Line line = new Line(this.action.trans(absFile) + " " + this.varEx.trans(absFile)); 
		
		absFile.add(line);

		return line;  
	} 
}