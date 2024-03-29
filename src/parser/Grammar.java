package parser;

import java.util.*;
import exception.*;
import lexer.*;
import syntax.*;
import syntax.expression.*;

//SPL-COMPILER
public class Grammar
{
	//!Index of this.lookahead
	private int index;
	//!Current Token
	private Token current;
	//!Contains value of following token
	private Token.Tok lookahead;
	//!Tok stream - may be looked ahead up to any point < size
	private List<Token> tokenstream;

	private static String current_loc = "";

	public Grammar()
	{
		this.index = 0;
	}

	//!Constructs AST
	public AbstractSyntaxTree build(List<Token> stream) throws SyntaxException
	{
		this.tokenstream = stream;
		this.lookahead = look(0);

		try
        {
           	AbstractSyntaxTree tree = new AbstractSyntaxTree(START());

           	if(this.index + 1 < this.tokenstream.size())
           		throw new SyntaxException(this.tokenstream.get(this.index), "A proc definition cannot be followed by anything other than a new proc definition");

           	return tree;
        }
        catch(SyntaxException ex)
        {
            throw ex;
        }
	}

	private void readToken()
	{
		current = this.tokenstream.get(this.index);
		current_loc = current.getLocation();

		if(this.index + 1 < this.tokenstream.size())
			this.lookahead = this.tokenstream.get(++this.index).getToken();
		else
			this.lookahead = Token.Tok.NULL;
	}

	private Token.Tok look(int ahead)
	{
		if(this.index + ahead >= this.tokenstream.size())
			return Token.Tok.NULL;

		return this.tokenstream.get(this.index + ahead).getToken();
	}

	//START	→ PROG
	private Expression START() throws SyntaxException
	{
		try
		{
        	Expression root;

			root = PROG();
			
           	return root;
		}
		catch(Exception ex)
		{
			throw ex;
		}
	}

	//PROG → CODE PROG_
	private Expression PROG() throws SyntaxException
	{
		Expression ex = null;

		try
		{
			if(this.lookahead == Token.Tok.TOK_HALT || this.lookahead == Token.Tok.TOK_NUM || this.lookahead == Token.Tok.TOK_STRING || this.lookahead == Token.Tok.TOK_BOOL || this.lookahead == Token.Tok.TOK_INPUT || this.lookahead == Token.Tok.TOK_OUTPUT || this.lookahead == Token.Tok.TOK_IF || this.lookahead == Token.Tok.TOK_WHILE || this.lookahead == Token.Tok.TOK_FOR || this.lookahead == Token.Tok.TOK_ID)
			{
				Expression e1 = CODE();
				Expression e2 = PROG_();
				ex = new prog(e1, e2);
			}
			else
				throw new SyntaxException(this.current, "Instruction is expected at start of program or new procedure");
		}
		catch(Exception error)
		{
			throw error;
		}

		return ex;
	}

	// PROG' → ; PROC_DEFS | ϵ
	private Expression PROG_() throws SyntaxException
	{
		try
		{
			if(this.lookahead == Token.Tok.TOK_SEMI)
			{
				this.readToken();
				return PROC_DEFS();
			}

			else if(this.lookahead != Token.Tok.TOK_CB && this.lookahead != Token.Tok.NULL)
				throw new SyntaxException(this.current, "Instruction missing semicolon (;) as it has tokens following it");
			
			return null;
		}
		catch(Exception error)
		{
			throw error;
		}
	}

	// PROC_DEFS → PROC PROC_DEFS'
	private Expression PROC_DEFS() throws SyntaxException
	{
		try
		{
			Expression e1 = PROC();
			Expression e2 = PROC_DEFS_();
			return new proc_defs(e1, e2);
		}
		catch(Exception ex)
		{
			throw ex;
		}
	}

