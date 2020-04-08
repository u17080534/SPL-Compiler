package ast.expression;

public class cond_branch extends Expression 
{   
	private Expression boolEx, codeEx, cond_branch_Ex;   

	public cond_branch(Expression e1, Expression e2, Expression e3) 
	{ 
		this.boolEx = e1; 
		this.codeEx = e2; 
		this.cond_branch_Ex = e3; 
	}  

	public String eval() 
	{       
		return this.boolEx.eval() + this.codeEx.eval() + this.cond_branch_Ex.eval();   
	} 
}