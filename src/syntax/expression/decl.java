package syntax.expression;

import syntax.code.*;

public class decl extends Expression 
{   
	private Expression typeEx, nameEx, decl_Ex;   

	public decl(Expression e1, Expression e2, Expression e3) 
	{ 
		super(e1, e2, e3);
		this.expr = "DECL";
		this.typeEx = e1; 
		this.nameEx = e2; 
		this.decl_Ex = e2; 
	}  

	public Line trans(File absFile)
	{       
		System.out.println(this.expr);

		Line l1 = this.typeEx.trans(absFile);

		Line l2 = this.nameEx.trans(absFile);

		Line l3 = this.decl_Ex.trans(absFile);

		Line line = new Line(l1.toString() + l2.toString());
		
		// absFile.add(line);

		return null;   
	} 
}