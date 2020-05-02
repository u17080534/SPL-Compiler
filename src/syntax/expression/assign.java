package syntax.expression;

import syntax.code.*;

//SPL-COMPILER
public class assign extends Expression 
{   
	private Expression varEx, assign_Ex;   

	public assign(Expression e1, Expression e2)
	{ 
		super(e1, e2);
		this.expr = "ASSIGN";
		this.varEx = e1; 
		this.assign_Ex = e2; 
	}  

	public Line trans(File absFile)
	{
		Line l1 = this.varEx.trans(absFile);
		
		Line l2 = this.assign_Ex.trans(absFile);

		String str = l1.toString() + " = " + l2.toString();

		return new Line(str);
	}
}