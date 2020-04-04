import java.util.*;
import java.io.*;
import javafx.util.*;

public class spl 
{ 
    //!Handled File
    private String filename;
	private File file;
    private Lexer lexer;
    private Parser parser;

    //!Compiler uses the passed in filename - expected to be within current directory
    public spl(String file)
    { 
        this.filename = file.substring(file.lastIndexOf('/') + 1);
        this.filename = this.filename.substring(0, this.filename.indexOf('.'));

        try
        {
            this.lexer = new Lexer(this.filename);
            this.parser = new Parser(this.filename);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    } 

    public void parse()
    {
        List<Pair<String, Token>> tokens;

        try
        {
            tokens = this.lexer.readTokens();
            System.out.println("Tokens:\n" + tokens);
        }
        catch(Exception ex)
        {
            tokens = this.lexer.getTokens();
            System.out.println("Tokens:\t" + tokens);
            System.out.println("Lexical Error " + ex.getMessage());
        }

        this.parser.parseAST(tokens);
    }
  
    public static void main(String[] args) 
    { 
        spl splCompiler = new spl("../input/test.spl");
        splCompiler.parse();
    } 
}