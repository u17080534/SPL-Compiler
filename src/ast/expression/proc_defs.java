package ast.expression;

public class proc_defs extends Expression 
{   
	private Expression procEx, proc_defs_Ex;   

	public proc_defs(Expression e1, Expression e2)
	{ 
		super(e1, e2);
		this.expr = "PROC_DEFS";
		this.procEx = e1; 
		this.proc_defs_Ex = e2; 
	}  

	public String eval() 
	{       
		return this.procEx.eval() + this.proc_defs_Ex.eval();   
	} 
}