package syntax.expression;

import syntax.code.*;

//SPL-COMPILER
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

	//NO CODE GEN FOR INSTR
	public Line trans(File absFile)
	{
		this.typeEx.trans(absFile);

		this.nameEx.trans(absFile);

		this.decl_Ex.trans(absFile);
		
		return null;   
	} 
}