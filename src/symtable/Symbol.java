package symtable;

import syntax.expression.*;

//SPL-COMPILER
public class Symbol
{
	private Expression expr;
	private int scope;
	private String proc;
	private String type;
	private String alias;

	public Symbol(Expression expr)
	{
		this.proc = "";
		this.type = "";
		this.expr = expr;
		this.alias = "";// this.expr.getID() + " " + this.expr.getExpr();
		this.scope = -1;
	}

	public int getID()
	{
		return this.expr.getID();
	}

	public String getExpression()
	{
		return this.expr.getExpr();
	}

	public Expression getExpr()
	{
		return this.expr;
	}

	public void setExpression(String expression)
	{
		this.alias = this.expr.getExpr();
		this.expr.setExpr(expression);
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

	public String getType()
	{
		return this.type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getAlias()
	{
		if(this.alias.equals(""))
			this.alias = this.expr.getExpr();

		return this.alias;
	}

	public String getLocation()
	{
		return this.expr.getLocation();
	}

	public boolean isTerminal()
	{
		return this.expr.isTerminal();
	}

	public boolean equals(Symbol other)
	{
		return this.getID() == other.getID();
	}

	public String getSymbol()
	{
		String str = this.expr.getID() + ":" + this.expr.getExpr();
		
		if(this.scope != -1)
			str = "[" + this.scope + "] " + str;


		if(!this.type.equals(""))
			str += " " + this.type;

		return str;
	}

	public String toPrint()
	{
		String strAlias = this.expr.getExpr();
		String strType = "";

		if(!this.alias.equals(""))
			strAlias = this.alias;

		if(!this.type.equals(""))
			strType = " " + this.type;

		return  this.expr.getID() + ":" + strAlias + strType;
	}

	@Override
	public String toString()
	{	
		// String strAlias = this.expr.getExpr();
		String strType = "";

		if(!this.type.equals(""))
			strType = " " + this.type;
	
		return  this.expr.getID() + ":" + this.expr.getExpr() + strType;
	}
}
