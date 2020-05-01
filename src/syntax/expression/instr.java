package syntax.expression;

import syntax.code.*;

//SPL-COMPILER
public class instr extends Expression 
{   
	private Expression ex;   

	public instr() 
	{ 
		this.expr = "INSTR";
		this.ex = null; 
	} 

	public instr(Expression e) 
	{ 
		super(e);
		this.expr = "INSTR";
		this.ex = e; 
	}  

	public Line trans(File absFile)
	{
		//System.out.println(this.expr);      
		if(this.ex == null) 
			return null;
		
		return this.ex.trans(absFile);
	}
}