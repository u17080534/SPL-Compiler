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
		this.symbol.setScope(scope + 1);

		for (Expression desc : this.descendents)
			desc.scope(this.symbol.getScope());
	}

	public String trans() 
	{       
		return this.codeEx.trans() + this.prog_Ex.trans();   
	} 
} 