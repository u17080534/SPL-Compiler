import java.util.*;
import java.io.*;
import javafx.util.*;

public class Grammar
{
	//!Index of this.lookahead
	private int index;
	//!Contains value of following token
	private Token lookahead;
	//!Token stream - may be looked ahead up to any point < size
	private List<Pair<String, Token>> tokenstream;

	public Grammar()
	{
		this.index = 0;
	}

	//!Constructs AST
	public void build(List<Pair<String, Token>> stream)
	{
		this.tokenstream = stream;
		this.lookahead = look(0);

		try
        {
            while(this.lookahead != Token.NULL) //Allow for contiguous program segments
           		START();
        }
        catch(Exception ex)
        {
            System.out.println("Syntax Error: " + ex.getMessage());
        }
	}

	private void readToken()
	{
		Pair<String, Token> read = this.tokenstream.get(this.index);

		if(this.index + 1 < this.tokenstream.size())
			this.lookahead = this.tokenstream.get(++this.index).getValue();
		else
			this.lookahead = Token.NULL;

		System.out.println(read);
	}

	private Token look(int ahead)
	{
		if(this.index + ahead >= this.tokenstream.size())
			return Token.NULL;

		return this.tokenstream.get(this.index + ahead).getValue();
	}

	//START	→ PROC | PROG
	private void START() throws Exception
	{
		try
		{
			if(this.lookahead == Token.TOK_PROC)
			{
				PROC();
			}
			else
			{
				PROG();
			}
		}
		catch(Exception ex)
		{
			throw ex;
		}
	}

	//PROG → CODE PROG'
	private void PROG() throws Exception
	{
		System.out.println("\t! PROG !");
		try
		{
			CODE();
			PROG_();
		}
		catch(Exception ex)
		{
			throw ex;
		}
	}

	// PROG' → ; PROC_DEFS | ϵ
	private void PROG_() throws Exception
	{
		try
		{
			if(this.lookahead == Token.TOK_SEMI)
			{
				this.readToken();

				PROC_DEFS();

				return;
			}
		}
		catch(Exception ex)
		{
			throw ex;
		}
	}

	// PROC_DEFS → PROC PROC_DEFS'
	private void PROC_DEFS() throws Exception
	{
		System.out.println("\t! PROC_DEFS !");
		try
		{
			PROC();
			PROC_DEFS_();
		}
		catch(Exception ex)
		{
			throw ex;
		}
	}

	// PROC_DEFS' → PROC_DEFS | ϵ
	private void PROC_DEFS_() throws Exception
	{
		try
		{
			if(this.lookahead == Token.TOK_PROC)
			{
				PROC_DEFS();
			}
		}
		catch(Exception ex)
		{
			throw ex;
		}
	}

	// PROC → proc UserDefinedIdentifier { PROG }
	private void PROC() throws Exception
	{
		System.out.println("\t! PROC !");
		try
		{
			if(this.lookahead == Token.TOK_PROC)
			{
				this.readToken();
				if(this.lookahead == Token.TOK_ID)
				{
					this.readToken();
					if(this.lookahead == Token.TOK_OB)
					{
						this.readToken();
						PROG();
						if(this.lookahead == Token.TOK_CB)
						{
							this.readToken();
							return;
						}
					}

					throw new Exception("Closing brace expected after instruction.\n\tHint: You may be missing a semicolon (;) between instructions.");
				}
			}
		}
		catch(Exception ex)
		{
			throw ex;
		}

		throw new Exception("Unexpected Token: " + this.lookahead + " - 'proc' expected.");
	}

	// CODE → INSTR CODE'
	private void CODE() throws Exception
	{
		System.out.println("\t! CODE !");

		try
		{
			INSTR();
			CODE_();
		}
		catch(Exception ex)
		{
			throw ex;
		}
	}

	// CODE' → ; CODE | ϵ
	private void CODE_() throws Exception
	{
		try
		{
			if(this.lookahead == Token.TOK_SEMI)
			{
				this.readToken();

				if(this.lookahead != Token.NULL)
					CODE();

				return;
			}
		}
		catch(Exception ex)
		{
			throw ex;
		}
	}

