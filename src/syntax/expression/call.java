package syntax.expression;

import syntax.code.*;

public class call extends Expression 
{   
	private TerminalExpression func;   

	public call(TerminalExpression id) 
	{ 
		super(id);
		this.func = id;
		this.expr = "CALL";
	}  

	public Line trans(File absFile)
	{System.out.println(this.expr);       
		//GOSUB %ID%
		return new Line("GOSUB %" + this.func.trans(absFile).toString() + "%");   
	}
}