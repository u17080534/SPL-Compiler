package syntax.code;

import java.util.*;
import java.io.*;	

//SPL-COMPILER
public class File
{
	private String name;
	private Vector<Line> lines;
	private Map<String, Integer> labels;
	private int point = -1;

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
			if(this.lines.get(index) != null)
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

					if(this.labels.get(label) == null)
						System.out.println("ERROR LABEL " + label);

					int lineRef = this.labels.get(label) + diff;

					line = line.substring(0, pIndex1) + lineRef;

					this.lines.get(index).setLine(line);
				}

				this.lines.get(index).setNumber(index);
			}
		}
	}

	public void add(Line line)
	{
		if(line != null)
		{
			if(this.point != -1)
			{
				for(HashMap.Entry<String, Integer> label : this.labels.entrySet())
					if(label.getValue() >= this.point)
						label.setValue(label.getValue() + 1);

				this.lines.add(this.point, line);

				this.point++;
			}
			else
				this.lines.add(line);
		}
	}

	//!Resets File Pointer
	public int point()
	{
		int tmp = this.point;
		this.point = -1;
		// if(tmp == -1)
		// 	tmp = this.lines.size();
		return tmp;
	}

	//Sets File Pointer Position
	public int point(int index)
	{
		int tmp = this.point;
		this.point = index;
		// if(tmp == -1)
		// 	tmp = this.lines.size();
		return tmp;
	}

	public boolean label(String label)
	{
		if(this.labels.containsKey(label))
			return false;
		
		int lblIndex = this.lines.size();

		if(this.point != -1)
			lblIndex = this.point;

		this.labels.put(label, new Integer(lblIndex));
		return true;
	}

	public boolean label(String label, int diff)
	{
		// if(this.labels.containsKey(label))
		// 	return false;

		int lblIndex = this.lines.size();

		if(this.point != -1)
			lblIndex = this.point;
		
		this.labels.put(label, new Integer(lblIndex - diff));
		return true;
	}

	public boolean hasLabel(String label)
	{
		return this.labels.containsKey(label);
	}

	public int getLabel(String label)
	{
		if(!this.labels.containsKey(label))
			return -1;

		return this.labels.get(label);
	}

	public Map<String, Integer> getLabels()
	{
		return this.labels;
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

		if(file.hasLabel("PROC_DEFS"))
		{
			refined.point(0);

			refined.add(new Line("GOTO %PROC_DEFS%"));

			refined.point();
		}

		refined.add(new Line("END"));

		refined.number();

		return refined;
	}
}