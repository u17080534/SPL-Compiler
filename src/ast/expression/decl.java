package ast.expression;

public class decl extends Expression 
{   
	private Expression typeEx, nameEx, code_Ex;   

	public decl(Expression e1, Expression e2, Expression e3) 
	{ 
		this.typeEx = e1; 
		this.nameEx = e2; 
		this.code_Ex = e2; 
	}  

	public String eval() 
	{       
		return this.typeEx.eval() + this.nameEx.eval() + this.code_Ex.eval();   
	} 
}