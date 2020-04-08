package ast.expression;
import lexer.Token.Tok;

public class calc extends Expression 
{   
	private Tok action;
	private Expression calc_Ex;   

	public calc(Tok e1, Expression e2) 
	{ 
		this.action = e1; 
		this.calc_Ex = e2; 
	}  

	public String eval() 
	{       
		return "";   
	} 
}