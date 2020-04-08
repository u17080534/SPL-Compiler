package ast.expression;

public class cond_loop extends Expression 
{   
	private Expression boolEx, codeEx1, codeEx2, varEx1, varEx2, varEx3, varEx4, varEx5;   

	public cond_loop(Expression boolEx, Expression codeEx1, Expression varEx1, Expression varEx2, Expression varEx3, Expression varEx4, Expression varEx5, Expression codeEx2) 
	{ 
		this.boolEx = boolEx; 
		this.codeEx1 = codeEx1; 
		this.codeEx2 = codeEx2; 
		this.varEx1 = varEx1; 
		this.varEx2 = varEx2; 
		this.varEx3 = varEx3; 
		this.varEx4 = varEx4; 
		this.varEx5 = varEx5; 
	}  

	public String eval() 
	{       
		return "";   
	} 
}