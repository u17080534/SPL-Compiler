import java.util.*;
import java.io.*;
import exception.*;
import syntax.*;
import symtable.*;
import lexer.*;
import parser.*;

//SPL-COMPILER
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
            FileWriter fileWriter = new FileWriter(new java.io.File("output/" + tokenFile));

            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            for(int index = 0; index < tokens.size(); index++)
            {
                bufferedWriter.write((index + 1) + ": " + tokens.get(index).getInput() + " (" + tokens.get(index).getToken() + ")\n");
            }

            bufferedWriter.close();
            fileWriter.close();

            if(this.out)
            {
                System.out.println("\tToken List:");
            	System.out.println(tokens);
                System.out.print("\n");
            }
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
            FileWriter fileWriter = new FileWriter(new java.io.File("output/" + astFile));

            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write(tree.getExpressions());

            bufferedWriter.close();

            fileWriter.close();

            if(this.out)
            {
                System.out.println("\tAbstract Syntax Tree:");
            	System.out.println(tree);
                System.out.print("\n");
            }
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
            FileWriter fileWriter = new FileWriter(new java.io.File("output/" + symFile));

            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            Vector<Symbol> symbols = table.list();
            for(int index = 0; index < symbols.size(); index++)
                bufferedWriter.write(symbols.get(index) + "\n");

            bufferedWriter.close();

            fileWriter.close();

            if(this.out)
            {
                System.out.println("\tSymbol Table:");
                System.out.println(table);
            	System.out.print("\n");
            }
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }
	}

    public void export(syntax.code.File file)
    {
        if(file == null)
            return;

        String basicFile = this.filename + ".bas";

        try
        {
            FileWriter fileWriter = new FileWriter(new java.io.File(basicFile));

            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write(file.toString());
                
            bufferedWriter.close();

            fileWriter.close();
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
} 
