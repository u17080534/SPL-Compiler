package analysis;

import java.util.*;
import syntax.*;
import symtable.*;
import exception.UsageException;

//SPL-COMPILER
//SPL-COMPILER
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

		//ASSIGN PROC NAME TO EVERY SYMBOL USING STACK
		String procName = "";
		Stack<String> procStack = new Stack<String>();
		for(int index = 0; index < symbols.size(); index++)
		{
			symbols.get(index).setProc(procName);

			if(index + 1 < symbols.size())
			{
				int scope_diff = symbols.get(index + 1).getScope() - symbols.get(index).getScope();

				if(scope_diff < 0)
					for(int step = scope_diff; step < 0; step++)
						procName = procStack.pop();

				else if(scope_diff > 0)
					for(int step = 0; step < scope_diff; step++)
					{
						if(symbols.get(index).getExpression().indexOf("proc") == 0) //ProcDef
						{
							procStack.push(procName);
							procName = getValue(symbols.get(index).getExpression());
						}
						else //While/For/If/Else
						{
							procStack.push(procName);
							procName = procName + symbols.get(index).getID();
						}
					}
			}
		}

		//BUILD DECLARATIONS LIST & USAGES LIST
		for(int index = 0; index < terminals.size(); index++)
		{
			if(terminals.get(index).getExpression().indexOf("variable") == 0)
			{
				if(index > 0 && terminals.get(index - 1).getExpression().indexOf("type") == 0)
				{
					declarations.add(terminals.get(index));
					terminals.get(index).setType(getValue(terminals.get(index - 1).getExpression()));
				}
				else
					usages.add(terminals.get(index));
			}
			else if(terminals.get(index).getExpression().indexOf("call") == 0)
				usages.add(terminals.get(index));
			else if(terminals.get(index).getExpression().indexOf("proc") == 0)
				declarations.add(terminals.get(index));
		}

		//CHECK FOR REDEFINITIONS IN DECLARATIONS
		for(int index_decl = 0; index_decl < declarations.size(); index_decl++)
			for(int index_decl_ = 0; index_decl_ < declarations.size(); index_decl_++)
				//Names Match
				if(index_decl != index_decl_ && getValue(declarations.get(index_decl).getExpression()).equals(getValue(declarations.get(index_decl_).getExpression())))
				{
					//Different code segment, so treat as different var
					if(declarations.get(index_decl).getScope() < declarations.get(index_decl_).getScope())
						throw new UsageException(declarations.get(Math.max(index_decl, index_decl_)), "Identifier is already defined outside of this scope");

					if(declarations.get(index_decl).getScope() == declarations.get(index_decl_).getScope() && declarations.get(index_decl).getProc() == declarations.get(index_decl_).getProc())
						throw new UsageException(declarations.get(Math.max(index_decl, index_decl_)), "Identifier is used more than once within same scope");
					// else
						// declarations.get(index_decl_).setExpression(declarations.get(index_decl_).getExpression().substring(0,declarations.get(index_decl_).getExpression().length()) + index_decl_ +"'");
				}

		//MARK ALL USAGES WITHOUT A VALID DEFINITION
		for(int index_use = 0; index_use < usages.size(); index_use++)
		{
			boolean declared = false;
			Symbol usage = usages.get(index_use);
			for(int index_decl = 0; index_decl < declarations.size(); index_decl++)
			{
				Symbol decl = declarations.get(index_decl);
				if(getValue(decl.getExpression()).equals(getValue(usage.getExpression())) && decl.getID() < usage.getID() && (decl.getScope() < usage.getScope() || (decl.getScope() == usage.getScope() && usage.getProc().indexOf(decl.getProc()) == 0)))
				{
					declared = true;
					usage.setType(decl.getType());
				}
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
					if(decl.getScope() < usage.getScope() || (decl.getScope() == usage.getScope() && usage.getProc().indexOf(decl.getProc()) == 0))
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

		symbols = table.list();
	}

	public static String getValue(String expr)
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