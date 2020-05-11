package exception;

import symtable.Symbol;

//SPL-COMPILER
public class AnalysisException extends Exception 
{
	private Symbol sym;

	public AnalysisException(String message)
    {
        super(message);
    }

    public AnalysisException(Symbol sym, String message)
    {
        super(message + " [" + sym + "]" + sym.getLocation());
    }

    @Override 
    public String toString() 
	{ 
		String str = this.getMessage();
		return "Analysis Exception: " + str;
	}
}