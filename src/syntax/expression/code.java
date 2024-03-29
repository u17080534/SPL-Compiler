package syntax.expression;

import syntax.code.*;

//SPL-COMPILER
public class code extends Expression 
{   
	private Expression instrEx, code_Ex;   

	public code(Expression e1, Expression e2)
	{ 
		super(e1, e2);
		this.expr = "CODE";
		this.instrEx = e1; 
		this.code_Ex = e2; 
	}  

	public Line trans(File absFile)
	{
		this.instrEx.trans(absFile);

		if(this.code_Ex != null)
			this.code_Ex.trans(absFile);
		
		return null;
	} 
}