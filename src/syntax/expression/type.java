package syntax.expression;

import syntax.code.*;
import lexer.*;
import lexer.Token;

//SPL-COMPILER
public class type extends Expression 
{   
	private TerminalExpression typex;

	public type(TerminalExpression tok) 
	{ 
		super(tok);
		this.typex = tok;
		this.expr = "TYPE";
	}  

	public Line trans(File absFile)
	{
		return this.typex.trans(absFile);      
	}
}