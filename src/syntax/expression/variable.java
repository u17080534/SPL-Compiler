package syntax.expression;

import syntax.code.*;
import lexer.Token.*;

public class variable extends Expression 
{   
	private TerminalExpression variable;   

	public variable(TerminalExpression id) 
	{ 
		super(id);
		this.variable = id;
		this.expr = "VARIABLE";
	}  

	public Line trans(File absFile)
	{       
		return new Line("");
	}
}