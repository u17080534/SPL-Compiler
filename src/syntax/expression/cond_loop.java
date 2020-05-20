package syntax.expression;

import syntax.code.*;

//SPL-COMPILER
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

	//CODE GEN FOR INSTR
	public Line trans(File absFile)
	{     
		Line codeTrans = null;

		String ty = this.type.trans(absFile).toString();

		if(ty.equals("while")) //while
		{
			/*
			%START<ID>%	TEMPB<ID> = NOT <bool>
			N			IF TEMPB<ID> THEN GOTO %END<ID>+1%
			N			<code>
			%END<ID>%	GOTO %START<ID>%
			*/

			String temp = "TEMPB" + this.getID();
			String startLabel = "START" + this.getID();
			String endLabel = "END" + this.getID();

			absFile.label(startLabel, true);

			absFile.add(new Line(temp + " = NOT " + this.condEx1.trans(absFile).toString()));

			absFile.anchorLabel(startLabel);

			absFile.add(new Line("IF " + temp + " THEN GOTO %END" + this.getID() + "+1%"));

			this.condEx2.trans(absFile);

			absFile.label(endLabel, true);

			absFile.add(new Line("GOTO %START" + this.getID() + "%"));

			absFile.anchorLabel(endLabel);
		}
		else if(ty.equals("for")) //for
		{
			//VARIABLE SET TO 0
			// this.condEx1 ; i = 0
			//VARIABLE <
			// this.condEx2 ; i <
			// < VARIABLE 
			// this.condEx3 ; < n
			//variable = add
			// this.condEx4 ; i = add()
			//variable added to
			// this.condEx5 ; = add(i,1)
			//CODE
			// this.condEx6 ; {}

			/*
			N			i = 0
			%START<ID>%	TEMPB<ID> = i < n
			N			TEMPB<ID> = NOT TEMPB<ID>
			N			IF TEMPB<ID> THEN GOTO %END<ID>+1%
			N			<code>
			N			i = i + 1
			%END<ID>%	GOTO %START<ID>%
			*/

			String temp = "TEMPB" + this.getID();
			String startLabel = "START" + this.getID();
			String endLabel = "END" + this.getID();

			absFile.add(new Line(this.condEx1.trans(absFile).toString() + " = 0"));

			absFile.label(startLabel, true);

			absFile.add(new Line(temp + " = " + this.condEx2.trans(absFile).toString() + " < " + this.condEx3.trans(absFile).toString()));

			absFile.anchorLabel(startLabel);

			absFile.add(new Line(temp + " = NOT " + temp));

			absFile.add(new Line("IF " + temp + " THEN GOTO %" + endLabel + "+1%"));

			this.condEx6.trans(absFile); //Code

			absFile.add(new Line(this.condEx4.trans(absFile).toString() + " = " + this.condEx5.trans(absFile).toString() + " + 1"));

			absFile.label(endLabel, true);

			absFile.add(new Line("GOTO %" + startLabel + "%"));

			absFile.anchorLabel(endLabel);
		}

		return null;
	} 
}