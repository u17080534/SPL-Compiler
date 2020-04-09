package symtable;

public class Symbol
{
	private int id;
	private String symbol;
	private int scope;

	public Symbol(int id, String symbol)
	{
		this.id = id;
		this.symbol = symbol;
	}

	public int getID()
	{
		return this.id;
	}

	@Override
	public String toString()
	{
		return this.id + ":" + this.symbol;
	}
}