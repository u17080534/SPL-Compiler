import java.util.*;
import java.io.*;
import javafx.util.*;

public class Lexer 
{
	private String filename;
	//!Character Read Directly from Buffer
	private int readChar;
	//!Buffer for Reading Input File
	private BufferedReader buffer;
	//!Stack for Carrying over Rejected Characters
	Stack<Character> bufferStack = new Stack<Character>();
	//!List of Accept State ID's
	private List<Integer> acceptStates;
	//!Start State ID
	private int startState;
	//!List of Tokens Read from Input
	private List<Pair<String, String>> tokens;
	//!Row of Input File
	int row;
	//!Column of Input File
	int col;

	private enum Token
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
		TOK_T("tok_t"),
		TOK_F("tok_f"),
		TOK_GT("tok_greater"),
		TOK_LT("tok_less"),
		TOK_OP("tok_oparen"),
		TOK_CP("tok_cparen"),
		TOK_OB("tok_obrace"),
		TOK_CB("tok_cbrace"),
		TOK_ASS("tok_assign"),
		TOK_COMM("tok_comma"),
		TOK_SEMI("tok_semi"),
		TOK_STR("tok_str"),
		TOK_INT("tok_int"),
		TOK_ID("tok_id");

		private String str; 

		private Token(String str) { this.str = str; } 

