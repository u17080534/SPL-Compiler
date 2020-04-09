package ast.expression;

public class cond_loop extends Expression 
{   
	private Expression boolEx, codeEx1, codeEx2, varEx1, varEx2, varEx3, varEx4, varEx5;   

	public cond_loop(Expression e1, Expression e2, Expression e3, Expression e4, Expression e5, Expression e6, Expression e7, Expression e8) 
	{ 
		super(e1, e2, e3, e4, e5, e6, e7, e8);
		this.expr = "COND_LOOP";
		this.boolEx = e1; 
		this.codeEx1 = e2; 
		this.varEx1 = e3; 
		this.varEx2 = e4; 
		this.varEx3 = e5; 
		this.varEx4 = e6; 
		this.varEx5 = e7; 
		this.codeEx2 = e8; 
	}  

	public String eval() 
	{       
		return "";   
	} 
}