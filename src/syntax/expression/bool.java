package syntax.expression;

import syntax.code.*;
import lexer.Token;

//SPL-COMPILER
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

	public bool(Expression e1) 
	{ 
		super(e1);
		this.e1 = e1; 
		this.expr = "BOOL";
	}

	public bool(TerminalExpression action, Expression e1) 
	{ 
		super(action, e1);
		this.action = action;
		this.e1 = e1; 
		this.expr = "BOOL";
	}

	public bool(TerminalExpression action, Expression e1, Expression e2) 
	{ 
		super(action, e1, e2);
		this.action = action;
		this.e1 = e1; 
		this.e2 = e2; 
		this.expr = "BOOL";
	}  

	@Override
	public String getTerminalType()
	{
		String type = this.descendents.get(0).getTerminalType();

		//SPECIAL ACCOUNTING FOR < AND >
		if(this.action != null && (Expression.getValue(this.action.toString()).equals(">") || Expression.getValue(this.action.toString()).equals("<")))
		{
			for (int index = 1; index < this.descendents.size(); index++)
				if(!this.descendents.get(index).getTerminalType().equals("N"))
					return "";
		}
		else
		{
			for (int index = 1; index < this.descendents.size(); index++)
				if(!type.equals(this.descendents.get(index).getTerminalType()))
					return "";
		}

		this.symbol.setType(type);

		return type;
	}

	//!address this
	public Line trans(File absFile)
	{
		System.out.println(this.expr);       

		String temp = "TMPB" + this.getID();

		if(this.action == null)
		{
			absFile.add(new Line(temp + " = " + this.e1.trans(absFile).toString()));
		}
		else
		{
			String distinct = this.action.trans(absFile).toString();

			if(this.e1 == null) // T / F
			{
				if(distinct.equals("T"))
					return new Line("1");

				if(distinct.equals("F"))
					return new Line("0");
			}
			else
			{
				if(this.e2 == null) // ID / NOT 
				{
					if(distinct.equals("not"))
					{
						absFile.add(new Line(temp + " = NOT " + this.e1.trans(absFile).toString()));
					}
				}
				else // EQ / > / < / AND / OR 
				{
					if(distinct.equals("eq"))
					{
						absFile.add(new Line(temp + "1 = " + this.e1.trans(absFile).toString()));
						absFile.add(new Line(temp + "2 = " + this.e2.trans(absFile).toString()));
						absFile.add(new Line(temp + " = " + temp + "1 = " + temp + "2"));
					}
					else if(distinct.equals(">"))
					{
						absFile.add(new Line(temp + "1 = " + this.e1.trans(absFile).toString()));
						absFile.add(new Line(temp + "2 = " + this.e2.trans(absFile).toString()));
						absFile.add(new Line(temp + " = " + temp + "1 > " + temp + "2"));
					}
					else if(distinct.equals("<"))
					{
						absFile.add(new Line(temp + "1 = " + this.e1.trans(absFile).toString()));
						absFile.add(new Line(temp + "2 = " + this.e2.trans(absFile).toString()));
						absFile.add(new Line(temp + " = " + temp + "1 < " + temp + "2"));
					}
					else if(distinct.equals("and"))
					{

					}
					else if(distinct.equals("or"))
					{

					}
				}
			}
		}

		return new Line(temp);
	}
}