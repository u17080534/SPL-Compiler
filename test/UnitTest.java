import java.io.*;
import java.util.*;
import lexer.*;

public class UnitTest
{
	private static Vector<SPL> compilers;

	public static void execute()
	{
        String[] args = {"input/LexerTest1.spl", "input/LexerTest2.spl", "input/LexerTest3.spl", "input/LexerTest4.spl", "input/LexerTest5.spl", "input/LexerTest6.spl", "input/LexerTest7.spl"};
		compilers = new Vector<SPL>();

		try
        {
			for(int index = 0; index < args.length; index++)
	            compilers.add(new SPL(args[index]));
        }
		catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }

		System.out.println("TESTING ENVIRONMENT...");

		testLexer();

		testParser();

		System.exit(0);
	} 

	public static void testLexer() 
    { 
		System.out.println("LEXER UNIT TESTING...");
        
        for(int index = 0; index < compilers.size(); index++)
        {
        	System.out.println(compilers.get(index));

            List<Token> tokens = compilers.get(index).tokenize();

            System.out.print("\n");
        }
    } 

    public static void testParser() 
    { 
		System.out.println("PARSER UNIT TESTING...");
		
        for(int index = 0; index < compilers.size(); index++)
        {
        	System.out.println(compilers.get(index));

            List<Token> tokens = compilers.get(index).tokenize();

            if(tokens != null)
            	compilers.get(index).parse(tokens);

            System.out.print("\n");
        }
    } 
}