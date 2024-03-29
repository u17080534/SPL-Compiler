package syntax.expression;

import syntax.code.*;

//SPL-COMPILER
public class proc_defs extends Expression 
{   
	private Expression procEx, proc_defs_Ex;   

	public proc_defs(Expression e1, Expression e2)
	{ 
		super(e1, e2);
		this.expr = "PROC_DEFS";
		this.procEx = e1; 
		this.proc_defs_Ex = e2; 
	}  

	public Line trans(File absFile)
	{
		this.procEx.trans(absFile);
		
		if(this.proc_defs_Ex != null)
			this.proc_defs_Ex.trans(absFile);

		return null;
	} 
}