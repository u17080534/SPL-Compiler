package syntax.expression;

import syntax.code.*;

public class name extends Expression 
{   
	private TerminalExpression variable;   

	public name(TerminalExpression id) 
	{ 
		super(id);
		this.variable = id;
		this.expr = "NAME";
	}  

	public Line trans(File absFile)
	{       
		String name = this.variable.trans(absFile).toString();

		//String variables append $
		if(this.variable.getType().equals("string"))
			name += "$";

		return new Line(name);   
	}
}