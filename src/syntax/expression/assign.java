package syntax.expression;

import syntax.code.*;

public class assign extends Expression 
{   
	private Expression varEx, assign_Ex;   

	public assign(Expression e1, Expression e2)
	{ 
		super(e1, e2);
		this.expr = "ASSIGN";
		this.varEx = e1; 
		this.assign_Ex = e2; 
	}  

	public Line trans(File absFile)
	{       
		Line line = new Line(this.varEx.trans(absFile).toString() + this.assign_Ex.trans(absFile).toString());
		absFile.add(line);
		return line;
	}
}