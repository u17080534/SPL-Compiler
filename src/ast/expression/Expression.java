package ast.expression;

import java.util.*;
import symtable.Symbol;

public abstract class Expression
{   
	public static int exprCount = 0;
	protected int id;
	protected int level;
	protected String expr;
	protected Vector<Expression> descendents;
	protected Symbol symbol;

	public Expression(Expression... descendents)
	{
		this.expr = "";
		this.descendents = new Vector<Expression>();

		for (Expression desc : descendents)
			if(desc != null)
				this.descendents.add(desc);

		this.id();
		this.level(0);

		this.symbol = new Symbol(this);
	}

	//!Must Evaluate to BASIC
	public abstract String eval(); 

	public void id()
	{
		this.id = exprCount++;

		for (Expression desc : this.descendents)
			desc.id();
	}

	public void level(int level)
	{
		this.level = level;

		for (Expression desc : this.descendents)
			desc.level(this.level + 1);
	}

	public void scope()
	{
		this.symbol.setScope(0);

		for (Expression desc : this.descendents)
				desc.scope(this.symbol.getScope());
	}

	public void scope(int scope)
	{
		this.symbol.setScope(scope);

		for (Expression desc : this.descendents)
				desc.scope(this.symbol.getScope());
	}

	public Symbol getSymbol()
	{
		return this.symbol;
	}

	public int getID()
	{
		return this.id;
	}

	public String getExpr()
	{
		return this.expr;
	}

	public void setExpr(String expr)
	{
		this.expr = expr;
	}

	public String getExpression()
	{
		String str = this.id + ":";

		for(int index = 0; index < this.descendents.size(); index++)
		{
			str += this.descendents.get(index).id;
			if(index < this.descendents.size() - 1)
				str += ",";
		}

		return str;
	}

	public int countDescendents()
	{
		return this.descendents.size();
	}

	public boolean isTerminal()
	{
		return false;
	}

	public void trim()
	{
		for(int index = 0; index < this.descendents.size(); index++)
			if(this.descendents.get(index).countDescendents() == 0 && !this.descendents.get(index).isTerminal())
				this.descendents.remove(index);

		for (Expression desc : this.descendents)
			desc.trim();
	}

	public Expression get(int index)
	{
		return this.descendents.get(index);
	}

	@Override public String toString() 
	{
		String str = "";

		if(this.level > 0)
			str += "|";

		for(int index = 0; index++ < this.level;)
			str += "__";

		str += this.id + " " + this.expr + System.getProperty("line.separator");

		for (Expression desc : this.descendents)
			str += desc.toString();

		return str;
	}
}