		@Override public String toString() { return this.str; } 
	}

	public Lexer(String file) throws Exception
	{
	    this.startState = 26; //start state
		this.acceptStates = new ArrayList<Integer>();
		this.acceptStates.add(new Integer(0));
		this.acceptStates.add(new Integer(2));
		this.acceptStates.add(new Integer(20));
		this.acceptStates.add(new Integer(24));
		this.acceptStates.add(new Integer(32));
		this.acceptStates.add(new Integer(65));

		this.tokens = new ArrayList<Pair<String, String>>();

		this.filename = file;

		try
		{
			this.buffer = new BufferedReader(new FileReader(new File("../input/" + this.filename + ".spl")));
		}
		catch(Exception e)
		{
			throw e;
		}

		this.row = 1;
		this.col = 0;
	}

	public List<Pair<String, String>> getTokens()
	{
		return this.tokens;
	}

	//!Generates list of all tokens in input file, throws exception if unexpected input
	public List<Pair<String, String>> readTokens() throws Exception
	{
		//Continues iterating till no more tokens can be read, or an error is encountered

		boolean tokenize = true;
		Pair<String, String> token;

		while(tokenize)
		{
			try
			{
				tokenize = this.readToken();
			}
			catch(Exception exp)
			{
				tokenize = false;
				throw exp;
			}
		}

		String tokenFile = this.filename + ".tok";
        File file = new File("../output/" + tokenFile);
        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;

        try
        {
            fileWriter = new FileWriter(file);

            bufferedWriter = new BufferedWriter(fileWriter);

            for(int index = 0; index < tokens.size(); index++)
            {
                bufferedWriter.write((index + 1) + ": " + tokens.get(index).getKey() + " (" + tokens.get(index).getValue() + ")\n");
            }
        } 
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                bufferedWriter.close();
                fileWriter.close();
            } 
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

		return this.tokens;
	}

	private boolean readToken() throws Exception
	{
	    //Each State Contains its own IF Statement, which contains each of its transitions
	    /*
			Consider transitions of currState
				IF currentChar matches a transition
					-> add currentChar to charStack
					-> change currState to transitionState

				ELSE IF currentState is accept state (implied there is no matching transition)
					-> accept token:
						-> character stack (charStack) is cleared, and appended to the token string
						-> continue to follow the DFA, building the charStack

				ELSE
					-> reject input;

			-> read in next char and repeat
	    */

		char ch = ' ';
		String token = "";
		Token tokenRep = Token.NULL;
		int state = this.startState;
		Stack<Character> charStack = new Stack<Character>();
		boolean cont = true;

		Tokenized:
	    while(!this.emptyStream())
	    {	
	    	try
	    	{   	
	    		ch = this.read();
	    	}
	    	catch(IOException e)
	    	{
	    		ch = ' ';
	    		cont = false;
	    	}

			if(state == 0)
			{
				if(isAcceptState(0))
				{
					while(!charStack.empty())
						token = (Character) charStack.pop() + token;

					tokenRep = Token.TOK_INT;

					//Return Token Directly when following input is irrelevant
					break Tokenized;
				}
				else
					throw new Exception("[line: " + this.row + ", col: " + this.col + "]: " + "'" + ch + "' was unexpected in this case");
			}
			else if(state == 1)
			{
				if(ch == '"')
				{
					state = 2;
					charStack.push(new Character(ch));
				}
				else if(charIsLetter(ch) || numInRange(ch, 0, 9) || ch == ' ')
				{
					state = 3;
					charStack.push(new Character(ch));
				}
				else
					throw new Exception("[line: " + this.row + ", col: " + this.col + "]: " + "'" + ch + "' was unexpected in this case");
			}
			else if(state == 2)
			{
				//No Transitions
				if(isAcceptState(2))
				{
					while(!charStack.empty())
						token = (Character) charStack.pop() + token;

					tokenRep = Token.TOK_STR;

					break Tokenized;
				}
				else
					throw new Exception("[line: " + this.row + ", col: " + this.col + "]: " + "'" + ch + "' was unexpected in this case");
			}
			else if(state == 3)
			{
				if(ch == '"')
				{
					state = 2;
					charStack.push(new Character(ch));
				}
				else if(charIsLetter(ch) || numInRange(ch, 0, 9) || ch == ' ')
				{
					state = 5;
					charStack.push(new Character(ch));
				}
				else
					throw new Exception("[line: " + this.row + ", col: " + this.col + "]: " + "'" + ch + "' was unexpected in this case");
			}
			// else if(state == 4)
			else if(state == 5)
			{	
				if(ch == '"')
				{
					state = 2;
					charStack.push(new Character(ch));
				}
				else if(charIsLetter(ch) || numInRange(ch, 0, 9) || ch == ' ')
				{
					state = 7;
					charStack.push(new Character(ch));
				}
				else
					throw new Exception("[line: " + this.row + ", col: " + this.col + "]: " + "'" + ch + "' was unexpected in this case");
			}
			// else if(state == 6)
			else if(state == 7)
			{
				if(ch == '"')
				{
					state = 2;
					charStack.push(new Character(ch));
				}
				else if(charIsLetter(ch) || numInRange(ch, 0, 9) || ch == ' ')
				{
					state = 9;
					charStack.push(new Character(ch));
				}
				else
					throw new Exception("[line: " + this.row + ", col: " + this.col + "]: " + "'" + ch + "' was unexpected in this case");
			}
			// else if(state == 8)
			else if(state == 9)
			{
				if(ch == '"')
				{
					state = 2;
					charStack.push(new Character(ch));
				}
				else if(charIsLetter(ch) || numInRange(ch, 0, 9) || ch == ' ')
				{
					state = 11;
					charStack.push(new Character(ch));
				}
				else
					throw new Exception("[line: " + this.row + ", col: " + this.col + "]: " + "'" + ch + "' was unexpected in this case");
			}
			// else if(state == 10)
			else if(state == 11)
			{
				if(ch == '"')
				{
					state = 2;
					charStack.push(new Character(ch));
				}
				else if(charIsLetter(ch) || numInRange(ch, 0, 9) || ch == ' ')
				{
					state = 13;
					charStack.push(new Character(ch));
				}
				else
					throw new Exception("[line: " + this.row + ", col: " + this.col + "]: " + "'" + ch + "' was unexpected in this case");
			}
			// else if(state == 12)
			else if(state == 13)
			{
				if(ch == '"')
				{
					state = 2;
					charStack.push(new Character(ch));
				}
				else if(charIsLetter(ch) || numInRange(ch, 0, 9) || ch == ' ')
				{
					state = 15;
					charStack.push(new Character(ch));
				}
				else
					throw new Exception("[line: " + this.row + ", col: " + this.col + "]: " + "'" + ch + "' was unexpected in this case");
			}
			// else if(state == 14)
			else if(state == 15)
			{
				if(ch == '"')
				{
					state = 2;
					charStack.push(new Character(ch));
				}
				else if(charIsLetter(ch) || numInRange(ch, 0, 9) || ch == ' ')
				{
					state = 17;
					charStack.push(new Character(ch));
				}
				else
					throw new Exception("[line: " + this.row + ", col: " + this.col + "]: " + "'" + ch + "' was unexpected in this case");
			}
			// else if(state == 16)
			else if(state == 17)
			{
				if(ch == '"')
				{
					state = 2;
					charStack.push(new Character(ch));
				}
				else
				{
					String badstr = "";
					while(!charStack.empty())
						badstr = (Character) charStack.pop() + badstr;
					throw new Exception("[line: " + this.row + ", col: " + (this.col - 9) + "]: " + "'" + badstr + "' strings have at most 8 characters");
				}
			}
			// else if(state == 18)
			// else if(state == 19)
			else if(state == 20)
			{
				if(numInRange(ch, 0, 9) || charIsLetter(ch))
                {
					charStack.push(new Character(ch));
					state = 20;
				}
				else if(isAcceptState(20))
                {
                	while(!charStack.empty())
						token = (Character) charStack.pop() + token;

					tokenRep = Token.TOK_ID;

					break Tokenized;
                }
				else
					throw new Exception("[line: " + this.row + ", col: " + this.col + "]: " + "Unexpected Input: Identifier Token Rejected");
			}
			// else if(state == 21)
			// else if(state == 22)
			// else if(state == 23)
			else if(state == 24)
			{
				if(numInRange(ch, 0, 9))
                {
					charStack.push(new Character(ch));
					state = 24;
                }
                else if(isAcceptState(24))
                {
                	while(!charStack.empty())
						token = (Character) charStack.pop() + token;

					tokenRep = Token.TOK_INT;

					break Tokenized;
                }
                else
					throw new Exception("[line: " + this.row + ", col: " + this.col + "]: " + "Unexpected Input: Integer Token Rejected");
			}
			else if(state == 25)
			{
				if(numInRange(ch, 1, 9))
                {
					charStack.push(new Character(ch));
                	state = 24;
                }
                else
                	throw new Exception("[line: " + this.row + ", col: " + this.col + "]: " + "Unexpected Input: Integer Token Rejected");
			}
			else if(state == 26)
			{
				if(ch == '"')
				{
					state = 1;
					charStack.push(new Character(ch));
				}
				else if(matchChar(ch, "<> #(){}=;,")) //ch == <,>, ,#,(,),{,},=
				{
					state = 65;
					charStack.push(new Character(ch));
				}
				else if(numInRange(ch, 1, 9)) //ch == [1-9]
				{
					state = 24;
					charStack.push(new Character(ch));
				}
				else if(ch == '-')
				{
					state = 25;
					charStack.push(new Character(ch));
				}
				else if(ch == 'T')
				{
					tokenRep = Token.TOK_T;
					state = 32;
					charStack.push(new Character(ch));
				}
				else if(ch == 'F')
				{
					tokenRep = Token.TOK_F;
					state = 32;
					charStack.push(new Character(ch));
				}
				else if(matchChar(ch, "cdgjklqruvxyz"))
				{
					state = 20;
					charStack.push(new Character(ch));
				}
				else if(ch == 'a')
				{
					charStack.push(new Character(ch));
					state = 45;
				}
				else if(ch == 'b')
				{
					charStack.push(new Character(ch));
					state = 42;
				}
				else if(ch == 'e')
				{
					charStack.push(new Character(ch));
					state = 39;
				}
				else if(ch == 'f')
				{
					charStack.push(new Character(ch));
					state = 37;
				}
				else if(ch == 'i')
				{
					charStack.push(new Character(ch));
					state = 29;
				}
				else if(ch == 'h')
				{
					charStack.push(new Character(ch));
					state = 30;
				}
				else if(ch == 'm')
				{
					charStack.push(new Character(ch));
					state = 27;
				}
				else if(ch == 'n')
				{
					charStack.push(new Character(ch));
					state = 35;
				}
				else if(ch == 'o')
				{
					charStack.push(new Character(ch));
					state = 47;
				}
				else if(ch == 'p')
				{
					charStack.push(new Character(ch));
					state = 49;
				}
				else if(ch == 's')
				{
					charStack.push(new Character(ch));
					state = 52;
				}
				else if(ch == 't')
				{
					charStack.push(new Character(ch));
					state = 58;
				}
				else if(ch == 'w')
				{
					charStack.push(new Character(ch));
					state = 61;
				}
				else if(ch == '0')
				{
					charStack.push(new Character(ch));
					state = 0;
				}
				else
					throw new Exception("[line: " + this.row + ", col: " + this.col + "]: " + "Unexpected Input: \"" + ch + "\"");
			}         
			else if(state == 27)
			{
				if(ch == 'u')
				{
					tokenRep = Token.TOK_MULT;
					charStack.push(new Character(ch));
					state = 28;
				}
				else if(charIsLetter(ch))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else
					throw new Exception("[line: " + this.row + ", col: " + this.col + "]: " + "'" + ch + "' was unexpected in this case");
			}
			else if(state == 28)
			{    
				if(ch == 'l')
				{
					charStack.push(new Character(ch));
					state = 31;
				}
				else if(charIsLetter(ch))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else
					throw new Exception("[line: " + this.row + ", col: " + this.col + "]: " + "'" + ch + "' was unexpected in this case");
			}
			else if(state == 29)
			{
				if(ch == 'n')
				{
					tokenRep = Token.TOK_INPUT;
					charStack.push(new Character(ch));
					state = 33;
				}
				else if(ch == 'f')
				{
					tokenRep = Token.TOK_IF;
					charStack.push(new Character(ch));
					state = 32;
				}
				else if(charIsLetter(ch))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else
					throw new Exception("[line: " + this.row + ", col: " + this.col + "]: " + "'" + ch + "' was unexpected in this case");
			}
			else if(state == 30)
			{
				if(ch == 'a')
				{
					tokenRep = Token.TOK_HALT;
					charStack.push(new Character(ch));
					state = 28;
				}
				else if(charIsLetter(ch))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else
					throw new Exception("[line: " + this.row + ", col: " + this.col + "]: " + "'" + ch + "' was unexpected in this case");
			}
			else if(state == 31)
			{       
				if(ch == 't')
				{
					charStack.push(new Character(ch));
					state = 32;
				}
				else if(charIsLetter(ch))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else
					throw new Exception("[line: " + this.row + ", col: " + this.col + "]: " + "'" + ch + "' was unexpected in this case");
			}
			else if(state == 32)
			{
				if(numInRange(ch, 0, 9) || charIsLetter(ch))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else if(isAcceptState(32))
				{
					while(!charStack.empty())
						token = (Character) charStack.pop() + token;

					break Tokenized;
				}
				else
					throw new Exception("[line: " + this.row + ", col: " + this.col + "]: " + "'" + ch + "' was unexpected in this case");
			}
			else if(state == 33)
			{      
				if(ch == 'p')
				{
					charStack.push(new Character(ch));
					state = 34;
				}
				else if(charIsLetter(ch))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else
					throw new Exception("[line: " + this.row + ", col: " + this.col + "]: " + "'" + ch + "' was unexpected in this case");
			}
			else if(state == 34)
			{
				if(ch == 'u')
				{
					tokenRep = Token.TOK_INPUT;
					charStack.push(new Character(ch));
					state = 31;
				}
				else if(charIsLetter(ch))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else
					throw new Exception("[line: " + this.row + ", col: " + this.col + "]: " + "'" + ch + "' was unexpected in this case");
			}
			else if(state == 35)
			{
				if(ch == 'o')
				{
					tokenRep = Token.TOK_NOT;
					charStack.push(new Character(ch));
					state = 31;
				}
				else if(ch == 'u')
				{
					tokenRep = Token.TOK_NUM;
					charStack.push(new Character(ch));
					state = 36;
				}
				else if(charIsLetter(ch))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else
					throw new Exception("[line: " + this.row + ", col: " + this.col + "]: " + "'" + ch + "' was unexpected in this case");
			}
			else if(state == 36)
			{
				if(ch == 'm')
				{
					charStack.push(new Character(ch));
					state = 32;
				}
				else if(charIsLetter(ch))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else
					throw new Exception("[line: " + this.row + ", col: " + this.col + "]: " + "'" + ch + "' was unexpected in this case");
			}
			else if(state == 37)
			{
				if(ch == 'o')
				{
					tokenRep = Token.TOK_FOR;
					charStack.push(new Character(ch));
					state = 38;
				}
				else if(charIsLetter(ch))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else
					throw new Exception("[line: " + this.row + ", col: " + this.col + "]: " + "'" + ch + "' was unexpected in this case");
			}
			else if(state == 38)
			{
				if(ch == 'r')
				{
					charStack.push(new Character(ch));
					state = 32;
				}
				else if(charIsLetter(ch))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else
					throw new Exception("[line: " + this.row + ", col: " + this.col + "]: " + "'" + ch + "' was unexpected in this case");
			}
			else if(state == 39)
			{
				if(ch == 'q')
				{
					tokenRep = Token.TOK_EQ;
					charStack.push(new Character(ch));
					state = 32;
				}
				else if(ch == 'l')
				{
					tokenRep = Token.TOK_ELSE;
					charStack.push(new Character(ch));
					state = 40;
				}
				else if(charIsLetter(ch))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else
					throw new Exception("[line: " + this.row + ", col: " + this.col + "]: " + "'" + ch + "' was unexpected in this case");
			}
			else if(state == 40)
			{
				if(ch == 's')
				{
					charStack.push(new Character(ch));
					state = 41;
				}
				else if(charIsLetter(ch))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else
					throw new Exception("[line: " + this.row + ", col: " + this.col + "]: " + "'" + ch + "' was unexpected in this case");
			}
			else if(state == 41)
			{      
				if(ch == 'e')
				{
					charStack.push(new Character(ch));
					state = 32;
				}
				else if(charIsLetter(ch))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else
					throw new Exception("[line: " + this.row + ", col: " + this.col + "]: " + "'" + ch + "' was unexpected in this case");
			}
			else if(state == 42)
			{
				if(ch == 'o')
				{
					tokenRep = Token.TOK_BOOL;
					charStack.push(new Character(ch));
					state = 43;
				}
				else if(charIsLetter(ch))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else
					throw new Exception("[line: " + this.row + ", col: " + this.col + "]: " + "'" + ch + "' was unexpected in this case");
			}
			else if(state == 43)
			{
				if(ch == 'o')
				{
					charStack.push(new Character(ch));
					state = 44;
				}
				else if(charIsLetter(ch))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else
					throw new Exception("[line: " + this.row + ", col: " + this.col + "]: " + "'" + ch + "' was unexpected in this case");
			}
			else if(state == 44)
			{
				if(ch == 'l')
				{
					charStack.push(new Character(ch));
					state = 32;
				}
				else if(charIsLetter(ch))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else
					throw new Exception("[line: " + this.row + ", col: " + this.col + "]: " + "'" + ch + "' was unexpected in this case");
			}
			else if(state == 45)
			{
				if(ch == 'd')
				{
					tokenRep = Token.TOK_ADD;
					charStack.push(new Character(ch));
					state = 46;
				}
				else if(ch == 'n')
				{
					tokenRep = Token.TOK_AND;
					charStack.push(new Character(ch));
					state = 46;
				}
				else if(charIsLetter(ch))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else
					throw new Exception("[line: " + this.row + ", col: " + this.col + "]: " + "'" + ch + "' was unexpected in this case");
			}
			else if(state == 46)
			{
				if(ch == 'd')
				{
					charStack.push(new Character(ch));
					state = 32;
				}
				else if(charIsLetter(ch))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else
					throw new Exception("[line: " + this.row + ", col: " + this.col + "]: " + "'" + ch + "' was unexpected in this case");
			}
			else if(state == 47)
			{
				if(ch == 'u')
				{
					tokenRep = Token.TOK_OUTPUT;
					charStack.push(new Character(ch));
					state = 48;
				}
				else if(ch == 'r')
				{
					tokenRep = Token.TOK_OR;
					charStack.push(new Character(ch));
					state = 32;
				}
				else if(charIsLetter(ch))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else
					throw new Exception("[line: " + this.row + ", col: " + this.col + "]: " + "'" + ch + "' was unexpected in this case");
			}
			else if(state == 48)
			{
				if(ch == 't')
				{
					charStack.push(new Character(ch));
					state = 33;
				}
				else if(charIsLetter(ch))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else
					throw new Exception("[line: " + this.row + ", col: " + this.col + "]: " + "'" + ch + "' was unexpected in this case");
			}
			else if(state == 49)
			{
				if(ch == 'r')
				{
					tokenRep = Token.TOK_PROC;
					charStack.push(new Character(ch));
					state = 50;
				}
				else if(charIsLetter(ch))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else
					throw new Exception("[line: " + this.row + ", col: " + this.col + "]: " + "'" + ch + "' was unexpected in this case");
			}
			else if(state == 50)
			{
				if(ch == 'o')
				{
					charStack.push(new Character(ch));
					state = 51;
				}
				else if(charIsLetter(ch))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else
					throw new Exception("[line: " + this.row + ", col: " + this.col + "]: " + "'" + ch + "' was unexpected in this case");
			}
			else if(state == 51)
			{
				if(ch == 'c')
				{
					charStack.push(new Character(ch));
					state = 32;
				}
				else if(charIsLetter(ch))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else
					throw new Exception("[line: " + this.row + ", col: " + this.col + "]: " + "'" + ch + "' was unexpected in this case");
			}
			else if(state == 52)
			{
				if(ch == 't')
				{
					tokenRep = Token.TOK_STRING;
					charStack.push(new Character(ch));
					state = 53;
				}
				else if(ch == 'u')
				{
					tokenRep = Token.TOK_SUB;
					charStack.push(new Character(ch));
					state = 57;
				}
				else if(charIsLetter(ch))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else
					throw new Exception("[line: " + this.row + ", col: " + this.col + "]: " + "'" + ch + "' was unexpected in this case");
			}
			else if(state == 53)
			{
				if(ch == 'r')
				{
					charStack.push(new Character(ch));
					state = 54;
				}
				else if(charIsLetter(ch))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else
					throw new Exception("[line: " + this.row + ", col: " + this.col + "]: " + "'" + ch + "' was unexpected in this case");
			}
			else if(state == 54)
			{
				if(ch == 'i')
				{
					charStack.push(new Character(ch));
					state = 55;
				}
				else if(charIsLetter(ch))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else
					throw new Exception("[line: " + this.row + ", col: " + this.col + "]: " + "'" + ch + "' was unexpected in this case");
			}
			else if(state == 55)
			{
				if(ch == 'n')
				{
					charStack.push(new Character(ch));
					state = 56;
				}
				else if(charIsLetter(ch))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else
					throw new Exception("[line: " + this.row + ", col: " + this.col + "]: " + "'" + ch + "' was unexpected in this case");
			}
			else if(state == 56)
			{
				if(ch == 'g')
				{
					charStack.push(new Character(ch));
					state = 32;
				}
				else if(charIsLetter(ch))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else
					throw new Exception("[line: " + this.row + ", col: " + this.col + "]: " + "'" + ch + "' was unexpected in this case");
			}
			else if(state == 57)
			{
				if(ch == 'b')
				{
					charStack.push(new Character(ch));
					state = 32;
				}
				else if(charIsLetter(ch))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else
					throw new Exception("[line: " + this.row + ", col: " + this.col + "]: " + "'" + ch + "' was unexpected in this case");
			}
			else if(state == 58)
			{
				if(ch == 'h')
				{
					tokenRep = Token.TOK_THEN;
					charStack.push(new Character(ch));
					state = 59;
				}
				else if(charIsLetter(ch))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else
					throw new Exception("[line: " + this.row + ", col: " + this.col + "]: " + "'" + ch + "' was unexpected in this case");
			}
			else if(state == 59)
			{
				if(ch == 'e')
				{
					charStack.push(new Character(ch));
					state = 60;
				}
				else if(charIsLetter(ch))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else
					throw new Exception("[line: " + this.row + ", col: " + this.col + "]: " + "'" + ch + "' was unexpected in this case");
			}
			else if(state == 60)
			{
				if(ch == 'n')
				{
					charStack.push(new Character(ch));
					state = 32;
				}
				else if(charIsLetter(ch))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else
					throw new Exception("[line: " + this.row + ", col: " + this.col + "]: " + "'" + ch + "' was unexpected in this case");
			}
			else if(state == 61)
			{
				if(ch == 'h')
				{
					tokenRep = Token.TOK_WHILE;
					charStack.push(new Character(ch));
					state = 62;
				}
				else if(charIsLetter(ch))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else
					throw new Exception("[line: " + this.row + ", col: " + this.col + "]: " + "'" + ch + "' was unexpected in this case");
			}
			else if(state == 62)
			{
				if(ch == 'i')
				{
					charStack.push(new Character(ch));
					state = 63;
				}
				else if(charIsLetter(ch))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else
					throw new Exception("[line: " + this.row + ", col: " + this.col + "]: " + "'" + ch + "' was unexpected in this case");
			}
			else if(state == 63)
			{
				if(ch == 'l')
				{
					charStack.push(new Character(ch));
					state = 41;
				}
				else if(charIsLetter(ch))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else
					throw new Exception("[line: " + this.row + ", col: " + this.col + "]: " + "'" + ch + "' was unexpected in this case");
			}
			else if(state == 65)
			{
				if(isAcceptState(65))
				{
					while(!charStack.empty())
						token = (Character) charStack.pop() + token;

					if(token.indexOf('(') >= 0)
						tokenRep = Token.TOK_OP;
					else if(token.indexOf(')') >= 0)
						tokenRep = Token.TOK_CP;
					else if(token.indexOf('{') >= 0)
						tokenRep = Token.TOK_OB;
					else if(token.indexOf('}') >= 0)
						tokenRep = Token.TOK_CB;
					else if(token.indexOf('=') >= 0)
						tokenRep = Token.TOK_ASS;
					else if(token.indexOf('>') >= 0)
						tokenRep = Token.TOK_GT;
					else if(token.indexOf('<') >= 0)
						tokenRep = Token.TOK_LT;
					else if(token.indexOf(',') >= 0)
						tokenRep = Token.TOK_COMM;
					else if(token.indexOf(';') >= 0)
						tokenRep = Token.TOK_SEMI;
					else if(token.indexOf('#') >= 0)
					{
						this.row++;
						this.col = 0;
					}

					break Tokenized;
				}
				else
					throw new Exception("[line: " + this.row + ", col: " + this.col + "]: " + "'" + ch + "' was unexpected in this case");
			}
			else
				throw new Exception("[line: " + this.row + ", col: " + this.col + "]: " + "Unexpected Input Behaviour: " + ch);

			this.col++;

			if(!isValidChar(ch))
	    		throw new Exception("[line: " + this.row + ", col: " + this.col + "]: " + "'" + ch + "' is not a valid character");
	    } 
		
	    //If token followed by space, save time by skipping it
		if(ch != ' ')
	    	this.bufferStack.push(ch);

	    if(tokenRep != Token.NULL)
	    	this.tokens.add(new Pair<String, String>(token, tokenRep.toString()));

		this.col++;

	   	return cont;
	}

	private boolean emptyStream()
	{
		return this.readChar == -1 && this.bufferStack.empty();
	}

	//!Reads in next character in file stream, returns the previously last-read character
	private char read() throws IOException
	{
		if(!this.bufferStack.empty())
			return this.bufferStack.pop();

		char ch;

		try
		{
			this.readChar = this.buffer.read();
	    }
	    catch(IOException ex)
	    {
	    	this.readChar = -1;
	    }

	    if(this.readChar == -1)
			throw new IOException("[line: " + this.row + ", col: " + this.col + "]: " + "Empty Input Stream");

		ch = (char) this.readChar;

		while(this.readChar == '\n' || this.readChar == '\r')
		{
			this.readChar = this.buffer.read();
			ch = (char) this.readChar;
		}

	    return ch;
	}

	public boolean isAcceptState(int id)
	{
		return this.acceptStates.contains(id);
	}

	public boolean isValidChar(char ch)
	{
		String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789<>(){}# =,;\"";
		return str.indexOf(ch) >= 0;
	}

	public boolean charIsLetter(char ch)
	{
		String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		return str.indexOf(ch) >= 0;
	}

	public boolean matchChar(char ch, String str)
	{
		return str.indexOf(ch) >= 0;
	}

	public boolean numInRange(char ch, int min, int max)
	{
		int num = Character.getNumericValue(ch);
		return min <= num && num <= max;
	}	
}