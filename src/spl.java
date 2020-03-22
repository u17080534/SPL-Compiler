import java.util.*;
import java.io.*;

public class spl 
{ 
    //!Handled File
	private File file;
    private Lexer lexer;

    //!Compiler uses the passed in filename - expected to be within current directory
    public spl(String file) 
    { 
    	try
    	{
	    	this.file = new File(file);
			this.lexer = new Lexer(new BufferedReader(new FileReader(this.file)));
		}
		catch(IOException ex)
		{
			System.out.println(ex);
		}
    } 

    public void tokenize()
    {
        try
        {
            ArrayList<String> tokens = this.lexer.readTokens();
            System.out.println(tokens);
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }
    }
  
    public static void main(String[] args) 
    { 
        spl splCompiler = new spl("../input/test.spl");
        splCompiler.tokenize();
    } 
}