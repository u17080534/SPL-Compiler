package syntax.code;

public class Line 
{
	private int number;
	private String line;

	public Line(String line)
	{
		this.number = 0;
		this.line = line;
	}

	public void setLine(String line)
	{
		this.line = line;
	}

	public void setNumber(int number)
	{
		this.number = number;
	}

	public int getNumber()
	{
		return this.number;
	}

	@Override
	public String toString()
	{
		return this.line;
	}
}