	// PROC_DEFS' → PROC_DEFS | ϵ
	private Expression PROC_DEFS_() throws SyntaxException
	{
		try
		{
			if(this.lookahead == Token.Tok.TOK_SEMI)
				throw new SyntaxException(this.current, "Semicolon (;) is not expected after a new proc definition");

			if(this.lookahead == Token.Tok.TOK_PROC)
			{
				return PROC_DEFS();
			}
			
			return null;
		}
		catch(Exception ex)
		{
			throw ex;
		}
	}

	// PROC → proc UserDefinedIdentifier { PROG }
	private Expression PROC() throws SyntaxException
	{
		try
		{
			if(this.lookahead == Token.Tok.TOK_PROC)
			{
				this.readToken();
				Token tok = this.current;
				if(this.lookahead == Token.Tok.TOK_ID)
				{
					this.readToken();
					String e1 = this.current.getInput();

					if(this.lookahead == Token.Tok.TOK_OB)
					{
						this.readToken();

						Expression e2 = PROG();

						if(this.lookahead == Token.Tok.TOK_CB)
						{
							this.readToken();
							return new proc(new TerminalExpression(tok, e1), e2);
						}

						throw new SyntaxException(this.current, "Expected Closing Brace\n\tHint: You may be missing a semicolon (;) between instructions");
					}

					throw new SyntaxException(this.current, "Expected Opening Brace\n\tHint: You may be missing a semicolon (;) between instructions");
				}

				throw new SyntaxException(this.current, "Unexpected Token: " + this.lookahead + " - proc identifier expected");
			}
		}
		catch(Exception ex)
		{
			throw ex;
		}

		if(this.lookahead == Token.Tok.NULL || this.lookahead == Token.Tok.TOK_CB)
			throw new SyntaxException(this.current, "Final instruction in code block has a trailing semicolon (;)");

		throw new SyntaxException(this.current, "Unknown instruction found");
	}

	// CODE → INSTR CODE'
	private Expression CODE() throws SyntaxException
	{
		try
		{
			Expression e1 = INSTR();
			Expression e2 = CODE_();
			return new code(e1, e2);
		}
		catch(Exception ex)
		{
			throw ex;
		}
	}

	// CODE' → ; CODE | ϵ
	private Expression CODE_() throws SyntaxException
	{
		try
		{
			if(this.lookahead == Token.Tok.TOK_SEMI)
			{				
				if(this.look(1) == Token.Tok.TOK_HALT || this.look(1) == Token.Tok.TOK_NUM || this.look(1) == Token.Tok.TOK_STRING || this.look(1) == Token.Tok.TOK_BOOL || this.look(1) == Token.Tok.TOK_INPUT || this.look(1) == Token.Tok.TOK_OUTPUT || this.look(1) == Token.Tok.TOK_IF || this.look(1) == Token.Tok.TOK_WHILE || this.look(1) == Token.Tok.TOK_FOR || this.look(1) == Token.Tok.TOK_ID)
				{
					this.readToken();
					return CODE();
				}
			}

			return null;
		}
		catch(Exception ex)
		{
			throw ex;
		}
	}

	// DECL → TYPE NAME DECL'
	private Expression DECL() throws SyntaxException
	{
		try
		{
			Expression e1 = TYPE();
			Expression e2 = NAME();
			Expression e3 = DECL_();
			return new decl(e1, e2, e3);
		}
		catch(Exception ex)
		{
			throw ex;
		}
	}

	// DECL' → ; DECL | ϵ
	private Expression DECL_() throws SyntaxException
	{
		try
		{
			if(this.lookahead == Token.Tok.TOK_SEMI)
			{
				if(this.look(1) == Token.Tok.TOK_NUM || this.look(1) == Token.Tok.TOK_STRING || this.look(1) == Token.Tok.TOK_BOOL)
				{
					this.readToken();
					return DECL();
				}
			}

			return null;
		}
		catch(Exception ex)
		{
			throw ex;
		}
	}

