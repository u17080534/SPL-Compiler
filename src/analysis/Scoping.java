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

		Vector<Symbol> symbols = table.list();
		Vector<Symbol> terminals = table.terminals();
		Vector<Symbol> declarations = new Vector<Symbol>();
		Vector<Symbol> usages = new Vector<Symbol>();
		Vector<String> undefined = new Vector<String>();

		//ASSIGN PROC NAME TO EVERY SYMBOL
		String procName = "";
		Stack<String> procStack = new Stack<String>();
		for(int index = 0; index < symbols.size(); index++)
		{
			symbols.get(index).setProc(procName);

			if(index + 1 < symbols.size())
			{
				if(symbols.get(index).getScope() < symbols.get(index + 1).getScope())
				{	
					if(symbols.get(index).getExpression().indexOf("proc") == 0)
					{
						procStack.push(procName);
						procName = symbols.get(index).getExpression().substring(4);
					}
					else
						throw new UsageException(symbols.get(index), "Unknown Scoping issue encountered - scope increased without proc definition...");
				}
				else if(symbols.get(index).getScope() > symbols.get(index + 1).getScope())
					procName = procStack.pop();
			}
		}

		//BUILD DECLARATIONS LIST & USAGES LIST
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
				usages.add(terminals.get(index));
			else if(terminals.get(index).getExpression().indexOf("proc") == 0)
				declarations.add(terminals.get(index));
		}

		//CHECK FOR REDEFINITIONS
		for(int index_decl = 0; index_decl < declarations.size(); index_decl++)
			for(int index_decl_ = 0; index_decl_ < declarations.size(); index_decl_++)
				if(index_decl != index_decl_ && getValue(declarations.get(index_decl).getExpression()).equals(getValue(declarations.get(index_decl_).getExpression())))
					throw new UsageException(declarations.get(Math.max(index_decl, index_decl_)), "Identifier is used more than once");

		//MARK ALL USAGES WITHOUT A VALID DEFINITION
		for(int index_use = 0; index_use < usages.size(); index_use++)
		{
			boolean declared = false;
			Symbol usage = usages.get(index_use);
			for(int index_decl = 0; index_decl < declarations.size(); index_decl++)
			{
				Symbol decl = declarations.get(index_decl);
				if(getValue(decl.getExpression()).equals(getValue(usage.getExpression())) && decl.getID() < usage.getID() && (decl.getScope() < usage.getScope() || (decl.getScope() == usage.getScope() && decl.getProc().equals(usage.getProc()))))
					declared = true;
			}

			if(!declared)
			{
				undefined.add(usage.toString());
				if(usage.getExpression().indexOf("variable") == 0)
					usage.setExpression("variable \'U\'");
				else if(usage.getExpression().indexOf("call") == 0)
					usage.setExpression("call \'U\'");
			}
		}

		//RENAME ALL VALID USAGES TO THE NEW NAME OF DEFINITION
		int varcount = 0;
		int proccount = 0;
		for(int index_decl = 0; index_decl < declarations.size(); index_decl++)
		{
			Symbol decl = declarations.get(index_decl);

			String rename = decl.getExpression();

			for(int index_use = 0; index_use < usages.size(); index_use++)
			{
				Symbol usage = usages.get(index_use);

				if(getValue(decl.getExpression()).equals(getValue(usage.getExpression())) && decl.getID() < usage.getID())
				{
					if(decl.getScope() < usage.getScope() || (decl.getScope() == usage.getScope() && decl.getProc().equals(usage.getProc())))
					{
						if(usage.getExpression().indexOf("variable") == 0)
							usage.setExpression("variable 'V" + varcount + "'");

						if(usage.getExpression().indexOf("call") == 0)
							usage.setExpression("call 'P" + proccount + "'");
					}
				}
			}

			if(decl.getExpression().indexOf("variable") == 0)
				rename = "variable 'V" + (varcount++) + "'";
			if(decl.getExpression().indexOf("proc") == 0)
				rename = "proc 'P" + (proccount++) + "'";

			decl.setExpression(rename);
		}

		if(undefined.size() > 0)
		{
			String str = undefined.get(0);
			for(int index = 1; index < undefined.size(); index++)
				str += "; " + undefined.get(index);

			throw new UsageException("There are undefined usages/calls: " + str);
		}
	}

	private static String getValue(String expr)
	{
		int start = expr.indexOf("'") + 1;

		int end = expr.lastIndexOf("'");

		if(start == -1)
			start = 0;

		if(end == -1)
			end = expr.length();

		String str = expr.substring(start, end);

		return str;
	}
}