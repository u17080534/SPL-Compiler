package syntax.expression;

import syntax.code.*;

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
		Line line;

		if(this.code_Ex.trans(absFile) == null)
			line = this.instrEx.trans(absFile);   
		else
			line = new Line(this.instrEx.trans(absFile).toString() + this.code_Ex.trans(absFile).toString());   

		return line;
	} 
}