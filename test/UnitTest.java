import java.io.*;
import java.util.*;
import lexer.*;
import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.AfterClass;
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

    @Test
	public void testLexer() 
    { 
		System.out.println("\tLEXER UNIT TESTING...\n");
        
        String[] args = {
            "input/LexerTest1.spl",
            "input/LexerTest2.spl", 
            "input/LexerTest3.spl", 
            "input/LexerTest4.spl", 
            "input/LexerTest5.spl", 
            "input/LexerTest6.spl", 
            "input/LexerTest7.spl",
            "input/LexerTest8.spl",
            "input/LexerTest9.spl",
            "input/LexerTest10.spl",//error
            "input/LexerTest11.spl",
            "input/LexerTest12.spl",
            "input/LexerTest13.spl", //Test that all keywords are recognized
            "input/LexerTest14.spl", //Test that all operators and seperators are recognized
            "input/LexerTest15.spl",
            "input/LexerTest16.spl",
            "input/LexerTest17.spl",
            "input/LexerTest18.spl"
        };

        String[] results = {
            "Lexical Error [line: 1, col: 1]: '@' is not a recognized character",  //1
            "Lexical Error [line: 5, col: 22]: '\"JohnIsAB' strings have at most 8 characters", //2
            "Lexical Error [line: 40, col: 10]: '!' is not a recognized character", //3
            "", //4
            "", //5
            "Lexical Error [line: 11, col: 12]: Numeric literals other than 0 must begin with [1-9]", //6
            "Lexical Error [line: 15, col: 1]: newline character was unexpected in this case", //7
            "", //8
            "", //9
            "Lexical Error [line: 1, col: 9]: Unexpected Input: Identifiers may not begin with numerical characters, only literal numeric expressions may start with a number",//error //10
            "",  //11
            "Lexical Error [line: 2, col: 6]: Unexpected Input: Numeric Token Rejected" ,//12
            "",//13
            "",//14
            "Lexical Error [line: 2, col: 10]: ';' was unexpected in this case",//15
            "",//16
            "Lexical Error [line: 2, col: 11]: Unexpected Input: Identifiers may not begin with numerical characters, only literal numeric expressions may start with a number",//17
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

    @Test
    public void testParser() 
    { 
        System.out.println("\tPARSER UNIT TESTING...\n");
        
        String[] args = {
            "input/ParserTest1.spl",
            "input/ParserTest2.spl",
            "input/ParserTest3.spl",
            "input/ParserTest4.spl",
            "input/ParserTest5.spl",
            "input/ParserTest6.spl",
            "input/ParserTest7.spl",
            "input/ParserTest8.spl",
            "input/ParserTest9.spl",
            "input/ParserTest10.spl",
            "input/ParserTest11.spl",
            "input/ParserTest12.spl",
            "input/ParserTest13.spl", //error
            "input/ParserTest15.spl",
            "input/ParserTest16.spl"
        };

        String[] results = {
            "Syntax Error: Invalid Conditional Syntax: Expected Opening Parenthesis Token=[if - tok_if[1,1]]",//1
            "",//2
            "",//3
            "Syntax Error: Procedural Error: Expected Closing Brace.\n" +
                "\tHint: You may be missing a semicolon (;) between instructions. Token=[zero - tok_identifier[2,10]]",//4
            "Syntax Error: Procedural Error: Expected Closing Brace.\n" +
            "\tHint: You may be missing a semicolon (;) between instructions. Token=[zero - tok_identifier[3,6]]",//5
            "Syntax Error: Invalid Conditional Syntax: Expected Opening Parenthesis Token=[if - tok_if[5,6]]",//6
            "Syntax Error: Invalid Conditional Syntax: Expected Closing Parenthesis Token=[m - tok_identifier[5,10]]",//7
            "Syntax Error: Invalid Conditional Syntax: Expected Closing Parenthesis Token=[) - tok_close_paren[7,20]]",//8
            "Syntax Error: Procedural Error: Expected Closing Brace.\n" +
                "\tHint: You may be missing a semicolon (;) between instructions. Token=[} - tok_close_brace[11,6]]",//9
            "Syntax Error: Invalid assignment: Bad Right Operand. Token=[= - tok_assign[3,10]]",//10
            "Syntax Error: Unexpected Token: tok_open_brace - 'proc' expected. Token=[proc - tok_proc[1,1]]",//11
            "Syntax Error: Invalid boolean expression given. Token=[m - tok_identifier[5,11]]",//12
            "Syntax Error: Procedural Error: Expected Closing Brace.\n" +
                "\tHint: You may be missing a semicolon (;) between instructions. Token=[} - tok_close_brace[11,6]]",//9,///error/13
            "Syntax Error: Intruction Syntax Error: Instruction expected following semicolon. (;) Token=[; - tok_semi[3,13]]",//15
            ""
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
}
