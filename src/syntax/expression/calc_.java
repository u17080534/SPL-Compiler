package syntax.expression;

import syntax.code.*;

//SPL-COMPILER
public class calc_ extends Expression 
{   
	private Expression numexprEx1, numexprEx2;   

	public calc_(Expression e1, Expression e2)
	{ 
		super(e1, e2);
		
		this.numexprEx1 = e1; 

		this.numexprEx2 = e2; 

		this.expr = "CALC_";
	}  

	public Line trans(File absFile)
	{		
		absFile.add(new Line("TMPC" + this.getID() + "1 = " + this.numexprEx1.trans(absFile).toString()));
		absFile.add(new Line("TMPC" + this.getID() + "2 = " + this.numexprEx2.trans(absFile).toString()));

		return null;
	} 
}