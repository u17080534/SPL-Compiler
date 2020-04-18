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

		if(str.indexOf("'#'") == 0)
    		str = "newline character was unexpected in this case";

		if(!this.location.equals(""))
			str = this.location + ": " + str;
		  
		return str;
	}
}