package ast.expression;

public class prog_ extends Expression 
{   
	private Expression proc_defsEx, code_Ex;   

	public prog_()
	{
		super();
		this.expr = "PROG_";
	}
	
	public prog_(Expression e1, Expression e2)
	{ 
		super(e1, e2);
		this.proc_defsEx = e1; 
		this.code_Ex = e2; 
		this.expr = "PROG_";
	}  

	public String eval() 
	{       
		return "";
	} 
} 