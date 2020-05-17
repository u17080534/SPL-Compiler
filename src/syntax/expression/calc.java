package syntax.expression;

import syntax.code.*;
import lexer.Token;

//SPL-COMPILER
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

	//!address this
	public Line trans(File absFile)
	{
		/*
			CALC_ will assign param values to two variables [TMPC<CALC_ID><1/2>]
			Perform operation between variables.
			eg.
				* operation is +
				* CALC's ID is 00
				* CALC_'s ID is 99
				* TMPC991 and TMPC992 are assigned to the result variables of the contained arguments.
				TMPC00 = TMPC991 + TMPC992
		*/

		Line l1 = this.action.trans(absFile);
		
		Line l2 = this.calc_Ex.trans(absFile);

		String str = "";

		return new Line(l1.toString() + l2.toString());
	} 
}