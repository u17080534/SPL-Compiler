package exception;

import symtable.Symbol;

//SPL-COMPILER
public class UsageException extends AnalysisException 
{
    public UsageException(String message)
    {
        super(message);
    }

    public UsageException(Symbol sym, String message)
    {
        super(sym, message);
    }

    @Override 
    public String toString() 
	{ 
		String str = this.getMessage();
		return "Usage Exception: " + str;
	}
}