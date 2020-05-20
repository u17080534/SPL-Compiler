package syntax.expression;

import syntax.code.*;

//SPL-COMPILER
public class cond_branch extends Expression 
{   
	private TerminalExpression terminal;
	private Expression boolEx, codeEx, cond_branch_Ex;   

	public cond_branch(Expression e1, Expression e2, Expression e3) 
	{ 
		super(e1, e2, e3);
		this.boolEx = e1; 
		this.codeEx = e2; 
		this.cond_branch_Ex = e3; 
		this.expr = "COND_BRANCH";
	}  

	//CODE GEN FOR INSTR
	public Line trans(File absFile)
	{
		/*
		IF:
			N			TEMPC<ID> = NOT <bool>
			N			IF TEMPC<ID> THEN GOTO %END<ID>+1%
			%END<ID>%	<code>

		IF-ELSE:
			N			TEMPC<ID> = NOT <bool>
			N			IF TEMPC<ID> THEN GOTO %ELSE<ID>%
			N			<codex>
			N			GOTO %END<ID>+1%
			%ELSE<ID>%	<codey>
			%END<ID>%
		*/

		String temp = "TMPC" + this.getID();

		if(this.cond_branch_Ex == null)
		{
			absFile.add(new Line(temp + " = NOT " + this.boolEx.trans(absFile).toString()));

			absFile.add(new Line("IF " + temp + " THEN GOTO %END" + this.getID() + "+1%"));

			this.codeEx.trans(absFile);

			absFile.label("END" +  this.getID(), 1, false);
		}
		else
		{
			absFile.add(new Line(temp + " = NOT " + this.boolEx.trans(absFile).toString()));

			absFile.add(new Line("IF " + temp + " THEN GOTO %ELSE" + this.getID() + "%"));

			this.codeEx.trans(absFile);

			absFile.add(new Line("GOTO %END" + this.getID() + "+1%"));

			absFile.label("ELSE" +  this.getID(), true);

			this.cond_branch_Ex.trans(absFile);

			absFile.anchorLabel("ELSE" +  this.getID());

			absFile.label("END" +  this.getID(), 1, false);
		}

		return null;   
	} 
}