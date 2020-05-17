package analysis;

import java.util.*;
import syntax.*;
import syntax.expression.Expression;
import symtable.*;
import exception.*;

//SPL-COMPILER
public class TypeCheck
{
    public static void check(AbstractSyntaxTree tree, SymbolTable table) throws TypeException
    {
        //vectors
        Vector<Symbol> symbols = table.list();
        Vector<Symbol> terminals = table.terminals();
        Vector<Symbol> declarations = new Vector<Symbol>();
        Vector<Symbol> usages = new Vector<Symbol>();
        Vector<String> undefined = new Vector<String>();
        Vector<Symbol> strings = new Vector<Symbol>();
        Vector<Symbol> nums = new Vector<Symbol>();
        Vector<Symbol> bools = new Vector<Symbol>();
        Vector<Symbol> procs = new Vector<Symbol>();
        Vector<Symbol> ioCalls = new Vector<Symbol>();

        for(int i = 0; i < symbols.size(); i++) 
        {
            Symbol searchSym = symbols.get(i);
            String search = symbols.get(i).toString();

            String find, save;
            if(search.indexOf("type") >= 0)
            {
                //Save strings
                if(search.indexOf("string") >= 0 && search.indexOf("type") >= 0)
                {
                    save = symbols.get(i + 2).toString();

                    if(save.indexOf("variable") >= 0)
                    {
                        find = Expression.getValue(save);

                        for(int x = 0; x < symbols.size(); x++) 
                        {
                            if(x > 2 && symbols.get(x-1).toString().indexOf("string") >= 0)
                            {
                                if(symbols.get(x).toString().indexOf(find) >= 0) 
                                    symbols.get(x).setType("S");
                            }
                        }
                    }
                    //end
                    strings.add(symbols.get(i + 2));
                }

                //Save bools
                if(search.indexOf("bool") >= 0 && search.indexOf("type") >= 0)
                {
                    save = symbols.get(i + 2).toString();

                    if(save.indexOf("variable") >= 0)
                    {
                        find = Expression.getValue(save);

                        for(int x = 0; x < symbols.size(); x++)
                        {
                            if(x > 2 && symbols.get(x-1).toString().indexOf("bool") >= 0)
                            {
                                if(symbols.get(x).toString().indexOf(find) >= 0)
                                    symbols.get(x).setType("B");
                            }

                        }
                    }

                    bools.add(symbols.get(i + 2));
                }

                //Save nums
                if(search.indexOf("num") >= 0 && search.indexOf("type") >= 0)
                {
                    save = symbols.get(i + 2).toString();

                    if(save.indexOf("variable") >= 0)
                    {
                        find = Expression.getValue(save);

                        for(int x = 0; x < symbols.size(); x++)
                        {
                           if(x>2 && symbols.get(x-1).toString().indexOf("num") >= 0)
                           {
                                if(symbols.get(x).toString().indexOf(find) >= 0 || symbols.get(x).toString().indexOf("numexpr") >= 0)
                                {
                                    symbols.get(x).setType("N");
                                }
                            }
                        }

                        nums.add(symbols.get(i + 2));
                    }
                }
            }

            if(search.indexOf("PROC_DEFS") >= 0)
            {
                if(symbols.get(i + 2).toString().indexOf("proc") >= 0)
                {
                    save = symbols.get(i + 2).toString();
                    procs.add(symbols.get(i + 2));

                    find = Expression.getValue(save);
                    for(int x = 0; x < symbols.size(); x++)
                    {
                        if(symbols.get(x).toString().indexOf(find) >= 0)
                        {
                            symbols.get(x).setType("P");
                        }
                    }
                }
            }
        }

        for(int i = 0; i < symbols.size(); i++) 
        {
            String search = symbols.get(i).toString();

            if(search.indexOf("io") >= 0) 
                ioCalls.add(symbols.get(i + 2));
        }

        for(int i = 0; i < ioCalls.size(); i++) 
        {
            //check each indv
            String test = ioCalls.get(i).toString();
            Symbol testSym = ioCalls.get(i);
            boolean correct = contains(test, nums, bools, strings);

            if(testSym.getType().equals("B") || testSym.getType().equals("S") || testSym.getType().equals("N"))
                correct = true;

            if(correct == false)
                throw new TypeException(testSym, "Variable must be of type num, bool or string in IO call");     
        }

        for(int i = 0; i < symbols.size()-2; i++) 
        {
            Symbol searchSym = symbols.get(i);
            String search = symbols.get(i).toString();
            String searchAlt = symbols.get(i+1).toString();

            String line = symbols.get(i+2).toString();
            Symbol lineSym = symbols.get(i+2);

            boolean correct = false;

            //Know we are assigning
            if(search.indexOf("variable") >= 0 && searchAlt.indexOf("ASSIGN") >= 0 ) 
            {
                //bool var and bool value
                if(searchSym.getType().equals("B") && line.indexOf("bool") >= 0)
                    correct = true;

                if(searchSym.getType().equals("B") && lineSym.getType().equals("B"))
                    correct = true;
                
               //string var to string var
                if(searchSym.getType().equals("S") && lineSym.getType().equals("B"))
                    correct = true;

                //num var to num var
                if(searchSym.getType().equals("N") && lineSym.getType().equals("B"))
                    correct = true;

                if(searchSym.getType().equals("N") && line.indexOf("numexpr") >= 0)
                    correct = true;
                
                //static string to string var
                if(searchSym.getType().equals("S") && line.indexOf('"') >= 0)
                    correct = true;
                
                if(searchSym.getType().equals("S") && symbols.get(i+2).toString().indexOf("variable") >= 0)
                {
                    if(symbols.get(i+3).getType().equals("S"))
                        correct=true;
                }

                if(searchSym.getType().equals("B") && symbols.get(i+2).toString().indexOf("variable") >= 0)
                {
                    if(symbols.get(i+3).getType().equals("B"))
                        correct=true;
                }

                if(searchSym.getType().equals("N") && symbols.get(i+2).toString().indexOf("variable") >= 0)
                {
                    if(symbols.get(i+3).getType().equals("N"))
                        correct=true;
                }

                if(correct == false) 
                    throw new TypeException(searchSym, "Assignment must match type of variable");
            }
        }

        String now;

        for(int i = 0; i < symbols.size(); i++)
        {
    		now = symbols.get(i).toString();
        
            if(now.indexOf("bool 'eq'") >= 0)
            {
                if(!symbols.get(i+2).getType().equals(symbols.get(i+4).getType()))
                    throw new TypeException(symbols.get(i), "Boolean type mismatch, expected eq(bool, bool)"); 
            }

    		if(now.indexOf("bool '<'") >= 0 || now.indexOf("bool '>'") >= 0)
            {
    			if(!symbols.get(i+2).getType().equals("N") || !symbols.get(i+4).getType().equals("N"))
    				throw new TypeException(symbols.get(i), "Boolean type mismatch, expected (num < num); (num > num)"); 
    		}

            if(now.indexOf("bool 'not'") >= 0)
            {
                if(!symbols.get(i+1).getExpr().getTerminalType().equals("B"))
                    throw new TypeException(symbols.get(i), "Boolean expression contains symbols that are not of type bool");
            }
    		
    		if(now.indexOf("bool 'and'") >= 0 || now.indexOf("bool 'or'") >= 0)
            {
                if(!symbols.get(i+1).getExpr().getTerminalType().equals("B"))
                    throw new TypeException(symbols.get(i), "Boolean expression contains symbols that are not of type bool");
            }

            //CHECK IF STATEMENTS
            if(now.indexOf("COND_BRANCH") >= 0)
            {
                boolean correct = false;

                if(symbols.get(i + 2).getType().equals("B"))
                    correct = true;

                if(symbols.get(i + 2).toString().indexOf("bool 'T'") >= 0 || symbols.get(i + 2).toString().indexOf("bool 'F'") >= 0)
                    correct = true;

                if(!correct)
                    throw new TypeException(symbols.get(i + 3), "Boolean type mismatch inside if condition");
            }

            //CHECKS WHILE LOOPS
            if(now.indexOf("COND_LOOP") >= 0 && symbols.get(i + 1).toString().indexOf("while") >= 0)
            {
                boolean correct = false;

                if(symbols.get(i + 3).toString().indexOf("bool 'T'") >= 0 )
                    correct = true;

                if((symbols.get(i + 3).getType().equals("B")) && (symbols.get(i + 3).toString().indexOf("VAR")==-1) )
                    correct = true;

                if(symbols.get(i + 4).getType().equals("B") && symbols.get(i + 3).toString().indexOf("VAR")==-1)
                    correct = true;

                if(!correct)
                    throw new TypeException(symbols.get(i + 4), "Boolean type mismatch inside while loop condition");

            }

            //CHECKS CALC STATEMENTS
            if(now.indexOf("calc") >= 0)
            {
                if(!symbols.get(i+1).getExpr().getTerminalType().equals("N"))
                    throw new TypeException(symbols.get(i), "Number expression contains symbols that are not of type num");
            }

            //CHECKS FOR LOOPS
            if(now.indexOf("loop 'for'") >= 0)
            {
                if(symbols.get(i + 2).getType().equals("N") && symbols.get(i + 4).getType().equals("N"))
                {
                    if(symbols.get(i + 6).getType().equals("N"))
                    {
                        if(symbols.get(i + 8).getType().equals("N"))
                        {
                            if(!symbols.get(i + 10).getType().equals("N"))
                                throw new TypeException(symbols.get(i + 10), "For loop expression types do no match");
                         }
                        else
                            throw new TypeException(symbols.get(i + 8), "For loop expression types do no match");
                    }
                    else
                        throw new TypeException(symbols.get(i + 6), "For loop expression types do no match");
                }
                else
                    throw new TypeException(symbols.get(i + 2), "For loop expression types do no match");
            }

            //CHECKS PROCS
            if(now.indexOf("CALL") >= 0)
            {
                if(!symbols.get(i + 1).getType().equals("P"))
                    throw new TypeException(symbols.get(i + 1), "Invalid proc call made");
            }
        }
    }

    //Check if the symbol is in the variable list
    public static boolean contains(String search, Vector<Symbol> nums, Vector<Symbol> bools, Vector<Symbol> strings)
    {
        //check nums
        for(int i = 0; i < nums.size(); i++)
            if(nums.get(i).getType().equals("N") && Expression.getValue(search).equals(Expression.getValue(nums.get(i).toString())))
                return true;

        //check bools
        for(int i = 0;i < bools.size(); i++)
            if(bools.get(i).getType().equals("B") && Expression.getValue(search).equals(Expression.getValue(bools.get(i).toString())))
                return true;

        //check strings
        for(int i = 0;i < strings.size(); i++)
            if(strings.get(i).getType().equals("S") && Expression.getValue(search).equals(Expression.getValue(strings.get(i).toString())))
                return true;

        return false;
    }
}
