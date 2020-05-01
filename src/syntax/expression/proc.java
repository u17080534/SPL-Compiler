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
		//System.out.println(this.expr);       
		//Label the start of the proc
		absFile.point(0);
		
		absFile.label(this.proced.trans(absFile).toString());

		this.progEx.trans(absFile);

		absFile.add(new Line("RETURN"));

		absFile.label("PROC_DEFS");

		absFile.point();

		return null; 
	} 
}