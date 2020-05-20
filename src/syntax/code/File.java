package syntax.code;

import java.util.*;
import java.io.*;	

//SPL-COMPILER
public class File
{
	private int point;
	private String name;
	private Vector<Line> lines;
	private Vector<Label> labels;

	public File(String name)
	{
		this.name = name;
		this.lines = new Vector<Line>();
		this.labels = new Vector<Label>();
		this.point = 0;
	}

	public File(File other)
	{
		this.name = other.name;
		this.lines = other.lines;
		this.labels = other.labels;
	}

	private void number()
	{
		int line_count = 0;
		for(int index = 0; index < this.lines.size(); index++)
			if(this.lines.get(index) != null)
			{
				label_line(this.lines.get(index), this.labels);
				this.lines.get(index).setNumber(line_count++);
			}
	}

	public void add(Line line)
	{
		System.out.println(this.toString());
		System.out.println(this.labels);
		if(line != null)
		{
			for(Label label : this.labels)
				if(!label.isAnchor() && label.getLine() >= this.point)
					label.setLine(label.getLine() + 1);

			this.lines.add(this.point, line);
			this.point++;
		}
	}

	//!Resets File Pointer
	public int point()
	{
		int tmp = this.point;
		this.point = this.lines.size();
		System.out.println("Pointing " + tmp + " -> " + this.point);
		return tmp;
	}

	//Sets File Pointer Position
	public int point(int index)
	{
		int tmp = this.point;
		this.point = Math.min(index, this.lines.size());
		System.out.println("Pointing " + tmp + " -> " + this.point);
		return tmp;
	}

	public void label(String label)
	{
		this.labels.add(new Label(label, this.point));
	}

	public void label(String label, boolean anchor)
	{
		this.labels.add(new Label(label, this.point, anchor));
	}

	public void label(String lbl, int diff, boolean anchor)
	{
		this.labels.add(new Label(lbl, this.point - diff));
	}

	public boolean hasLabel(String lbl)
	{
		for(Label label : this.labels)
			if(label.getLabel().equals(lbl))
				return true;
		return false;
	}

	public void anchorLabel(String lbl)
	{
		for(Label label : this.labels)
			if(label.getLabel().equals(lbl))
				label.anchor();
	}

	public int getLabel(String lbl)
	{
		for(Label label : this.labels)
			if(label.getLabel().equals(lbl))
				return label.getLine();

		return -1;
	}

	public Vector<Label> getLabels()
	{
		return this.labels;
	}

	public int getPointer()
	{
		return this.point;
	}

	public Line getPointed()
	{
		return this.lines.get(this.point);
	}

	public Line getPointed(int diff)
	{
		return this.lines.get(this.point + diff);
	}

	public int size()
	{
		return this.lines.size();
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

		refined.point(0);

		refined.add(new Line("GOTO %START%"));

		refined.point();

		refined.add(new Line("END"));

		refined.number();

		return refined;
	}

	public static String label_line(Line line, Vector<Label> labels)
	{
		String label = "";

		int pIndex1 = line.toString().indexOf("%");

		int pIndex2 = line.toString().lastIndexOf("%");

		if(pIndex1 >= 0 && pIndex2 >= 0)
		{
			label = line.toString().substring(pIndex1 + 1, pIndex2);

			int diff = 0;

			int dIndex = label.indexOf("+");

			if(dIndex >= 0)
			{
				diff = Integer.parseInt(label.substring(dIndex + 1, label.length()));
				label = label.substring(0, dIndex);
			}

			// if(labels.get(label) == null)
			// {
			// 	int lineRef = labels.get(label).getLine() + diff;
			// 	line.setLine(line.toString().substring(0, pIndex1) + lineRef);
			// }
			// else
			// 	System.out.println("Labelling Error: " + label);

			for(Label lbl : labels)
				if(lbl.getLabel().equals(label))
				{
					int lineRef = lbl.getLine() + diff;
					line.setLine(line.toString().substring(0, pIndex1) + lineRef);
				}
		}

		return label;
	}

	private class Label 
	{
		int line;
		String label;
		boolean anchor;

		public Label(String label, int line)
		{
			this.label = label;
			this.line = line;
			this.anchor = false;
		}

		public Label(String label, int line, boolean anchor)
		{
			this.label = label;
			this.line = line;
			this.anchor = anchor;
		}
		public String getLabel()
		{
			return this.label;
		}

		public void setLine(int line)
		{
			this.line = line;
		}

		public int getLine()
		{
			return this.line;
		}

		public void anchor()
		{
			this.anchor = !this.anchor;
		}

		public boolean isAnchor()
		{
			return this.anchor;
		}

		@Override
		public String toString()
		{
			return this.line + " : " + this.label + "(" + this.anchor + ")";
		}
	}
}