package ast.expression;

public class proc_defs_ extends Expression 
{   
	private Expression proc_defsEx;   

	public proc_defs_()
	{
		super();
		this.expr = "PROC_DEFS_";
	}
	
	public proc_defs_(Expression e)
	{ 
		super(e);
		this.proc_defsEx = e; 
		this.expr = "PROC_DEFS_";
	}  

	public String trans() 
	{       
		return "";
	} 
}