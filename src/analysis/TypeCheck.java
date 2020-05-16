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

        //find all variable declerations
        String search = "";
        Symbol searchSym = null;
        boolean bnested;

        for (int i = 0; i < symbols.size(); i++) 
        {
            searchSym = symbols.get(i);

            search = symbols.get(i).toString();

            String find, save;
            if (search.indexOf("type")!=-1)
            {
                //Save strings
                if (search.indexOf("string")!=-1 && search.indexOf("type")!=-1)
                {
                    save = symbols.get(i + 2).toString();

                    if (save.indexOf("variable")!=-1)
                    {
                        find = Expression.getValue(save);

                        for (int x = 0; x < symbols.size(); x++) 
                        {
                            if(x > 2 && symbols.get(x-1).toString().indexOf("string")!=-1)
                            {
                                if(symbols.get(x).toString().indexOf(find) != -1) 
                                    symbols.get(x).setType("S");
                            }
                        }
                    }
                    //end
                    strings.add(symbols.get(i + 2));
                }

                //Save bools
                if (search.indexOf("bool")!=-1 && search.indexOf("type")!=-1)
                {
                    save = symbols.get(i + 2).toString();

                    if (save.indexOf("variable")!=-1)
                    {
                        find = Expression.getValue(save);

                        for (int x = 0; x < symbols.size(); x++)
                        {
                            if(x > 2 && symbols.get(x-1).toString().indexOf("bool")!=-1)
                            {
                                if (symbols.get(x).toString().indexOf(find) != -1)
                                    symbols.get(x).setType("B");
                            }

                        }
                    }

                    bools.add(symbols.get(i + 2));
                }

                //Save nums
                if (search.indexOf("num")!=-1 && search.indexOf("type")!=-1)
                {
                    save = symbols.get(i + 2).toString();

                    if (save.indexOf("variable")!=-1)
                    {
                        find = Expression.getValue(save);

                        for (int x = 0; x < symbols.size(); x++)
                        {
                           if(x>2 && symbols.get(x-1).toString().indexOf("num")!=-1)
                           {
                                if (symbols.get(x).toString().indexOf(find)!=-1 || symbols.get(x).toString().indexOf("numexpr")!=-1)
                                {
                                    symbols.get(x).setType("N");
                                }
                            }
                        }

                        nums.add(symbols.get(i + 2));
                    }
                }
            }

            if (search.indexOf("PROC_DEFS") != -1)
            {
                if (symbols.get(i + 2).toString().indexOf("proc")!=-1)
                {
                    save = symbols.get(i + 2).toString();
                    procs.add(symbols.get(i + 2));

                    find = Expression.getValue(save);
                    for (int x = 0; x < symbols.size(); x++)
                    {
                        if(symbols.get(x).toString().indexOf(find) != -1)
                        {
                            symbols.get(x).setType("P");
                        }
                    }
                }
            }
        }

        //IO checks
        //Find all IO instructions

        Vector<Symbol> IOCalls = new Vector<Symbol>();

        for (int i = 0; i < symbols.size(); i++) 
        {
            searchSym = symbols.get(i);
            search = symbols.get(i).toString();

            if (search.indexOf("io")!=-1) 
            {
                IOCalls.add(symbols.get(i + 2));
            }
        }
        //Check that they either N,B,S

        boolean bcorrectAlt;

        for (int i = 0; i < IOCalls.size(); i++) 
        {
            //check each indv
            String test = IOCalls.get(i).toString();
            Symbol testSym = IOCalls.get(i);
            bcorrectAlt = contains(test, nums, bools, strings);

            if (testSym.getType().equals("B") || testSym.getType().equals("S") || testSym.getType().equals("N"))
                bcorrectAlt=true;

            if(bcorrectAlt==false)
                throw new TypeException(testSym, "Variable must be of type num, bool or string in IO call");     
        }

        //END IO checks
        //Assign checks
        String line, searchAlt;
        Symbol lineSym;
        boolean bcorrect;

        searchSym = null;
        search = "";

        for (int i = 0; i < symbols.size()-2; i++) 
        {
            searchSym = symbols.get(i);
            search = symbols.get(i).toString();
            searchAlt = symbols.get(i+1).toString();
            line = symbols.get(i+2).toString();
            lineSym = symbols.get(i+2);

            bcorrect = false;

            //Know we are assigning
            if (search.indexOf("variable")!=-1 && searchAlt.indexOf("ASSIGN")!=-1 ) 
            {
                //bool var and bool value
                if (searchSym.getType().equals("B") && line.indexOf("bool")!=-1)
                    bcorrect = true;

                if (searchSym.getType().equals("B") && lineSym.getType().equals("B"))
                    bcorrect = true;
                
               //string var to string var
                if (searchSym.getType().equals("S") && lineSym.getType().equals("B"))
                    bcorrect = true;

                //num var to num var
                if (searchSym.getType().equals("N") && lineSym.getType().equals("B"))
                    bcorrect = true;

                if (searchSym.getType().equals("N") && line.indexOf("numexpr")!=-1)
                    bcorrect = true;
                
                //static string to string var
                if (searchSym.getType().equals("S") && line.indexOf('"')!=-1)
                    bcorrect = true;
                
                if(searchSym.getType().equals("S") && symbols.get(i+2).toString().indexOf("variable")!=-1)
                {
                    if(symbols.get(i+3).getType().equals("S"))
                        bcorrect=true;
                }

                if(searchSym.getType().equals("B") && symbols.get(i+2).toString().indexOf("variable")!=-1)
                {
                    if(symbols.get(i+3).getType().equals("B"))
                        bcorrect=true;
                }

                if(searchSym.getType().equals("N") && symbols.get(i+2).toString().indexOf("variable")!=-1)
                {
                    if(symbols.get(i+3).getType().equals("N"))
                        bcorrect=true;
                }

                //error found
                if (bcorrect == false) 
                    throw new TypeException(searchSym, "Assignment must match type of variable");
            }
        }

        String now;
        String newvar,newvar2,newvar3,newvar4,newvar5;

        boolean bceq = false;
        boolean bOr = false;
        boolean bOrNested = false;

        for(int i=0;i<symbols.size();i++)
        {
    		now = symbols.get(i).toString();

            //check eq
        
            if(now.indexOf("bool 'eq'") != -1)
            {
                bceq=false;

                if(symbols.get(i+2).getType().equals(symbols.get(i+4).getType()))
                    bceq = true;
                
                if(bceq == false)
                    throw new TypeException(symbols.get(i), "Boolean type mismatch, expected eq(bool, bool)"); 
            }
    		//check < or >
    		if(now.indexOf("bool '<'")!=-1 || now.indexOf("bool '>'")!=-1)
            {
    			if(!symbols.get(i+2).getType().equals("N") || !symbols.get(i+4).getType().equals("N"))
    				throw new TypeException(symbols.get(i), "Boolean type mismatch, expected (num < num); (num > num)"); 
    		}

            if(now.indexOf("bool 'not'")!=-1)
            {
                if(!symbols.get(i+1).getExpr().getTerminalType().equals("B"))
                    throw new TypeException(symbols.get(i), "Boolean expression contains symbols that are not of type bool");
            }
    		
    		if(now.indexOf("bool 'and'")!=-1 || now.indexOf("bool 'or'")!=-1)
            {
                if(!symbols.get(i+1).getExpr().getTerminalType().equals("B"))
                    throw new TypeException(symbols.get(i), "Boolean expression contains symbols that are not of type bool");

                // bOr = false;  
                // bOr = checkBool(symbols,i+4);

                // if(!bOr)
                //     throw new TypeException(symbols.get(i), "Boolean type mismatch, expected or(bool, bool); and(bool, bool)"); 
            }

            //CHECK IF STATEMENTS
            if(now.indexOf("COND_BRANCH") != -1)
            {
                bcorrect = false;

                if (symbols.get(i + 2).getType().equals("B"))
                    bcorrect = true;

                if (symbols.get(i + 2).toString().indexOf("bool 'T'")!=-1 || symbols.get(i + 2).toString().indexOf("bool 'F'") != -1)
                    bcorrect = true;

                if (!bcorrect)
                    throw new TypeException(symbols.get(i + 3), "Boolean type mismatch inside if condition");
            }

            //CHECKS WHILE LOOPS
            if(now.indexOf("COND_LOOP")!=-1 && symbols.get(i + 1).toString().indexOf("while")!=-1)
            {
                bcorrect=false;

                if (symbols.get(i + 3).toString().indexOf("bool 'T'")!=-1 )
                    bcorrect = true;

                if ((symbols.get(i + 3).getType().equals("B")) && (symbols.get(i + 3).toString().indexOf("VAR")==-1) )
                    bcorrect = true;

                if (symbols.get(i + 4).getType().equals("B") && symbols.get(i + 3).toString().indexOf("VAR")==-1)
                    bcorrect = true;

                if (!bcorrect)
                {
                    newvar = Expression.getValue(symbols.get(i + 3).toString());
                    throw new TypeException(symbols.get(i + 4), "Boolean type mismatch inside while loop condition");
                }

            }

            //CHECKS CALC STATEMENTS
            if(now.indexOf("calc")!=-1)
            {
                if(!symbols.get(i+1).getExpr().getTerminalType().equals("N"))
                    throw new TypeException(symbols.get(i), "Number expression contains symbols that are not of type num");

                /*
                bcorrect = false;
                if(symbols.get(i + 2).toString().indexOf("NUMEXPR") != -1 && symbols.get(i + 4).toString().indexOf("NUMEXPR") != -1)
                {
                    bcorrect = true;
                }

                if(symbols.get(i + 2).toString().indexOf("sub")!=-1 || symbols.get(i + 2).toString().indexOf("add")!=-1|| symbols.get(i +2).toString().indexOf("mult")!=-1)
                {
                    bnested = checkCalc(symbols,i+2);

                    bcorrect = bnested;

                    if(bnested == false)
                        throw new TypeException(symbols.get(i + 2), "Number expression types do no match");
                }
                else if(symbols.get(i + 2).getType().equals("N"))
                    bcorrect = true;
                else
                    throw new TypeException(symbols.get(i + 2), "Number expression types do no match");

                if(symbols.get(i + 4).toString().indexOf("sub") != -1 || symbols.get(i + 4).toString().indexOf("add") != -1|| symbols.get(i +4).toString().indexOf("mult") != -1)
                {
                    bnested = checkCalc(symbols, i+4);

                    bcorrect = bcorrect && bnested;

                    if(bnested == false)
                        throw new TypeException(symbols.get(i + 4), "Number expression types do no match");
                }
                else if(symbols.get(i + 2).getType().equals("N"))
                    bcorrect = bcorrect && true;
                else
                    throw new TypeException(symbols.get(i + 4), "Number expression types do no match");
                */
            }

            //CHECKS FOR LOOPS
            if(now.indexOf("loop 'for'")!=-1)
            {
                bcorrect=false;

                if (symbols.get(i + 2).getType().equals("N") && symbols.get(i + 4).getType().equals("N"))
                {
                    if (symbols.get(i + 6).getType().equals("N"))
                    {
                        if (symbols.get(i + 8).getType().equals("N"))
                        {
                            if (symbols.get(i + 10).getType().equals("N"))
                                bcorrect = true;
                            else
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
            if(now.indexOf("CALL")!=-1)
            {
                bcorrect=false;

                if(symbols.get(i + 1).getType().equals("P"))
                    bcorrect = true;

                if (!bcorrect)
                {
                    newvar = Expression.getValue(symbols.get(i + 2).toString());
                    throw new TypeException("Incorrect proc call, "+newvar+" is not a valid proc");
                }
            }
        }
    }

    //Check if the symbol is in the variable list
    public static boolean contains(String searchValue, Vector<Symbol> nums, Vector<Symbol> bools, Vector<Symbol> strings)
    {
        boolean found = false;
        String findValue;

        //check nums
        for(int i=0;i<nums.size();i++)
        {
            findValue = nums.get(i).toString();

            if(nums.get(i).getType().equals("N") && Expression.getValue(searchValue).equals(Expression.getValue(findValue)))
                found= true;
        }

        //check bools
        for(int i=0;i<bools.size();i++)
        {
            findValue = bools.get(i).toString();

            if(bools.get(i).getType().equals("B") && Expression.getValue(searchValue).equals(Expression.getValue(findValue)))
                found= true;
        }

        //check strings
        for(int i=0;i < strings.size();i++)
        {
            findValue = strings.get(i).toString();

            if(strings.get(i).getType().equals("S") && Expression.getValue(searchValue).equals(Expression.getValue(findValue)))
                found= true;
        }

        return found;
    }

    /*
    public static boolean checkCalc(Vector<Symbol> symbols, int i) throws TypeException
    {
        boolean bnested = false;

        if (symbols.get(i + 2).toString().indexOf("NUMEXPR")!=-1 && symbols.get(i + 4).toString().indexOf("NUMEXPR")!=-1)
            return true;

        if(symbols.get(i + 2).toString().indexOf("sub")!=-1 || symbols.get(i + 2).toString().indexOf("add")!=-1|| symbols.get(i +2).toString().indexOf("mult")!=-1)
        {
            bnested = checkCalc(symbols,i+2);

            if(bnested == false)
                throw new TypeException("Expected (numexpr,numexpr) but mistake found in "+symbols.get(i + 2).toString());
        }

        bnested = false;

        if(symbols.get(i + 4).toString().indexOf("sub")!=-1 || symbols.get(i + 4).toString().indexOf("add")!=-1|| symbols.get(i +4).toString().indexOf("mult")!=-1)
        {
            bnested = checkCalc(symbols,i+4);

            if(bnested == false)
                throw new TypeException("Expected (numexpr,numexpr) but mistake found in "+symbols.get(i + 4).toString());
        }

        return false;
    }

    public static boolean checkBool(Vector<Symbol> symbols, int i) throws TypeException
    {
        if(symbols.get(i).getType().equals("B") && symbols.get(i+3).getType().equals("B"))
            return true;
        
        if(symbols.get(i).toString().indexOf("bool 'and'") != -1 || symbols.get(i).toString().indexOf("bool 'or'") != -1) 
            return checkBool(symbols, i+3) && checkBool(symbols, i+3);

        return false;
    }
    */
}
