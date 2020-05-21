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
            "input/CodeGeneration/a.txt",
            "input/CodeGeneration/b.txt",
            "input/CodeGeneration/c.txt",
            "input/CodeGeneration/d.txt",
            "input/CodeGeneration/e.txt",
            "input/CodeGeneration/f.txt",
            "input/CodeGeneration/g.txt",
            "input/CodeGeneration/h.txt",
            "input/CodeGeneration/i.txt",
            "input/CodeGeneration/j.txt",
            "input/CodeGeneration/k.txt",
            "input/CodeGeneration/l.txt"
        };

        String[] results = {
            // a: 25
            "0 GOTO 1\n1 TMPN24 = 2\n2 TMPC231 = TMPN24\n3 TMPN30 = 4\n4 TMPC291 = TMPN30\n5 TMPN32 = 1\n6 TMPC292 = TMPN32\n7 TMPC27 = TMPC291 - TMPC292\n8 TMPN26 = TMPC27\n9 TMPC232 = TMPN26\n10 TMPC21 = TMPC231 + TMPC232\n11 TMPN20 = TMPC21\n12 TMPA18 = TMPN20\n13 V0 = TMPA18\n14 TMPN45 = V0\n15 TMPC441 = TMPN45\n16 TMPN48 = 5\n17 TMPC442 = TMPN48\n18 TMPC42 = TMPC441 * TMPC442\n19 TMPN41 = TMPC42\n20 TMPA39 = TMPN41\n21 V1 = TMPA39\n22 PRINT V1\n23 END\n",
            // b: winner/loser
            "0 GOTO 1\n1 INPUT guess\n2 V2 = guess\n3 TMPN36 = 9\n4 TMPA34 = TMPN36\n5 V3 = TMPA34\n6 V0$ = \"winner\"\n7 V1$ = \"loser\"\n8 TMPB551 = V2\n9 TMPB552 = V3\n10 TMPB55 = TMPB551 = TMPB552\n11 TMPC54 = NOT TMPB55\n12 IF TMPC54 THEN GOTO 15\n13 PRINT V0$\n14 GOTO 16\n15 PRINT V1$\n16 END\n",
            // c: 5040
            "0 GOTO 36\n1 TMPA53 = V1\n2 V3 = TMPA53\n3 TMPA62 = V3\n4 V0 = TMPA62\n5 TMPN77 = V1\n6 TMPC761 = TMPN77\n7 TMPN80 = 1\n8 TMPC762 = TMPN80\n9 TMPC74 = TMPC761 - TMPC762\n10 TMPN73 = TMPC74\n11 TMPA71 = TMPN73\n12 V1 = TMPA71\n13 V2 = 0\n14 TEMPB84 = V2 < V1\n15 TEMPB84 = NOT TEMPB84\n16 IF TEMPB84 THEN GOTO 35\n17 TMPN107 = V3\n18 TMPC1061 = TMPN107\n19 TMPN110 = 1\n20 TMPC1062 = TMPN110\n21 TMPC104 = TMPC1061 - TMPC1062\n22 TMPN103 = TMPC104\n23 TMPA101 = TMPN103\n24 V3 = TMPA101\n25 TMPN123 = V0\n26 TMPC1221 = TMPN123\n27 TMPN126 = V3\n28 TMPC1222 = TMPN126\n29 TMPC120 = TMPC1221 * TMPC1222\n30 TMPN119 = TMPC120\n31 TMPA117 = TMPN119\n32 V0 = TMPA117\n33 V2 = V2 + 1\n34 GOTO 14\n35 RETURN\n36 TMPN20 = 7\n37 TMPA18 = TMPN20\n38 V1 = TMPA18\n39 GOSUB 1\n40 PRINT V0\n41 END\n",
            // d: fib sequence of n size
            "0 GOTO 35\n1 TMPN79 = 1\n2 TMPA77 = TMPN79\n3 V2 = TMPA77\n4 TMPN88 = 1\n5 TMPA86 = TMPN88\n6 V3 = TMPA86\n7 TMPB941 = V0\n8 TMPB942 = V1\n9 TMPB94 = TMPB941 > TMPB942\n10 TEMPB92 = NOT TMPB94\n11 IF TEMPB92 THEN GOTO 33\n12 TMPA112 = V2\n13 V4 = TMPA112\n14 TMPN127 = V2\n15 TMPC1261 = TMPN127\n16 TMPN130 = V3\n17 TMPC1262 = TMPN130\n18 TMPC124 = TMPC1261 + TMPC1262\n19 TMPN123 = TMPC124\n20 TMPA121 = TMPN123\n21 V2 = TMPA121\n22 TMPA138 = V4\n23 V3 = TMPA138\n24 TMPN153 = V0\n25 TMPC1521 = TMPN153\n26 TMPN156 = 1\n27 TMPC1522 = TMPN156\n28 TMPC150 = TMPC1521 - TMPC1522\n29 TMPN149 = TMPC150\n30 TMPA147 = TMPN149\n31 V0 = TMPA147\n32 GOTO 7\n33 PRINT V3\n34 RETURN\n35 TMPN20 = 0\n36 TMPA18 = TMPN20\n37 V1 = TMPA18\n38 TMPB26 = 1\n39 TEMPB24 = NOT TMPB26\n40 IF TEMPB24 THEN GOTO 63\n41 INPUT n\n42 V0 = n\n43 TMPB401 = V0\n44 TMPB402 = V1\n45 TMPB40 = TMPB401 < TMPB402\n46 TMPB391 = TMPB40\n47 TMPB461 = V0\n48 TMPB462 = V1\n49 TMPB46 = TMPB461 = TMPB462\n50 TMPB392 = TMPB46\n51 TMPB37 = 1\n52 TMPBB37 = TMPB391\n53 IF TMPBB37 THEN GOTO 57\n54 TMPBB37 = TMPB392\n55 IF TMPBB37 THEN GOTO 57\n56 TMPB37 = 0\n57 TMPC36 = NOT TMPB37\n58 IF TMPC36 THEN GOTO 61\n59 GOTO 63\n60 GOTO 62\n61 GOSUB 1\n62 GOTO 38\n63 END\n",
            // e: x xor y
            "0 GOTO 1\n1 TMPN30 = 0\n2 TMPA28 = TMPN30\n3 V1 = TMPA28\n4 TMPN39 = 1\n5 TMPA37 = TMPN39\n6 V2 = TMPA37\n7 TMPN48 = 2\n8 TMPA46 = TMPN48\n9 V3 = TMPA46\n10 TMPN64 = 2\n11 TMPA62 = TMPN64\n12 V4 = TMPA62\n13 TMPN89 = V2\n14 TMPC881 = TMPN89\n15 TMPN92 = V4\n16 TMPC882 = TMPN92\n17 TMPC86 = TMPC881 * TMPC882\n18 TMPN85 = TMPC86\n19 TMPA83 = TMPN85\n20 V5 = TMPA83\n21 TMPN106 = V3\n22 TMPC1051 = TMPN106\n23 TMPN109 = V4\n24 TMPC1052 = TMPN109\n25 TMPC103 = TMPC1051 * TMPC1052\n26 TMPN102 = TMPC103\n27 TMPA100 = TMPN102\n28 V6 = TMPA100\n29 TMPB1181 = V5\n30 TMPB1182 = V3\n31 TMPB118 = TMPB1181 = TMPB1182\n32 TMPB1171 = TMPB118\n33 TMPB1241 = V6\n34 TMPB1242 = V2\n35 TMPB124 = TMPB1241 = TMPB1242\n36 TMPB1172 = TMPB124\n37 TMPB115 = 0\n38 TMPBB115 = NOT TMPB1171\n39 IF TMPBB115 THEN GOTO 43\n40 TMPBB115 = NOT TMPB1172\n41 IF TMPBB115 THEN GOTO 43\n42 TMPB115 = 1\n43 TMPC114 = NOT TMPB115\n44 IF TMPC114 THEN GOTO 47\n45 V0$ = \"a eq b\"\n46 GOTO 87\n47 TMPB1541 = V2\n48 TMPB1542 = V3\n49 TMPB154 = TMPB1541 < TMPB1542\n50 TMPB1531 = TMPB154\n51 TMPB1601 = V2\n52 TMPB1602 = V3\n53 TMPB160 = TMPB1601 > TMPB1602\n54 TMPB1532 = TMPB160\n55 TMPB151 = 1\n56 TMPBB151 = TMPB1531\n57 IF TMPBB151 THEN GOTO 61\n58 TMPBB151 = TMPB1532\n59 IF TMPBB151 THEN GOTO 61\n60 TMPB151 = 0\n61 TMPA149 = TMPB151\n62 V7 = TMPA149\n63 TMPB1831 = V2\n64 TMPB1832 = V3\n65 TMPB183 = TMPB1831 < TMPB1832\n66 TMPB1821 = TMPB183\n67 TMPB1891 = V2\n68 TMPB1892 = V3\n69 TMPB189 = TMPB1891 > TMPB1892\n70 TMPB1822 = TMPB189\n71 TMPB180 = 0\n72 TMPBB180 = NOT TMPB1821\n73 IF TMPBB180 THEN GOTO 77\n74 TMPBB180 = NOT TMPB1822\n75 IF TMPBB180 THEN GOTO 77\n76 TMPB180 = 1\n77 TMPA178 = TMPB180\n78 V8 = TMPA178\n79 TMPB1981 = V7\n80 TMPB1982 = V8\n81 TMPB198 = TMPB1981 = TMPB1982\n82 TMPC197 = NOT TMPB198\n83 IF TMPC197 THEN GOTO 86\n84 V0$ = \"x eq y\"\n85 GOTO 87\n86 V0$ = \"x xor y\"\n87 PRINT V0$\n88 END\n",
            // f: gcd of numbers
            "0 GOTO 52\n1 TMPB1841 = V3\n2 TMPB1842 = V0\n3 TMPB184 = TMPB1841 = TMPB1842\n4 TMPB182 = NOT TMPB184\n5 TEMPB180 = NOT TMPB182\n6 IF TEMPB180 THEN GOTO 13\n7 GOSUB 16\n8 TMPA199 = V3\n9 V2 = TMPA199\n10 TMPA208 = V1\n11 V3 = TMPA208\n12 GOTO 1\n13 TMPA217 = V2\n14 V1 = TMPA217\n15 RETURN\n16 TMPB961 = V2\n17 TMPB962 = V3\n18 TMPB96 = TMPB961 = TMPB962\n19 TMPC95 = NOT TMPB96\n20 IF TMPC95 THEN GOTO 25\n21 TMPN109 = 0\n22 TMPA107 = TMPN109\n23 V1 = TMPA107\n24 GOTO 51\n25 TMPA116 = V2\n26 V1 = TMPA116\n27 TMPB1241 = V2\n28 TMPB1242 = V0\n29 TMPB124 = TMPB1241 > TMPB1242\n30 TEMPB122 = NOT TMPB124\n31 IF TEMPB122 THEN GOTO 43\n32 TMPA135 = V2\n33 V1 = TMPA135\n34 TMPN150 = V2\n35 TMPC1491 = TMPN150\n36 TMPN153 = V3\n37 TMPC1492 = TMPN153\n38 TMPC147 = TMPC1491 - TMPC1492\n39 TMPN146 = TMPC147\n40 TMPA144 = TMPN146\n41 V2 = TMPA144\n42 GOTO 27\n43 TMPB1591 = V2\n44 TMPB1592 = V0\n45 TMPB159 = TMPB1591 = TMPB1592\n46 TMPC158 = NOT TMPB159\n47 IF TMPC158 THEN GOTO 51\n48 TMPN172 = 0\n49 TMPA170 = TMPN172\n50 V1 = TMPA170\n51 RETURN\n52 TMPN15 = 0\n53 TMPA13 = TMPN15\n54 V0 = TMPA13\n55 TMPN41 = 0\n56 TMPA39 = TMPN41\n57 V1 = TMPA39\n58 TMPN50 = 0\n59 TMPA48 = TMPN50\n60 V2 = TMPA48\n61 TMPN59 = 0\n62 TMPA57 = TMPN59\n63 V3 = TMPA57\n64 TMPB65 = 1\n65 TEMPB63 = NOT TMPB65\n66 IF TEMPB63 THEN GOTO 74\n67 INPUT rbx\n68 V2 = rbx\n69 INPUT rcx\n70 V3 = rcx\n71 GOSUB 1\n72 PRINT V1\n73 GOTO 64\n74 END\n",
            // g: divides numbers
            "0 GOTO 50\n1 TMPB1051 = V1\n2 TMPB1052 = V2\n3 TMPB105 = TMPB1051 = TMPB1052\n4 TMPB103 = NOT TMPB105\n5 TMPC102 = NOT TMPB103\n6 IF TMPC102 THEN GOTO 46\n7 TMPN118 = 0\n8 TMPA116 = TMPN118\n9 V0 = TMPA116\n10 TMPB1241 = V1\n11 TMPB1242 = V3\n12 TMPB124 = TMPB1241 > TMPB1242\n13 TEMPB122 = NOT TMPB124\n14 IF TEMPB122 THEN GOTO 32\n15 TMPN141 = V0\n16 TMPC1401 = TMPN141\n17 TMPN144 = 1\n18 TMPC1402 = TMPN144\n19 TMPC138 = TMPC1401 + TMPC1402\n20 TMPN137 = TMPC138\n21 TMPA135 = TMPN137\n22 V0 = TMPA135\n23 TMPN157 = V1\n24 TMPC1561 = TMPN157\n25 TMPN160 = V2\n26 TMPC1562 = TMPN160\n27 TMPC154 = TMPC1561 - TMPC1562\n28 TMPN153 = TMPC154\n29 TMPA151 = TMPN153\n30 V1 = TMPA151\n31 GOTO 10\n32 TMPB1661 = V1\n33 TMPB1662 = V3\n34 TMPB166 = TMPB1661 < TMPB1662\n35 TMPC165 = NOT TMPB166\n36 IF TMPC165 THEN GOTO 45\n37 TMPN183 = V0\n38 TMPC1821 = TMPN183\n39 TMPN186 = 1\n40 TMPC1822 = TMPN186\n41 TMPC180 = TMPC1821 - TMPC1822\n42 TMPN179 = TMPC180\n43 TMPA177 = TMPN179\n44 V0 = TMPA177\n45 GOTO 49\n46 TMPN195 = 1\n47 TMPA193 = TMPN195\n48 V0 = TMPA193\n49 RETURN\n50 TMPN30 = 0\n51 TMPA28 = TMPN30\n52 V3 = TMPA28\n53 TMPB36 = 1\n54 TEMPB34 = NOT TMPB36\n55 IF TEMPB34 THEN GOTO 102\n56 INPUT rbx\n57 V1 = rbx\n58 INPUT rcx\n59 V2 = rcx\n60 TMPB591 = V1\n61 TMPB592 = V3\n62 TMPB59 = TMPB591 < TMPB592\n63 TMPB581 = TMPB59\n64 TMPB651 = V2\n65 TMPB652 = V3\n66 TMPB65 = TMPB651 < TMPB652\n67 TMPB582 = TMPB65\n68 TMPB56 = 1\n69 TMPBB56 = TMPB581\n70 IF TMPBB56 THEN GOTO 74\n71 TMPBB56 = TMPB582\n72 IF TMPBB56 THEN GOTO 74\n73 TMPB56 = 0\n74 TMPB551 = TMPB56\n75 TMPB741 = V1\n76 TMPB742 = V3\n77 TMPB74 = TMPB741 = TMPB742\n78 TMPB731 = TMPB74\n79 TMPB801 = V2\n80 TMPB802 = V3\n81 TMPB80 = TMPB801 = TMPB802\n82 TMPB732 = TMPB80\n83 TMPB71 = 1\n84 TMPBB71 = TMPB731\n85 IF TMPBB71 THEN GOTO 89\n86 TMPBB71 = TMPB732\n87 IF TMPBB71 THEN GOTO 89\n88 TMPB71 = 0\n89 TMPB552 = TMPB71\n90 TMPB53 = 1\n91 TMPBB53 = TMPB551\n92 IF TMPBB53 THEN GOTO 96\n93 TMPBB53 = TMPB552\n94 IF TMPBB53 THEN GOTO 96\n95 TMPB53 = 0\n96 TMPC52 = NOT TMPB53\n97 IF TMPC52 THEN GOTO 99\n98 GOTO 102\n99 GOSUB 1\n100 PRINT V0\n101 GOTO 53\n102 END\n",
            // h: 791505
            "0 GOTO 1\n1 INPUT m\n2 V1 = m\n3 INPUT q\n4 V2 = q\n5 TMPN41 = 369\n6 TMPC401 = TMPN41\n7 TMPN55 = V1\n8 TMPC541 = TMPN55\n9 TMPN58 = 381\n10 TMPC542 = TMPN58\n11 TMPC52 = TMPC541 * TMPC542\n12 TMPN51 = TMPC52\n13 TMPC501 = TMPN51\n14 TMPN60 = V2\n15 TMPC502 = TMPN60\n16 TMPC48 = TMPC501 * TMPC502\n17 TMPN47 = TMPC48\n18 TMPC461 = TMPN47\n19 TMPN63 = 141\n20 TMPC462 = TMPN63\n21 TMPC44 = TMPC461 - TMPC462\n22 TMPN43 = TMPC44\n23 TMPC402 = TMPN43\n24 TMPC38 = TMPC401 * TMPC402\n25 TMPN37 = TMPC38\n26 TMPA35 = TMPN37\n27 V0 = TMPA35\n28 PRINT V0\n29 GOTO 30\n30 END\n",
            // i: 5 or 11
            "0 GOTO 3\n1 PRINT V0\n2 RETURN\n3 INPUT a\n4 V0 = a\n5 TMPN33 = 5\n6 TMPA31 = TMPN33\n7 V1 = TMPA31\n8 V2 = 0\n9 TEMPB37 = V2 < V0\n10 TEMPB37 = NOT TEMPB37\n11 IF TEMPB37 THEN GOTO 17\n12 TMPN56 = 11\n13 TMPA54 = TMPN56\n14 V1 = TMPA54\n15 V2 = V2 + 1\n16 GOTO 9\n17 PRINT V1\n18 GOSUB 1\n19 END\n",
            // j: calculator
            "0 GOTO 1\n1 V2$ = \"add\"\n2 V3$ = \"sub\"\n3 V4$ = \"mult\"\n4 TMPN63 = 0\n5 TMPA61 = TMPN63\n6 V5 = TMPA61\n7 V6$ = \"number\"\n8 V7$ = \"operator\"\n9 PRINT V6$\n10 INPUT a\n11 V0 = a\n12 PRINT V6$\n13 INPUT b\n14 V1 = b\n15 PRINT V7$\n16 INPUT op$\n17 V8$ = op$\n18 TMPB1391$ = V8$\n19 TMPB1392$ = V2$\n20 TMPB139 = TMPB1391$ = TMPB1392$\n21 TMPC138 = NOT TMPB139\n22 IF TMPC138 THEN GOTO 32\n23 TMPN156 = V0\n24 TMPC1551 = TMPN156\n25 TMPN159 = V1\n26 TMPC1552 = TMPN159\n27 TMPC153 = TMPC1551 + TMPC1552\n28 TMPN152 = TMPC153\n29 TMPA150 = TMPN152\n30 V5 = TMPA150\n31 GOTO 54\n32 TMPB1651$ = V8$\n33 TMPB1652$ = V3$\n34 TMPB165 = TMPB1651$ = TMPB1652$\n35 TMPC164 = NOT TMPB165\n36 IF TMPC164 THEN GOTO 46\n37 TMPN182 = V0\n38 TMPC1811 = TMPN182\n39 TMPN185 = V1\n40 TMPC1812 = TMPN185\n41 TMPC179 = TMPC1811 - TMPC1812\n42 TMPN178 = TMPC179\n43 TMPA176 = TMPN178\n44 V5 = TMPA176\n45 GOTO 54\n46 TMPN199 = V0\n47 TMPC1981 = TMPN199\n48 TMPN202 = V1\n49 TMPC1982 = TMPN202\n50 TMPC196 = TMPC1981 * TMPC1982\n51 TMPN195 = TMPC196\n52 TMPA193 = TMPN195\n53 V5 = TMPA193\n54 PRINT V5\n55 TMPB215 = 1\n56 TEMPB213 = NOT TMPB215\n57 IF TEMPB213 THEN GOTO 102\n58 PRINT V6$\n59 INPUT a\n60 V0 = a\n61 PRINT V7$\n62 INPUT op$\n63 V8$ = op$\n64 TMPB2441$ = V8$\n65 TMPB2442$ = V2$\n66 TMPB244 = TMPB2441$ = TMPB2442$\n67 TMPC243 = NOT TMPB244\n68 IF TMPC243 THEN GOTO 78\n69 TMPN261 = V5\n70 TMPC2601 = TMPN261\n71 TMPN264 = V0\n72 TMPC2602 = TMPN264\n73 TMPC258 = TMPC2601 + TMPC2602\n74 TMPN257 = TMPC258\n75 TMPA255 = TMPN257\n76 V5 = TMPA255\n77 GOTO 100\n78 TMPB2701$ = V8$\n79 TMPB2702$ = V3$\n80 TMPB270 = TMPB2701$ = TMPB2702$\n81 TMPC269 = NOT TMPB270\n82 IF TMPC269 THEN GOTO 92\n83 TMPN287 = V5\n84 TMPC2861 = TMPN287\n85 TMPN290 = V0\n86 TMPC2862 = TMPN290\n87 TMPC284 = TMPC2861 - TMPC2862\n88 TMPN283 = TMPC284\n89 TMPA281 = TMPN283\n90 V5 = TMPA281\n91 GOTO 100\n92 TMPN304 = V5\n93 TMPC3031 = TMPN304\n94 TMPN307 = V0\n95 TMPC3032 = TMPN307\n96 TMPC301 = TMPC3031 * TMPC3032\n97 TMPN300 = TMPC301\n98 TMPA298 = TMPN300\n99 V5 = TMPA298\n100 PRINT V5\n101 GOTO 55\n102 END\n",
            // k: 110
            "0 GOTO 27\n1 TMPN202 = 120\n2 TMPA200 = TMPN202\n3 V0 = TMPA200\n4 RETURN\n5 TMPN180 = 212\n6 TMPA178 = TMPN180\n7 V4 = TMPA178\n8 TMPN189 = 312\n9 TMPA187 = TMPN189\n10 V1 = TMPA187\n11 RETURN\n12 TMPB158 = 1\n13 TMPA156 = TMPB158\n14 V6 = TMPA156\n15 TMPN167 = 111\n16 TMPA165 = TMPN167\n17 V5 = TMPA165\n18 RETURN\n19 TMPN113 = 110\n20 TMPA111 = TMPN113\n21 V0 = TMPA111\n22 TMPN129 = 210\n23 TMPA127 = TMPN129\n24 V4 = TMPA127\n25 GOSUB 5\n26 RETURN\n27 TMPN15 = 100\n28 TMPA13 = TMPN15\n29 V0 = TMPA13\n30 GOSUB 1\n31 TMPN40 = 300\n32 TMPA38 = TMPN40\n33 V1 = TMPA38\n34 GOSUB 19\n35 PRINT V0\n36 TMPA64 = V1\n37 V3 = TMPA64\n38 TMPN75 = 3\n39 TMPA73 = TMPN75\n40 V0 = TMPA73\n41 V2$ = \"dfsd\"\n42 TMPA89 = V1\n43 V0 = TMPA89\n44 TMPA98 = V3\n45 V1 = TMPA98\n46 END\n",
            // l: -100 or none
            "0 GOTO 5\n1 TMPN180 = 10\n2 TMPA178 = TMPN180\n3 V6 = TMPA178\n4 RETURN\n5 INPUT b\n6 V1 = b\n7 TMPB22 = V1\n8 TMPC21 = NOT TMPB22\n9 IF TMPC21 THEN GOTO 16\n10 TMPN39 = -5\n11 TMPA37 = TMPN39\n12 V2 = TMPA37\n13 TMPA46 = V2\n14 V0 = TMPA46\n15 GOTO 20\n16 TMPN57 = -10\n17 TMPA55 = TMPN57\n18 V0 = TMPA55\n19 GOTO 56\n20 TMPB69 = V1\n21 TMPC68 = NOT TMPB69\n22 IF TMPC68 THEN GOTO 27\n23 TMPN79 = -12\n24 TMPA77 = TMPN79\n25 V3 = TMPA77\n26 GOTO 35\n27 TMPN92 = V0\n28 TMPC911 = TMPN92\n29 TMPN95 = V0\n30 TMPC912 = TMPN95\n31 TMPC89 = TMPC911 + TMPC912\n32 TMPN88 = TMPC89\n33 TMPA86 = TMPN88\n34 V3 = TMPA86\n35 TMPN117 = -100\n36 TMPA115 = TMPN117\n37 V4 = TMPA115\n38 V5 = 0\n39 TEMPB121 = V5 < V0\n40 TEMPB121 = NOT TEMPB121\n41 IF TEMPB121 THEN GOTO 45\n42 GOTO 56\n43 V5 = V5 + 1\n44 GOTO 39\n45 TMPB1371 = V0\n46 TMPB1372 = V4\n47 TMPB137 = TMPB1371 < TMPB1372\n48 TEMPB135 = NOT TMPB137\n49 IF TEMPB135 THEN GOTO 54\n50 TMPN150 = 0\n51 TMPA148 = TMPN150\n52 V4 = TMPA148\n53 GOTO 45\n54 GOSUB 1\n55 PRINT V4\n56 END\n"
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
