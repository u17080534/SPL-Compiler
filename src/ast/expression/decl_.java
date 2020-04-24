package ast.expression;

public class decl_ extends Expression 
{   
	private Expression declEx;   

	public decl_() 
	{ 
		super();
		this.expr = "DECL_";
	}  

	public decl_(Expression e1) 
	{ 
		super(e1);
		this.expr = "DECL_";
		this.declEx = e1; 
	}  

	public String trans() 
	{       
		return "";
	} 
}