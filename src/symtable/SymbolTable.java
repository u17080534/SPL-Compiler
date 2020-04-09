package symtable;

import java.util.*;

public class SymbolTable
{
	Vector<Symbol> symbols;

	public SymbolTable()
	{
		this.symbols = new Vector<Symbol>();
	}

	public void add(Symbol sym)
	{
		if(this.symbols.contains(sym))
			this.symbols.set(sym.getID(), sym);
		else
			this.symbols.add(sym.getID(), sym);
	}

	@Override
	public String toString()
	{
		String str = "";

		for(int index = 0; index < this.symbols.size(); index++)
		{
			str += this.symbols.get(index).toString();
			if(index < this.symbols.size() - 1)
				str += "\n";
		}

		return str;
	}
}