	// COND_BRANCH → if ( BOOL ) then { CODE } COND_BRANCH'
	private Expression COND_BRANCH() throws SyntaxException
	{
		try
		{
			if(this.lookahead == Token.Tok.TOK_IF)
			{
				this.readToken();
				Token tok = this.current;
				if(this.lookahead == Token.Tok.TOK_OP)
				{
					this.readToken();
					Expression e1 = BOOL();
					if(this.lookahead == Token.Tok.TOK_CP)
					{
						this.readToken();
						if(this.lookahead == Token.Tok.TOK_THEN)
						{
							this.readToken();
							if(this.lookahead == Token.Tok.TOK_OB)
							{
								this.readToken();
								Expression e2 = CODE();
								if(this.lookahead == Token.Tok.TOK_CB)
								{
									this.readToken();
									Expression e3 = COND_BRANCH_();
									return new cond_branch(e1, e2, e3);
								}
								throw new SyntaxException(this.current, "Invalid Conditional Syntax: Expected Closing Brace");
							}
							throw new SyntaxException(this.current, "Invalid Conditional Syntax: Expected Opening Brace");
						}
						throw new SyntaxException(this.current, "Invalid Conditional Syntax: Expected Token (then)");
					}
					throw new SyntaxException(this.current, "Invalid Conditional Syntax: Expected Closing Parenthesis");
				}
				throw new SyntaxException(this.current, "Invalid Conditional Syntax: Expected Opening Parenthesis");
			}
		}
		catch(Exception ex)
		{
			throw ex;
		}

		throw new SyntaxException(this.current, "Invalid Conditional Syntax: Expected Token (if)");
	}

	// COND_BRANCH'→ else { CODE } | ϵ
	private Expression COND_BRANCH_() throws SyntaxException
	{
		try
		{
			if(this.lookahead == Token.Tok.TOK_ELSE)
			{
				this.readToken();
				if(this.lookahead == Token.Tok.TOK_OB)
				{
					this.readToken();
					Expression e = CODE();
					if(this.lookahead == Token.Tok.TOK_CB)
					{
						this.readToken();
						return e;
					}

					throw new SyntaxException(this.current, "Invalid Conditional Syntax: Expected Closing Brace");
				}
				throw new SyntaxException(this.current, "Invalid Conditional Syntax: Expected Opening Brace");
			}

			return null;
		}
		catch(Exception ex)
		{
			throw ex;
		}
	}

