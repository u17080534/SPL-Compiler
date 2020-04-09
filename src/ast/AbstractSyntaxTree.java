package ast;

import java.util.*;
import java.io.*;	
import ast.expression.*;

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

	public String getSymbols()
	{
		String str = "";

		for (Expression root : this.roots)
		{
			str += root.getExpr();
			for(int index = 0; index < root.countDescendents(); index++)
				str += this.getSymbols(root.get(index));
			str += "\n";
		}

		return str;
	}

	public String getSymbols(Expression exp)
	{	
		String str = "\n" + exp.getExpr();

		for(int index = 0; index < exp.countDescendents(); index++)
			str += this.getSymbols(exp.get(index));

		return str;
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