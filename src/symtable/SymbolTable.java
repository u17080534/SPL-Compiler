package symtable;

import java.util.*;

//SPL-COMPILER
public class SymbolTable
{
	private Vector<Symbol> symbols;

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

	public Vector<Symbol> terminals()
	{
		Vector<Symbol> terminals = new Vector<Symbol>();

		for(Symbol sym : this.symbols)
			if(sym.isTerminal())
				terminals.add(sym);

		return terminals;
	}

	public Vector<Symbol> list()
	{
		return this.symbols;
	}

	@Override
	public String toString()
	{
		String str = "";

		for(int index = 0; index < this.symbols.size(); index++)
		{
			str += this.symbols.get(index).toString();
			if(index < this.symbols.size() - 1)
				if((index+1) % 5 == 0)
					str += "\n";
				else
				{
					int no_spaces = 30 - this.symbols.get(index).toString().length();
					if(no_spaces < 0) no_spaces = 0;
					for(int s = 0; s < no_spaces; s++)
						str += " ";
				}
		}

		return str;
	}
}