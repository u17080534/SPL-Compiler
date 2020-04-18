package symtable;

import java.util.*;
import exception.UsageException;

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

	public void scoping() throws UsageException
	{
		/*
			1. Find every usage of a variable
				(i) Rescan all definitions of less or equal scope
				(ii) If definition is found
					(a) Rename Definition Vn if not already renamed
					(b) Rename current variable to same name
				(iii) If not found
					(a) Rename variable to U
			2. Find every usage of a procedure
				(i) Rescan all definitions of less or equal scope
				(ii) If definition is found
					(a) Rename Definition Pn if not already renamed
					(b) Rename current procedure to same name
				(iii) If not found
					(a) Rename procedure to U
		*/

		Vector<Symbol> terminals = this.terminals();
		Vector<Symbol> declarations = new Vector<Symbol>(), usages = new Vector<Symbol>();
		Vector<Symbol> lost = new Vector<Symbol>();

		for(int index = 0; index < terminals.size(); index++)
		{
			if(terminals.get(index).getExpression().indexOf("variable") == 0)
			{
				if(index > 1 && terminals.get(index - 1).getExpression().indexOf("type") == 0)
					declarations.add(terminals.get(index));
				else
					usages.add(terminals.get(index));
			}
			else if(terminals.get(index).getExpression().indexOf("call") == 0)
			{

			}
			else if(terminals.get(index).getExpression().indexOf("proc") == 0)
			{
				
			}
		}

		for(int index_use = 0; index_use < usages.size(); index_use++)
		{
			boolean declared = false;
			Symbol usage = usages.get(index_use);
			for(int index_decl = 0; index_decl < declarations.size(); index_decl++)
			{
				Symbol decl = declarations.get(index_decl);
				if(getValue(decl.getExpression()).equals(getValue(usage.getExpression())) && decl.getScope() <= usage.getScope() && decl.getID() < usage.getID())
					declared = true;
			}

			if(!declared)
				usage.setExpression("variable \'U\'");
		}


		int varcount = 0;

		for(int index_decl = 0; index_decl < declarations.size(); index_decl++)
		{
			Symbol decl = declarations.get(index_decl);

			String rename;

			for(int index_use = 0; index_use < usages.size(); index_use++)
			{
				Symbol usage = usages.get(index_use);

				if(getValue(decl.getExpression()).equals(getValue(usage.getExpression())) && decl.getScope() <= usage.getScope() && decl.getID() < usage.getID())
					usage.setExpression("variable 'V" + varcount + "'");
			}

			rename = "variable 'V" + (varcount++) + "'";

			decl.setExpression(rename);
		}

		System.out.println(declarations);
		System.out.println(usages);
	}

	private Vector<Symbol> terminals()
	{
		Vector<Symbol> terminals = new Vector<Symbol>();

		for(Symbol sym : this.symbols)
			if(sym.isTerminal())
				terminals.add(sym);

		return terminals;
	}

	private static String getValue(String expr)
	{
		int start = expr.indexOf('\'') + 1;

		int end = expr.lastIndexOf('\'');

		if(start == -1)
			start = 0;

		if(end == -1)
			end = expr.length();

		expr.substring(start, end);

		return expr;
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
					int no_spaces = 25 - this.symbols.get(index).toString().length();
					if(no_spaces < 0) no_spaces = 0;
					for(int s = 0; s < no_spaces; s++)
						str += " ";
				}
		}

		return str;
	}
}