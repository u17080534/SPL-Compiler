package syntax.expression;

import syntax.code.*;
import lexer.Token;

public class calc extends Expression 
{   
	private TerminalExpression action;
	private Expression calc_Ex;   

	public calc(TerminalExpression e1, Expression e2) 
	{ 
		super(e1, e2);
		this.action = e1;
		this.calc_Ex = e2; 
		this.expr = "CALC";
	}  

	public Line trans(File absFile)
	{       
		System.out.println(this.expr);

		Line l1 = this.action.trans(absFile);
		
		Line l2 = this.calc_Ex.trans(absFile);

		String str = "";

		return new Line(l1.toString() + l2.toString());
	} 
}