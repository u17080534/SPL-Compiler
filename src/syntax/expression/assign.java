package syntax.expression;

import syntax.code.*;

//SPL-COMPILER
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

	//CODE GEN FOR INSTR
	public Line trans(File absFile)
	{
		String name = this.varEx.trans(absFile).toString();
		
		String assn = this.assign_Ex.trans(absFile).toString();

		absFile.add(new Line(name + " = " + assn));

		return null;
	}
}