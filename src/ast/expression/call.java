package ast.expression;

public class call extends Expression 
{   
	private String id;   

	public call(String id) 
	{ 
		this.id = id; 
	}  

	public String eval() 
	{       
		return this.id;   
	}
}