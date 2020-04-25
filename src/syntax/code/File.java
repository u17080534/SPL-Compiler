package syntax.code;

import java.util.*;
import java.io.*;	

public class File
{
	private String name;
	private Vector<Line> lines;

	public File(String name)
	{
		this.name = name;
		this.lines = new Vector<Line>();
	}

	public void add(Line line)
	{
		this.lines.add(line);
	}

	@Override
	public String toString()
	{
		String str = "";

		for(int index = 0; index < lines.size(); index++)
			str += lines.get(index).toString();

		return str;
	}
}