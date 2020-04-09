package ast.expression;

public class instr extends Expression 
{   
	private Expression ex;   

	public instr() 
	{ 
		this.expr = "INSTR";
	} 

	public instr(Expression e) 
	{ 
		super(e);
		this.expr = "INSTR";
		this.ex = e; 
	}  

	public String eval() 
	{       
		return this.ex.eval();   
	}
}