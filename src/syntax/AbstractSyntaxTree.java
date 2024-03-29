package syntax;

import java.util.*;
import java.io.*;	
import syntax.expression.*;
import syntax.code.File;
import symtable.*;

//SPL-COMPILER
public class AbstractSyntaxTree
{
	Expression root;

	public AbstractSyntaxTree(Expression root)
	{
		Expression.exprCount = 0;
		this.root = root;
		this.root.trim();
		this.root.id();
	}

	public void trim()
	{
		Expression.exprCount = 0;

		//Recursive trim
		this.root.trim();
		this.root.id();
		this.root.level(0);
	}

	public void scope()
	{
		this.root.scope();
		//Find all variable declarations, name with Vn
		//Find all variable usages, check if declared in a <= scope and assign Vn, otherwise assign U
	}

	public File generation(String name)
	{
		File genFile = new File(name);

		this.root.trans(genFile);

		return genFile;
	}

	public Vector<Symbol> getSymbols()
	{
		Vector<Symbol> symbols = new Vector<Symbol>();

		symbols.add(this.root.getSymbol());
		for(int index = 0; index < this.root.countDescendents(); index++)
			symbols.addAll(this.getSymbols(this.root.get(index)));

		return symbols;
	}

	public Vector<Symbol> getSymbols(Expression exp)
	{	
		Vector<Symbol> symbols = new Vector<Symbol>();

		symbols.add(exp.getSymbol());
		for(int index = 0; index < exp.countDescendents(); index++)
			symbols.addAll(this.getSymbols(exp.get(index)));

		return symbols;
	}

	public String getExpressions()
	{
		String str = "";

		str += root.getExpression();

		for(int index = 0; index < root.countDescendents(); index++)
			if(root.get(index) != null)
				str += this.getExpressions(root.get(index));
			
		str += "\n";

		return str;
	}	

	public String getExpressions(Expression exp)
	{
		if(exp.countDescendents() == 0)
			return "";
		
		String str = "\n" + exp.getExpression();

		for(int index = 0; index < exp.countDescendents(); index++)
			if(exp.get(index) != null)
				str += this.getExpressions(exp.get(index));

		return str;
	}

	@Override 
	public String toString()
	{
		return root.print("", true);
	}
}