package exception;
import lexer.Token.Tok;

public class SyntaxException extends Exception 
{
	private Tok token;

    public SyntaxException(Tok token, String message)
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