	// COND_LOOP → while ( BOOL ) { CODE } | for ( VARIABLE = 0; VARIABLE < VARIABLE ; VARIABLE = add ( VARIABLE , 1 ) ) { CODE }
	private Expression COND_LOOP() throws SyntaxException
	{
		try
		{
			if(this.lookahead == Token.Tok.TOK_WHILE)
			{
				this.readToken();
				Token tok = this.current;
				if(this.lookahead == Token.Tok.TOK_OP)
				{
					this.readToken(); //THIS CHANGES WHETHER YOU HAVE TO NEST PARENTHESIS
					Expression e2 = BOOL();
					if(this.lookahead == Token.Tok.TOK_CP)
					{
						this.readToken(); //THIS CHANGES WHETHER YOU HAVE TO NEST PARENTHESIS
						if(this.lookahead == Token.Tok.TOK_OB)
						{
							this.readToken();
							Expression e3 = CODE();
							if(this.lookahead == Token.Tok.TOK_CB)
							{
								this.readToken();
								return new cond_loop(new TerminalExpression(tok, tok.getInput()), e2, e3);
							}
							throw new SyntaxException(this.current, "Invalid Conditional Syntax: Expected Closing Brace");
						}
						throw new SyntaxException(this.current, "Invalid Conditional Syntax: Expected Opening Brace");
					}
					throw new SyntaxException(this.current, "Invalid Conditional Syntax: Expected Closing Parenthesis");
				}
				throw new SyntaxException(this.current, "Invalid Conditional Syntax: Expected Opening Parenthesis");
			}
			else if(this.lookahead == Token.Tok.TOK_FOR)
			{
				this.readToken();
				Token tok = this.current;
				if(this.lookahead == Token.Tok.TOK_OP)
				{
					this.readToken();
					Expression e2 = VARIABLE();
					if(this.lookahead == Token.Tok.TOK_ASSN)
					{
						this.readToken();
						if(this.lookahead == Token.Tok.TOK_N)
						{
							this.readToken();
							if(this.current.getInput().equals("0"))
							{
								if(this.lookahead == Token.Tok.TOK_SEMI)
								{
									this.readToken();
									Expression e3 = VARIABLE();
									if(this.lookahead == Token.Tok.TOK_LT)
									{
										this.readToken();
										Expression e4 = VARIABLE();
										if(this.lookahead == Token.Tok.TOK_SEMI)
										{
											this.readToken();
											Expression e5 = VARIABLE();
											if(this.lookahead == Token.Tok.TOK_ASSN)
											{
												this.readToken();
												if(this.lookahead == Token.Tok.TOK_ADD)
												{
													this.readToken();
													if(this.lookahead == Token.Tok.TOK_OP)
													{
														this.readToken();
														Expression e6 = VARIABLE();
														if(this.lookahead == Token.Tok.TOK_COMM)
														{
															this.readToken();
															if(this.lookahead == Token.Tok.TOK_N)
															{
																this.readToken();
																if(this.current.getInput().equals("1"))
																{
																	if(this.lookahead == Token.Tok.TOK_CP)
																	{
																		this.readToken();
																		if(this.lookahead == Token.Tok.TOK_CP)
																		{
																			this.readToken();
																			if(this.lookahead == Token.Tok.TOK_OB)
																			{
																				this.readToken();
																				Expression e7 = CODE();
																				if(this.lookahead == Token.Tok.TOK_CB)
																				{
																					this.readToken();
																					return new cond_loop(new TerminalExpression(tok, tok.getInput()), e2, e3, e4, e5, e6, e7);
																				}
																				throw new SyntaxException(this.current, "Invalid Conditional Syntax: Expected Closing Brace");
																			}
																			throw new SyntaxException(this.current, "Invalid Conditional Syntax: Expected Opening Brace");
																		}
																		throw new SyntaxException(this.current, "Invalid Conditional Syntax: Expected Closing Parenthesis");
																	}
																	throw new SyntaxException(this.current, "Invalid Conditional Syntax: Expected Closing Parenthesis");
																}
																throw new SyntaxException(this.current, "Invalid Conditional Syntax: Expected Number Value => 1");
															}
															throw new SyntaxException(this.current, "Invalid Conditional Syntax: Expected Number Value");
														}
														throw new SyntaxException(this.current, "Invalid Conditional Syntax: Expected Token (,)");
													}
													throw new SyntaxException(this.current, "Invalid Conditional Syntax: Expected Opening Parenthesis");
												}
												throw new SyntaxException(this.current, "Invalid Conditional Syntax: Expected Token (add)");
											}
											throw new SyntaxException(this.current, "Invalid Conditional Syntax: Expected Token (=)");
										}
										throw new SyntaxException(this.current, "Invalid Conditional Syntax: Expected Token (;)");
									}
									throw new SyntaxException(this.current, "Invalid Conditional Syntax: Expected Token (<)");
								}
								throw new SyntaxException(this.current, "Invalid Conditional Syntax: Expected Token (;)");
							}
							throw new SyntaxException(this.current, "Invalid Conditional Syntax: Expected Number Value => 0");
						}
						throw new SyntaxException(this.current, "Invalid Conditional Syntax: Expected Number Value");
					}
					throw new SyntaxException(this.current, "Invalid Conditional Syntax: Assignment Operation");
				}
				throw new SyntaxException(this.current, "Invalid Conditional Syntax: Expected Opening Parenthesis");
			}
		}
		catch(Exception ex)
		{
			throw ex;
		}

		throw new SyntaxException(this.current, "Invalid loop syntax");
	}

