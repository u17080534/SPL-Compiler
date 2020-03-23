import java.util.*;
import java.io.*;
import javafx.util.*;

public class spl 
{ 
    //!Handled File
    private String filename;
	private File file;
    private Lexer lexer;

    //!Compiler uses the passed in filename - expected to be within current directory
    public spl(String file) 
    { 
        this.filename = file.substring(file.lastIndexOf('/') + 1);
        this.filename = this.filename.substring(0, this.filename.indexOf('.'));

    	try
    	{
	    	this.file = new File(file);
			this.lexer = new Lexer(new BufferedReader(new FileReader(this.file)));
		}
		catch(IOException ex)
		{
			System.out.println("Lexical Error " + ex.getMessage());
		}
    } 

    public void tokenize()
    {
        List<Pair<String, String>> tokens;

        try
        {
            tokens = this.lexer.readTokens();
            System.out.println("Tokens:\n" + tokens);
        }
        catch(Exception ex)
        {
            tokens = this.lexer.getTokens();
            System.out.println("Tokens:\n" + tokens);
            System.out.println(ex.getMessage());
        }

        String tokenFile = this.filename + ".tok";
        File file = new File("../output/" + tokenFile);
        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;

        try
        {
            fileWriter = new FileWriter(file);

            bufferedWriter = new BufferedWriter(fileWriter);

            for(int index = 0; index < tokens.size(); index++)
            {
                bufferedWriter.write((index + 1) + ": " + tokens.get(index).getKey() + " " + tokens.get(index).getValue() + "\n");
            }
        } 
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                bufferedWriter.close();
                fileWriter.close();
            } 
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
  
    public static void main(String[] args) 
    { 
        spl splCompiler = new spl("../input/test.spl");
        splCompiler.tokenize();
    } 
}