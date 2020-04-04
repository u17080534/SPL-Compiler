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
		lookahead = tokenstream.get(index).getValue();
	}

	public static void readToken()
	{
		Pair<String, Token> read = tokenstream.get(index);

		System.out.println(read);

		if(++index < tokenstream.size())
			lookahead = tokenstream.get(index).getValue();
		else
			lookahead = Token.NULL;
	}

	//PROG → CODE PROG'
	public static void PROG()
	{
		CODE();
		PROG_();
	}

	// PROG' → ; PROC_DEFS | ϵ
	public static void PROG_()
	{
		if(lookahead == Token.NULL)
		{
			return;
		}
		else if(lookahead == Token.TOK_SEMI)
		{
			readToken();
			PROC_DEFS();
			return;
		}

		//error
	}

	// PROC_DEFS → PROC PROC_DEFS'
	public static void PROC_DEFS()
	{
		PROC();
		PROC_DEFS_();
	}

	// PROC_DEFS' → PROC_DEFS | ϵ
	public static void PROC_DEFS_()
	{
		if(lookahead == Token.NULL)
		{
			return;
		}

		PROC_DEFS();
	}

	// PROC → proc UserDefinedIdentifier { PROG }
	public static void PROC()
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
			}
		}

		//error
	}

	// CODE → INSTR CODE'
	public static void CODE()
	{
		INSTR();
		CODE_();
	}

	// CODE' → ; CODE | ϵ
	public static void CODE_()
	{
		if(lookahead == Token.NULL)
		{
			return;
		}

		CODE();
	}

	// DECL → TYPE DECL'
	public static void DECL()
	{
		TYPE();
		DECL_();
	}

	// DECL' → ; DECL | ϵ
	public static void DECL_()
	{
		if(lookahead == Token.NULL)
		{
			return;
		}

		DECL();
	}

	// COND_BRANCH → if ( BOOL ) then { CODE } COND_BRANCH'
	public static void COND_BRANCH()
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
		//error
	}

	// COND_BRANCH'→ else { CODE } | ϵ
	public static void COND_BRANCH_()
	{
		if(lookahead == Token.NULL)
		{
			return;
		}
		else if(lookahead == Token.TOK_ELSE)
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
		//error
	}

	// COND_LOOP → while ( BOOL ) { CODE } | for ( VAR = 0; VAR < VAR ; VAR = add ( VAR , 1 ) ) { CODE }
	public static void COND_LOOP()
	{

	}

	// IO → input ( VAR ) | output ( VAR )
	public static void IO()
	{

	}

	// BOOL → T | F | VAR | eq ( VAR , VAR ) | ( VAR < VAR ) | ( VAR > VAR ) | not BOOL | and ( BOOL' | or ( BOOL'
	public static void BOOL()
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

	// BOOL' → BOOL , BOOL"
	public static void BOOL_()
	{
		BOOL();

		if(lookahead == Token.TOK_COMM)
		{
			readToken();
			BOOL__();
			return;
		}
	}

	// BOOL" → BOOL )
	public static void BOOL__()
	{
		BOOL();
		if(lookahead == Token.TOK_CP)
		{
			readToken();
			return;
		}
	}

	// CALC → add ( NUMEXPR , CALC' | → sub ( NUMEXPR , CALC' | mult ( NUMEXPR , CALC'
	public static void CALC()
	{
		
	}

	// CALC' → NUMEXPR )
	public static void CALC_()
	{
		
	}

	// ASSIGN → VAR = ASSIGN'
	public static void ASSIGN()
	{
		
	}

	// ASSIGN' → stringLiteral | VAR | NUMEXPR | BOOL
	public static void ASSIGN_()
	{
		
	}

	// INSTR → halt | DECL | IO | CALL | ASSIGN  | COND_BRANCH | COND_LOOP
	public static void INSTR()
	{
		readToken();
	}

	// NUMEXPR → VAR | CALC | integerLiteral
	public static void NUMEXPR()
	{
		
	}

	// TYPE → num | string | bool
	public static void TYPE()
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
		//error
	}

	// CALL → userDefinedIdentifier
	public static void CALL()
	{
		if(lookahead == Token.TOK_ID)
		{
			readToken();
			return;
		}
	}

	// NAME → userDefinedIdentifier
	public static void NAME()
	{
		if(lookahead == Token.TOK_ID)
		{
			readToken();
			return;
		}
	}

	// VAR → userDefinedIdentifier
	public static void VAR()
	{
		if(lookahead == Token.TOK_ID)
		{
			readToken();
			return;
		}
	}
} 