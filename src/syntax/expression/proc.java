package syntax.expression;

import syntax.code.*;

public class proc extends Expression 
{   
	private TerminalExpression proced;   
	private Expression progEx;   

	public proc(TerminalExpression e1, Expression e2) 
	{ 
		super(e1, e2);
		this.proced = e1;
		this.progEx = e2; 
		this.expr = "PROC";
	}  

	public Line trans(File absFile)
	{       
		System.out.println(this.expr);

		Line l1 = this.proced.trans(absFile);
		
		boolean lbl = absFile.label(l1.toString());

		Line l2 = this.progEx.trans(absFile);

		if(l1 != null)
			absFile.add(new Line("RETURN"));

		return null; 
	} 
}