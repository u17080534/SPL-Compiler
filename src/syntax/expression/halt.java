package syntax.expression;

import syntax.code.*;

//SPL-COMPILER
public class halt extends Expression 
{   
	public halt() 
	{ 
		super();
		this.expr = "HALT";
	}  

	//CODE GEN FOR INSTR
	public Line trans(File absFile)
	{
		absFile.add(new Line("GOTO %END%"));

		return null;
	}
}