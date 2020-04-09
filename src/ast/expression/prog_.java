package ast.expression;

public class prog_ extends Expression 
{   
	private Expression proc_defsEx, code_Ex;   

	public prog_()
	{
		super();
		this.expr = "PROG_";
	}
	
	public prog_(Expression e1)
	{ 
		super(e1);
		this.code_Ex = this.proc_defsEx = e1; 
		this.expr = "PROG_";
	}  

	public String eval() 
	{       
		return "";
	} 
} 