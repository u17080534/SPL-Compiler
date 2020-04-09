package lexer;

import java.util.*;
import java.io.*;
import exception.*;

public class Lexer 
{
	private String filename;
	//!Character Read Directly from Buffer
	private int readChar;
	//!Buffer for Reading Input File
	private BufferedReader buffer;
	//!Stack for Carrying over Rejected Characters
	private Stack<Character> bufferStack;
	//!List of Accept State ID's
	private List<Integer> acceptStates;
	//!Start State ID
	private int startState;
	//!List of Toks Read from Input
	private List<Token> tokens;
	//!Row of Input File
	private int row;
	//!Column of Input File
	private int col;

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
		this.acceptStates.add(new Integer(27));
		this.acceptStates.add(new Integer(28));
		this.acceptStates.add(new Integer(29));
		this.acceptStates.add(new Integer(30));
		this.acceptStates.add(new Integer(31));
		this.acceptStates.add(new Integer(33));
		this.acceptStates.add(new Integer(34));
		this.acceptStates.add(new Integer(35));
		this.acceptStates.add(new Integer(36));
		this.acceptStates.add(new Integer(37));
		this.acceptStates.add(new Integer(38));
		this.acceptStates.add(new Integer(39));
		this.acceptStates.add(new Integer(40));
		this.acceptStates.add(new Integer(41));
		this.acceptStates.add(new Integer(42));
		this.acceptStates.add(new Integer(43));
		this.acceptStates.add(new Integer(44));
		this.acceptStates.add(new Integer(45));
		this.acceptStates.add(new Integer(46));
		this.acceptStates.add(new Integer(47));
		this.acceptStates.add(new Integer(48));
		this.acceptStates.add(new Integer(49));
		this.acceptStates.add(new Integer(50));
		this.acceptStates.add(new Integer(51));
		this.acceptStates.add(new Integer(52));
		this.acceptStates.add(new Integer(53));
		this.acceptStates.add(new Integer(54));
		this.acceptStates.add(new Integer(55));
		this.acceptStates.add(new Integer(56));
		this.acceptStates.add(new Integer(57));
		this.acceptStates.add(new Integer(58));
		this.acceptStates.add(new Integer(59));
		this.acceptStates.add(new Integer(60));
		this.acceptStates.add(new Integer(61));
		this.acceptStates.add(new Integer(62));
		this.acceptStates.add(new Integer(63));		

		this.tokens = new ArrayList<Token>();

		this.filename = file;

		try
		{
			this.buffer = new BufferedReader(new FileReader(new File("../input/" + this.filename + ".spl")));
			this.bufferStack = new Stack<Character>();
		}
		catch(FileNotFoundException e)
		{
			throw e;
		}

