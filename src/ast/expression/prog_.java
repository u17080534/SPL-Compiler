package ast.expression;

public class prog_ extends Expression 
{   
	private Expression proc_defsEx;   

	public prog_()
	{

	}
	
	public prog_(Expression e) : Expression(e) 
	{ 
		this.proc_defsEx = e; 
	}  

	public String eval() 
	{       
		// return (this.proc_defsEx == NULL) ? "" : this.proc_defsEx.eval();   
		return "";
	} 
} 