import java.io.*;
import java.util.*;
import lexer.*;
import parser.*;
import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Ignore;
import static org.junit.Assert.*;

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
            "input/LexerTest/LexerTest1.spl",
            "input/LexerTest/LexerTest2.spl", 
            "input/LexerTest/LexerTest3.spl", 
            "input/LexerTest/LexerTest4.spl", 
            "input/LexerTest/LexerTest5.spl", 
            "input/LexerTest/LexerTest6.spl", 
            "input/LexerTest/LexerTest7.spl",
            "input/LexerTest/LexerTest8.spl",
            "input/LexerTest/LexerTest9.spl",
            "input/LexerTest/LexerTest10.spl",
            "input/LexerTest/LexerTest11.spl",
            "input/LexerTest/LexerTest12.spl",
            "input/LexerTest/LexerTest13.spl", //Test that all keywords are recognized
            "input/LexerTest/LexerTest14.spl", //Test that all operators and seperators are recognized
            "input/LexerTest/LexerTest15.spl",
            "input/LexerTest/LexerTest16.spl",
            "input/LexerTest/LexerTest17.spl",
            "input/LexerTest/LexerTest18.spl"
        };

        String[] results = {
            "Lexical Error [line: 1, col: 1]: '@' is not a recognized character",  //1
            "Lexical Error [line: 5, col: 22]: '\"johnisab' strings have at most 8 characters", //2
            "Lexical Error [line: 40, col: 10]: '!' is not a recognized character", //3
            "", //4
            "", //5
            "Lexical Error [line: 11, col: 12]: Numeric literals other than 0 must begin with [1-9]", //6
            "Lexical Error [line: 14, col: 14]: newline character found within string", //7
            "Lexical Error [line: 1, col: 15]: '#' is not a recognized character", //8
            "", //9
            "Lexical Error [line: 1, col: 9]: 'a' was unexpected: Identifiers may not begin with numeric characters, only literal numeric expressions may start with a number",//error //10
            "",  //11
            "Lexical Error [line: 2, col: 6]: '-' was unexpected: numeric token rejected" ,//12
            "",//13
            "Lexical Error [line: 1, col: 5]: '#' is not a recognized character",//14
            "Lexical Error [line: 2, col: 10]: ';' is not accepted within literal character strings",//15
            "",//16
            "Lexical Error [line: 2, col: 11]: 'd' was unexpected: Identifiers may not begin with numeric characters, only literal numeric expressions may start with a number",//17
            "" //18
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
            "input/ParserTest/ParserTest1.spl",
            "input/ParserTest/ParserTest2.spl",
            "input/ParserTest/ParserTest3.spl",
            "input/ParserTest/ParserTest4.spl",
            "input/ParserTest/ParserTest5.spl",
            "input/ParserTest/ParserTest6.spl",
            "input/ParserTest/ParserTest7.spl",
            "input/ParserTest/ParserTest8.spl",
            "input/ParserTest/ParserTest9.spl",
            "input/ParserTest/ParserTest10.spl",
            "input/ParserTest/ParserTest11.spl",
            "input/ParserTest/ParserTest12.spl",
            "input/ParserTest/ParserTest13.spl", //error
            "input/ParserTest/ParserTest15.spl",
            "input/ParserTest/ParserTest16.spl"
        };

        String[] results = {
            "Syntax Error: Invalid Conditional Syntax: Expected Opening Parenthesis [if - tok_if[1,1]]",//1
            "Syntax Error: Instruction is expected at start of program or new procedure [{ - tok_open_brace[21,8]]",//2
            "",//3
            "Syntax Error: Instruction missing semicolon (;) as it has tokens following it [zero - tok_identifier[3,10]]",//4
            "Syntax Error: Instruction missing semicolon (;) as it has tokens following it [zero - tok_identifier[4,6]]",//5
            "Syntax Error: Invalid Conditional Syntax: Expected Opening Parenthesis [if - tok_if[6,6]]",//6
            "Syntax Error: Invalid Conditional Syntax: Expected Closing Parenthesis [m - tok_identifier[6,10]]",//7
            "Syntax Error: Invalid Conditional Syntax: Expected Closing Parenthesis [) - tok_close_paren[8,20]]",//8
            "",//9
            "Syntax Error: Invalid assignment: Bad Right Operand [= - tok_assign[4,10]]",//10
            "Syntax Error: Unexpected Token: tok_open_brace - proc identifier expected [proc - tok_proc[1,10]]",//11
            "Syntax Error: Invalid boolean expression given [m - tok_identifier[6,11]]",//12
            "Syntax Error: Expected Closing Brace\n" +
                "\tHin You may be missing a semicolon (;) between instructions [} - tok_close_brace[12,6]]",//9,///error/13
            "Syntax Error: Final instruction in code block has a trailing semicolon (;) [; - tok_semi[4,14]]",//15
            ""//16
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
            "input/ScopeCheckTest/ScopeCheckTest1.spl",
            "input/ScopeCheckTest/ScopeCheckTest2.spl",
            "input/ScopeCheckTest/ScopeCheckTest3.spl",
            "input/ScopeCheckTest/ScopeCheckTest4.spl",
            "input/ScopeCheckTest/ScopeCheckTest5.spl",
            "input/ScopeCheckTest/ScopeCheckTest6.spl",
            "input/ScopeCheckTest/ScopeCheckTest7.spl",
            "input/ScopeCheckTest/ScopeCheckTest8.spl",
            "input/ScopeCheckTest/ScopeCheckTest9.spl",
            "input/ScopeCheckTest/ScopeCheckTest10.spl"
        };

        String[] results = {
            "Usage Exception: There are undefined usages/calls: [59:variable 'n1s1'][15,6]",//1
            "Usage Exception: Identifier is used more than once within same scope [28:variable 'n1s1'][7,10]",//2
            "Usage Exception: There are undefined usages/calls: [37:variable 'undef'][8,6]",//3
            "",//4
            "",//5
            "Usage Exception: There are undefined usages/calls: [62:call 'innertest'][20,6]",//6
            "Usage Exception: Identifier is used more than once within same scope [50:variable 'numtest'][15,13]",//7
            "", //8          
            "", //9          
            ""  //10          
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
            "input/TypeCheckTest/TypeCheckTest1.spl",
            "input/TypeCheckTest/TypeCheckTest2.spl",
            "input/TypeCheckTest/TypeCheckTest3.spl",
            "input/TypeCheckTest/TypeCheckTest4.spl",
            "input/TypeCheckTest/TypeCheckTest5.spl",
            "input/TypeCheckTest/TypeCheckTest6.spl",
            "input/TypeCheckTest/TypeCheckTest7.spl",
            "input/TypeCheckTest/TypeCheckTest8.spl",
            "input/TypeCheckTest/TypeCheckTest9.spl",
            "input/TypeCheckTest/TypeCheckTest10.spl",
            "input/TypeCheckTest/TypeCheckTest11.spl",
            "input/TypeCheckTest/TypeCheckTest12.spl"
        };

        String[] results = {
            "Type Exception: Variable must be of type num, bool or string in IO call [variable 'guess'][49,21]", //1
            "Type Exception: Assignment must match type of variable [variable 'x'][36,4]", //2
            "Type Exception: Assignment must match type of variable [variable 'x'][36,5]", //3
            "Type Exception: Assignment must match type of variable [variable 'correct'][36,5]", //4
            "Type Exception: Boolean type mismatch inside if conditional branch [COND_BRANCH][63,7]", //5
            "Type Exception: Boolean type mismatch inside while loop condition [loop 'while'][37,5]", //6
            "Type Exception: Number expression contains symbols that are not of type num [calc 'add'][14,5]", //7
            "", //8
            "Type Exception: For loop expression types do no match [variable 'i'][17,10]", //9
            "", //10
            "Type Exception: Assignment must match type of variable [variable 'eqv'][14,6]", //11
            "Type Exception: Boolean type mismatch inside if conditional branch [COND_BRANCH][11,16]" //12
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

    @Test
    public void testValueCheck() 
    { 
        System.out.println("\tVALUE CHECK UNIT TESTING...\n");
        
        String[] args = {
            "input/ValueCheckTest/ValueCheckTest1.spl",
            "input/ValueCheckTest/ValueCheckTest2.spl",
            "input/ValueCheckTest/ValueCheckTest3.spl",
            "input/ValueCheckTest/ValueCheckTest4.spl",
            "input/ValueCheckTest/ValueCheckTest5.spl"
        };

        String[] results = {
            "Value Exception: Variable undefined when used in a for loop [variable 'a'][13,17]; Variable undefined when used in a for loop [variable 'a'][17,21]; Variable needs a value when being assigned to another variable [variable 'other'][43,15]; Variable needs a value to be outputted [variable 't'][44,18]; Variable undefined in bool condition [variable 'answer'][46,17]; Variable undefined in bool condition [variable 'other'][46,25]; Variable needs a value to be outputted [variable 's'][49,21]",
            "Value Exception: Variable undefined when used in a for loop [variable 'a'][13,17]; Variable undefined when used in a for loop [variable 'a'][17,21]; Variable needs a value when being assigned to another variable [variable 'other'][43,15]; Variable needs a value to be outputted [variable 't'][44,18]; Variable undefined in bool condition [variable 'answer'][46,17]; Variable undefined in bool condition [variable 'other'][46,25]; Variable needs a value to be outputted [variable 's'][49,21]; Variable undefined when used in a for loop [variable 'a'][13,17]; Variable undefined when used in a for loop [variable 'a'][17,21]; Variable needs a value when being assigned to another variable [variable 'other'][43,15]; Variable needs a value to being outputted [variable 't'][44,18]; Variable undefined in if statement condition [variable 'answer'][46,17]; Variable undefined in if statement condition [variable 'other'][46,25]",
            "Value Exception: Variable undefined when used in a for loop [variable 'a'][13,17]; Variable undefined when used in a for loop [variable 'a'][17,21]; Variable needs a value when being assigned to another variable [variable 'other'][43,15]; Variable needs a value to be outputted [variable 't'][44,18]; Variable undefined in bool condition [variable 'answer'][46,17]; Variable undefined in bool condition [variable 'other'][46,25]; Variable needs a value to be outputted [variable 's'][49,21]; Variable undefined when used in a for loop [variable 'a'][13,17]; Variable undefined when used in a for loop [variable 'a'][17,21]; Variable needs a value when being assigned to another variable [variable 'other'][43,15]; Variable needs a value to being outputted [variable 't'][44,18]; Variable undefined in if statement condition [variable 'answer'][46,17]; Variable undefined in if statement condition [variable 'other'][46,25]; Variable undefined when used in a for loop [variable 'a'][13,17]; Variable undefined when used in a for loop [variable 'a'][17,21]; Variable needs a value to be outputted [variable 'a'][19,16]; Variable needs a value when being assigned to another variable [variable 'other'][43,15]; Variable needs a value to be outputted [variable 't'][44,18]; Variable undefined in bool condition [variable 'answer'][46,17]; Variable undefined in bool condition [variable 'other'][46,25]; Variable needs a value to being outputted [variable 'h'][79,16]",
            ""
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
                // compiler.output(true);
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

    @Ignore
    public void testCodeGeneration() 
    { 
        System.out.println("\tCODE GENERATION UNIT TESTING...\n");
        
        String[] args = {
            "input/CodeGeneration/CodeGenerationTest1.spl"
        };

        String[] results = {
            "" //1
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
        
        System.out.println("\tCODE GENERATION UNIT TESTING COMPLETE...\n");
    } 
}
