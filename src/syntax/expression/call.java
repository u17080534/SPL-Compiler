package syntax.expression;

import syntax.code.*;

//SPL-COMPILER
public class call extends Expression 
{   
	private TerminalExpression func;   

	public call(TerminalExpression id) 
	{ 
		super(id);
		this.func = id;
		this.expr = "CALL";
	}  

	//CODE GEN FOR INSTR
	public Line trans(File absFile)
	{
		absFile.add(new Line("GOSUB %" + this.func.trans(absFile).toString() + "%"));   
		
		return null;
	}
}