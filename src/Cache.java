import java.util.*;
import java.io.*;
import lexer.*;
import exception.LexerException;
import exception.EmptyStreamException;

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
} 
