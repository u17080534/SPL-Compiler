package syntax.expression;

import syntax.code.*;
import lexer.Token.*;

//SPL-COMPILER
public class variable extends Expression 
{   
	private TerminalExpression variable;   

	public variable(TerminalExpression id) 
	{ 
		super(id);
		this.variable = id;
		this.expr = "VARIABLE";
	}  

	public String getLabel()
	{
		return this.variable.getLabel();
	}

	public Line trans(File absFile)
	{
		String name = this.variable.trans(absFile).toString();

		//String variables append $
		if(this.variable.getType().equals("S"))
			name += "$";

		return new Line(name);   
	}
}