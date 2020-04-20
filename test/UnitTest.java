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
            "input/LexerTest7.spl"
        };

        String[] results = {
            "Lexical Error [line: 1, col: 1]: '@' is not a recognized character", 
            "Lexical Error [line: 5, col: 22]: \'\"JohnIsAB\' strings have at most 8 characters", 
            "Lexical Error [line: 40, col: 10]: \'!\' is not a recognized character",
            "",
            "",
            "Lexical Error [line: 11, col: 12]: Number literals other than 0 must begin with [1-9]",
            "Lexical Error [line: 15, col: 1]: newline character was unexpected in this case"
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