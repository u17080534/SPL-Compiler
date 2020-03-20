import java.util.ArrayList;
import java.util.List;

public class Lexer 
{
	private int readChar;
	private BufferedReader buffer;
	private int stateID;
	private List<Integer> acceptStates;
	private List<String> tokens;

	public Lexer(BufferedReader buffer)
	{
		this.buffer = buffer;
	    this.readChar = this.buffer.read();
		this.tokens = new ArrayList<>();
	    this.stateID = 26; //start state
	}

	//!Reads in next character in file stream, returns the previously last-read character
	private char read()
	{
		char ch = (char) this.readChar;
		this.readChar = this.buffer.read();
	    return ch;
	}

	//!Generates list of all tokens in input file, throws exception if unexpected input
	public ArrayList<String> readTokens() throws Exception
	{
		//Continues iterating till no more tokens can be read, or an error is encountered
	}

	private String readToken() throws Exception
	{
		string token = "";
	    char ch;

	    //Each State Contains its own IF Statement, which contains each of its transitions
	    /*
			Consider transitions of currState
				IF currentChar matches a transition
					add currentChar to token
					change currState to transitionState
				ELSE IF currentState is accept state (implied there is no matching transition)
					accept token;
				ELSE
					reject input;
			-> read in next char and repeat
	    */

	    while(this.readChar != -1) //readChar stays one character ahead in input stream
	    {
			ch = this.read(); //Cannot read in -1, only valid chars

			if(this.state == 26) //start at start state for this possible token
			{
				if(ch == '"')
				{
					this.state = 1;
					token += ch;
				}
				else if(matchChar(ch, "<> #(){}=")) //ch == <,>, ,#,(,),{,},=
				{
					this.state = 65;

				}
				else if(numInRange(ch, 1, 9)) //ch == [1-9]
				{
					this.state = 23;
				}
				else if(ch == '-')
				{
					this.state = ;
				}

			}

	    }
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