package syntax.expression;

import syntax.code.*;

public class prog_ extends Expression 
{   
	private Expression proc_defsEx, code_Ex;   

	public prog_()
	{
		super();
		this.expr = "PROG_";
	}
	
	public prog_(Expression e1)
	{ 
		super(e1);
		this.code_Ex = this.proc_defsEx = e1; 
		this.expr = "PROG_";
	}  

	public Line trans(File absFile)
	{       
		if(this.code_Ex == null)
			if(this.proc_defsEx == null)
				return null;
			else
				return this.proc_defsEx.trans(absFile);         

		return this.code_Ex.trans(absFile);         
	} 
} 