	// IO → input ( VARIABLE ) | output ( VARIABLE )
	private Expression IO() throws SyntaxException
	{
		try
		{
			if(this.lookahead == Token.Tok.TOK_INPUT)
			{
				this.readToken();
				Token tok = this.current;				
				if(this.lookahead == Token.Tok.TOK_OP)
				{
					this.readToken();
					Expression e2 = VARIABLE();
					if(this.lookahead == Token.Tok.TOK_CP)
					{
						this.readToken();
						return new io(new TerminalExpression(tok, tok.getInput()), e2);
					}
					throw new SyntaxException(this.current, "Invalid I/O Syntax. Expected Closing Parenthesis");
				}
				throw new SyntaxException(this.current, "Invalid I/O Syntax. Expected Opening Parenthesis");
			}
			else if(this.lookahead == Token.Tok.TOK_OUTPUT)
			{
				this.readToken();
				Token tok = this.current;				
				if(this.lookahead == Token.Tok.TOK_OP)
				{
					this.readToken();
					Expression e2 = VARIABLE();
					if(this.lookahead == Token.Tok.TOK_CP)
					{
						this.readToken();
						return new io(new TerminalExpression(tok, tok.getInput()), e2);
					}
					throw new SyntaxException(this.current, "Invalid I/O Syntax. Expected Closing Parenthesis");
				}
				throw new SyntaxException(this.current, "Invalid I/O Syntax. Expected Opening Parenthesis");
			}
		}
		catch(Exception ex)
		{
			throw ex;
		}

		throw new SyntaxException(this.current, "Invalid I/O operator given. Expected Token (input/output)");
	}

	// BOOL → T | F | VARIABLE | eq ( VARIABLE , VARIABLE ) | ( VARIABLE < VARIABLE ) | ( VARIABLE > VARIABLE ) | not BOOL | and ( BOOL' | or ( BOOL'
	private Expression BOOL() throws SyntaxException
	{
		try
		{
			if(this.lookahead == Token.Tok.TOK_T)
			{
				this.readToken();
				Token tok = this.current;	// T			
				return new bool(new TerminalExpression(tok, tok.getInput()));
			}
			else if(this.lookahead == Token.Tok.TOK_F)
			{
				this.readToken();
				Token tok = this.current;	// F		
				return new bool(new TerminalExpression(tok, tok.getInput()));
			}
			else if(this.lookahead == Token.Tok.TOK_ID)
			{
				Expression e2 = VARIABLE();
				Token tok = new Token(this.current, Token.Tok.TOK_VAR); // ID
				return new bool(e2);
				// return new bool(new TerminalExpression(tok, tok.getInput()), e2);
			}
			else if(this.lookahead == Token.Tok.TOK_EQ)
			{
				this.readToken();
				Token tok = this.current; // eq			
				if(this.lookahead == Token.Tok.TOK_OP)
				{
					this.readToken();
					Expression e2 = VARIABLE();
					if(this.lookahead == Token.Tok.TOK_COMM)
					{
						this.readToken();
						Expression e3 = VARIABLE();
						if(this.lookahead == Token.Tok.TOK_CP)
						{
							this.readToken();
							return new bool(new TerminalExpression(tok, tok.getInput()), e2, e3);
						}
						throw new SyntaxException(this.current, "Invalid I/O Syntax. Expected Closing Parenthesis");
					}
					throw new SyntaxException(this.current, "Invalid I/O Syntax. Expected Token (,)");
				}
				throw new SyntaxException(this.current, "Invalid Boolean Syntax: Expected Opening Parenthesis");
			}
			else if(this.lookahead == Token.Tok.TOK_OP)
			{
				this.readToken();
				Expression e2 = VARIABLE();

				if(this.lookahead == Token.Tok.TOK_LT)
				{
					this.readToken();
					Token tok = this.current; //<
					Expression e3 = VARIABLE();
					if(this.lookahead == Token.Tok.TOK_CP)
					{
						this.readToken();
						return new bool(new TerminalExpression(tok, tok.getInput()), e2, e3);
					}
					throw new SyntaxException(this.current, "Invalid I/O Syntax. Expected Closing Parenthesis");
				}
				else if(this.lookahead == Token.Tok.TOK_GT)
				{
					this.readToken();
					Token tok = this.current; //>
					Expression e3 = VARIABLE();
					if(this.lookahead == Token.Tok.TOK_CP)
					{
						this.readToken();
						return new bool(new TerminalExpression(tok, tok.getInput()), e2, e3);
					}
					throw new SyntaxException(this.current, "Invalid I/O Syntax. Expected Closing Parenthesis");
				}
			}
			else if(this.lookahead == Token.Tok.TOK_NOT)
			{
				this.readToken();
				Token tok = this.current;				
				Expression e2 = BOOL();
				return new bool(new TerminalExpression(tok, tok.getInput()), e2);
			}
			else if(this.lookahead == Token.Tok.TOK_AND)
			{
				this.readToken();
				Token tok = this.current;				
				if(this.lookahead == Token.Tok.TOK_OP)
				{
					this.readToken();
					Expression e2 = BOOL_();
					return new bool(new TerminalExpression(tok, tok.getInput()), e2);
				}
				throw new SyntaxException(this.current, "Invalid Boolean Syntax: Expected Opening Parenthesis");
			}
			else if(this.lookahead == Token.Tok.TOK_OR)
			{
				this.readToken();
				Token tok = this.current;				
				if(this.lookahead == Token.Tok.TOK_OP)
				{
					this.readToken();
					Expression e2 = BOOL_();
					return new bool(new TerminalExpression(tok, tok.getInput()), e2);
				}
				throw new SyntaxException(this.current, "Invalid Boolean Syntax: Expected Opening Parenthesis");
			}
		}
		catch(Exception ex)
		{
			throw ex;
		}

		throw new SyntaxException(this.current, "Invalid boolean expression given");
	}

