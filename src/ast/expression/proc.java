package ast.expression;

public class proc extends Expression 
{   
	private String id;   
	private Expression progEx;   

	public proc(String id, Expression progEx) 
	{ 
		this.id = id; 
		this.progEx = progEx; 
	}  

	public String eval() 
	{       
		return this.id + this.progEx.eval();   
	} 
}