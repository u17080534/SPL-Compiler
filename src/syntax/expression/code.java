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
		System.out.println(this.expr);

		Line l1 = this.instrEx.trans(absFile);
		
		absFile.add(l1);

		Line l2 = this.code_Ex.trans(absFile);
		
		return l1;
	} 
}