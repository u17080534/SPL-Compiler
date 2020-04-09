package exception;

public class LexerException extends Exception 
{
	private String location;

    public LexerException(String location, String message)
    {
        super(message);
    	this.location = location;
    }

    @Override public String toString() 
	{ 
		String str = this.getMessage();

		if(!this.location.equals(""))
			str = this.location + ": " + str;
		  
		return str;
	}
}