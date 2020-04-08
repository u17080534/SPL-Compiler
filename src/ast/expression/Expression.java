package ast.expression;
import java.util.*;

public abstract class Expression
{   
	static int exprCount = 0;

	private int id;
	private int level;
	private Vector descendents;

	public Expression(Expression... descendents)
	{
		this.level = 0;
		this.id = exprCount++;

		for (Expression desc : descendents)
		{
			desc.level(this.level + 1);
			this.descendents.add(desc);
		}
	}

	//!Must Evaluate to BASIC
	public abstract String eval(); 

	public void level(int level)
	{
		this.level = level;

		for (Expression desc : this.descendents)
			desc.level(this.level + 1);
	}

	@Override public String toString() 
	{
		return "" + id;
	}
}
