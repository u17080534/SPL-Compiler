package exception;

public class EmptyStreamException extends LexerException 
{
    public EmptyStreamException(String message)
    {
        super("", message);
    }
}