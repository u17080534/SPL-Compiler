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

		System.out.println(this.proced + " " + this.progEx + " OLD -> "+oldIndex);
		System.out.println("\t"+this.proced.trans(absFile).toString() + " > "+this.proced.getSymbol().getProc() + " " + index);
		System.out.println(absFile.getLabels());
		
		absFile.label(this.proced.trans(absFile).toString());
		
		Line line = this.progEx.trans(absFile);

		absFile.add(new Line("RETURN"));

		System.out.println("00	"+line);

		absFile.label("PROC_DEFS");

		oldIndex = absFile.point(oldIndex + 1);

		System.out.println("00"+this.proced + " " + this.progEx + " OLD -> "+oldIndex);

		System.out.println("\t"+this.proced.trans(absFile).toString() + " > "+this.proced.getSymbol().getProc() + " " + index);

		System.out.println(absFile.getLabels());

		return line; 
	} 
}