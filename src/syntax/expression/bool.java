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

	@Override // For Type Checking
	public String getTerminalType()
	{
		String type = this.descendents.get(0).getTerminalType();

		//SPECIAL ACCOUNTING FOR eq
		if(this.action != null && Expression.getValue(this.action.toString()).equals("eq"))
		{
			for (int index = 1; index < this.descendents.size(); index++)
				if(!this.descendents.get(index).getTerminalType().equals("N") && !this.descendents.get(index).getTerminalType().equals("B") && !this.descendents.get(index).getTerminalType().equals("S"))
					return "";
		}
		//SPECIAL ACCOUNTING FOR < AND >
		else if(this.action != null && (Expression.getValue(this.action.toString()).equals(">") || Expression.getValue(this.action.toString()).equals("<")))
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

	public Line trans(File absFile)
	{
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
					if(distinct.equals("and"))
					{
						String var1 = "TMPB" + this.e1.getID() + "1";
						String var2 = "TMPB" + this.e1.getID() + "2";

						this.e1.trans(absFile);

						absFile.add(new Line(temp + " = 0"));
						absFile.add(new Line("TMPBB" + this.getID() + " = NOT " + var1));
						absFile.add(new Line("IF TMPBB" + this.getID() + " THEN GOTO %END" +  this.getID() + "+1%"));
						absFile.add(new Line("TMPBB" + this.getID() + " = NOT " + var2));
						absFile.add(new Line("IF TMPBB" + this.getID() + " THEN GOTO %END" +  this.getID() + "+1%"));
						absFile.label("END" +  this.getID(), true);
						absFile.add(new Line(temp + " = 1"));
						absFile.anchorLabel("END" +  this.getID());
					}
					else if(distinct.equals("or"))
					{
						String var1 = "TMPB" + this.e1.getID() + "1";
						String var2 = "TMPB" + this.e1.getID() + "2";

						this.e1.trans(absFile);

						absFile.add(new Line(temp + " = 1"));
						absFile.add(new Line("TMPBB" + this.getID() + " = " + var1));
						absFile.add(new Line("IF TMPBB" + this.getID() + " THEN GOTO %END" +  this.getID() + "+1%"));
						absFile.add(new Line("TMPBB" + this.getID() + " = " + var2));
						absFile.add(new Line("IF TMPBB" + this.getID() + " THEN GOTO %END" +  this.getID() + "+1%"));
						absFile.label("END" +  this.getID(), true);
						absFile.add(new Line(temp + " = 0"));
						absFile.anchorLabel("END" +  this.getID());
					}
					else if(distinct.equals("not"))
						absFile.add(new Line(temp + " = NOT " + this.e1.trans(absFile).toString()));
					else
						absFile.add(new Line(temp + " = " + this.e1.trans(absFile).toString()));
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
				}
			}
		}

		return new Line(temp);
	}
}