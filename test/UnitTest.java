import java.io.*;
import java.util.*;
import lexer.*;
import org.junit.Test;
import org.junit.BeforeClass;
import static org.junit.Assert.*;
import parser.*;
import ast.*;
import symtable.*;
import analysis.*;
import exception.*;

public class UnitTest
{
    @BeforeClass
	public static void execute()
	{
		System.out.println("\nTESTING ENVIRONMENT...");
	} 

    @Test
	public void testParser()
    { 
		System.out.println("\nPARSER UNIT TESTING...\n");

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
               // "input/ParserTest13.spl", //error
                "input/ParserTest15.spl",
                "input/ParserTest16.spl",







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
                //"Invalid Assignment: Unknown Operator, Expected Token (=)",/error/13
                "Syntax Error: Intruction Syntax Error: Instruction expected following semicolon. (;) Token=[; - tok_semi[3,13]]",//15
                "",




        };






        for(int index = 0; index < args.length; index++)
        {
            System.out.println("HERE");
            String result = "";
            try
            {
                SPL compiler = new SPL(args[index]); 
                System.out.println(compiler);
                List<Token> tokens= compiler.tokenize();

                if(tokens != null)
                      {this.parse(tokens,args[index]);};

            }
            catch(Exception e)
            {
                result = e.toString();
            }

            System.out.println("\t" + result);
            assertEquals(result, results[index]);
        }
        
        System.out.println("\nPARSER UNIT COMPLETE...\n");

    }


    public void parse(List<Token> tokens,String filename)  throws SyntaxException
    {

        Cache cache;
        Parser parser;
        AbstractSyntaxTree tree;
        SymbolTable table;

        cache = new Cache(filename);
        parser = new Parser();
        table = new SymbolTable();
        tree = null;


        try
        {
            tree = parser.parse(tokens);
            cache.export(tree);
        }
        catch(SyntaxException ex)
        {
            throw ex;
        }

        if(tree != null)
        {
            Vector<Symbol> symbols = tree.getSymbols();

            for(Symbol sym : symbols)
                table.add(sym);

            cache.export(table);
        }


    };
}
