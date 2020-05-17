package syntax.expression;

import syntax.code.*;
import lexer.Token;

//SPL-COMPILER
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

	//CODE GEN FOR INSTR
	public Line trans(File absFile)
	{
		String act = this.action.trans(absFile).toString();

		if(act.equals("output"))
			absFile.add(new Line("PRINT " + this.varEx.trans(absFile)));

		else if(act.equals("input"))
		{
			String label = this.varEx.getLabel();
			absFile.add(new Line("INPUT " + label));
			absFile.add(new Line(this.varEx.trans(absFile) + " = " + label));
		} 
		
		return null;
	} 
}