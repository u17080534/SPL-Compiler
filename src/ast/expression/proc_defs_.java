package ast.expression;

public class proc_defs_ extends Expression 
{   
	private Expression proc_defsEx;   

	public proc_defs_()
	{

	}
	
	public proc_defs_(Expression e) : Expression(e) 
	{ 
		this.proc_defsEx = e; 
	}  

	public String eval() 
	{       
		// return (this.proc_defsEx == NULL) ? "" : this.proc_defsEx.eval();   
		return "";
	} 
}