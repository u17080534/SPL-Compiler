import java.util.*;
import java.io.*;

public class Lexer 
{
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
	private List<String> tokens;

	public Lexer(BufferedReader buffer)
	{
	    this.startState = 26; //start state
		this.acceptStates = new ArrayList<Integer>();
		this.acceptStates.add(new Integer(0));
		this.acceptStates.add(new Integer(2));
		this.acceptStates.add(new Integer(20));
		this.acceptStates.add(new Integer(24));
		this.acceptStates.add(new Integer(32));
		this.acceptStates.add(new Integer(65));
		this.tokens = new ArrayList<String>();
		this.buffer = buffer;
	}

	//!Generates list of all tokens in input file, throws exception if unexpected input
	public ArrayList<String> readTokens() throws Exception
	{
		//Continues iterating till no more tokens can be read, or an error is encountered

		ArrayList<String> tokens = new ArrayList<String>();
		String token;

		while((token = this.readToken()) != "")
			tokens.add(token);

		return tokens;
	}

	private String readToken() throws Exception
	{
	    char ch = ' ';
		String token = "";
		int state = this.startState;
		Stack<Character> charStack = new Stack<Character>();

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

		Tokenized:
	    while(!this.emptyStream())
	    {	
	    	try
	    	{   	
	    		ch = this.read();
	    	}
	    	catch(IOException e)
	    	{
	    		//End of file in accept state
			    if(isAcceptState(state))
					while(!charStack.empty())
						token = (Character) charStack.pop() + token;

				return token;
	    	}

	    	// System.out.println("ch "+ch);
	    	// System.out.println("state "+state);

			if(state == 0)
			{
				if(isAcceptState(0))
				{
					while(!charStack.empty())
						token = (Character) charStack.pop() + token;

					//Return Token Directly when following input is irrelevant
					break Tokenized;
				}
				else
					throw new Exception("Unexpected Input: " + ch);
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
					throw new Exception("Unexpected Input: " + ch);
			}
			else if(state == 2)
			{
				//No Transitions
				if(isAcceptState(2))
				{
					while(!charStack.empty())
						token = (Character) charStack.pop() + token;

					break Tokenized;
				}
				else
					throw new Exception("Unexpected Input: " + ch);
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
					throw new Exception("Unexpected Input: " + ch);
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
					throw new Exception("Unexpected Input: " + ch);
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
					throw new Exception("Unexpected Input: " + ch);
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
					throw new Exception("Unexpected Input: " + ch);
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
					throw new Exception("Unexpected Input: " + ch);
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
					throw new Exception("Unexpected Input: " + ch);
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
					throw new Exception("Unexpected Input: " + ch);
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
					throw new Exception("Unexpected Input: Strings can only be up to 8 characters in length");
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

					break Tokenized;
                }
				else
					throw new Exception("Unexpected Input: Identifier Token Rejected");
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

					break Tokenized;
                }
                else
					throw new Exception("Unexpected Input: Integer Token Rejected");
			}
			else if(state == 25)
			{
				if(numInRange(ch, 1, 9))
                {
					charStack.push(new Character(ch));
                	state = 24;
                }
                else
                	throw new Exception("Unexpected Input: Integer Token Rejected");
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
				else if(matchChar(ch, "FT"))
				{
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
					throw new Exception("Unexpected Input: \"" + ch + "\"");
			}         
			else if(state == 27)
			{
				if(ch == 'u')
				{
					charStack.push(new Character(ch));
					state = 28;
				}
				else if(charIsLetter(ch))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else
					throw new Exception("Unexpected Input: " + ch);
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
					throw new Exception("Unexpected Input: " + ch);
			}
			else if(state == 29)
			{
				if(ch == 'n')
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
					throw new Exception("Unexpected Input: " + ch);
			}
			else if(state == 30)
			{
				if(ch == 'a')
				{
					charStack.push(new Character(ch));
					state = 28;
				}
				else if(charIsLetter(ch))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else
					throw new Exception("Unexpected Input: " + ch);
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
					throw new Exception("Unexpected Input: " + ch);
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
					throw new Exception("Unexpected Input: " + ch);
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
					throw new Exception("Unexpected Input: " + ch);
			}
			else if(state == 34)
			{
				if(ch == 'u')
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
					throw new Exception("Unexpected Input: " + ch);
			}
			else if(state == 35)
			{
				if(ch == 'o')
				{
					charStack.push(new Character(ch));
					state = 31;
				}
				else if(ch == 'u')
				{
					charStack.push(new Character(ch));
					state = 36;
				}
				else if(charIsLetter(ch))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else
					throw new Exception("Unexpected Input: " + ch);
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
					throw new Exception("Unexpected Input: " + ch);
			}
			else if(state == 37)
			{
				if(ch == 'o')
				{
					charStack.push(new Character(ch));
					state = 38;
				}
				else if(charIsLetter(ch))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else
					throw new Exception("Unexpected Input: " + ch);
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
					throw new Exception("Unexpected Input: " + ch);
			}
			else if(state == 39)
			{
				if(ch == 'q')
				{
					charStack.push(new Character(ch));
					state = 32;
				}
				else if(ch == 'l')
				{
					charStack.push(new Character(ch));
					state = 40;
				}
				else if(charIsLetter(ch))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else
					throw new Exception("Unexpected Input: " + ch);
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
					throw new Exception("Unexpected Input: " + ch);
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
					throw new Exception("Unexpected Input: " + ch);
			}
			else if(state == 42)
			{
				if(ch == 'o')
				{
					charStack.push(new Character(ch));
					state = 43;
				}
				else if(charIsLetter(ch))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else
					throw new Exception("Unexpected Input: " + ch);
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
					throw new Exception("Unexpected Input: " + ch);
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
					throw new Exception("Unexpected Input: " + ch);
			}
			else if(state == 45)
			{
				if(ch == 'd' || ch == 'n')
				{
					charStack.push(new Character(ch));
					state = 46;
				}
				else if(charIsLetter(ch))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else
					throw new Exception("Unexpected Input: " + ch);
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
					throw new Exception("Unexpected Input: " + ch);
			}
			else if(state == 47)
			{
				if(ch == 'u')
				{
					charStack.push(new Character(ch));
					state = 48;
				}
				else if(ch == 'r')
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
					throw new Exception("Unexpected Input: " + ch);
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
					throw new Exception("Unexpected Input: " + ch);
			}
			else if(state == 49)
			{
				if(ch == 'r')
				{
					charStack.push(new Character(ch));
					state = 50;
				}
				else if(charIsLetter(ch))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else
					throw new Exception("Unexpected Input: " + ch);
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
					throw new Exception("Unexpected Input: " + ch);
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
					throw new Exception("Unexpected Input: " + ch);
			}
			else if(state == 52)
			{
				if(ch == 't')
				{
					charStack.push(new Character(ch));
					state = 53;
				}
				else if(ch == 'u')
				{
					charStack.push(new Character(ch));
					state = 57;
				}
				else if(charIsLetter(ch))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else
					throw new Exception("Unexpected Input: " + ch);
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
					throw new Exception("Unexpected Input: " + ch);
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
					throw new Exception("Unexpected Input: " + ch);
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
					throw new Exception("Unexpected Input: " + ch);
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
					throw new Exception("Unexpected Input: " + ch);
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
					throw new Exception("Unexpected Input: " + ch);
			}
			else if(state == 58)
			{
				if(ch == 'h')
				{
					charStack.push(new Character(ch));
					state = 59;
				}
				else if(charIsLetter(ch))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else
					throw new Exception("Unexpected Input: " + ch);
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
					throw new Exception("Unexpected Input: " + ch);
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
					throw new Exception("Unexpected Input: " + ch);
			}
			else if(state == 61)
			{
				if(ch == 'h')
				{
					charStack.push(new Character(ch));
					state = 62;
				}
				else if(charIsLetter(ch))
				{
					charStack.push(new Character(ch));
					state = 20;
				}
				else
					throw new Exception("Unexpected Input: " + ch);
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
					throw new Exception("Unexpected Input: " + ch);
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
					throw new Exception("Unexpected Input: " + ch);
			}
			else if(state == 65)
			{
				if(isAcceptState(65))
				{
					while(!charStack.empty())
						token = (Character) charStack.pop() + token;

					break Tokenized;
				}
				else
					throw new Exception("Unexpected Input: " + ch);
			}
			else
				throw new Exception("Unexpected Behaviour when Traversing RegEx based DFA");
	    } 
		
	    //If token followed by space, save time by skipping it
		if(ch != ' ')
	    	this.bufferStack.push(ch);

	   	return token;
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
			throw new IOException("Empty Input Stream");

		ch = (char) this.readChar;

		while(this.readChar == '\n')
		{
			this.buffer.read();
			ch = (char) this.readChar;
		}

	    return ch;
	}

	public boolean isAcceptState(int id)
	{
		try
		{
	    	this.buffer.reset();
		}
		catch(IOException e) {}

		return true;
	}

	public boolean charIsLetter(char ch)
	{
		String str = "abcdefghijklmnopqrstuvwxyz";
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