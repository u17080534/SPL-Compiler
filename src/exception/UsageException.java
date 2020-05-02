package exception;

import symtable.Symbol;

//SPL-COMPILER
public class UsageException extends Exception 
{
	private Symbol sym;

    public UsageException(String message)
    {
        super(message);
    }

    public UsageException(Symbol sym, String message)
    {
        super(message + " Symbol=[" + sym + "]" + sym.getLocation());
    	this.sym = sym;
    }

    @Override public String toString() 
	{ 
		String str = this.getMessage();
		return "Usage Exception: " + str;
	}
}