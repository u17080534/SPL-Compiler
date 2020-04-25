package syntax.expression;

import syntax.code.*;

public class decl extends Expression 
{   
	private Expression typeEx, nameEx, code_Ex;   

	public decl(Expression e1, Expression e2, Expression e3) 
	{ 
		super(e1, e2, e3);
		this.expr = "DECL";
		this.typeEx = e1; 
		this.nameEx = e2; 
		this.code_Ex = e2; 
	}  

	public Line trans(File absFile)
	{       
		return new Line("");   
	} 
}