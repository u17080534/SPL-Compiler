package lexer;

import java.util.*;
import java.io.*;
import javafx.util.*;

public class Token
{
	private String input;
	private Tok token;
	private int line = 0;
	private int col = 0;

	public Token(String input, Tok token)
	{
		this.input = input;
		this.token = token;
	}

	public void setLocation(int line, int col)
	{
		this.line = line;
		this.col = col;
	}

	public String getInput()
	{
		return this.input;
	}

	public Tok getToken()
	{
		return this.token;
	}

	@Override public String toString() 
	{ 
		return this.input + " - " + this.token + "[" + this.line + "," + this.col + "]"; 
	} 

	public enum Tok
	{ 
		NULL("_"),
		TOK_AND("tok_and"), 
		TOK_OR("tok_or"), 
		TOK_NOT("tok_not"),
		TOK_ADD("tok_add"),
		TOK_SUB("tok_sub"),
		TOK_MULT("tok_mult"),
		TOK_IF("tok_if"),
		TOK_THEN("tok_then"),
		TOK_ELSE("tok_else"),
		TOK_WHILE("tok_while"),
		TOK_FOR("tok_for"),
		TOK_EQ("tok_eq"),
		TOK_INPUT("tok_input"),
		TOK_OUTPUT("tok_output"),
		TOK_HALT("tok_halt"),
		TOK_NUM("tok_num"),
		TOK_BOOL("tok_bool"),
		TOK_STRING("tok_string"),
		TOK_PROC("tok_proc"),
		TOK_GT("tok_greater"),
		TOK_LT("tok_less"),
		TOK_OP("tok_open_paren"),
		TOK_CP("tok_close_paren"),
		TOK_OB("tok_open_brace"),
		TOK_CB("tok_close_brace"),
		TOK_ASSN("tok_assign"),
		TOK_COMM("tok_comma"),
		TOK_SEMI("tok_semi"),
		TOK_T("tok_true"),
		TOK_F("tok_false"),
		TOK_S("tok_string_literal"),//literal
		TOK_N("tok_number_literal"),//literal
		TOK_ID("tok_identifier");

		private String str; 

		private Tok(String str) { this.str = str; } 

		@Override public String toString() { return this.str; } 
	}
}