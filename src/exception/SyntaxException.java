package exception;
import lexer.Token;

//SPL-COMPILER
public class SyntaxException extends Exception 
{
	private Token token;

    public SyntaxException(Token token, String message)
    {
        super(message + " [" + (token == null ? "1,1" : token) + "]");
    	this.token = token;
    }

    @Override public String toString() 
	{ 
		String str = this.getMessage();
		return "Syntax Error: " + str;
	}
}