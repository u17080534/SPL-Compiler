import java.util.*;
import java.io.*;
import exception.*;
import lexer.*;
import parser.*;
import syntax.*;
import symtable.*;
import analysis.*;
import syntax.code.File;

//SPL-COMPILER
public class SPL 
{ 
    //!Handled File
    private String filename;
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

        this.filename = filename;

        BufferedReader buffer;

        try
        {
            buffer = new BufferedReader(new FileReader(new java.io.File(file)));
        }
        catch(FileNotFoundException ex)
        {
            throw new Exception("Error: The file \"" + file + "\" cannot be found on this machine.");
        }

        this.cache = new Cache(this.filename);
        this.lexer = new Lexer(buffer);
        this.parser = new Parser();
        this.table = new SymbolTable();
        this.tree = null;
    } 

    public File compile(boolean debug)
    {
        this.output(debug);

        try
        {
            List<Token> tokens = this.tokenize();

            if(tokens != null)
                if(this.parse(tokens))
                    this.analysis();

            File genFile = File.complete_file(this.tree.generation(this.filename));
            
            this.cache.export(genFile);

            return genFile;
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }

        return null;
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

    public boolean parse(List<Token> tokens) throws SyntaxException
    {
        try
        {
            this.tree = this.parser.parse(tokens);
            this.cache.export(this.tree);
        }
        catch(SyntaxException ex)
        {
            throw ex;
        }

        if(this.tree != null)
        {
            Vector<Symbol> symbols = this.tree.getSymbols();

            for(Symbol sym : symbols)
                this.table.add(sym);

            this.cache.export(this.table);

            return true;
        }

        return false;
    }

    public void analysis() throws AnalysisException
    {
        try
        {
            this.checkScope();
            this.checkType();
            this.checkValues();

            this.cache.export(this.tree);
            this.cache.export(this.table);
        }
        catch(AnalysisException ex)
        {
            throw ex;
        }

    }

    public void checkScope() throws UsageException
    {
        try
        {
            ScopeCheck.check(this.tree, this.table);
        }
        catch(UsageException ex)
        {
            throw ex;
        }
    }

    public void checkType() throws TypeException
    {
        try
        {
            TypeCheck.check(this.tree, this.table);
        }
        catch(TypeException ex)
        {
            throw ex;
        }
    }

    public void checkValues() throws ValueException
    {
        try
        {
            ValueCheck.check(this.table);
        }
        catch(ValueException ex)
        {
            throw ex;
        }
    }
  
    public void output(boolean out)
    {
        this.cache.output(out);
    }

    public String getFilename()
    {
        return this.filename;
    }

    @Override 
    public String toString() 
    {
        return "SPL-Compiler version \"1.0.2\"\n\tfile : " + this.filename + ".spl";
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