package ast.expression;

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

	public String eval() 
	{       
		return this.varEx.eval() + this.assign_Ex.eval();   
	}
}