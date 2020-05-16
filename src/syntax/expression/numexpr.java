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

	@Override
	public String getTerminalType()
	{
		String type = type = this.descendents.get(0).getTerminalType();

		for (int index = 1; index < this.descendents.size(); index++)
		{
			if(!type.equals(this.descendents.get(index).getTerminalType()))
				return "";
		}
		this.symbol.setType(type);
		return type;
	}

	public Line trans(File absFile)
	{
		//System.out.println(this.expr);       
		if(this.number != null)    
			return this.number.trans(absFile);
		
		return this.ex.trans(absFile);
	}
}