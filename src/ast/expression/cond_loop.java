package ast.expression;

public class cond_loop extends Expression 
{   
	private TokenExpression type;
	private Expression condEx1, condEx2, condEx3, condEx4, condEx5, condEx6;   

	public cond_loop(TokenExpression e1, Expression e2, Expression e3) 
	{ 
		super(e1, e2, e3);
		this.type = e1; 
		this.condEx1 = e2; 
		this.condEx2 = e3; 
		this.expr = "COND_LOOP";
	}  

	public cond_loop(TokenExpression e1, Expression e2, Expression e3, Expression e4, Expression e5, Expression e6, Expression e7) 
	{ 
		super(e1, e2, e3, e4, e5, e6, e7);
		this.type = e1; 
		this.condEx1 = e2; 
		this.condEx2 = e3; 
		this.condEx3 = e4; 
		this.condEx4 = e5; 
		this.condEx5 = e6; 
		this.condEx6 = e7; 
		this.expr = "COND_LOOP";
	}  

	public String trans() 
	{       
		return "";   
	} 
}