package syntax.expression;

import syntax.code.*;

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

	public Line trans(File absFile)
	{       
		this.codeEx.trans(absFile);
		this.prog_Ex.trans(absFile);   
		return null;
	} 
} 