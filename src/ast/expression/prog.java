package ast.expression;

public class prog extends Expression 
{   
	private Expression codeEx, prog_Ex;   

	public prog(Expression e1, Expression e2)
	{ 
		super(e1, e2);
		this.expr = "PROG";
		this.codeEx = e1; 
		this.prog_Ex = e2; 
	}  

	@Override
	public void scope(int scope)
	{
		this.scope = scope + 1;

		for (Expression desc : this.descendents)
			desc.scope(this.scope);
	}

	public String eval() 
	{       
		return this.codeEx.eval() + this.prog_Ex.eval();   
	} 
} 