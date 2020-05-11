package exception;

import symtable.Symbol;

//SPL-COMPILER
public class ValueException extends AnalysisException
{
	private Symbol sym;

    public ValueException(String message)
    {
        super(message);
    }

    public ValueException(Symbol sym, String message)
    {
        super(sym, message);
    }

    @Override 
    public String toString() 
	{ 
		String str = this.getMessage();
		return "Value Exception: " + str;
	}
}