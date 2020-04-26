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
	{       
		System.out.println(this.expr);
		
		String str = this.action.trans(absFile).toString();

		if(str.equals("output"))
			str = "PRINT";
		else if(str.equals("input"))
			str = "INPUT";

		Line line = new Line(str + " " + this.varEx.trans(absFile)); 
		
		return line;  
	} 
}