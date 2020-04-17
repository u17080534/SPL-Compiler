import java.util.*;
import java.io.*;
import exception.*;
import lexer.*;
import parser.*;
import ast.*;
import symtable.*;

public class SPL 
{ 
    //!Handled File
    private Cache cache;
    private Lexer lexer;
    private Parser parser;
    private AbstractSyntaxTree tree;
    private SymbolTable table;

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
        BufferedReader buffer;

        try
        {
            buffer = new BufferedReader(new FileReader(new File(file)));
        }
        catch(FileNotFoundException ex)
        {
            throw new Exception("Error: The file \"" + file + "\" cannot be found on this machine.");
        }

        this.cache = new Cache(filename);
        this.lexer = new Lexer(buffer);
        this.parser = new Parser();
        this.table = new SymbolTable();
        this.tree = null;
    } 

    public void compile(boolean debug)
    {
        this.cache.output(debug);

        List<Token> tokens = this.tokenize();

        if(tokens != null)
            this.parse(tokens);
    }

    private List<Token> tokenize()
    {
        List<Token> tokens = null;

        try
        {
            tokens = this.lexer.readTokens();
            this.cache.export(tokens);
        }
        catch(LexerException ex)
        {
            System.out.println("Lexical Error " + ex);
        }

        return tokens;
    }

    private void parse(List<Token> tokens)
    {
        try
        {
            this.tree = this.parser.parse(tokens);
            this.cache.export(this.tree);
        }
        catch(SyntaxException ex)
        {
            System.out.println("Syntax Error: " + ex);
        }

        if(this.tree != null)
        {
            Vector<Symbol> symbols = this.tree.getSymbols();

            for(Symbol sym : symbols)
                this.table.add(sym);

            this.cache.export(this.table);
        }
    }
  
    public static void main(String[] args) 
    { 
        //multiple files will compile into different programs
        Vector<SPL> compilers = new Vector<SPL>();
        boolean debug = false;

        try
        {
            for(int index = 0; index < args.length; index++)
            {
                if(args[index].equals("-debug"))
                    debug = true;
                else if(args[index].equals("-test"))
                    compilers.add(new SPL("input/test.spl"));
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