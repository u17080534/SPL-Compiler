package ast.expression;

public class var extends Expression 
{   
	private String id;   

	public var(String id) 
	{ 
		this.id = id; 
	}  

	public String eval() 
	{       
		return this.id;   
	}
}