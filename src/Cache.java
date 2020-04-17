import java.util.*;
import java.io.*;
import exception.*;
import ast.*;
import symtable.*;
import lexer.*;
import parser.*;

public class Cache
{
	private String filename;
	private boolean out;

	public Cache(String filename)
	{
		this.filename = filename;
		this.out = true;
	}

	public void output(boolean out)
	{
		this.out = out;
	}

	public void export(List<Token> tokens)
	{
		if(tokens == null)
			return;

		String tokenFile = this.filename + ".tok";

		try
        {
            FileWriter fileWriter = new FileWriter(new File("output/" + tokenFile));

            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            for(int index = 0; index < tokens.size(); index++)
            {
                bufferedWriter.write((index + 1) + ": " + tokens.get(index).getInput() + " (" + tokens.get(index).getToken() + ")\n");
            }

            bufferedWriter.close();
            fileWriter.close();

            if(this.out)
            	System.out.println(tokens);
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }
	}

	public void export(AbstractSyntaxTree tree)
	{
		if(tree == null)
			return;

		String astFile = this.filename + ".ast";

		try
        {
            FileWriter fileWriter = new FileWriter(new File("output/" + astFile));

            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write(tree.getExpressions());

            bufferedWriter.close();

            fileWriter.close();

            if(this.out)
            	System.out.println(tree);
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }
	}

	public void export(SymbolTable table)
	{
		if(table == null)
			return;

		String symFile = this.filename + ".sym";

		try
        {
            FileWriter fileWriter = new FileWriter(new File("output/" + symFile));

            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write(table.toString());

            bufferedWriter.close();

            fileWriter.close();

            if(this.out)
            	System.out.println(table);
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }
	}
} 
