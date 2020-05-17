package syntax.expression;

import syntax.code.*;

//SPL-COMPILER
public class name extends Expression 
{   
	private TerminalExpression variable;   

	public name(TerminalExpression id) 
	{ 
		super(id);
		this.variable = id;
		this.expr = "NAME";
	}  

	public Line trans(File absFile)
	{
		return this.variable.trans(absFile);   
	}
}