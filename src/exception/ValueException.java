package exception;

import symtable.Symbol;

//SPL-COMPILER
public class ValueException extends Exception
{
	private Symbol sym;

    public ValueException(String message)
    {
        super(message);
    }

    public ValueException(Symbol sym, String message)
    {
        super(message +  sym.getExpression() + " at " + sym.getLocation());
    	this.sym = sym;
    }

    @Override public String toString() 
	{ 
		String str = this.getMessage();
		return "Value Exception: " + str;
	}
}