import java.util.*;
import java.io.*;
import lexer.*;
import exception.LexerException;
import exception.EmptyStreamException;

public class SPL 
{ 
    //!Handled File
    private String filename;
    private Cache cache;
    private Lexer lexer;

    //!Compiler uses the passed in filename - expected to be within current directory
    public SPL(String file) throws Exception
    { 
        //Strip /dir/ and .spl from filepath
        int start = file.lastIndexOf('/') + 1;
        int end = file.lastIndexOf('.');

        if(start == -1)
            start = 0;
        if(end == -1)
            end = file.length();

        String filename = file.substring(start, end);
        this.filename = filename;
        BufferedReader buffer;

        try
        {
            buffer = new BufferedReader(new FileReader(new File(file)));
        }
        catch(FileNotFoundException ex)
        {
            throw new Exception("Error: The file \"" + file + "\" cannot be found on this machine.");
        }

        this.cache = new Cache(this.filename);
        this.lexer = new Lexer(buffer);
    } 

    public void compile(boolean debug)
    {
        this.cache.output(debug);

        try
        {
            List<Token> tokens = this.tokenize();
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }
    }

    public List<Token> tokenize() throws LexerException
    {
        List<Token> tokens = null;

        try
        {
            tokens = this.lexer.readTokens();
            this.cache.export(tokens);
        }
        catch(LexerException ex)
        {
            throw ex;
        }

        return tokens;
    }
  
    @Override 
    public String toString() 
    {
        return "SPL-Compiler version \"1.0.1\"\n\tfile : " + this.filename + ".spl";
    }

    public static void main(String[] args) 
    { 
        //multiple files will compile into different programs
        Vector<SPL> compilers = new Vector<SPL>();
        boolean debug = false, test = false;

        try
        {
            for(int index = 0; index < args.length; index++)
            {
                if(args[index].equals("-test"))
                {
                    debug = true;
                    compilers.add(new SPL("input/test.spl"));
                    break;
                }
                else if(args[index].equals("-debug"))
                    debug = true;
                else
                    compilers.add(new SPL(args[index]));
            }

            for(int index = 0; index < compilers.size(); index++)
                if(compilers.get(index) != null)
                    compilers.get(index).compile(debug);
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }

        if(compilers.size() == 0)
            System.out.println("Please enter a .spl file name as a command line argument for compilation...\n\t" +
                            "Enter -debug to compile with debug output.\n\t"+
                            "Enter -test to compile a given example file (This assumes you have 'input/test.spl' present.)\n");
    } 
}