package syntax.expression;

import syntax.code.*;
import lexer.Token;
import lexer.Token.Tok;
import analysis.Scoping;

//SPL-COMPILER
public class TerminalExpression extends Expression 
{   
	private Token token;   
	private String value;  
	private String alias; 
	private String label;

	public TerminalExpression(Token token, String value)
	{
		super();
		this.token = token;
		this.alias = this.token.getToken().toString();
		this.value = value;

		switch(this.token.getToken())
		{
			case TOK_PROC:
				this.alias = "proc";
				break;
				//USAGE
			case TOK_VAR:
				this.alias = "variable";
				break;
			case TOK_CALL:
				this.alias = "call";
				break;
				//LOOP
			case TOK_WHILE:
				this.alias = "loop";
				break;
			case TOK_FOR:
				this.alias = "loop";
				break;
				//IO
			case TOK_INPUT:
				this.alias = "io";
				break;
			case TOK_OUTPUT:
				this.alias = "io";
				break;
				//TYPES
			case TOK_NUM:
				this.alias = "type";
				break;
			case TOK_STRING:
				this.alias = "type";
				break;
			case TOK_BOOL:
				this.alias = "type";
				break;
				//BOOL
			case TOK_T:
				this.alias = "bool";//"True";
				break;
			case TOK_F:
				this.alias = "bool";//"False";
				break;
			case TOK_EQ:
				this.alias = "bool";//"eq";
				break;
			case TOK_LT:
				this.alias = "bool";//"<";
				break;
			case TOK_GT:
				this.alias = "bool";//">";
				break;
			case TOK_NOT:
				this.alias = "bool";//"not";
				break;
			case TOK_AND:
				this.alias = "bool";//"and";
				break;
			case TOK_OR:
				this.alias = "bool";//"or";
				break;
				//CALC
			case TOK_ADD:
				this.alias = "calc";//"add";
				break;
			case TOK_SUB:
				this.alias = "calc";//"sub";
				break;
			case TOK_MULT:
				this.alias = "calc";//"mult";
				break;
				//ASSIGN
			case TOK_ASSN:
				this.alias = "assign";
				break;
		}

		this.expr = this.alias + " '" + this.value + "'";

		this.label = this.value; //original label in code, for getting user input
	}

	@Override
	public void setExpr(String expr)
	{
		this.expr = expr;
		this.value = Expression.getValue(expr);
	}

	public String getLocation()
	{
		return this.token.getLocation();
	}

	public String getLabel()
	{
		return this.label;
	}

	@Override
	public boolean isTerminal()
	{
		return true;
	} 

	public Line trans(File absFile)
	{
		//System.out.println(this.expr);       
		return new Line(this.value);
	}
} 