	// BOOL' → BOOL , BOOL )
	private Expression BOOL_() throws SyntaxException
	{
		try
		{
			Expression e1 = BOOL();

			if(this.lookahead == Token.Tok.TOK_COMM)
			{
				this.readToken();
				Expression e2 = BOOL();
				if(this.lookahead == Token.Tok.TOK_CP)
				{
					this.readToken();
					return new bool_(e1, e2);
				}
				throw new SyntaxException(this.current, "Invalid Boolean Syntax: Expected Closing Parenthesis");
			}
			throw new SyntaxException(this.current, "Invalid Boolean Syntax: Expected Token (,)");
		}
		catch(Exception ex)
		{
			throw ex;
		}
	}

	// CALC → add ( CALC' | → sub ( CALC' | mult ( CALC'
	private Expression CALC() throws SyntaxException
	{
		try
		{
			if(this.lookahead == Token.Tok.TOK_ADD)
			{
				this.readToken();
				Token tok = this.current;				
				if(this.lookahead == Token.Tok.TOK_OP)
				{
					this.readToken();
					Expression e2 = CALC_();
					return new calc(new TerminalExpression(tok, tok.getInput()), e2);
				}

				throw new SyntaxException(this.current, "Invalid Arithmetic Syntax: Expected Opening Parenthesis");
			}
			else if(this.lookahead == Token.Tok.TOK_SUB)
			{
				this.readToken();
				Token tok = this.current;				
				if(this.lookahead == Token.Tok.TOK_OP)
				{
					this.readToken();
					Expression e2 = CALC_();
					return new calc(new TerminalExpression(tok, tok.getInput()), e2);
				}

				throw new SyntaxException(this.current, "Invalid Arithmetic Syntax: Expected Opening Parenthesis");
			}
			else if(this.lookahead == Token.Tok.TOK_MULT)
			{
				this.readToken();
				Token tok = this.current;				
				if(this.lookahead == Token.Tok.TOK_OP)
				{
					this.readToken();
					Expression e2 = CALC_();
					return new calc(new TerminalExpression(tok, tok.getInput()), e2);
				}

				throw new SyntaxException(this.current, "Invalid Arithmetic Syntax: Expected Opening Parenthesis");
			}		
		}
		catch(Exception ex)
		{
			throw ex;
		}	

		throw new SyntaxException(this.current, "Invalid Arithmetic Syntax");
	}

