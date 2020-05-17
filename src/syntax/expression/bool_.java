package syntax.expression;

import syntax.code.*;

//SPL-COMPILER
public class bool_ extends Expression 
{   
	private Expression boolEx1, boolEx2;   

	public bool_(Expression e1, Expression e2)
	{ 
		super(e1, e2);
		this.boolEx1 = e1; 
		this.boolEx2 = e2; 
		this.expr = "BOOL_";
	}  

	public Line trans(File absFile)
	{
		/*
			BOOL_ will assign param values to two variables [TMPB<BOOL_ID><1/2>]
			Perform operation between variables.
			eg.
				* operation is and
				* BOOL's ID is 00
				* BOOL_'s ID is 99
				* TMPB991 and TMPB992 are assigned to the result variables of the contained arguments.
				TMPB00 = TMPB991 && TMPB992
		*/
				
		// This will assign two new variables to this expression that BOOL will use with and/or
		absFile.add(new Line("TMPB" + this.getID() + "1 = " + this.boolEx1.trans(absFile).toString()));
		absFile.add(new Line("TMPB" + this.getID() + "2 = " + this.boolEx1.trans(absFile).toString()));
		
		return null;   
	} 
}