package syntax.expression;

import syntax.code.*;

//SPL-COMPILER
public class numexpr extends Expression 
{   
	private TerminalExpression number;
	private Expression ex;    

	public numexpr(TerminalExpression literal) 
	{ 
		super(literal);
		this.number = literal;
		this.ex = null;  
		this.expr = "NUMEXPR";
	}  

	public numexpr(Expression e) 
	{ 
		super(e);
		this.number = null;
		this.ex = e;  
		this.expr = "NUMEXPR";
	}  

	public Line trans(File absFile)
	{
		//System.out.println(this.expr);       
		if(this.number != null)    
			return this.number.trans(absFile);
		
		return this.ex.trans(absFile);
	}
}