		this.row = 1;
		this.col = 1;
	}

	public List<Token> getTokens()
	{
		return this.tokens;
	}

	//!Generates list of all tokens in input file, throws exception if unexpected input
	public List<Token> readTokens() throws LexerException
	{
		//Continues iterating till no more tokens can be read, or an error is encountered
		boolean tokenize = true;

		while(tokenize)
		{
			try
			{
				tokenize = this.readToken();
			}
			catch(LexerException exp)
			{
				tokenize = false;
				throw exp;
			}
		}

        this.export();

		return this.tokens;
	}

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

	private boolean readToken() throws LexerException
	{
		int currRow = this.row;
		int currCol = this.col;

		char ch = 0;
		String token = "";
		Token.Tok tokenRep = Token.Tok.NULL;
		int state = this.startState;
		Stack<Character> charStack = new Stack<Character>();
		boolean cont = true;

		Tokized:
	    while(!this.emptyStream())
	    {	
	    	try
	    	{   	
	    		ch = this.read();
	    	}
	    	catch(EmptyStreamException ex)
	    	{
	    		ch = 0;
	    		cont = false;
	    	}

			if(state == 0)
			{
				if(isAcceptState(0))
				{
					while(!charStack.empty())
						token = (Character) charStack.pop() + token;

					tokenRep = Token.Tok.TOK_N;

					//Return Tok Directly when following input is irrelevant
					break Tokized;
				}
				else
					throw new LexerException(tokenPosition(token), ch + "' was unexpected in this case");
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
					throw new LexerException(tokenPosition(token), ch + "' was unexpected in this case");
			}
			else if(state == 2)
			{
				//No Transitions
				if(isAcceptState(2))
				{
					while(!charStack.empty())
						token = (Character) charStack.pop() + token;

					tokenRep = Token.Tok.TOK_S;

					break Tokized;
				}
				else
					throw new LexerException(tokenPosition(token), ch + "' was unexpected in this case");
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
					throw new LexerException(tokenPosition(token), ch + "' was unexpected in this case");
			}
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
					throw new LexerException(tokenPosition(token), ch + "' was unexpected in this case");
			}
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
					throw new LexerException(tokenPosition(token), ch + "' was unexpected in this case");
			}
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
					throw new LexerException(tokenPosition(token), ch + "' was unexpected in this case");
			}
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
					throw new LexerException(tokenPosition(token), ch + "' was unexpected in this case");
			}
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
					throw new LexerException(tokenPosition(token), ch + "' was unexpected in this case");
			}
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
					throw new LexerException(tokenPosition(token), ch + "' was unexpected in this case");
			}
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
					throw new LexerException(tokenPosition(token), "'" + badstr + "' strings have at most 8 characters");
				}
			}
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

					tokenRep = Token.Tok.TOK_ID;

					break Tokized;
                }
				else
					throw new LexerException(tokenPosition(token), "Unexpected Input: Identifier Tok Rejected");
			}
			else if(state == 24)
			{
				if(numInRange(ch, 0, 9))
                {
					charStack.push(new Character(ch));
					state = 24;
                }
                else if(charIsLetter(ch))
                {
					throw new LexerException(tokenPosition(token), "Unexpected Input: Integer Tok May Not Contain Alphabet Characters");
                }
                else if(isAcceptState(24))
                {
                	while(!charStack.empty())
						token = (Character) charStack.pop() + token;

					tokenRep = Token.Tok.TOK_N;

					break Tokized;
                }
                else
					throw new LexerException(tokenPosition(token), "Unexpected Input: Integer Tok Rejected");
			}
			else if(state == 25)
			{
				if(numInRange(ch, 1, 9))
                {
					charStack.push(new Character(ch));
                	state = 24;
                }
                else
                	throw new LexerException(tokenPosition(token), "Unexpected Input: Integer Tok Rejected");
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
					tokenRep = Token.Tok.TOK_T;
					state = 32;
					charStack.push(new Character(ch));
				}
				else if(ch == 'F')
				{
					tokenRep = Token.Tok.TOK_F;
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
					throw new LexerException(tokenPosition(token), "Unexpected Input: \"" + ch + "\"");
			}         
			else if(state == 27)
			{
				if(ch == 'u')
				{
					tokenRep = Token.Tok.TOK_MULT;
					charStack.push(new Character(ch));
					state = 28;
				}
				else if(charIsLetter(ch) || numInRange(ch, 0, 9))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else if(isAcceptState(state))
				{
					while(!charStack.empty())
						token = (Character) charStack.pop() + token;

					tokenRep = Token.Tok.TOK_ID;

					break Tokized;
				}
				else
					throw new LexerException(tokenPosition(token), ch + "' was unexpected in this case");
			}
			else if(state == 28)
			{    
				if(ch == 'l')
				{
					charStack.push(new Character(ch));
					state = 31;
				}
				else if(charIsLetter(ch) || numInRange(ch, 0, 9))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else if(isAcceptState(state))
				{
					while(!charStack.empty())
						token = (Character) charStack.pop() + token;

					tokenRep = Token.Tok.TOK_ID;

					break Tokized;
				}
				else
					throw new LexerException(tokenPosition(token), ch + "' was unexpected in this case");
			}
			else if(state == 29)
			{
				if(ch == 'n')
				{
					tokenRep = Token.Tok.TOK_INPUT;
					charStack.push(new Character(ch));
					state = 33;
				}
				else if(ch == 'f')
				{
					tokenRep = Token.Tok.TOK_IF;
					charStack.push(new Character(ch));
					state = 32;
				}
				else if(charIsLetter(ch) || numInRange(ch, 0, 9))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else if(isAcceptState(state))
				{
					while(!charStack.empty())
						token = (Character) charStack.pop() + token;

					tokenRep = Token.Tok.TOK_ID;

					break Tokized;
				}
				else
					throw new LexerException(tokenPosition(token), ch + "' was unexpected in this case");
			}
			else if(state == 30)
			{
				if(ch == 'a')
				{
					tokenRep = Token.Tok.TOK_HALT;
					charStack.push(new Character(ch));
					state = 28;
				}
				else if(charIsLetter(ch) || numInRange(ch, 0, 9))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else if(isAcceptState(state))
				{
					while(!charStack.empty())
						token = (Character) charStack.pop() + token;

					tokenRep = Token.Tok.TOK_ID;

					break Tokized;
				}
				else
					throw new LexerException(tokenPosition(token), ch + "' was unexpected in this case");
			}
			else if(state == 31)
			{       
				if(ch == 't')
				{
					charStack.push(new Character(ch));
					state = 32;
				}
				else if(charIsLetter(ch) || numInRange(ch, 0, 9))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else if(isAcceptState(state))
				{
					while(!charStack.empty())
						token = (Character) charStack.pop() + token;

					tokenRep = Token.Tok.TOK_ID;

					break Tokized;
				}
				else
					throw new LexerException(tokenPosition(token), ch + "' was unexpected in this case");
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

					break Tokized;
				}
				else
					throw new LexerException(tokenPosition(token), ch + "' was unexpected in this case");
			}
			else if(state == 33)
			{      
				if(ch == 'p')
				{
					charStack.push(new Character(ch));
					state = 34;
				}
				else if(charIsLetter(ch) || numInRange(ch, 0, 9))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else if(isAcceptState(state))
				{
					while(!charStack.empty())
						token = (Character) charStack.pop() + token;

					tokenRep = Token.Tok.TOK_ID;

					break Tokized;
				}
				else
					throw new LexerException(tokenPosition(token), ch + "' was unexpected in this case");
			}
			else if(state == 34)
			{
				if(ch == 'u')
				{
					charStack.push(new Character(ch));
					state = 31;
				}
				else if(charIsLetter(ch) || numInRange(ch, 0, 9))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else if(isAcceptState(state))
				{
					while(!charStack.empty())
						token = (Character) charStack.pop() + token;

					tokenRep = Token.Tok.TOK_ID;

					break Tokized;
				}
				else
					throw new LexerException(tokenPosition(token), ch + "' was unexpected in this case");
			}
			else if(state == 35)
			{
				if(ch == 'o')
				{
					tokenRep = Token.Tok.TOK_NOT;
					charStack.push(new Character(ch));
					state = 31;
				}
				else if(ch == 'u')
				{
					tokenRep = Token.Tok.TOK_NUM;
					charStack.push(new Character(ch));
					state = 36;
				}
				else if(charIsLetter(ch) || numInRange(ch, 0, 9))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else if(isAcceptState(state))
				{
					while(!charStack.empty())
						token = (Character) charStack.pop() + token;

					tokenRep = Token.Tok.TOK_ID;

					break Tokized;
				}
				else
					throw new LexerException(tokenPosition(token), ch + "' was unexpected in this case");
			}
			else if(state == 36)
			{
				if(ch == 'm')
				{
					charStack.push(new Character(ch));
					state = 32;
				}
				else if(charIsLetter(ch) || numInRange(ch, 0, 9))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else if(isAcceptState(state))
				{
					while(!charStack.empty())
						token = (Character) charStack.pop() + token;

					tokenRep = Token.Tok.TOK_ID;

					break Tokized;
				}
				else
					throw new LexerException(tokenPosition(token), ch + "' was unexpected in this case");
			}
			else if(state == 37)
			{
				if(ch == 'o')
				{
					tokenRep = Token.Tok.TOK_FOR;
					charStack.push(new Character(ch));
					state = 38;
				}
				else if(charIsLetter(ch) || numInRange(ch, 0, 9))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else if(isAcceptState(state))
				{
					while(!charStack.empty())
						token = (Character) charStack.pop() + token;

					tokenRep = Token.Tok.TOK_ID;

					break Tokized;
				}
				else
					throw new LexerException(tokenPosition(token), ch + "' was unexpected in this case");
			}
			else if(state == 38)
			{
				if(ch == 'r')
				{
					charStack.push(new Character(ch));
					state = 32;
				}
				else if(charIsLetter(ch) || numInRange(ch, 0, 9))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else if(isAcceptState(state))
				{
					while(!charStack.empty())
						token = (Character) charStack.pop() + token;

					tokenRep = Token.Tok.TOK_ID;

					break Tokized;
				}
				else
					throw new LexerException(tokenPosition(token), ch + "' was unexpected in this case");
			}
			else if(state == 39)
			{
				if(ch == 'q')
				{
					tokenRep = Token.Tok.TOK_EQ;
					charStack.push(new Character(ch));
					state = 32;
				}
				else if(ch == 'l')
				{
					tokenRep = Token.Tok.TOK_ELSE;
					charStack.push(new Character(ch));
					state = 40;
				}
				else if(charIsLetter(ch) || numInRange(ch, 0, 9))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else if(isAcceptState(state))
				{
					while(!charStack.empty())
						token = (Character) charStack.pop() + token;

					tokenRep = Token.Tok.TOK_ID;

					break Tokized;
				}
				else
					throw new LexerException(tokenPosition(token), ch + "' was unexpected in this case");
			}
			else if(state == 40)
			{
				if(ch == 's')
				{
					charStack.push(new Character(ch));
					state = 41;
				}
				else if(charIsLetter(ch) || numInRange(ch, 0, 9))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else if(isAcceptState(state))
				{
					while(!charStack.empty())
						token = (Character) charStack.pop() + token;

					tokenRep = Token.Tok.TOK_ID;

					break Tokized;
				}
				else
					throw new LexerException(tokenPosition(token), ch + "' was unexpected in this case");
			}
			else if(state == 41)
			{      
				if(ch == 'e')
				{
					charStack.push(new Character(ch));
					state = 32;
				}
				else if(charIsLetter(ch) || numInRange(ch, 0, 9))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else if(isAcceptState(state))
				{
					while(!charStack.empty())
						token = (Character) charStack.pop() + token;

					tokenRep = Token.Tok.TOK_ID;

					break Tokized;
				}
				else
					throw new LexerException(tokenPosition(token), ch + "' was unexpected in this case");
			}
			else if(state == 42)
			{
				if(ch == 'o')
				{
					tokenRep = Token.Tok.TOK_BOOL;
					charStack.push(new Character(ch));
					state = 43;
				}
				else if(charIsLetter(ch) || numInRange(ch, 0, 9))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else if(isAcceptState(state))
				{
					while(!charStack.empty())
						token = (Character) charStack.pop() + token;

					tokenRep = Token.Tok.TOK_ID;

					break Tokized;
				}
				else
					throw new LexerException(tokenPosition(token), ch + "' was unexpected in this case");
			}
			else if(state == 43)
			{
				if(ch == 'o')
				{
					charStack.push(new Character(ch));
					state = 44;
				}
				else if(charIsLetter(ch) || numInRange(ch, 0, 9))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else if(isAcceptState(state))
				{
					while(!charStack.empty())
						token = (Character) charStack.pop() + token;

					tokenRep = Token.Tok.TOK_ID;

					break Tokized;
				}
				else
					throw new LexerException(tokenPosition(token), ch + "' was unexpected in this case");
			}
			else if(state == 44)
			{
				if(ch == 'l')
				{
					charStack.push(new Character(ch));
					state = 32;
				}
				else if(charIsLetter(ch) || numInRange(ch, 0, 9))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else if(isAcceptState(state))
				{
					while(!charStack.empty())
						token = (Character) charStack.pop() + token;

					tokenRep = Token.Tok.TOK_ID;

					break Tokized;
				}
				else
					throw new LexerException(tokenPosition(token), ch + "' was unexpected in this case");
			}
			else if(state == 45)
			{
				if(ch == 'd')
				{
					tokenRep = Token.Tok.TOK_ADD;
					charStack.push(new Character(ch));
					state = 46;
				}
				else if(ch == 'n')
				{
					tokenRep = Token.Tok.TOK_AND;
					charStack.push(new Character(ch));
					state = 46;
				}
				else if(charIsLetter(ch) || numInRange(ch, 0, 9))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
                else if(isAcceptState(state))
				{
					while(!charStack.empty())
						token = (Character) charStack.pop() + token;

					tokenRep = Token.Tok.TOK_ID;

					break Tokized;
				}
				else
					throw new LexerException(tokenPosition(token), ch + "' was unexpected in this case");
			}
			else if(state == 46)
			{
				if(ch == 'd')
				{
					charStack.push(new Character(ch));
					state = 32;
				}
				else if(charIsLetter(ch) || numInRange(ch, 0, 9))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else if(isAcceptState(state))
				{
					while(!charStack.empty())
						token = (Character) charStack.pop() + token;

					tokenRep = Token.Tok.TOK_ID;

					break Tokized;
				}
				else
					throw new LexerException(tokenPosition(token), ch + "' was unexpected in this case");
			}
			else if(state == 47)
			{
				if(ch == 'u')
				{
					tokenRep = Token.Tok.TOK_OUTPUT;
					charStack.push(new Character(ch));
					state = 48;
				}
				else if(ch == 'r')
				{
					tokenRep = Token.Tok.TOK_OR;
					charStack.push(new Character(ch));
					state = 32;
				}
				else if(charIsLetter(ch) || numInRange(ch, 0, 9))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else if(isAcceptState(state))
				{
					while(!charStack.empty())
						token = (Character) charStack.pop() + token;

					tokenRep = Token.Tok.TOK_ID;

					break Tokized;
				}
				else
					throw new LexerException(tokenPosition(token), ch + "' was unexpected in this case");
			}
			else if(state == 48)
			{
				if(ch == 't')
				{
					charStack.push(new Character(ch));
					state = 33;
				}
				else if(charIsLetter(ch) || numInRange(ch, 0, 9))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else if(isAcceptState(state))
				{
					while(!charStack.empty())
						token = (Character) charStack.pop() + token;

					tokenRep = Token.Tok.TOK_ID;

					break Tokized;
				}
				else
					throw new LexerException(tokenPosition(token), ch + "' was unexpected in this case");
			}
			else if(state == 49)
			{
				if(ch == 'r')
				{
					tokenRep = Token.Tok.TOK_PROC;
					charStack.push(new Character(ch));
					state = 50;
				}
				else if(charIsLetter(ch) || numInRange(ch, 0, 9))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else if(isAcceptState(state))
				{
					while(!charStack.empty())
						token = (Character) charStack.pop() + token;

					tokenRep = Token.Tok.TOK_ID;

					break Tokized;
				}
				else
					throw new LexerException(tokenPosition(token), ch + "' was unexpected in this case");
			}
			else if(state == 50)
			{
				if(ch == 'o')
				{
					charStack.push(new Character(ch));
					state = 51;
				}
				else if(charIsLetter(ch) || numInRange(ch, 0, 9))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else if(isAcceptState(state))
				{
					while(!charStack.empty())
						token = (Character) charStack.pop() + token;

					tokenRep = Token.Tok.TOK_ID;

					break Tokized;
				}
				else
					throw new LexerException(tokenPosition(token), ch + "' was unexpected in this case");
			}
			else if(state == 51)
			{
				if(ch == 'c')
				{
					charStack.push(new Character(ch));
					state = 32;
				}
				else if(charIsLetter(ch) || numInRange(ch, 0, 9))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else if(isAcceptState(state))
				{
					while(!charStack.empty())
						token = (Character) charStack.pop() + token;

					tokenRep = Token.Tok.TOK_ID;

					break Tokized;
				}
				else
					throw new LexerException(tokenPosition(token), ch + "' was unexpected in this case");
			}
			else if(state == 52)
			{
				if(ch == 't')
				{
					tokenRep = Token.Tok.TOK_STRING;
					charStack.push(new Character(ch));
					state = 53;
				}
				else if(ch == 'u')
				{
					tokenRep = Token.Tok.TOK_SUB;
					charStack.push(new Character(ch));
					state = 57;
				}
				else if(charIsLetter(ch) || numInRange(ch, 0, 9))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else if(isAcceptState(state))
				{
					while(!charStack.empty())
						token = (Character) charStack.pop() + token;

					tokenRep = Token.Tok.TOK_ID;

					break Tokized;
				}
				else
					throw new LexerException(tokenPosition(token), ch + "' was unexpected in this case");
			}
			else if(state == 53)
			{
				if(ch == 'r')
				{
					charStack.push(new Character(ch));
					state = 54;
				}
				else if(charIsLetter(ch) || numInRange(ch, 0, 9))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else if(isAcceptState(state))
				{
					while(!charStack.empty())
						token = (Character) charStack.pop() + token;

					tokenRep = Token.Tok.TOK_ID;

					break Tokized;
				}
				else
					throw new LexerException(tokenPosition(token), ch + "' was unexpected in this case");
			}
			else if(state == 54)
			{
				if(ch == 'i')
				{
					charStack.push(new Character(ch));
					state = 55;
				}
				else if(charIsLetter(ch) || numInRange(ch, 0, 9))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else if(isAcceptState(state))
				{
					while(!charStack.empty())
						token = (Character) charStack.pop() + token;

					tokenRep = Token.Tok.TOK_ID;

					break Tokized;
				}
				else
					throw new LexerException(tokenPosition(token), ch + "' was unexpected in this case");
			}
			else if(state == 55)
			{
				if(ch == 'n')
				{
					charStack.push(new Character(ch));
					state = 56;
				}
				else if(charIsLetter(ch) || numInRange(ch, 0, 9))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else if(isAcceptState(state))
				{
					while(!charStack.empty())
						token = (Character) charStack.pop() + token;

					tokenRep = Token.Tok.TOK_ID;

					break Tokized;
				}
				else
					throw new LexerException(tokenPosition(token), ch + "' was unexpected in this case");
			}
			else if(state == 56)
			{
				if(ch == 'g')
				{
					charStack.push(new Character(ch));
					state = 32;
				}
				else if(charIsLetter(ch) || numInRange(ch, 0, 9))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else if(isAcceptState(state))
				{
					while(!charStack.empty())
						token = (Character) charStack.pop() + token;

					tokenRep = Token.Tok.TOK_ID;

					break Tokized;
				}
				else
					throw new LexerException(tokenPosition(token), ch + "' was unexpected in this case");
			}
			else if(state == 57)
			{
				if(ch == 'b')
				{
					charStack.push(new Character(ch));
					state = 32;
				}
				else if(charIsLetter(ch) || numInRange(ch, 0, 9))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else if(isAcceptState(state))
				{
					while(!charStack.empty())
						token = (Character) charStack.pop() + token;

					tokenRep = Token.Tok.TOK_ID;

					break Tokized;
				}
				else
					throw new LexerException(tokenPosition(token), ch + "' was unexpected in this case");
			}
			else if(state == 58)
			{
				if(ch == 'h')
				{
					tokenRep = Token.Tok.TOK_THEN;
					charStack.push(new Character(ch));
					state = 59;
				}
				else if(charIsLetter(ch) || numInRange(ch, 0, 9))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else if(isAcceptState(state))
				{
					while(!charStack.empty())
						token = (Character) charStack.pop() + token;

					tokenRep = Token.Tok.TOK_ID;

					break Tokized;
				}
				else
					throw new LexerException(tokenPosition(token), ch + "' was unexpected in this case");
			}
			else if(state == 59)
			{
				if(ch == 'e')
				{
					charStack.push(new Character(ch));
					state = 60;
				}
				else if(charIsLetter(ch) || numInRange(ch, 0, 9))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else if(isAcceptState(state))
				{
					while(!charStack.empty())
						token = (Character) charStack.pop() + token;

					tokenRep = Token.Tok.TOK_ID;

					break Tokized;
				}
				else
					throw new LexerException(tokenPosition(token), ch + "' was unexpected in this case");
			}
			else if(state == 60)
			{
				if(ch == 'n')
				{
					charStack.push(new Character(ch));
					state = 32;
				}
				else if(charIsLetter(ch) || numInRange(ch, 0, 9))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else if(isAcceptState(state))
				{
					while(!charStack.empty())
						token = (Character) charStack.pop() + token;

					tokenRep = Token.Tok.TOK_ID;

					break Tokized;
				}
				else
					throw new LexerException(tokenPosition(token), ch + "' was unexpected in this case");
			}
			else if(state == 61)
			{
				if(ch == 'h')
				{
					tokenRep = Token.Tok.TOK_WHILE;
					charStack.push(new Character(ch));
					state = 62;
				}
				else if(charIsLetter(ch) || numInRange(ch, 0, 9))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else if(isAcceptState(state))
				{
					while(!charStack.empty())
						token = (Character) charStack.pop() + token;

					tokenRep = Token.Tok.TOK_ID;

					break Tokized;
				}
				else
					throw new LexerException(tokenPosition(token), ch + "' was unexpected in this case");
			}
			else if(state == 62)
			{
				if(ch == 'i')
				{
					charStack.push(new Character(ch));
					state = 63;
				}
				else if(charIsLetter(ch) || numInRange(ch, 0, 9))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else if(isAcceptState(state))
				{
					while(!charStack.empty())
						token = (Character) charStack.pop() + token;

					tokenRep = Token.Tok.TOK_ID;

					break Tokized;
				}
				else
					throw new LexerException(tokenPosition(token), ch + "' was unexpected in this case");
			}
			else if(state == 63)
			{
				if(ch == 'l')
				{
					charStack.push(new Character(ch));
					state = 41;
				}
				else if(charIsLetter(ch) || numInRange(ch, 0, 9))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else if(isAcceptState(state))
				{
					while(!charStack.empty())
						token = (Character) charStack.pop() + token;

					tokenRep = Token.Tok.TOK_ID;

					break Tokized;
				}
				else
					throw new LexerException(tokenPosition(token), ch + "' was unexpected in this case");
			}
			else if(state == 65)
			{
				if(isAcceptState(65))
				{
					while(!charStack.empty())
						token = (Character) charStack.pop() + token;

					if(token.indexOf('(') >= 0)
						tokenRep = Token.Tok.TOK_OP;
					else if(token.indexOf(')') >= 0)
						tokenRep = Token.Tok.TOK_CP;
					else if(token.indexOf('{') >= 0)
						tokenRep = Token.Tok.TOK_OB;
					else if(token.indexOf('}') >= 0)
						tokenRep = Token.Tok.TOK_CB;
					else if(token.indexOf('=') >= 0)
						tokenRep = Token.Tok.TOK_ASSN;
					else if(token.indexOf('>') >= 0)
						tokenRep = Token.Tok.TOK_GT;
					else if(token.indexOf('<') >= 0)
						tokenRep = Token.Tok.TOK_LT;
					else if(token.indexOf(',') >= 0)
						tokenRep = Token.Tok.TOK_COMM;
					else if(token.indexOf(';') >= 0)
						tokenRep = Token.Tok.TOK_SEMI;
					else if(token.indexOf('#') >= 0) 
						{/*newline*/}

					break Tokized;
				}
				else
					throw new LexerException(tokenPosition(token), ch + "' was unexpected in this case");
			}
			else
				throw new LexerException(tokenPosition(token), "Unexpected Input Behaviour: " + ch);

			if(!isValidChar(ch))
	    		throw new LexerException(tokenPosition(token), ch + "' is not a valid character");
	    } 
		
	    //If token followed by space, save time by skipping it
	    if(ch != 0)
			this.bufferStack.push(ch);

	    if(tokenRep != Token.Tok.NULL)
	    {
	    	Token tokObject = new Token(token, tokenRep);
	    	tokObject.setLocation(currRow, currCol);
	    	this.tokens.add(tokObject);
	    }

	   	return cont;
	}

	//!Reads in next character in file stream, returns the previously last-read character
	private char read() throws EmptyStreamException
	{
		if(!this.bufferStack.empty())
			return this.bufferStack.pop();

		char ch = 0;

		try
		{
			this.readChar = this.buffer.read();

			ch = (char) this.readChar;

			if(this.readChar == '\n' || this.readChar == '\r')
			{
				if(System.getProperty("line.separator").equals("\r\n"))
					this.readChar = this.buffer.read(); //skip second char

				ch = '#';
				this.col = 0;
				this.row++;
			}
			else if(this.readChar == '\t')
				ch = ' ';
			else
				this.col++;
	    }
	    catch(IOException ex)
	    {
	    	this.readChar = -1;
	    }

	    if(this.readChar < 0)
			throw new EmptyStreamException("Empty Input Stream");

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

	private String tokenPosition(String token)
	{
		return "[line: " + this.row + ", col: " + (this.col - token.length()) + "]";
	}

	private boolean emptyStream()
	{
		return this.readChar < 0 && this.bufferStack.empty();
	}

	public void export()
	{
		String tokenFile = this.filename + ".tok";
		try
        {
            FileWriter fileWriter = new FileWriter(new File("../output/" + tokenFile));

            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            for(int index = 0; index < this.tokens.size(); index++)
            {
                bufferedWriter.write((index + 1) + ": " + tokens.get(index).getInput() + " (" + tokens.get(index).getToken() + ")\n");
            }

            bufferedWriter.close();
            fileWriter.close();
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }
	}
}