import java.util.*;
import java.io.*;
import javafx.util.*;

public class Grammar
{
	private static int index;
	private static List<Pair<String, Token>> tokenstream;
	private static Token lookahead;

	public static void tokens(List<Pair<String, Token>> stream)
	{
		tokenstream = stream;
		index = 0;
		lookahead = look(0);

		try
        {
            while(lookahead != Token.NULL)
           		S();
        }
        catch(Exception ex)
        {
            System.out.println("Syntax Error: " + ex.getMessage());
        }
	}

	public static void readToken()
	{
		Pair<String, Token> read = tokenstream.get(index);

		if(index + 1 < tokenstream.size())
			lookahead = tokenstream.get(++index).getValue();
		else
			lookahead = Token.NULL;

		System.out.println(read);
	}

	private static Token look(int ahead)
	{
		if(index + ahead >= tokenstream.size())
			return Token.NULL;

		return tokenstream.get(index + ahead).getValue();
	}

	//S	→ PROC | PROG
	private static void S() throws Exception
	{
		try
		{
			if(lookahead == Token.TOK_PROC)
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
	private static void PROG() throws Exception
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
	private static void PROG_() throws Exception
	{
		try
		{
			if(lookahead == Token.TOK_SEMI)
			{
				readToken();

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
	private static void PROC_DEFS() throws Exception
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
	private static void PROC_DEFS_() throws Exception
	{
		try
		{
			if(lookahead == Token.TOK_PROC)
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
	private static void PROC() throws Exception
	{
		System.out.println("\t! PROC !");
		try
		{
			if(lookahead == Token.TOK_PROC)
			{
				readToken();
				if(lookahead == Token.TOK_ID)
				{
					readToken();
					if(lookahead == Token.TOK_OB)
					{
						readToken();
						PROG();
						if(lookahead == Token.TOK_CB)
						{
							readToken();
							return;
						}
					}

					throw new Exception("Missing Braces after proc defition.");
				}
			}
		}
		catch(Exception ex)
		{
			throw ex;
		}

		throw new Exception("Unexpected Token: " + lookahead + " - 'proc' expected.");
	}

	// CODE → INSTR CODE'
	private static void CODE() throws Exception
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
	private static void CODE_() throws Exception
	{
		try
		{
			if(lookahead == Token.TOK_SEMI)
			{
				readToken();

				if(lookahead != Token.NULL)
					CODE();

				return;
			}
		}
		catch(Exception ex)
		{
			throw ex;
		}
	}

	// DECL → TYPE NAME DECL'
	private static void DECL() throws Exception
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

	// DECL' → ; DECL | ϵ
	private static void DECL_() throws Exception
	{
		try
		{
			if(lookahead == Token.TOK_SEMI)
			{
				readToken();
				// CODE();
				return;
			}		
		}
		catch(Exception ex)
		{
			throw ex;
		}
	}

	// COND_BRANCH → if ( BOOL ) then { CODE } COND_BRANCH'
	private static void COND_BRANCH() throws Exception
	{
		try
		{
			if(lookahead == Token.TOK_IF)
			{
				readToken();
				if(lookahead == Token.TOK_OP)
				{
					readToken();
					BOOL();

					if(lookahead == Token.TOK_CP)
					{
						readToken();
						if(lookahead == Token.TOK_THEN)
						{
							readToken();
							if(lookahead == Token.TOK_OB)
							{
								readToken();
								CODE();
								if(lookahead == Token.TOK_CB)
								{
									readToken();
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
	private static void COND_BRANCH_() throws Exception
	{
		try
		{
			if(lookahead == Token.TOK_ELSE)
			{
				readToken();
				if(lookahead == Token.TOK_OB)
				{
					readToken();
					CODE();
					if(lookahead == Token.TOK_CB)
					{
						readToken();
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
	private static void COND_LOOP() throws Exception
	{
		try
		{
			if(lookahead == Token.TOK_WHILE)
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
	private static void IO() throws Exception
	{
		System.out.println("\t! IO !");
		try
		{
			if(lookahead == Token.TOK_INPUT)
			{
				readToken();
				if(lookahead == Token.TOK_OP)
				{
					readToken();
					VAR();
					if(lookahead == Token.TOK_CP)
					{
						readToken();
						return;
					}
				}
			}
			else if(lookahead == Token.TOK_OUTPUT)
			{
				readToken();
				if(lookahead == Token.TOK_OP)
				{
					readToken();
					VAR();
					if(lookahead == Token.TOK_CP)
					{
						readToken();
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
	private static void BOOL() throws Exception
	{
		try
		{
			if(lookahead == Token.TOK_T)
			{
				readToken();
				return;
			}
			else if(lookahead == Token.TOK_F)
			{
				readToken();
				return;
			}
			else if(lookahead == Token.TOK_ID)
			{
				VAR();
				return;
			}
			else if(lookahead == Token.TOK_EQ)
			{
				readToken();
				if(lookahead == Token.TOK_OP)
				{
					readToken();
					VAR();
					if(lookahead == Token.TOK_COMM)
					{
						readToken();
						VAR();
						if(lookahead == Token.TOK_CP)
						{
							readToken();
							return;
						}
					}
				}
			}
			else if(lookahead == Token.TOK_OP)
			{
				readToken();
				VAR();

				if(lookahead == Token.TOK_LT)
				{
					readToken();
					VAR();
					if(lookahead == Token.TOK_CP)
					{
						readToken();
						return;
					}
				}
				else if(lookahead == Token.TOK_GT)
				{
					readToken();
					VAR();
					if(lookahead == Token.TOK_CP)
					{
						readToken();
						return;
					}
				}
			}
			else if(lookahead == Token.TOK_NOT)
			{
				readToken();
				BOOL();
				return;
			}
			else if(lookahead == Token.TOK_AND)
			{
				readToken();
				if(lookahead == Token.TOK_OP)
				{
					readToken();
					BOOL_();
					return;
				}
			}
			else if(lookahead == Token.TOK_OR)
			{
				readToken();
				if(lookahead == Token.TOK_OP)
				{
					readToken();
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
	private static void BOOL_() throws Exception
	{
		try
		{
			BOOL();

			if(lookahead == Token.TOK_COMM)
			{
				readToken();
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
	private static void BOOL__() throws Exception
	{
		try
		{
			BOOL();
			if(lookahead == Token.TOK_CP)
			{
				readToken();
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
	private static void CALC() throws Exception
	{
		System.out.println("\t! CALC !");

		try
		{
			if(	lookahead == Token.TOK_ADD)
			{
				readToken();
				if(lookahead == Token.TOK_OP)
				{
					readToken();
					CALC_();
					return;
				}

				throw new Exception("Missing open parenthesis.");
			}
			else if(lookahead == Token.TOK_SUB)
			{
				readToken();
				if(lookahead == Token.TOK_OP)
				{
					readToken();
					CALC_();
					return;
				}

				throw new Exception("Missing open parenthesis.");
			}
			else if(lookahead == Token.TOK_MULT)
			{
				readToken();
				if(lookahead == Token.TOK_OP)
				{
					readToken();
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
	private static void CALC_() throws Exception
	{
		try
		{
			NUMEXPR();

			if(lookahead == Token.TOK_COMM)
			{
				readToken();
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
	private static void CALC__() throws Exception
	{
		try
		{
			NUMEXPR();

			if(lookahead == Token.TOK_CP)
			{
				readToken();
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
	private static void ASSIGN() throws Exception
	{
		System.out.println("\t! ASSIGN !");

		try
		{
			VAR();
			if(lookahead == Token.TOK_ASSN)
			{
				readToken();
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
	private static void ASSIGN_() throws Exception
	{
		try
		{
			if(lookahead == Token.TOK_S)
			{
				readToken();
				return;
			}
			else if(lookahead == Token.TOK_ID)
			{
				VAR();
				return;
			}
			else if(lookahead == Token.TOK_N || lookahead == Token.TOK_ADD || lookahead == Token.TOK_SUB || lookahead == Token.TOK_MULT)
			{
				NUMEXPR();
				return;
			}
			else if(lookahead == Token.TOK_T || lookahead == Token.TOK_F || lookahead == Token.TOK_EQ || lookahead == Token.TOK_OP || lookahead == Token.TOK_NOT || lookahead == Token.TOK_AND || lookahead == Token.TOK_OR) 
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
	private static void INSTR() throws Exception 
	{ 
		System.out.println("\t! INSTR !" + lookahead);

		try
		{
			if(lookahead == Token.TOK_HALT)
			{
				readToken();
				return;
			}
			else if(lookahead == Token.TOK_NUM || lookahead == Token.TOK_STRING || lookahead == Token.TOK_BOOL)
			{
				DECL();
				return;
			}
			else if(lookahead == Token.TOK_INPUT || lookahead == Token.TOK_OUTPUT)
			{
				IO();
				return;
			}
			else if(lookahead == Token.TOK_IF)
			{
				COND_BRANCH();
				return;
			}
			else if(lookahead == Token.TOK_WHILE)
			{
				COND_LOOP();
				return;
			}
			else if(lookahead == Token.TOK_ID && look(1) == Token.TOK_ASSN)
			{
				ASSIGN();
				return;
			}
			else if(lookahead == Token.TOK_ID)
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
	private static void NUMEXPR() throws Exception
	{
		System.out.println("\t! NUMEXPR !");
		try
		{
			if(lookahead == Token.TOK_N)
			{
				readToken();
				return;
			}
			else if(lookahead == Token.TOK_ID)
			{
				VAR();
				return;
			}
			else if(lookahead == Token.TOK_ADD || lookahead == Token.TOK_SUB || lookahead == Token.TOK_MULT)
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
	private static void TYPE() throws Exception
	{
		System.out.println("\t! TYPE !");
		try
		{
			if(lookahead == Token.TOK_NUM)
			{
				readToken();
				return;
			}
			else if(lookahead == Token.TOK_STRING)
			{
				readToken();
				return;
			}
			else if(lookahead == Token.TOK_BOOL)
			{
				readToken();
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
	private static void CALL() throws Exception
	{
		System.out.println("\t! CALL !");

		try
		{
			if(lookahead == Token.TOK_ID)
			{
				readToken();
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
	private static void NAME() throws Exception
	{
		System.out.println("\t! NAME !");
		try
		{
			if(lookahead == Token.TOK_ID)
			{
				readToken();
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
	private static void VAR() throws Exception
	{
		System.out.println("\t! VAR !");
		try
		{
			if(lookahead == Token.TOK_ID)
			{
				readToken();
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