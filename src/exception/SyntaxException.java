package exception;
import lexer.Token;

public class SyntaxException extends Exception 
{
	private Token token;

    public SyntaxException(Token token, String message)
    {
        super(message + " Token=[" + token + "]");
    	this.token = token;
    }

    @Override public String toString() 
	{ 
		String str = this.getMessage();
		return str;
	}
}