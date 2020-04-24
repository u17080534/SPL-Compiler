package ast.expression;

public class bool__ extends Expression 
{   
	private Expression boolEx;   

	public bool__(Expression e)
	{ 
		super(e);
		this.boolEx = e; 
		this.expr = "BOOL__";
	}  

	public String trans() 
	{       
		return this.boolEx.trans();   
	} 
}