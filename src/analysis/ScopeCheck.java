package analysis;

import java.util.*;
import syntax.*;
import syntax.expression.Expression;
import symtable.*;
import exception.UsageException;

//SPL-COMPILER
//SPL-COMPILER
public class ScopeCheck
{
	//Static Function which fixes the sco
	public static void check(AbstractSyntaxTree tree, SymbolTable table) throws UsageException
	{
		Vector<Symbol> symbols = table.list();
		Vector<Symbol> terminals = table.terminals();
		Vector<Symbol> declarations = new Vector<Symbol>();
		Vector<Symbol> usages = new Vector<Symbol>();
		Vector<String> undefined = new Vector<String>();
		Vector<String> aliases = new Vector<String>();

		//Traverse AST and assign scope values
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

		//BUILD DECLARATIONS LIST & USAGES LIST
		for(int index = 0; index < terminals.size(); index++)
		{
			if(terminals.get(index).getExpression().indexOf("variable") == 0)
			{
				if(index > 0 && terminals.get(index - 1).getExpression().indexOf("type") == 0)
				{
					declarations.add(terminals.get(index));
					terminals.get(index).setType(Character.toUpperCase(Expression.getValue(terminals.get(index - 1).getExpression()).charAt(0)) + "");
				}
				else
					usages.add(terminals.get(index));
			}
			else if(terminals.get(index).getExpression().indexOf("call") == 0)
				usages.add(terminals.get(index));
			else if(terminals.get(index).getExpression().indexOf("proc") == 0)
			{
				declarations.add(terminals.get(index));
				terminals.get(index).setType("P");
			}
		}

		//ORDER BY SCOPE & ID SO USAGE CHOOSES MOST RECENT DECLARATION
		for(int index_decl = 0; index_decl < declarations.size(); index_decl++)
			for(int index_decl_ = index_decl; index_decl_ < declarations.size(); index_decl_++)
				if(declarations.get(index_decl_).getScope() < declarations.get(index_decl).getScope())
				{
					Symbol temp = declarations.get(index_decl);
					declarations.set(index_decl, declarations.get(index_decl_));
					declarations.set(index_decl_, temp);
				}
				else if(declarations.get(index_decl_).getID() < declarations.get(index_decl).getID() && declarations.get(index_decl_).getScope() == declarations.get(index_decl).getScope())
				{
					Symbol temp = declarations.get(index_decl);
					declarations.set(index_decl, declarations.get(index_decl_));
					declarations.set(index_decl_, temp);
				}

		for(int index_usage = 0; index_usage < usages.size(); index_usage++)
			for(int index_usage_ = index_usage; index_usage_ < usages.size(); index_usage_++)
				if(usages.get(index_usage_).getScope() < usages.get(index_usage).getScope())
				{
					Symbol temp = usages.get(index_usage);
					usages.set(index_usage, usages.get(index_usage_));
					usages.set(index_usage_, temp);
				}
				else if(usages.get(index_usage_).getID() < usages.get(index_usage).getID() && usages.get(index_usage_).getScope() == usages.get(index_usage).getScope())
				{
					Symbol temp = usages.get(index_usage);
					usages.set(index_usage, usages.get(index_usage_));
					usages.set(index_usage_, temp);
				}

		//BUILD ALIASES OF ALL VALID USAGES TO THE NEW NAME OF DEFINITION
		int varcount = 0;
		int proccount = 0;
		for(int index_decl = 0; index_decl < declarations.size(); index_decl++)
		{
			Symbol decl = declarations.get(index_decl);

			String rename = decl.getExpression();

			if(decl.getExpression().indexOf("variable") == 0)
				rename = "variable 'V" + (varcount++) + "'";
			
			if(decl.getExpression().indexOf("proc") == 0)
				rename = "proc 'P" + (proccount++) + "'";

			aliases.add(rename);
		}

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
							// procName = Expression.getValue(symbols.get(index).getExpression());

							//find the index for alias
							for(int index_decl = declarations.size() - 1; index_decl >= 0; index_decl--)
								if(symbols.get(index).equals(declarations.get(index_decl)))
								{
									procName = Expression.getValue(aliases.get(index_decl));
									index_decl = 0;
								}
						}
						else //While/For/If/Else
						{
							procStack.push(procName);
							procName = procName + symbols.get(index).getID();
						}
					}
			}
		}

		//MARK ALL USAGES WITHOUT A VALID DEFINITION & SET TYPES
		for(int index_use = 0; index_use < usages.size(); index_use++)
		{
			boolean declared = false;

			Symbol usage = usages.get(index_use);

			for(int index_decl = 0; index_decl < declarations.size(); index_decl++)
			{
				Symbol decl = declarations.get(index_decl);

				if(decl.getScope() < usage.getScope() || (decl.getScope() == usage.getScope() && usage.getProc().equals(decl.getProc())))
				{
					if(usage.getExpression().indexOf("variable") == 0)
					{
						if(Expression.getValue(decl.getExpression()).equals(Expression.getValue(usage.getExpression())) && decl.getID() < usage.getID())
						{
							declared = true;
							usage.setType(decl.getType());
						}
					}
					else if(usage.getExpression().indexOf("call") == 0)
					{
						if(Expression.getValue(decl.getExpression()).equals(Expression.getValue(usage.getExpression())) && decl.getID() != usage.getID())
						{
							declared = true;
							usage.setType(decl.getType());
						}
					}
				}
			}

			if(!declared)
			{
				undefined.add("[" + usage.toString() + "]" + usage.getLocation());
				if(usage.getExpression().indexOf("variable") == 0)
					usage.setExpression("variable \'U\'");
				else if(usage.getExpression().indexOf("call") == 0)
					usage.setExpression("call \'U\'");
			}
		}

		//RENAME ALL VALID EXPRESSIONS
		for(int index_use = 0; index_use < usages.size(); index_use++)
		{
			Symbol usage = usages.get(index_use);
			for(int index_decl = declarations.size() - 1; index_decl >= 0; index_decl--)
			{
				Symbol decl = declarations.get(index_decl);

				//Same Identifier
				if(Expression.getValue(decl.getExpression()).equals(Expression.getValue(usage.getExpression())) && decl.getID() != usage.getID())
				{
					if(usage.getExpression().indexOf("variable") == 0) //Variable Usage
					{
						if(decl.getID() < usage.getID()) //Sequential Order
							if(decl.getScope() - usage.getScope() < 0 || (decl.getScope() == usage.getScope() && usage.getProc().equals(decl.getProc())))
								usage.setExpression(aliases.get(index_decl));
					}

					else if(usage.getExpression().indexOf("call") == 0) //Proc Usage
					{
						if(decl.getScope() - usage.getScope() < 0 || (decl.getScope() == usage.getScope() && usage.getProc().equals(decl.getProc())))
							usage.setExpression("call '" + Expression.getValue(aliases.get(index_decl)) + "'");
					}
				}
			}			
		}

		//CHECK FOR REDEFINITIONS IN DECLARATIONS
		for(int index_decl = 0; index_decl < declarations.size(); index_decl++)
			for(int index_decl_ = 0; index_decl_ < declarations.size(); index_decl_++)
				//Names Match
				if(index_decl != index_decl_ && Expression.getValue(declarations.get(index_decl).getExpression()).equals(Expression.getValue(declarations.get(index_decl_).getExpression())))
				{
					//Same scope and same proc
					if(declarations.get(index_decl).getScope() == declarations.get(index_decl_).getScope() && declarations.get(index_decl).getProc().equals(declarations.get(index_decl_).getProc()) && declarations.get(index_decl).getType().equals(declarations.get(index_decl_).getType()))
						throw new UsageException(declarations.get(Math.max(index_decl, index_decl_)), "Identifier is used more than once within same scope");
				}

		for(int index_decl = 0; index_decl < declarations.size(); index_decl++)
		{
			Symbol decl = declarations.get(index_decl);
			decl.setExpression(aliases.get(index_decl));
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
}