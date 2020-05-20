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

    @Test
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
                compiler.output(false);
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

    @Test
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
                compiler.output(false);
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

    @Test
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
                compiler.output(false);
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

    @Test
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
                compiler.output(false);
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

    @Test
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
            "0 GOTO 23\n1 V6$ = \"yy\"\n2 PRINT V6$\n3 RETURN\n4 V5$ = \"yx\"\n5 PRINT V5$\n6 RETURN\n7 V2$ = \"y\"\n8 PRINT V2$\n9 GOSUB 4\n10 GOSUB 1\n11 RETURN\n12 V4$ = \"xy\"\n13 PRINT V4$\n14 RETURN\n15 V3$ = \"xx\"\n16 PRINT V3$\n17 RETURN\n18 V1$ = \"x\"\n19 PRINT V1$\n20 GOSUB 15\n21 GOSUB 12\n22 RETURN\n23 V0$ = \"hello0\"\n24 GOSUB 18\n25 GOSUB 7\n26 END\n",
            "0 GOTO 13\n1 TMPN123 = V2\n2 TMPC1221 = TMPN123\n3 TMPN126 = V4\n4 TMPC1222 = TMPN126\n5 TMPC120 = TMPC1221 * TMPC1222\n6 TMPN119 = TMPC120\n7 TMPA117 = TMPN119\n8 V2 = TMPA117\n9 RETURN\n10 PRINT V1\n11 PRINT V2\n12 RETURN\n13 V0$ = \"counter\"\n14 PRINT V0$\n15 TMPN50 = 10\n16 TMPA48 = TMPN50\n17 V3 = TMPA48\n18 TMPN59 = 2\n19 TMPA57 = TMPN59\n20 V4 = TMPA57\n21 TMPN68 = 1\n22 TMPA66 = TMPN68\n23 V2 = TMPA66\n24 V1 = 0\n25 TEMPB72 = V1 < V3\n26 TEMPB72 = NOT TEMPB72\n27 IF TEMPB72 THEN GOTO 32\n28 GOSUB 1\n29 GOSUB 10\n30 V1 = V1 + 1\n31 GOTO 25\n32 END\n"
        };

        for(int index = 0; index < args.length; index++)
        {
            String result = "";
            SPL compiler = null;
            syntax.code.File resFile = null;

            try
            {
                compiler = new SPL(args[index]);
                System.out.println(compiler);
                compiler.output(false);
                resFile = compiler.compile(false);
            }
            catch(Exception e)
            {
                result = e.toString();
            }

            if(result == "")
            {
                if(resFile != null)
                    result = resFile.toString();
            }
            else
                System.out.println("\t" + result + "\n");

            assertEquals(result, results[index]);
        }
        
        System.out.println("\tCODE GENERATION UNIT TESTING COMPLETE...\n");
    } 
}
