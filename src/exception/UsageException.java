package exception;

import symtable.Symbol;

public class UsageException extends Exception 
{
	private Symbol sym;

    public UsageException(Symbol sym, String message)
    {
        super(message + " Symbol=[" + sym + "]");
    	this.sym = sym;
    }

    @Override public String toString() 
	{ 
		String str = this.getMessage();
		return str;
	}
}