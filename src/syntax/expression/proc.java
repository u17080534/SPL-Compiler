package syntax.expression;

import syntax.code.*;

//SPL-COMPILER
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

	//CODE GEN FOR INSTR
	public Line trans(File absFile)
	{
		int oldIndex = absFile.point(0); //where proc is defined in upper level

		int size = absFile.size();

		this.progEx.trans(absFile);

		size = absFile.size() - size;

		absFile.point(oldIndex + size);

		return null; 
	} 
}