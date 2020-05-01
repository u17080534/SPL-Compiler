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
		//System.out.println(this.expr);    

		Line line = null;

		if(this.codeEx != null)
			line = this.codeEx.trans(absFile);

		if(this.prog_Ex != null)
			this.prog_Ex.trans(absFile);   
 
		return line;
	} 
} 