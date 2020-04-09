package ast.expression;

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

	public String eval() 
	{       
		return this.instrEx.eval() + this.code_Ex.eval();   
	} 
}