package syntax.code;

import java.util.*;
import java.io.*;	

public class File
{
	private String name;
	private Vector<Line> lines;
	private Map<String, Integer> labels;

	public File(String name)
	{
		this.name = name;
		this.lines = new Vector<Line>();
		this.labels = new HashMap<String, Integer>();
	}

	public File(File other)
	{
		this.name = other.name;
		this.lines = other.lines;
		this.labels = other.labels;
	}

	private void number()
	{
		for(int index = 0; index < this.lines.size(); index++)
		{
			String line = this.lines.get(index).toString();

			int pIndex1 = line.indexOf("%");
			int pIndex2 = line.lastIndexOf("%");

			if(pIndex1 >= 0 && pIndex2 >= 0)
			{
				String label = line.substring(pIndex1 + 1, pIndex2);

				int diff = 0;

				int dIndex = label.indexOf("+");

				if(dIndex >= 0)
				{
					diff = Integer.parseInt(label.substring(dIndex + 1, label.length()));
					label = label.substring(0, dIndex);
				}

				int lineRef = this.labels.get(label) + diff;

				line = line.substring(0, pIndex1) + lineRef;

				this.lines.get(index).setLine(line);
			}

			this.lines.get(index).setNumber(index);
		}
	}

	public void add(Line line)
	{
		if(line != null)
			this.lines.add(line);
	}

	public boolean label(String label)
	{
		if(this.labels.containsKey(label))
			return false;
		
		this.labels.put(label, new Integer(this.lines.size()));
		return true;
	}

	public boolean label(String label, int diff)
	{
		if(this.labels.containsKey(label))
			return false;
		
		this.labels.put(label, new Integer(this.lines.size() - diff));
		return true;
	}

	@Override
	public String toString()
	{
		String str = "";

		for(int index = 0; index < lines.size(); index++)
			str += lines.get(index).getNumber() + " " + lines.get(index).toString() + System.getProperty("line.separator");

		return str;
	}

	public static File complete_file(File file)
	{
		File refined = new File(file);

		refined.add(new Line("END"));

		refined.number();

		return refined;
	}
}