package ast.expression;

public class prog extends Expression 
{   
	private Expression codeEx, prog_Ex;   

	public prog(Expression e1, Expression e2) : Expression(e1, e2) 
	{ 
		this.codeEx = e1; 
		this.prog_Ex = e2; 
	}  

	public String eval() 
	{       
		return this.codeEx.eval() + this.prog_Ex.eval();   
	} 
} 