package ast.expression;

public class name extends Expression 
{   
	private String id;   

	public name(String id) 
	{ 
		this.id = id; 
	}  

	public String eval() 
	{       
		return this.id;   
	}
}