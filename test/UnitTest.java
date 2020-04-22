import java.io.*;
import java.util.*;
import lexer.*;
import org.junit.Test;
import org.junit.BeforeClass;
import static org.junit.Assert.*;

public class UnitTest
{
    @BeforeClass
	public static void execute()
	{
		System.out.println("\nTESTING ENVIRONMENT...");
	} 

    @Test
	public void testLexer() 
    { 
		System.out.println("\nLEXER UNIT TESTING...\n");
        
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
           // "input/LexerTest10.spl",//error
            "input/LexerTest11.spl",
            "input/LexerTest12.spl",
            "input/LexerTest13.spl", //Test that all keywords are recognized
            "input/LexerTest14.spl", //Test that all operators and seperators are recognized
            "input/LexerTest15.spl",
            "input/LexerTest16.spl",
            "input/LexerTest17.spl",
            "input/LexerTest18.spl",



        };

        String[] results = {
            "Lexical Error [line: 1, col: 1]: '@' is not a recognized character",  //1
            "Lexical Error [line: 5, col: 22]: \'\"JohnIsAB\' strings have at most 8 characters", //2
            "Lexical Error [line: 40, col: 10]: \'!\' is not a recognized character", //3
            "", //4
            "", //5
            "Lexical Error [line: 11, col: 12]: Number literals other than 0 must begin with [1-9]", //6
            "Lexical Error [line: 15, col: 1]: newline character was unexpected in this case", //7
            "", //8
            "", //9
           // "",//error //10
            "",  //11
            "Lexical Error [line: 2, col: 6]: Unexpected Input: Integer Tok Rejected" ,//12
            "",//13
            "",//14
            "Lexical Error [line: 2, col: 10]: ';' was unexpected in this case",//15
            "",//16
            "Lexical Error [line: 2, col: 11]: Unexpected Input: Integer Tok May Not Contain Alphabet Characters",//17
            "",//18
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

            System.out.println("\t" + result);
            assertEquals(result, results[index]);
        }
        
        System.out.println("\nLEXER UNIT COMPLETE...\n");
    } 
}
