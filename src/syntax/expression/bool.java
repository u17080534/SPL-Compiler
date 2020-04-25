package syntax.expression;

import syntax.code.*;
import lexer.Token;

public class bool extends Expression 
{   
	private TerminalExpression action; // T F ID EQ LT GT NOT AND OR
	private Expression e1;   
	private Expression e2;   

	public bool(TerminalExpression action) 
	{ 
		super(action);
		this.action = action;
		this.expr = "BOOL";
	}

	public bool(TerminalExpression action, Expression e1) 
	{ 
		super(action, e1);
		this.action = action;
		this.expr = "BOOL";
		this.e1 = e1; 
	}

	public bool(TerminalExpression action, Expression e1, Expression e2) 
	{ 
		super(action, e1, e2);
		this.action = action;
		this.expr = "BOOL";
		this.e1 = e1; 
		this.e2 = e2; 
	}  

	public Line trans(File absFile)
	{       
		Line line = null;

		if(this.e1 != null)
		{
			String distinct = this.action.trans(absFile).toString();

			if(this.e2 != null) // EQ / > / < 
			{
				if(distinct.equals("eq"))
					line = new Line(this.e1.trans(absFile).toString() + this.e2.trans(absFile).toString());
				else if(distinct.equals(">"))
					line = new Line(this.e1.trans(absFile).toString() + this.e2.trans(absFile).toString());
				else if(distinct.equals("<"))
					line = new Line(this.e1.trans(absFile).toString() + this.e2.trans(absFile).toString());
				
			}
			else // ID / NOT / AND / OR
			{
				if(distinct.equals("variable"))
					line = new Line(this.e1.trans(absFile).toString()); 
				else if(distinct.equals("not"))
					line = new Line(this.e1.trans(absFile).toString());
				else if(distinct.equals("and"))
					line = new Line(this.e1.trans(absFile).toString());
				else if(distinct.equals("or"))
					line = new Line(this.e1.trans(absFile).toString());
				
			}
		}
		else // T / F 
		{
			line = new Line(this.action.trans(absFile).toString());
		}

		absFile.add(line);

		return line;
	}
}