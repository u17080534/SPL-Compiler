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
	//!address this
	public Line trans(File absFile)
	{
		/*
		IF:
			N   		 	TMPC = <BOOL>
			N   			TMPC = NOT TMPC
			N   			IF TMPC THEN GOTO %IF_END+1%
			%IF_END%        <CODE>
		IF-ELSE:
			N   		 	TMPC = <BOOL>
			N   			IF TMPC THEN GOTO %IF_ELSE%
			N   			<ELSE>
			N 				GOTO %IF_END+1%
			%IF_ELSE%    	TMPC = NOT TMPC
			N 			   	IF TMPC THEN GOTO %IF_END+1%
			%IF_END%        <CODE>
		*/

		Line codeTrans = null;

		String lblElse = "IF_ELSE" + this.getID();

		String lblEnd = "IF_END" + this.getID();

		if(this.cond_branch_Ex == null)
		{
			absFile.add(new Line("TMPC = " + this.boolEx.trans(absFile).toString()));

			absFile.add(new Line("TMPC = NOT TMPC"));

			absFile.add(new Line("IF TMPC THEN GOTO %" + lblEnd + "+1%"));

			codeTrans = this.codeEx.trans(absFile);

			absFile.label(lblEnd, 1);
		}
		else
		{
			absFile.add(new Line("TMPC = " + this.boolEx.trans(absFile).toString()));

			absFile.add(new Line("IF TMPC THEN GOTO %" + lblElse + "%"));

			this.cond_branch_Ex.trans(absFile);

			absFile.add(new Line("GOTO %" + lblEnd + "+1%"));

			absFile.label(lblElse);

			absFile.add(new Line("TMPC = NOT TMPC"));

			absFile.add(new Line("IF TMPC THEN GOTO %" + lblEnd + "+1%"));

			codeTrans = this.codeEx.trans(absFile);

			absFile.label(lblEnd, 1);
		}

		return null;   
	} 
}