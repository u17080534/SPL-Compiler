package syntax.expression;

import syntax.code.*;

//SPL-COMPILER
public class prog extends Expression 
{   
	private Expression codeEx, prog_Ex_;   

	public prog(Expression e1, Expression e2)
	{ 
		super(e1, e2);
		this.expr = "PROG";
		this.codeEx = e1; 
		this.prog_Ex_ = e2; 
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
		if(this.symbol.getScope() == 0) //Label code block start
			absFile.label("START", true);
		else
			absFile.label(this.symbol.getProc(), true);

		this.codeEx.trans(absFile); //Add lines of code block

		if(this.symbol.getScope() == 0) //Release label anchor, so that it will move when lines are added before it
			absFile.anchorLabel("START");
		else
		{
			absFile.add(new Line("RETURN"));
			absFile.anchorLabel(this.symbol.getProc());
		}

		if(this.prog_Ex_ != null)
			this.prog_Ex_.trans(absFile);   
 
		return null;
	} 
} 