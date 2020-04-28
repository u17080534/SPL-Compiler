package syntax.expression;

import syntax.code.*;

public class cond_loop extends Expression 
{   
	private TerminalExpression type;
	private Expression condEx1, condEx2, condEx3, condEx4, condEx5, condEx6;   

	public cond_loop(TerminalExpression e1, Expression e2, Expression e3) 
	{ 
		super(e1, e2, e3);
		this.type = e1; 
		this.condEx1 = e2; 
		this.condEx2 = e3; 
		this.expr = "COND_LOOP";
	}  

	public cond_loop(TerminalExpression e1, Expression e2, Expression e3, Expression e4, Expression e5, Expression e6, Expression e7) 
	{ 
		super(e1, e2, e3, e4, e5, e6, e7);
		this.type = e1; 
		this.condEx1 = e2; 
		this.condEx2 = e3; 
		this.condEx3 = e4; 
		this.condEx4 = e5; 
		this.condEx5 = e6; 
		this.condEx6 = e7; 
		this.expr = "COND_LOOP";
	}  

	public Line trans(File absFile)
	{System.out.println(this.expr);       
		Line codeTrans = null;

		String ty = this.type.trans(absFile).toString();

		if(ty.equals("while")) //while
		{
			/*
			%LOOP_START% 	TMPW = <BOOL>
			N   			TMPW = NOT TMPW
			N   			IF TMPW THEN GOTO %LOOP_END+1%
			N               <CODE>
			%LOOP_END%      GOTO %LOOP_START%
			*/

			String temp = "TMPW" + this.getID();

			String lblStart = "LOOP_START" + this.getID();

			String lblEnd = "LOOP_END" + this.getID();

			absFile.label(lblStart);

			absFile.add(new Line(temp + " = " + this.condEx1.trans(absFile).toString()));

			absFile.add(new Line(temp + " = NOT " + temp));

			absFile.add(new Line("IF " + temp + " THEN GOTO %" + lblEnd + "+1%"));

			codeTrans = this.condEx2.trans(absFile);

			absFile.label(lblEnd);

			absFile.add(new Line("GOTO %" + lblStart + "%"));
		}
		else if(ty.equals("for")) //for
		{
			//VARIABLE SET TO 0
			// this.condEx1
			//VARIABLE <
			// this.condEx2
			// < VARIABLE
			// this.condEx3
			//variable = add
			// this.condEx4
			//variable added to
			// this.condEx5
			//CODE
			// this.condEx6

			/*
			N IF BOOL THEN GOTO %END_IF%
			N // else
			N RETURN
			%END_IF% // if EQ
			*/
		}

		return null;
	} 
}