	// CALC' → NUMEXPR , NUMEXPR )
	private Expression CALC_() throws SyntaxException
	{
		try
		{
			Expression e1 = NUMEXPR();

			if(this.lookahead == Token.Tok.TOK_COMM)
			{
				this.readToken();
				Expression e2 = NUMEXPR();
				if(this.lookahead == Token.Tok.TOK_CP)
				{
					this.readToken();
					return new calc_(e1, e2);
				}
			}
		}
		catch(Exception ex)
		{
			throw ex;
		}

		throw new SyntaxException(this.current, "Invalid Arithmetic Syntax: Missing parameter in operation\n\tHint: A comma (,) may be missing");
	}

	// ASSIGN → VARIABLE = ASSIGN'
	private Expression ASSIGN() throws SyntaxException
	{
		try
		{
			Expression e1 = VARIABLE();
			if(this.lookahead == Token.Tok.TOK_ASSN)
			{
				this.readToken();
				Expression e2 = ASSIGN_();
				return new assign(e1, e2);
			}
		}
		catch(Exception ex)
		{
			throw ex;
		}

		throw new SyntaxException(this.current, "Invalid Assignment: Unknown Operator, Expected Token (=)");
	}

	// ASSIGN' → stringLiteral | VARIABLE | NUMEXPR | BOOL
	private Expression ASSIGN_() throws SyntaxException
	{
		try
		{
			if(this.lookahead == Token.Tok.TOK_S)
			{
				this.readToken();
				// Token tok = new Token(this.current, Token.Tok.TOK_ASSN);
				return new assign_(new TerminalExpression(this.current, this.current.getInput()));
			}
			else if(this.lookahead == Token.Tok.TOK_ID)
			{
				Expression e = VARIABLE();
				Token tok = new Token(this.current, Token.Tok.TOK_ASSN);
				return new assign_(new TerminalExpression(tok, "variable"), e);
			}
			else if(this.lookahead == Token.Tok.TOK_N || this.lookahead == Token.Tok.TOK_ADD || this.lookahead == Token.Tok.TOK_SUB || this.lookahead == Token.Tok.TOK_MULT)
			{
				Expression e = NUMEXPR();
				Token tok = new Token(this.current, Token.Tok.TOK_ASSN);
				return new assign_(new TerminalExpression(tok, "numexpr"), e);
			}
			else if(this.lookahead == Token.Tok.TOK_T || this.lookahead == Token.Tok.TOK_F || this.lookahead == Token.Tok.TOK_EQ || this.lookahead == Token.Tok.TOK_OP || this.lookahead == Token.Tok.TOK_NOT || this.lookahead == Token.Tok.TOK_AND || this.lookahead == Token.Tok.TOK_OR) 
			{
				Expression e = BOOL();
				Token tok = new Token(this.current, Token.Tok.TOK_ASSN);
				return new assign_(new TerminalExpression(tok, "bool"), e);
			}
		}
		catch(Exception ex)
		{
			throw ex;
		}

		throw new SyntaxException(this.current, "Invalid assignment: Bad Right Operand");
	}

	// INSTR → halt | DECL | IO | CALL | ASSIGN  | COND_BRANCH | COND_LOOP
	private Expression INSTR() throws SyntaxException 
	{ 
		try
		{
			if(this.lookahead == Token.Tok.TOK_HALT)
			{
				this.readToken();
				return new halt();
			}
			else if(this.lookahead == Token.Tok.TOK_NUM || this.lookahead == Token.Tok.TOK_STRING || this.lookahead == Token.Tok.TOK_BOOL)
			{
				return new instr(DECL());
			}
			else if(this.lookahead == Token.Tok.TOK_INPUT || this.lookahead == Token.Tok.TOK_OUTPUT)
			{
				return new instr(IO());
			}
			else if(this.lookahead == Token.Tok.TOK_IF)
			{
				return new instr(COND_BRANCH());
			}
			else if(this.lookahead == Token.Tok.TOK_WHILE || this.lookahead == Token.Tok.TOK_FOR)
			{
				return new instr(COND_LOOP());
			}
			else if(this.lookahead == Token.Tok.TOK_ID && this.look(1) == Token.Tok.TOK_ASSN)
			{
				return new instr(ASSIGN());
			}
			else if(this.lookahead == Token.Tok.TOK_ID)
			{
				return new instr(CALL());
			}
		}
		catch(Exception ex)
		{
			throw ex;
		}

		throw new SyntaxException(this.current, "Final instruction in code block has a trailing semicolon (;)");
	}

