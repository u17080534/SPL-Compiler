import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UnitTest
{
    @BeforeClass
    public static void enter()
    {
        System.out.println("\nENTERING TESTING ENVIRONMENT...");
        System.out.println("\tNOTE: All the errors thrown within JUnit Testing are intended.");
    } 

    @AfterClass
    public static void exit()
    {
        System.out.println("\nEXITING TESTING ENVIRONMENT...");
    } 

    @Ignore
    public void testLexer() 
    { 
        System.out.println("\tLEXER UNIT TESTING...\n");
        
        String[] args = {
            "input/LexerTest/a.txt",
            "input/LexerTest/b.txt",
            "input/LexerTest/c.txt",
            "input/LexerTest/d.txt",
            "input/LexerTest/e.txt",
            "input/LexerTest/f.txt",
            "input/LexerTest/g.txt",
            "input/LexerTest/h.txt",
            "input/LexerTest/i.txt",
            "input/LexerTest/j.txt"
        };

        String[] results = {
            "", //a: no error (364 tokens)
            "Lexical Error [line: 2, col: 19]: \'J\' is not accepted in identifiers", //b: error: identifiers with uppercase letters
            "Lexical Error [line: 1, col: 43]: \'\"l3qpem26' strings have at most 8 characters", //c: error: strings greater than 8 characters
            "Lexical Error [line: 1, col: 52]: \'K\' is not accepted within literal character strings", //d: error: strings with uppercase letters
            "", //e: 6th token should be identifier (599 tokens) [first match 2 keywords]
            "Lexical Error [line: 1, col: 99]: Number literals other than 0 must begin with [1-9]", //f: error: number cannot start with 0 [first match should have multiple 0's]
            "", //g: starts with kInteger(281) ends with kwString (540 tokens)
            "", //h: starts with kwAdd ends with kUser(taxyto) (2377 tokens)
            "", //i: all keywords (393 tokens)
            ""  //j: starts with kUser(af8) ends with kwThen (9393 tokens)
        };

        for(int index = 0; index < args.length; index++)
        {
            String result = "";
            try
            {
                SPL compiler = new SPL(args[index]); 
                System.out.println(compiler);
                compiler.tokenize();
            }
            catch(Exception e)
            {
                result = e.toString();
            }

            if(result != "")
                System.out.println("\t" + result + "\n");

            assertEquals(result, results[index]);
        }
        
        System.out.println("\tLEXER UNIT TESTING COMPLETE...\n");
    } 

    @Ignore
    public void testParser() 
    { 
        System.out.println("\tPARSER UNIT TESTING...\n");
        
        String[] args = {
            "input/ParserTest/a.txt",
            "input/ParserTest/b.txt",
            "input/ParserTest/c.txt",
            "input/ParserTest/d.txt",
            "input/ParserTest/e.txt",
            "input/ParserTest/f.txt",
            "input/ParserTest/g.txt",
            "input/ParserTest/h.txt",
            "input/ParserTest/i.txt",
            "input/ParserTest/j.txt"
        };

        String[] results = {
            "", //a: no errors. 7 top level PROC_DEFS
            "", //b: no errors. for loop inside a for loop
            "", //c: no errors. 13 deep proc nesting
            "", //d: no errors. 3 nested while loop at bottom
            "Syntax Error: Instruction missing semicolon (;) as it has tokens following it [n - tok_identifier[6,5]]", //e: ! expected ';' on line 6 before start of proc
            "", //f: no errors
            "Syntax Error: Invalid Identifier Used [( - tok_open_paren[26,65]]", //g: ! unexpected '(' on line 26 after not
            "Syntax Error: Invalid Numerical Expression Given [( - tok_open_paren[7,15]]", //h: ! unexpected T on line 7. add cannot add T
            "Syntax Error: Invalid Conditional Syntax: Expected Closing Brace [} - tok_close_brace[10,1]]", //i: ! unbalanced '}' on line 7
            "" //j: no errors.
        };

        for(int index = 0; index < args.length; index++)
        {
            String result = "";
            try
            {
                SPL compiler = new SPL(args[index]); 
                System.out.println(compiler);
                compiler.parse(compiler.tokenize());
            }
            catch(Exception e)
            {
                result = e.toString();
            }

            if(result != "")
                System.out.println("\t" + result + "\n");

            assertEquals(result, results[index]);
        }
        
        System.out.println("\tPARSER UNIT TESTING COMPLETE...\n");
    } 

    @Ignore
    public void testScopeCheck() 
    { 
        System.out.println("\tSCOPE CHECKING UNIT TESTING...\n");
        
        String[] args = {
            "input/ScopeCheckTest/a.txt",
            "input/ScopeCheckTest/b.txt",
            "input/ScopeCheckTest/c.txt",
            "input/ScopeCheckTest/d.txt",
            "input/ScopeCheckTest/e.txt",
            "input/ScopeCheckTest/f.txt",
            "input/ScopeCheckTest/g.txt",
            "input/ScopeCheckTest/h.txt",
            "input/ScopeCheckTest/i.txt",
            "input/ScopeCheckTest/j.txt"
        };

        String[] results = {
            "", //a: no errors or undefined var. check if different `a`s
            "Usage Exception: Identifier is used more than once within same scope [variable 'b'][10,7]", //b: ? multiple variables with same name and type (redeclaration)
            "", //c: ? multiple variables with same name, but different types (no error)
            "", //d: ? same named variable and proc (no error)
            "", //e: no errors or undefined var
            "", //f: ? `if` declares different types on each branch, but with the same name (no error)
            "Usage Exception: There are undefined usages/calls: [784:variable 'gd44w'][137,14]", //g: ! use of undeclared variable `gd44w` on line 137
            "", //h: ! use of undeclared variable `myvariablename` in scope on line 161
            "", //i: no errors or undefined var
            "" //j: ? `foo` proc in another `foo`
        };

        for(int index = 0; index < args.length; index++)
        {
            String result = "";
            try
            {
                SPL compiler = new SPL(args[index]); 
                System.out.println(compiler);
                compiler.output(true);
                compiler.parse(compiler.tokenize());
                compiler.checkScope();
            }
            catch(Exception e)
            {
                result = e.toString();
            }

            if(result != "")
                System.out.println("\t" + result + "\n");

            assertEquals(result, results[index]);
        }
        
        System.out.println("\tSCOPE CHECKING UNIT TESTING COMPLETE...\n");
    } 

    @Ignore
    public void testTypeCheck() 
    { 
        System.out.println("\tTYPE CHECK UNIT TESTING...\n");
        
        String[] args = {
            "input/TypeCheckTest/a.txt",
            "input/TypeCheckTest/b.txt",
            "input/TypeCheckTest/c.txt",
            "input/TypeCheckTest/d.txt",
            "input/TypeCheckTest/e.txt",
            "input/TypeCheckTest/f.txt",
            "input/TypeCheckTest/g.txt",
            "input/TypeCheckTest/h.txt",
            "input/TypeCheckTest/i.txt",
            "input/TypeCheckTest/j.txt"
        };

        String[] results = {
            "", //a: no errors
            "Type Exception: Assignment must match type of variable [variable 'o'][5,1]", //b: ! can not assign string to num
            "", //c: no errors
            "Type Exception: Boolean type mismatch, expected eq(bool, bool) [bool 'eq'][44,7]", //d: ! line 44 and 47. eq given a string and bool, < given num and bool
            "Type Exception: Assignment must match type of variable [variable 'k'][16,7]", //e: ! line 16 can not assign bool to num
            "", //f: no errors
            "", //g: no errors
            "Type Exception: Assignment must match type of variable [variable 'ryn00'][539,9]", //h: ! line 539 can not assign num to string var
            "", //i: no errors
            "" //j: no errors
        };

        for(int index = 0; index < args.length; index++)
        {
            String result = "";
            try
            {
                SPL compiler = new SPL(args[index]);
                System.out.println(compiler);
                compiler.parse(compiler.tokenize());
                compiler.checkScope();
                compiler.checkType();
            }
            catch(Exception e)
            {
                result = e.toString();
            }

            if(result != "")
                System.out.println("\t" + result + "\n");

            assertEquals(result, results[index]);
        }
        
        System.out.println("\tTYPE CHECK UNIT TESTING COMPLETE...\n");
    } 

    @Ignore
    public void testValueCheck() 
    { 
        System.out.println("\tVALUE CHECK UNIT TESTING...\n");
        
        String[] args = {
            "input/ValueCheckTest/a.txt",
            "input/ValueCheckTest/b.txt",
            "input/ValueCheckTest/c.txt",
            "input/ValueCheckTest/d.txt",
            "input/ValueCheckTest/e.txt",
            "input/ValueCheckTest/f.txt",
            "input/ValueCheckTest/g.txt",
            "input/ValueCheckTest/h.txt",
            "input/ValueCheckTest/i.txt",
            "input/ValueCheckTest/j.txt"
        };

        String[] results = {
            "", // a: no errors
            "", // b: no errors
            "Value Exception: Variable not assigned a value [variable 'x'][15,15]; Variable not assigned a value [variable 'x'][15,27]; Variable needs a value when being used for output [variable 'x'][16,10]", // c: ! line 15, 16 x has no value at use
            "", // d: no errors
            "Value Exception: Variable not assigned a value in bool condition [variable 'nxi'][56,13]; Variable not assigned a value in bool condition [variable 'nxi'][56,18]", // e: ! line 56 nxi has not been assigned a value.
            "", // f: no errors
            "Value Exception: Variable not assigned a value in bool condition [variable 'sll'][63,50]; Variable not assigned a value in bool condition [variable 'wgb'][205,9]", // g: ! line 63 sll has no value
            "", // h: no errors
            "", // i: no errors
            "" // j: ! line 29, 30 x and y no value or warning
        };

        for(int index = 0; index < args.length; index++)
        {
            String result = "";
            try
            {
                SPL compiler = new SPL(args[index]);
                System.out.println(compiler);
                compiler.output(false);
                compiler.parse(compiler.tokenize());
                compiler.checkScope();
                compiler.checkType();
                compiler.output(true);
                compiler.checkValues();
            }
            catch(Exception e)
            {
                result = e.toString();
            }

            if(result != "")
                System.out.println("\t" + result + "\n");

            assertEquals(result, results[index]);
        }
        
        System.out.println("\tVALUE CHECK UNIT TESTING COMPLETE...\n");
    } 

    @Test
    public void testCodeGeneration() 
    { 
        System.out.println("\tCODE GENERATION UNIT TESTING...\n");
        
        String[] args = {
            "input/CodeGeneration/a.spl",
            "input/CodeGeneration/b.spl"
        };

        String[] results = {
            "",
            ""
        };

        for(int index = 0; index < args.length; index++)
        {
            String result = "";
            SPL compiler = null;

            try
            {
                compiler = new SPL(args[index]);
                System.out.println(compiler);
                compiler.compile(false);
            }
            catch(Exception e)
            {
                result = e.toString();
            }

            if(result == "")
            {
                try
                {
                    Runtime rt = Runtime.getRuntime();
                    Process pr = rt.exec("./extern/BASIC [" + compiler.getFilename() + ".bas]");
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
            else
                System.out.println("\t" + result + "\n");

            assertEquals(result, results[index]);
        }
        
        System.out.println("\tCODE GENERATION UNIT TESTING COMPLETE...\n");
    } 
}
