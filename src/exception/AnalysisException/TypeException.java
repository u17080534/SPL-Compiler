package exception;

import symtable.Symbol;

public class TypeException extends AnalysisException 
{
    public TypeException(String message)
    {
        super(message);
    }

    public TypeException(Symbol sym, String message)
    {
        super(sym, message);
    }

    @Override public String toString() 
	{ 
		String str = this.getMessage();
		return "Type Exception: " + str;
	}
}
