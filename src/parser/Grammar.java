package parser;

import java.util.*;
import java.io.*;
import lexer.*;
import ast.*;
import ast.expression.*;
import exception.*;

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

	public Grammar()
	{
		this.index = 0;
	}

	//!Constructs AST
	public AbstractSyntaxTree build(List<Token> stream)
	{
		this.tokenstream = stream;
		this.lookahead = look(0);
        AbstractSyntaxTree tree = null;

		try
        {
        	Vector<Expression> roots = new Vector<Expression>();

            while(this.lookahead != Token.Tok.NULL) //Allow for contiguous program segments
           		roots.add(START());

           	tree = new AbstractSyntaxTree(roots);
        }
        catch(SyntaxException ex)
        {
            System.out.println("Syntax Error: " + ex.getMessage());
        }

        return tree;
	}

	private void readToken()
	{
		current = this.tokenstream.get(this.index);

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

	//START	→ PROC | PROG
	private Expression START() throws SyntaxException
	{
		try
		{
        	Expression root;

			if(this.lookahead == Token.Tok.TOK_PROC)
			{
				root = PROC();
			}
			else
			{
				root = PROG();
			}
			
           	return root;
		}
		catch(Exception ex)
		{
			throw ex;
		}
	}

	//PROG → CODE PROG'
	private Expression PROG() throws SyntaxException
	{
		Expression ex;
		try
		{
			Expression e1 = CODE();
			Expression e2 = PROG_();
			ex = new prog(e1, e2);
		}
		catch(Exception error)
		{
			throw error;
		}
		return ex;
	}

	// PROG' → ; PROC_DEFS CODE' | ϵ
	private Expression PROG_() throws SyntaxException
	{
		try
		{
			if(this.lookahead == Token.Tok.TOK_SEMI)
			{
				this.readToken();

				Expression e1 = PROC_DEFS();
				Expression e2 = CODE_();
				return new prog_(e1, e2);
			}

			return new prog_();
		}
		catch(Exception ex)
		{
			throw ex;
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

	// PROC_DEFS' → PROC_DEFS | CODE | ϵ
	private Expression PROC_DEFS_() throws SyntaxException
	{
		try
		{
			if(this.lookahead == Token.Tok.TOK_PROC)
			{
				Expression e = PROC_DEFS();
				return new proc_defs_(e);
			}
			else if(this.lookahead == Token.Tok.TOK_HALT || this.lookahead == Token.Tok.TOK_NUM || this.lookahead == Token.Tok.TOK_STRING || this.lookahead == Token.Tok.TOK_BOOL || this.lookahead == Token.Tok.TOK_INPUT || this.lookahead == Token.Tok.TOK_OUTPUT || this.lookahead == Token.Tok.TOK_IF || this.lookahead == Token.Tok.TOK_WHILE || this.lookahead == Token.Tok.TOK_ID)
			{
				Expression e = CODE();
				return new proc_defs_(e);
			}

			return new proc_defs_();
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
							return new proc(e1, e2);
						}
					}

					throw new SyntaxException(this.lookahead, "Closing brace expected after instruction.\n\tHint: You may be missing a semicolon (;) between instructions.");
				}
			}
		}
		catch(Exception ex)
		{
			throw ex;
		}

		throw new SyntaxException(this.lookahead, "Unexpected Tok: " + this.lookahead + " - 'proc' expected.");
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

	// CODE' → ; CODE | ; PROC_DEFS | ϵ
	private Expression CODE_() throws SyntaxException
	{
		try
		{
			if(this.lookahead == Token.Tok.TOK_SEMI)
			{
				this.readToken();
				if(this.lookahead != Token.Tok.TOK_PROC)
				{
					if(this.lookahead != Token.Tok.NULL)
					{
						Expression e = CODE();
						return new code_(e);
					}
				}
				else
				{
					if(this.lookahead != Token.Tok.NULL)
					{
						Expression e = PROC_DEFS();
						return new code_(e);
					}
				}
			}

			return new code_();
		}
		catch(Exception ex)
		{
			throw ex;
		}
	}

	// DECL → TYPE NAME CODE'
	private Expression DECL() throws SyntaxException
	{
		try
		{
			Expression e1 = TYPE();
			Expression e2 = NAME();
			Expression e3 = CODE_();
			return new decl(e1, e2, e3);
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
									return new cond_branch(e1,e2,e3);
								}
							}
						}
					}
				}
			}
		}
		catch(Exception ex)
		{
			throw ex;
		}

		throw new SyntaxException(this.lookahead, "Invalid conditional syntax.");
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
						return new cond_branch_(e);
					}
				}
			}
			return new cond_branch_();
		}
		catch(Exception ex)
		{
			throw ex;
		}
	}

	// COND_LOOP → while ( BOOL ) { CODE } | for ( VAR = 0; VAR < VAR ; VAR = add ( VAR , 1 ) ) { CODE }
	private Expression COND_LOOP() throws SyntaxException
	{
		try
		{
			if(this.lookahead == Token.Tok.TOK_WHILE)
			{

			}
		}
		catch(Exception ex)
		{
			throw ex;
		}

		throw new SyntaxException(this.lookahead, "Invalid loop syntax.");
	}

	// IO → input ( VAR ) | output ( VAR )
	private Expression IO() throws SyntaxException
	{
		try
		{
			if(this.lookahead == Token.Tok.TOK_INPUT)
			{
				this.readToken();
				Token e1 = this.current;				
				if(this.lookahead == Token.Tok.TOK_OP)
				{
					this.readToken();
					Expression e2 = VAR();
					if(this.lookahead == Token.Tok.TOK_CP)
					{
						this.readToken();
						return new io(new TokenExpression("io", e1.getInput()), e2);
					}
				}
			}
			else if(this.lookahead == Token.Tok.TOK_OUTPUT)
			{
				this.readToken();
				Token e1 = this.current;				
				if(this.lookahead == Token.Tok.TOK_OP)
				{
					this.readToken();
					Expression e2 = VAR();
					if(this.lookahead == Token.Tok.TOK_CP)
					{
						this.readToken();
						return new io(new TokenExpression("io", e1.getInput()), e2);
					}
				}
			}
		}
		catch(Exception ex)
		{
			throw ex;
		}

		throw new SyntaxException(this.lookahead, "Invalid I/O operator given.");
	}

	// BOOL → T | F | VAR | eq ( VAR , VAR ) | ( VAR < VAR ) | ( VAR > VAR ) | not BOOL | and ( BOOL' | or ( BOOL'
	private Expression BOOL() throws SyntaxException
	{
		try
		{
			if(this.lookahead == Token.Tok.TOK_T)
			{
				this.readToken();
				Token e = this.current;				
				return new bool(new TokenExpression("bool",e.getInput()));
			}
			else if(this.lookahead == Token.Tok.TOK_F)
			{
				this.readToken();
				Token e = this.current;				
				return new bool(new TokenExpression("bool",e.getInput()));
			}
			else if(this.lookahead == Token.Tok.TOK_ID)
			{
				Token e1 = this.current;
				Expression e2 = VAR();
				return new bool(new TokenExpression("bool", e1.getInput()), e2);
			}
			else if(this.lookahead == Token.Tok.TOK_EQ)
			{
				this.readToken();
				Token e1 = this.current;				
				if(this.lookahead == Token.Tok.TOK_OP)
				{
					this.readToken();
					Expression e2 = VAR();
					if(this.lookahead == Token.Tok.TOK_COMM)
					{
						this.readToken();
						Expression e3 = VAR();
						if(this.lookahead == Token.Tok.TOK_CP)
						{
							this.readToken();
							return new bool(new TokenExpression("bool",e1.getInput()), e2, e3);
						}
					}
				}
			}
			else if(this.lookahead == Token.Tok.TOK_OP)
			{
				this.readToken();
				Expression e2 = VAR();

				if(this.lookahead == Token.Tok.TOK_LT)
				{
					Token e1 = this.current;
					this.readToken();
					Expression e3 = VAR();
					if(this.lookahead == Token.Tok.TOK_CP)
					{
						this.readToken();
						return new bool(new TokenExpression("bool",e1.getInput()), e2, e3);
					}
				}
				else if(this.lookahead == Token.Tok.TOK_GT)
				{
					Token e1 = this.current;
					this.readToken();
					Expression e3 = VAR();
					if(this.lookahead == Token.Tok.TOK_CP)
					{
						this.readToken();
						return new bool(new TokenExpression("bool",e1.getInput()), e2, e3);
					}
				}
			}
			else if(this.lookahead == Token.Tok.TOK_NOT)
			{
				this.readToken();
				Token e1 = this.current;				
				Expression e2 = BOOL();
				return new bool(new TokenExpression("bool",e1.getInput()), e2);
			}
			else if(this.lookahead == Token.Tok.TOK_AND)
			{
				this.readToken();
				Token e1 = this.current;				
				if(this.lookahead == Token.Tok.TOK_OP)
				{
					this.readToken();
					Expression e2 = BOOL_();
					return new bool(new TokenExpression("bool",e1.getInput()), e2);
				}
			}
			else if(this.lookahead == Token.Tok.TOK_OR)
			{
				this.readToken();
				Token e1 = this.current;				
				if(this.lookahead == Token.Tok.TOK_OP)
				{
					this.readToken();
					Expression e2 = BOOL_();
					return new bool(new TokenExpression("bool",e1.getInput()), e2);
				}
			}
		}
		catch(Exception ex)
		{
			throw ex;
		}

		throw new SyntaxException(this.lookahead, "Invalid boolean expression given.");
	}

	// BOOL' → BOOL , BOOL"
	private Expression BOOL_() throws SyntaxException
	{
		try
		{
			Expression e1 = BOOL();

			if(this.lookahead == Token.Tok.TOK_COMM)
			{
				this.readToken();
				Expression e2 = BOOL__();
				return new bool_(e1, e2);
			}
		}
		catch(Exception ex)
		{
			throw ex;
		}

		throw new SyntaxException(this.lookahead, "Missing arguments.");
	}

	// BOOL" → BOOL )
	private Expression BOOL__() throws SyntaxException
	{
		try
		{
			Expression e = BOOL();
			if(this.lookahead == Token.Tok.TOK_CP)
			{
				this.readToken();
				return new bool__(e);
			}
		}
		catch(Exception ex)
		{
			throw ex;
		}

		throw new SyntaxException(this.lookahead, "Missing close parenthesis.");
	}

	// CALC → add ( CALC' | → sub ( CALC' | mult ( CALC'
	private Expression CALC() throws SyntaxException
	{
		try
		{
			if(this.lookahead == Token.Tok.TOK_ADD)
			{
				this.readToken();
				Token e1 = this.current;				
				if(this.lookahead == Token.Tok.TOK_OP)
				{
					this.readToken();
					Expression e2 = CALC_();
					return new calc(new TokenExpression("calc", e1.getInput()), e2);
				}

				throw new SyntaxException(this.lookahead, "Missing open parenthesis.");
			}
			else if(this.lookahead == Token.Tok.TOK_SUB)
			{
				this.readToken();
				Token e1 = this.current;				
				if(this.lookahead == Token.Tok.TOK_OP)
				{
					this.readToken();
					Expression e2 = CALC_();
					return new calc(new TokenExpression("calc", e1.getInput()), e2);
				}

				throw new SyntaxException(this.lookahead, "Missing open parenthesis.");
			}
			else if(this.lookahead == Token.Tok.TOK_MULT)
			{
				this.readToken();
				Token e1 = this.current;				
				if(this.lookahead == Token.Tok.TOK_OP)
				{
					this.readToken();
					Expression e2 = CALC_();
					return new calc(new TokenExpression("calc", e1.getInput()), e2);
				}

				throw new SyntaxException(this.lookahead, "Missing open parenthesis.");
			}		
		}
		catch(Exception ex)
		{
			throw ex;
		}	

		throw new SyntaxException(this.lookahead, "Invalid calc operation.");
	}

	// CALC' → NUMEXPR , CALC"
	private Expression CALC_() throws SyntaxException
	{
		try
		{
			Expression e1 = NUMEXPR();

			if(this.lookahead == Token.Tok.TOK_COMM)
			{
				this.readToken();
				Expression e2 = CALC__();
				return new calc_(e1, e2);
			}
		}
		catch(Exception ex)
		{
			throw ex;
		}

		throw new SyntaxException(this.lookahead, "Missing parameter in calc operation.");
	}

	// CALC" → NUMEXPR )
	private Expression CALC__() throws SyntaxException
	{
		try
		{
			Expression e = NUMEXPR();

			if(this.lookahead == Token.Tok.TOK_CP)
			{
				this.readToken();
				return new calc__(e);
			}
		}
		catch(Exception ex)
		{
			throw ex;
		}

		throw new SyntaxException(this.lookahead, "Expected closing parenthesis.");
	}

	// ASSIGN → VAR = ASSIGN'
	private Expression ASSIGN() throws SyntaxException
	{
		try
		{
			Expression e1 = VAR();
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

		throw new SyntaxException(this.lookahead, "Invalid assignment: Unknown Operator.");
	}

	// ASSIGN' → stringLiteral | VAR | NUMEXPR | BOOL
	private Expression ASSIGN_() throws SyntaxException
	{
		try
		{
			if(this.lookahead == Token.Tok.TOK_S)
			{
				this.readToken();
				String e = this.current.getInput();
				return new assign_(new TokenExpression("=", e));
			}
			else if(this.lookahead == Token.Tok.TOK_ID)
			{
				Expression e = VAR();
				return new assign_(e);
			}
			else if(this.lookahead == Token.Tok.TOK_N || this.lookahead == Token.Tok.TOK_ADD || this.lookahead == Token.Tok.TOK_SUB || this.lookahead == Token.Tok.TOK_MULT)
			{
				Expression e = NUMEXPR();
				return new assign_(e);
			}
			else if(this.lookahead == Token.Tok.TOK_T || this.lookahead == Token.Tok.TOK_F || this.lookahead == Token.Tok.TOK_EQ || this.lookahead == Token.Tok.TOK_OP || this.lookahead == Token.Tok.TOK_NOT || this.lookahead == Token.Tok.TOK_AND || this.lookahead == Token.Tok.TOK_OR) 
			{
				Expression e = BOOL();
				return new assign_(e);
			}
		}
		catch(Exception ex)
		{
			throw ex;
		}

		throw new SyntaxException(this.lookahead, "Invalid assignment: Bad Right Operand.");
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
			else if(this.lookahead == Token.Tok.TOK_WHILE)
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

		throw new SyntaxException(this.lookahead, "Instruction expected following semicolon. (;)");
	}

	// NUMEXPR → integerLiteral | VAR | CALC
	private Expression NUMEXPR() throws SyntaxException
	{
		try
		{
			if(this.lookahead == Token.Tok.TOK_N)
			{
				this.readToken();
				String e = this.current.getInput();
				return new numexpr(new TokenExpression("number", e));
			}
			else if(this.lookahead == Token.Tok.TOK_ID)
			{
				Expression e = VAR();
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

		throw new SyntaxException(this.lookahead, "Invalid numerical expression given.");
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
				return new type(new TokenExpression("type", e.getInput()));
			}
			else if(this.lookahead == Token.Tok.TOK_STRING)
			{
				this.readToken();
				Token e = this.current;				
				return new type(new TokenExpression("type", e.getInput()));
			}
			else if(this.lookahead == Token.Tok.TOK_BOOL)
			{
				this.readToken();
				Token e = this.current;				
				return new type(new TokenExpression("type", e.getInput()));
			}
		}
		catch(Exception ex)
		{
			throw ex;
		}

		throw new SyntaxException(this.lookahead, "Invalid type specified.");
	}

	// CALL → userDefinedIdentifier
	private Expression CALL() throws SyntaxException
	{
		try
		{
			if(this.lookahead == Token.Tok.TOK_ID)
			{
				this.readToken();
				String e = this.current.getInput();
				return new call(new TokenExpression("call", e));
			}
		}
		catch(Exception ex)
		{
			throw ex;
		}

		throw new SyntaxException(this.lookahead, "Invalid identifier called.");
	}

	// NAME → userDefinedIdentifier
	private Expression NAME() throws SyntaxException
	{
		try
		{
			if(this.lookahead == Token.Tok.TOK_ID)
			{
				this.readToken();
				String e = this.current.getInput();
				return new name(new TokenExpression("variable",e));
			}
		}
		catch(Exception ex)
		{
			throw ex;
		}

		throw new SyntaxException(this.lookahead, "Invalid identifier used.");
	}

	// VAR → userDefinedIdentifier
	private Expression VAR() throws SyntaxException
	{
		try
		{
			if(this.lookahead == Token.Tok.TOK_ID)
			{
				this.readToken();
				String e = this.current.getInput();
				return new var(new TokenExpression("variable", e));
			}
		}
		catch(Exception ex)
		{
			throw ex;
		}

		throw new SyntaxException(this.lookahead, "Invalid identifier used.");
	}
} 