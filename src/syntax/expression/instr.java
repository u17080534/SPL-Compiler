package syntax.expression;

import syntax.code.*;

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
		if(this.ex != null) 
			this.ex.trans(absFile);
		
		return null;
	}
}