package syntax.expression;

import syntax.code.*;

//SPL-COMPILER
public class proc extends Expression 
{   
	private TerminalExpression proced;   
	private Expression progEx;   

	public proc(TerminalExpression e1, Expression e2) 
	{ 
		super(e1, e2);
		this.proced = e1;
		this.progEx = e2; 
		this.expr = "PROC";
	}  

	public Line trans(File absFile)
	{
		//System.out.println(this.expr);       
		//Label the start of the proc


		int index = absFile.getLabel(this.proced.getSymbol().getProc()); //pos of current proc in file

		int oldIndex = absFile.point(0); //where proc is defined in upper level
		int diff = absFile.getPointer();
		
		absFile.label(this.proced.trans(absFile).toString());
		
		Line line = this.progEx.trans(absFile);

		//Removes Duplicate RETURNS caused by nested procs without instructions (mostly needed for robustness and handling user inefficiencies)
		if(absFile.getPointed(-1).toString().indexOf("RETURN") < 0)
			absFile.add(new Line("RETURN"), true);

		absFile.label("PROC_DEFS");

		diff = absFile.getPointer() - diff;

		if(oldIndex == -1)
			diff = 0;

		absFile.point(oldIndex + diff);

		return line; 
	} 
}