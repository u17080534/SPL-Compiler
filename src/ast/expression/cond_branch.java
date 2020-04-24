package ast.expression;

public class cond_branch extends Expression 
{   
	private Expression boolEx, codeEx, cond_branch_Ex;   

	public cond_branch(Expression e1, Expression e2, Expression e3) 
	{ 
		super(e1, e2, e3);
		this.expr = "COND_BRANCH";
		this.boolEx = e1; 
		this.codeEx = e2; 
		this.cond_branch_Ex = e3; 
	}  

	public String trans() 
	{       
		return this.boolEx.trans() + this.codeEx.trans() + this.cond_branch_Ex.trans();   
	} 
}