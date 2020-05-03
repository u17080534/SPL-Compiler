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

		System.out.println("LAST LINE: " + absFile.getPointed().toString());

		int index = absFile.getLabel(this.proced.getSymbol().getProc()); //pos of current proc in file

		int oldIndex = absFile.point(0); //where proc is defined in upper level
		int diff = absFile.getPointer();
			// System.out.println(this.proced + " " + this.progEx + " OLD -> "+oldIndex);

			// System.out.println("\t"+this.proced.trans(absFile).toString() + " > "+this.proced.getSymbol().getProc() + " " + index);

			// System.out.println(absFile.getLabels());
		
		absFile.label(this.proced.trans(absFile).toString());
		
		Line line = this.progEx.trans(absFile);

		if(absFile.getPointed().toString().indexOf("RETURN") < 0)
			absFile.add(new Line("RETURN"), true);

		// if(!this.proced.getSymbol().getProc().equals(""))
		// 	absFile.label(this.proced.getSymbol().getProc());

		absFile.label("PROC_DEFS");

		diff = absFile.getPointer() - diff;

		if(oldIndex == -1)
			diff = 0;

		absFile.point(oldIndex + diff);

			System.out.println("THIS proced: " + this.proced + "\n\tPROGEX: " + this.progEx + "\n\tOLDindex -> "+oldIndex);

			System.out.println("\t"+this.proced.trans(absFile).toString() + " > "+this.proced.getSymbol().getProc() + " " + index);

			System.out.println(absFile.getLabels());

			System.out.println("LINE " + line);

		return line; 
	} 
}