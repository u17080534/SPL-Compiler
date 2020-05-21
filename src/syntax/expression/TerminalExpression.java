package syntax.expression;

import syntax.code.*;
import lexer.Token;
import lexer.Token.Tok;

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
				this.symbol.setType("P");
				break;
				//USAGE
			case TOK_VAR:
				this.alias = "variable";
				break;
			case TOK_CALL:
				this.alias = "call";
				this.symbol.setType("P");
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
				this.symbol.setType("B");
				break;
			case TOK_F:
				this.alias = "bool";//"False";
				this.symbol.setType("B");
				break;
			case TOK_EQ:
				this.alias = "bool";//"eq";
				this.symbol.setType("B");
				break;
			case TOK_LT:
				this.alias = "bool";//"<";
				this.symbol.setType("B");
				break;
			case TOK_GT:
				this.alias = "bool";//">";
				this.symbol.setType("B");
				break;
			case TOK_NOT:
				this.alias = "bool";//"not";
				this.symbol.setType("B");
				break;
			case TOK_AND:
				this.alias = "bool";//"and";
				this.symbol.setType("B");
				break;
			case TOK_OR:
				this.alias = "bool";//"or";
				this.symbol.setType("B");
				break;
				//CALC
			case TOK_ADD:
				this.alias = "calc";//"add";
				this.symbol.setType("N");
				break;
			case TOK_SUB:
				this.alias = "calc";//"sub";
				this.symbol.setType("N");
				break;
			case TOK_MULT:
				this.alias = "calc";//"mult";
				this.symbol.setType("N");
				break;
				//ASSIGN
			case TOK_ASSN:
				this.alias = "assign";
				break;
			case TOK_S:
				this.alias = "string_literal";
				this.symbol.setType("S");
				break;
			case TOK_N:
				this.alias = "number_literal";
				this.symbol.setType("N");
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

	@Override
	public void setType(String type)
	{
		this.symbol.setType(type);
		this.parent.setType(type);
	}

	@Override
	public String getTerminalType()
	{
		return this.symbol.getType();
	}

	@Override
	public boolean isTerminal()
	{
		return true;
	} 

	public String getLocation()
	{
		return this.token.getLocation();
	}

	public String getLabel()
	{
		return this.label;
	}

	public Line trans(File absFile)
	{     
		String name = this.value;

		// String variables append $
		if(this.symbol.getType().equals("S") && !this.alias.equals("string_literal"))
			name += "$";

		return new Line(name);
	}
} 