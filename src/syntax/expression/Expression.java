package syntax.expression;

import syntax.code.*;

import syntax.code.*;
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

	/**
		Trans Function for BASIC Code Generation
		Each concrete implementation will manage and return some Line object, and when necessary will add it to the recursively passed File
	**/
	public abstract Line trans(File absFile); 

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
		for (Expression desc : this.descendents)
			desc.trim();

		for(int index = 0; index < this.descendents.size(); index++)
			if(this.descendents.get(index).countDescendents() == 0 && !this.descendents.get(index).isTerminal())
				this.descendents.remove(index);
	}

	public Expression get(int index)
	{
		return this.descendents.get(index);
	}

	public Symbol getSymbol()
	{
		return this.symbol;
	}

	public int getID()
	{
		return this.id;
	}

	public String getType()
	{
		return this.symbol.getType();
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

	public String getExpr()
	{
		return this.expr;
	}

	public void setExpr(String expr)
	{
		this.expr = expr;
	}

	public String getLabel()
	{
		return this.expr;
	}

	public String getLocation()
	{
		return "";
	}

	public String print(String ind, boolean last) 
	{
		String str = ind;

		str += "|__";
		
		if(last)
			ind += "  ";
		else
			ind += "|  ";

		str += this + "\n";

		for (Expression desc : this.descendents)
			str += desc.print(ind, desc == this.descendents.lastElement());

		return str;
	}

	@Override 
	public String toString() 
	{
		return this.id + " " + this.expr;
	}
}
