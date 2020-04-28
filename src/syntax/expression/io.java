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
	{System.out.println(this.expr);       
		String act = this.action.trans(absFile).toString();

		if(act.equals("output"))
			return new Line("PRINT " + this.varEx.trans(absFile));

		if(act.equals("input"))
		{
			String label = this.varEx.getLabel();
			absFile.add(new Line("INPUT " + label));
			return new Line(this.varEx.trans(absFile) + " = " + label);
		} 
		
		return null;
	} 
}