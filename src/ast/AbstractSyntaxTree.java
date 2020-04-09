package ast;

import java.util.*;
import java.io.*;	
import ast.expression.*;
import symtable.*;

public class AbstractSyntaxTree
{
	Vector<Expression> roots;

	public AbstractSyntaxTree(Vector<Expression> roots)
	{
		this.roots = roots;

		Expression.exprCount = 0;

		for (Expression root : this.roots)
		{
			//Recursive trim
			root.trim();
			root.id();
		}
	}

	public void trim()
	{
		Expression.exprCount = 0;

		for (Expression root : this.roots)
		{
			//Recursive trim
			root.trim();
			root.id();
			root.level(0);
		}
	}

	public void scope()
	{
		for (Expression root : this.roots)
			root.scope();
	}

	public Vector<Symbol> getSymbols()
	{
		Vector<Symbol> symbols = new Vector<Symbol>();

		for (Expression root : this.roots)
		{
			symbols.add(new Symbol(root.getID(), root.getExpr()));
			for(int index = 0; index < root.countDescendents(); index++)
				symbols.addAll(this.getSymbols(root.get(index)));
		}

		return symbols;
	}

	public Vector<Symbol> getSymbols(Expression exp)
	{	
		Vector<Symbol> symbols = new Vector<Symbol>();

		symbols.add(new Symbol(exp.getID(), exp.getExpr()));
		for(int index = 0; index < exp.countDescendents(); index++)
			symbols.addAll(this.getSymbols(exp.get(index)));

		return symbols;
	}

	public String getExpressions()
	{
		String str = "";

		for (Expression root : this.roots)
		{
			str += root.getExpression();

			for(int index = 0; index < root.countDescendents(); index++)
				if(root.get(index) != null)
					str += this.getExpressions(root.get(index));
				
			str += "\n";
		}

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

	@Override public String toString()
	{
		String str = "";
		for (Expression root : this.roots)
			str += root.toString();
		return str;
	}
}