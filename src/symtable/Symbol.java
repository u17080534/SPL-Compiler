package symtable;

import ast.expression.*;

public class Symbol
{
	private Expression expr;
	private int scope;
	private String proc;
	private boolean altered;

	public Symbol(Expression expr)
	{
		this.proc = "";
		this.expr = expr;
		this.altered = false;
	}

	public int getID()
	{
		return this.expr.getID();
	}

	public String getExpression()
	{
		return this.expr.getExpr();
	}

	public void setExpression(String expression)
	{
		this.expr.setExpr(expression);
		this.altered = true;
	}

	public int getScope()
	{
		return this.scope;
	}

	public void setScope(int scope)
	{
		this.scope = scope;
	}

	public String getProc()
	{
		return this.proc;
	}

	public void setProc(String proc)
	{
		this.proc = proc;
	}

	public boolean isTerminal()
	{
		return this.expr.isTerminal();
	}

	public boolean isRenamed()
	{
		return this.altered;
	}

	@Override
	public String toString()
	{
		return this.expr.getID() + ":" + this.expr.getExpr();
	}
}