	// NUMEXPR → integerLiteral | VARIABLE | CALC
	private Expression NUMEXPR() throws SyntaxException
	{
		try
		{
			if(this.lookahead == Token.Tok.TOK_N)
			{
				this.readToken();
				Token e = this.current;
				return new numexpr(new TerminalExpression(e, e.getInput()));
			}
			else if(this.lookahead == Token.Tok.TOK_ID)
			{
				Expression e = VARIABLE();
				return new numexpr(e);
			}
			else if(this.lookahead == Token.Tok.TOK_ADD || this.lookahead == Token.Tok.TOK_SUB || this.lookahead == Token.Tok.TOK_MULT)
			{
				Expression e = CALC();
				return new numexpr(e);
			}
		}
		catch(Exception ex)
		{
			throw ex;
		}

		throw new SyntaxException(this.current, "Invalid Numerical Expression Given");
	}

	// TYPE → num | string | bool
	private Expression TYPE() throws SyntaxException
	{
		try
		{
			if(this.lookahead == Token.Tok.TOK_NUM)
			{
				this.readToken();
				Token e = this.current;				
				return new type(new TerminalExpression(e, e.getInput()));
			}
			else if(this.lookahead == Token.Tok.TOK_STRING)
			{
				this.readToken();
				Token e = this.current;				
				return new type(new TerminalExpression(e, e.getInput()));
			}
			else if(this.lookahead == Token.Tok.TOK_BOOL)
			{
				this.readToken();
				Token e = this.current;				
				return new type(new TerminalExpression(e, e.getInput()));
			}
		}
		catch(Exception ex)
		{
			throw ex;
		}

		throw new SyntaxException(this.current, "Invalid Type Specified");
	}

	// CALL → userDefinedIdentifier
	private Expression CALL() throws SyntaxException
	{
		try
		{
			if(this.lookahead == Token.Tok.TOK_ID)
			{
				this.readToken();
				Token e = new Token(this.current, Token.Tok.TOK_CALL);
				return new call(new TerminalExpression(e, e.getInput()));
			}
		}
		catch(Exception ex)
		{
			throw ex;
		}

		throw new SyntaxException(this.current, "Invalid Identifier Called");
	}

	// NAME → userDefinedIdentifier
	private Expression NAME() throws SyntaxException
	{
		try
		{
			if(this.lookahead == Token.Tok.TOK_ID)
			{
				this.readToken();
				Token e = new Token(this.current, Token.Tok.TOK_VAR);
				return new name(new TerminalExpression(e, e.getInput()));
			}
		}
		catch(Exception ex)
		{
			throw ex;
		}

		throw new SyntaxException(this.current, "Invalid Identifier Used");
	}

	// VARIABLE → userDefinedIdentifier
	private Expression VARIABLE() throws SyntaxException
	{
		try
		{
			if(this.lookahead == Token.Tok.TOK_ID)
			{
				this.readToken();
				Token e = new Token(this.current, Token.Tok.TOK_VAR);
				return new variable(new TerminalExpression(e, e.getInput()));
			}
		}
		catch(Exception ex)
		{
			throw ex;
		}

		throw new SyntaxException(this.current, "Invalid Identifier Used");
	}

	public static String location()
	{
		return current_loc;
	}
} 