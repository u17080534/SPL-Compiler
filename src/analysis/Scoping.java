package analysis;

import java.util.*;
import ast.*;
import symtable.*;
import exception.UsageException;

public class Scoping
{
	//Static Function which fixes the sco
	public static void check(AbstractSyntaxTree tree, SymbolTable table) throws UsageException
	{
		tree.scope();

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

		Vector<Symbol> terminals = table.terminals();
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
}