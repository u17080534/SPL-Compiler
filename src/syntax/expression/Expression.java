package syntax.expression;

import parser.*;
import symtable.Symbol;
import syntax.code.*;

import java.util.Vector;

public abstract class Expression
{   
	public static int exprCount = 0;
	protected int id;
	protected int level;
	protected String location;
	protected String expr;
	protected Symbol symbol;
	protected Expression parent;
	protected Vector<Expression> descendents;

	public Expression(Expression... descendents)
	{
		this.parent = null;
		this.expr = "";
		this.descendents = new Vector<Expression>();

		for (Expression desc : descendents)
			if(desc != null)
			{
				desc.setParent(this);
				this.descendents.add(desc);
			}

		this.id();
		this.level(0);

		this.location = Grammar.location();
		this.symbol = new Symbol(this);
	}

	// Trans Function for BASIC Code Generation, Each concrete implementation will manage and return some Line object, and when necessary will add it to the recursively passed File
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
		if(this.descendents.size() <= index)
			return null;
		
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

	public void setType(String type)
	{
		this.symbol.setType(type);
	}

	public String getTerminalType()
	{
		String type = this.descendents.get(0).getTerminalType();

		for (int index = 1; index < this.descendents.size(); index++)
			if(!type.equals(this.descendents.get(index).getTerminalType()))
				return "";

		this.symbol.setType(type);
		return type;
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
		return this.location;
	}

	public int getScope()
	{
		return this.symbol.getScope();
	}

	public void setScope(int scope)
	{
		this.symbol.setScope(scope);
	}

	public Expression getParent()
	{
		return this.parent;
	}

	public void setParent(Expression parent)
	{
		this.parent = parent;
	}

	public Vector<Expression> getDescendents()
	{
		return this.descendents;
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
		return this.id + " " + this.expr + "\t" + this.symbol.getType() + this.symbol.hasValueString();
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