	// DECL → TYPE NAME CODE'
	private void DECL() throws Exception
	{
		System.out.println("\t! DECL !");
		try
		{
			TYPE();
			NAME();
			CODE_();
		}
		catch(Exception ex)
		{
			throw ex;
		}
	}

	// COND_BRANCH → if ( BOOL ) then { CODE } COND_BRANCH'
	private void COND_BRANCH() throws Exception
	{
		try
		{
			if(this.lookahead == Token.TOK_IF)
			{
				this.readToken();
				if(this.lookahead == Token.TOK_OP)
				{
					this.readToken();
					BOOL();

					if(this.lookahead == Token.TOK_CP)
					{
						this.readToken();
						if(this.lookahead == Token.TOK_THEN)
						{
							this.readToken();
							if(this.lookahead == Token.TOK_OB)
							{
								this.readToken();
								CODE();
								if(this.lookahead == Token.TOK_CB)
								{
									this.readToken();
									COND_BRANCH_();
									return;
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

		throw new Exception("Invalid conditional syntax.");
	}

	// COND_BRANCH'→ else { CODE } | ϵ
	private void COND_BRANCH_() throws Exception
	{
		try
		{
			if(this.lookahead == Token.TOK_ELSE)
			{
				this.readToken();
				if(this.lookahead == Token.TOK_OB)
				{
					this.readToken();
					CODE();
					if(this.lookahead == Token.TOK_CB)
					{
						this.readToken();
						return;
					}
				}
			}
		}
		catch(Exception ex)
		{
			throw ex;
		}
	}

	// COND_LOOP → while ( BOOL ) { CODE } | for ( VAR = 0; VAR < VAR ; VAR = add ( VAR , 1 ) ) { CODE }
	private void COND_LOOP() throws Exception
	{
		try
		{
			if(this.lookahead == Token.TOK_WHILE)
			{

			}
		}
		catch(Exception ex)
		{
			throw ex;
		}

		throw new Exception("Invalid loop syntax.");
	}

	// IO → input ( VAR ) | output ( VAR )
	private void IO() throws Exception
	{
		System.out.println("\t! IO !");
		try
		{
			if(this.lookahead == Token.TOK_INPUT)
			{
				this.readToken();
				if(this.lookahead == Token.TOK_OP)
				{
					this.readToken();
					VAR();
					if(this.lookahead == Token.TOK_CP)
					{
						this.readToken();
						return;
					}
				}
			}
			else if(this.lookahead == Token.TOK_OUTPUT)
			{
				this.readToken();
				if(this.lookahead == Token.TOK_OP)
				{
					this.readToken();
					VAR();
					if(this.lookahead == Token.TOK_CP)
					{
						this.readToken();
						return;
					}
				}
			}
		}
		catch(Exception ex)
		{
			throw ex;
		}

		throw new Exception("Invalid I/O operator given.");
	}

	// BOOL → T | F | VAR | eq ( VAR , VAR ) | ( VAR < VAR ) | ( VAR > VAR ) | not BOOL | and ( BOOL' | or ( BOOL'
	private void BOOL() throws Exception
	{
		try
		{
			if(this.lookahead == Token.TOK_T)
			{
				this.readToken();
				return;
			}
			else if(this.lookahead == Token.TOK_F)
			{
				this.readToken();
				return;
			}
			else if(this.lookahead == Token.TOK_ID)
			{
				VAR();
				return;
			}
			else if(this.lookahead == Token.TOK_EQ)
			{
				this.readToken();
				if(this.lookahead == Token.TOK_OP)
				{
					this.readToken();
					VAR();
					if(this.lookahead == Token.TOK_COMM)
					{
						this.readToken();
						VAR();
						if(this.lookahead == Token.TOK_CP)
						{
							this.readToken();
							return;
						}
					}
				}
			}
			else if(this.lookahead == Token.TOK_OP)
			{
				this.readToken();
				VAR();

				if(this.lookahead == Token.TOK_LT)
				{
					this.readToken();
					VAR();
					if(this.lookahead == Token.TOK_CP)
					{
						this.readToken();
						return;
					}
				}
				else if(this.lookahead == Token.TOK_GT)
				{
					this.readToken();
					VAR();
					if(this.lookahead == Token.TOK_CP)
					{
						this.readToken();
						return;
					}
				}
			}
			else if(this.lookahead == Token.TOK_NOT)
			{
				this.readToken();
				BOOL();
				return;
			}
			else if(this.lookahead == Token.TOK_AND)
			{
				this.readToken();
				if(this.lookahead == Token.TOK_OP)
				{
					this.readToken();
					BOOL_();
					return;
				}
			}
			else if(this.lookahead == Token.TOK_OR)
			{
				this.readToken();
				if(this.lookahead == Token.TOK_OP)
				{
					this.readToken();
					BOOL_();
					return;
				}
			}
		}
		catch(Exception ex)
		{
			throw ex;
		}

		throw new Exception("Invalid boolean expression given.");
	}

	// BOOL' → BOOL , BOOL"
	private void BOOL_() throws Exception
	{
		try
		{
			BOOL();

			if(this.lookahead == Token.TOK_COMM)
			{
				this.readToken();
				BOOL__();
				return;
			}
		}
		catch(Exception ex)
		{
			throw ex;
		}

		throw new Exception("Missing arguments.");
	}

	// BOOL" → BOOL )
	private void BOOL__() throws Exception
	{
		try
		{
			BOOL();
			if(this.lookahead == Token.TOK_CP)
			{
				this.readToken();
				return;
			}
		}
		catch(Exception ex)
		{
			throw ex;
		}

		throw new Exception("Missing close parenthesis.");
	}

	// CALC → add ( CALC' | → sub ( CALC' | mult ( CALC'
	private void CALC() throws Exception
	{
		System.out.println("\t! CALC !");

		try
		{
			if(	this.lookahead == Token.TOK_ADD)
			{
				this.readToken();
				if(this.lookahead == Token.TOK_OP)
				{
					this.readToken();
					CALC_();
					return;
				}

				throw new Exception("Missing open parenthesis.");
			}
			else if(this.lookahead == Token.TOK_SUB)
			{
				this.readToken();
				if(this.lookahead == Token.TOK_OP)
				{
					this.readToken();
					CALC_();
					return;
				}

				throw new Exception("Missing open parenthesis.");
			}
			else if(this.lookahead == Token.TOK_MULT)
			{
				this.readToken();
				if(this.lookahead == Token.TOK_OP)
				{
					this.readToken();
					CALC_();
					return;
				}

				throw new Exception("Missing open parenthesis.");
			}		
		}
		catch(Exception ex)
		{
			throw ex;
		}	

		throw new Exception("Invalid calc operation.");
	}

	// CALC' → NUMEXPR , CALC"
	private void CALC_() throws Exception
	{
		try
		{
			NUMEXPR();

			if(this.lookahead == Token.TOK_COMM)
			{
				this.readToken();
				CALC__();
				return;
			}
		}
		catch(Exception ex)
		{
			throw ex;
		}

		throw new Exception("Missing parameter in calc operation.");
	}

	// CALC" → NUMEXPR )
	private void CALC__() throws Exception
	{
		try
		{
			NUMEXPR();

			if(this.lookahead == Token.TOK_CP)
			{
				this.readToken();
				return;
			}
		}
		catch(Exception ex)
		{
			throw ex;
		}

		throw new Exception("Expected closing parenthesis.");
	}

	// ASSIGN → VAR = ASSIGN'
	private void ASSIGN() throws Exception
	{
		System.out.println("\t! ASSIGN !");

		try
		{
			VAR();
			if(this.lookahead == Token.TOK_ASSN)
			{
				this.readToken();
				ASSIGN_();
				return;
			}
		}
		catch(Exception ex)
		{
			throw ex;
		}

		throw new Exception("Invalid assignment: Unknown Operator.");
	}

	// ASSIGN' → stringLiteral | VAR | NUMEXPR | BOOL
	private void ASSIGN_() throws Exception
	{
		try
		{
			if(this.lookahead == Token.TOK_S)
			{
				this.readToken();
				return;
			}
			else if(this.lookahead == Token.TOK_ID)
			{
				VAR();
				return;
			}
			else if(this.lookahead == Token.TOK_N || this.lookahead == Token.TOK_ADD || this.lookahead == Token.TOK_SUB || this.lookahead == Token.TOK_MULT)
			{
				NUMEXPR();
				return;
			}
			else if(this.lookahead == Token.TOK_T || this.lookahead == Token.TOK_F || this.lookahead == Token.TOK_EQ || this.lookahead == Token.TOK_OP || this.lookahead == Token.TOK_NOT || this.lookahead == Token.TOK_AND || this.lookahead == Token.TOK_OR) 
			{
				BOOL();
				return;
			}
		}
		catch(Exception ex)
		{
			throw ex;
		}

		throw new Exception("Invalid assignment: Bad Right Operand.");
	}

	// INSTR → halt | DECL | IO | CALL | ASSIGN  | COND_BRANCH | COND_LOOP
	private void INSTR() throws Exception 
	{ 
		System.out.println("\t! INSTR !" + this.lookahead);

		try
		{
			if(this.lookahead == Token.TOK_HALT)
			{
				this.readToken();
				return;
			}
			else if(this.lookahead == Token.TOK_NUM || this.lookahead == Token.TOK_STRING || this.lookahead == Token.TOK_BOOL)
			{
				DECL();
				return;
			}
			else if(this.lookahead == Token.TOK_INPUT || this.lookahead == Token.TOK_OUTPUT)
			{
				IO();
				return;
			}
			else if(this.lookahead == Token.TOK_IF)
			{
				COND_BRANCH();
				return;
			}
			else if(this.lookahead == Token.TOK_WHILE)
			{
				COND_LOOP();
				return;
			}
			else if(this.lookahead == Token.TOK_ID && this.look(1) == Token.TOK_ASSN)
			{
				ASSIGN();
				return;
			}
			else if(this.lookahead == Token.TOK_ID)
			{
				CALL();
				return;
			}
		}
		catch(Exception ex)
		{
			throw ex;
		}

		throw new Exception("Instruction expected following semicolon. (;)");
	}

	// NUMEXPR → integerLiteral | VAR | CALC
	private void NUMEXPR() throws Exception
	{
		System.out.println("\t! NUMEXPR !");
		try
		{
			if(this.lookahead == Token.TOK_N)
			{
				this.readToken();
				return;
			}
			else if(this.lookahead == Token.TOK_ID)
			{
				VAR();
				return;
			}
			else if(this.lookahead == Token.TOK_ADD || this.lookahead == Token.TOK_SUB || this.lookahead == Token.TOK_MULT)
			{
				CALC();
				return;
			}
		}
		catch(Exception ex)
		{
			throw ex;
		}

		throw new Exception("Invalid numerical expression given.");
	}

	// TYPE → num | string | bool
	private void TYPE() throws Exception
	{
		System.out.println("\t! TYPE !");
		try
		{
			if(this.lookahead == Token.TOK_NUM)
			{
				this.readToken();
				return;
			}
			else if(this.lookahead == Token.TOK_STRING)
			{
				this.readToken();
				return;
			}
			else if(this.lookahead == Token.TOK_BOOL)
			{
				this.readToken();
				return;
			}
		}
		catch(Exception ex)
		{
			throw ex;
		}

		throw new Exception("Invalid type specified.");
	}

	// CALL → userDefinedIdentifier
	private void CALL() throws Exception
	{
		System.out.println("\t! CALL !");

		try
		{
			if(this.lookahead == Token.TOK_ID)
			{
				this.readToken();
				return;
			}
		}
		catch(Exception ex)
		{
			throw ex;
		}

		throw new Exception("Invalid identifier called.");
	}

	// NAME → userDefinedIdentifier
	private void NAME() throws Exception
	{
		System.out.println("\t! NAME !");
		try
		{
			if(this.lookahead == Token.TOK_ID)
			{
				this.readToken();
				return;
			}
		}
		catch(Exception ex)
		{
			throw ex;
		}

		throw new Exception("Invalid identifier used.");
	}

	// VAR → userDefinedIdentifier
	private void VAR() throws Exception
	{
		System.out.println("\t! VAR !");
		try
		{
			if(this.lookahead == Token.TOK_ID)
			{
				this.readToken();
				return;
			}
		}
		catch(Exception ex)
		{
			throw ex;
		}

		throw new Exception("Invalid identifier used.");
	}
} 