package analysis;

import java.util.*;
import syntax.*;
import symtable.*;
import exception.*;

//Textfile Rewrite
// import java.io.File;
// import java.io.FileNotFoundException;
// import java.io.FileWriter;
// import java.io.IOException;
// import java.util.Scanner;
// import javax.naming.directory.SearchControls;

//SPL-COMPILER
public class TypeCheck {


    public static void check(AbstractSyntaxTree tree, SymbolTable table) throws TypeException {

        //vectors
        // tree.scope(); already being done now
        Vector<Symbol> symbols = table.list();
        Vector<Symbol> terminals = table.terminals();
        Vector<Symbol> declarations = new Vector<Symbol>();
        Vector<Symbol> usages = new Vector<Symbol>();
        Vector<String> undefined = new Vector<String>();
        Vector<Symbol> Strings = new Vector<Symbol>();
        Vector<Symbol> Nums = new Vector<Symbol>();
        Vector<Symbol> Bools = new Vector<Symbol>();
        Vector<Symbol> Procs = new Vector<Symbol>();

        //find all variable declerations
        String search;
        String output;
        boolean bnested;
        System.out.println("");
        System.out.println("SYMBOL TABLE");
        for (int i = 0; i < symbols.size(); i++) {
            search = symbols.get(i).toString();
            output=symbols.get(i).toString();
            if(output.indexOf("T:")!=-1){
                output=output.substring(0,output.indexOf("T:")-1)+"  "+output.substring(output.indexOf("T:")+2,output.length());
                System.out.println(output);
            }else{
                System.out.println(symbols.get(i));
            }
            String find, save;
            if (search.indexOf("type") != -1) {


                //Save strings
                if (search.indexOf("string") != -1 && search.indexOf("type") != -1) {
                    //symbols.get(i+2).setType("string");

                    //new
                    //System.out.println("here");
                    save = symbols.get(i + 2).toString();

                    if (save.indexOf("variable") != -1) {

                        find = save.substring(save.indexOf("e") + 3, save.length() - 1);
                        // System.out.println(find);
                        for (int x = 0; x < symbols.size(); x++) {
                            if(x>2 && symbols.get(x-1).toString().indexOf("string")!=-1){
                                if (symbols.get(x).toString().indexOf(find) != -1) {
                                    symbols.get(x).setType("S");
                                }
                            }

                        }
                    }


                    //end

                    Strings.add(symbols.get(i + 2));
                }

                //Save bools
                if (search.indexOf("bool") != -1 && search.indexOf("type") != -1) {

                    save = symbols.get(i + 2).toString();

                    if (save.indexOf("variable") != -1) {

                        find = save.substring(save.indexOf("e") + 3, save.length() - 1);
                        //  System.out.println(find);
                        for (int x = 0; x < symbols.size(); x++) {
                            if(x>2 && symbols.get(x-1).toString().indexOf("bool")!=-1){
                                if (symbols.get(x).toString().indexOf(find) != -1 || symbols.get(x).toString().indexOf("B") != -1) {
                                    symbols.get(x).setType("B");
                                }
                            }

                        }
                    }


                    Bools.add(symbols.get(i + 2));
                }

                //Save nums
                if (search.indexOf("num") != -1 && search.indexOf("type") != -1) {


                    save = symbols.get(i + 2).toString();


                    if (save.indexOf("variable") != -1) {

                        find = save.substring(save.indexOf("'") + 1, save.length() -5);

                        //System.out.println("save= "+save);
                       // System.out.println("Find= "+find);
                        for (int x = 0; x < symbols.size(); x++) {
                           if(x>2 && symbols.get(x-1).toString().indexOf("num")!=-1){
                                if (symbols.get(x).toString().indexOf(find) != -1 || symbols.get(x).toString().indexOf("numexpr") != -1) {
                                    symbols.get(x).setType("N");
                                }
                            }

                        }
                        Nums.add(symbols.get(i + 2));
                    }
                }


            }
            if (search.indexOf("PROC_DEFS") != -1) {
                if (symbols.get(i + 2).toString().indexOf("proc") != -1) {
                    save = symbols.get(i + 2).toString();
                    Procs.add(symbols.get(i + 2));


                    find = save.substring(save.indexOf("'") + 1, save.length() - 1);
                    for (int x = 0; x < symbols.size(); x++) {
                        if (symbols.get(x).toString().indexOf(find) != -1) {
                            symbols.get(x).setType("proc");
                        }
                    }
                }

                //find = save.substring(save.indexOf("'") , save.length() - 1);
                //System.out.println("SAVING: "+find);

            }
        }

        System.out.println("END OF SYMBOL TABLE");
        System.out.println("");



        //IO checks
        //Find all IO instructions

        Vector<Symbol> IOCalls = new Vector<Symbol>();
        for (int i = 0; i < symbols.size(); i++) {
            search = symbols.get(i).toString();

            if (search.indexOf("io") != -1) {

                IOCalls.add(symbols.get(i + 2));
            }
        }
        //Check that they either N,B,S
        boolean BCorrect;
        for (int i = 0; i < IOCalls.size(); i++) {

            //check each indv
            String test = IOCalls.get(i).toString();
            BCorrect = Contains(test, Nums, Bools, Strings);

           // System.out.println(test);
            if (test.indexOf("T:B")!=-1 ||test.indexOf("T:S")!=-1 || test.indexOf("T:N")!=-1) {
                BCorrect=true;}
            if(BCorrect==false){
                throw new TypeException("IO call, Var must be of type num,bool or string: " + test);
            }
        }
        //END IO checks

        //Assign checks
        String Variable, Line, Store,search1;
        boolean bcorrect;
        Vector<Symbol> Assigns = new Vector<Symbol>();
        search = "";
        for (int i = 0; i < symbols.size()-2; i++) {
            search = symbols.get(i).toString();
            Line="";

            search1=symbols.get(i+1).toString();
            Line =symbols.get(i+2).toString();


            bcorrect = false;

            //Know we are assigning
            if (search.indexOf("variable") != -1 && search1.indexOf("ASSIGN")!=-1 ) {
                //bool var and bool value
                if (search.indexOf("T:B") != -1 && Line.indexOf("bool") != -1) {
                    bcorrect = true;
                }

                if (search.indexOf("T:B") != -1 && Line.indexOf("T:B") != -1) {
                    bcorrect = true;
                }
                //string var to string var
                if (search.indexOf("T:S") != -1 && Line.indexOf("T:S") != -1) {
                    bcorrect = true;
                }

                //num var to num var
                if (search.indexOf("T:N") != -1 && Line.indexOf("T:N") != -1) {
                    bcorrect = true;
                }

                if (search.indexOf("T:N") != -1 && Line.indexOf("numexpr") != -1) {
                    bcorrect = true;
                }

                //static string to string var
                if (search.indexOf("T:S") != -1 && Line.indexOf('"') != -1) {
                    bcorrect = true;
                }

                if(search.indexOf("T:S")!=-1 && symbols.get(i+2).toString().indexOf("variable")!=-1){
                    if(symbols.get(i+3).toString().indexOf("T:S")!=-1){
                        bcorrect=true;
                    }
                }

                if(search.indexOf("T:B") !=-1&& symbols.get(i+2).toString().indexOf("variable")!=-1){
                    if(symbols.get(i+3).toString().indexOf("T:B")!=-1){
                        bcorrect=true;
                    }
                }

                if(search.indexOf("T:N")!=-1 && symbols.get(i+2).toString().indexOf("variable")!=-1){
                    if(symbols.get(i+3).toString().indexOf("T:N")!=-1){
                        bcorrect=true;
                    }
                }

                //error found
                if (bcorrect == false) {
                    if (Line.indexOf(":") != -1) {
                        Line = Line.substring(Line.indexOf(":") + 1, Line.length());
                    }
                    throw new TypeException("Assignment must match type of var: " + Line + " cannot be assigned to " + search);
                }
            }
            //check string assignments
            Variable = search.substring(0, search.length());
            // System.out.println("Var Found"+Variable);
        }
     //END Assign checks

     //COND CHECKS
        String now;
        String newvar,newvar2,newvar3,newvar4,newvar5;
        for(int i=0;i<symbols.size();i++){

                now=symbols.get(i).toString();
                //CHECK IF STATEMENTS
                if(now.indexOf("COND_BRANCH")!=-1) {
                    bcorrect = false;
                    if (symbols.get(i + 3).toString().indexOf("B") != -1) {
                        bcorrect = true;
                    }

                    if (symbols.get(i + 2).toString().indexOf("bool 'T'") != -1 ) {
                        bcorrect = true;
                    }

                    if (!bcorrect) {
                        throw new TypeException("Expected bool condition but received "+ symbols.get(i + 3).toString().substring(symbols.get(i + 3).toString().indexOf("'")+1,symbols.get(i + 3).toString().length()));
                    }
                }
                //CHECKS WHILE LOOPS

                if(now.indexOf("COND_LOOP")!=-1 && symbols.get(i + 1).toString().indexOf("while")!=-1) {
                    bcorrect=false;


                    if (symbols.get(i + 3).toString().indexOf("bool 'T'") != -1 ) {
                        bcorrect = true;
                        //System.out.println("Enter");
                    }


                  /*  boolean bone;
                    if(symbols.get(i + 3).toString().indexOf("VAR")=-1){
                        bone=true;
                    }else{
                        bone=false;
                    }*/

                    if ((symbols.get(i + 3).toString().indexOf("B") != -1) && (symbols.get(i + 3).toString().indexOf("VAR")==-1) ) {
                        bcorrect = true;
                      //  System.out.println("Enter"+symbols.get(i + 3).toString());
                    }

                    if (symbols.get(i + 4).toString().indexOf("B") != -1 && symbols.get(i + 3).toString().indexOf("VAR")==-1) {
                        bcorrect = true;

                    }


                    if (!bcorrect) {
                        newvar=symbols.get(i + 3).toString();
                        throw new TypeException("While loop expected bool condition but received "+newvar.substring(newvar.indexOf("'")+1,newvar.length()));
                    }

                }

                //CHECKS CALC STATEMENTS
                if(now.indexOf("calc")!=-1) {
                    bcorrect=false;
                    if (symbols.get(i + 2).toString().indexOf("NUMEXPR") != -1 && symbols.get(i + 4).toString().indexOf("NUMEXPR") != -1) {
                        bcorrect = true;
                    }
                    if(symbols.get(i + 2).toString().indexOf("sub")!=-1 || symbols.get(i + 2).toString().indexOf("add")!=-1|| symbols.get(i +2).toString().indexOf("mult")!=-1){
                        bnested=checkCalc(symbols,i+2);
                        bcorrect=bnested;
                        if(bnested==false){

                            throw new TypeException("Expected (numexpr,numexpr) but mistake found in "+symbols.get(i + 2).toString());
                        }
                    }
                    bnested=false;
                    if(symbols.get(i + 4).toString().indexOf("sub")!=-1 || symbols.get(i + 4).toString().indexOf("add")!=-1|| symbols.get(i +4).toString().indexOf("mult")!=-1){
                        bnested=checkCalc(symbols,i+4);
                        bcorrect=bnested;
                        if(bnested==false){

                            throw new TypeException("Expected (numexpr,numexpr) but mistake found in "+symbols.get(i + 4).toString());
                        }
                    }

                    if (!bcorrect) {
                        newvar=symbols.get(i + 2).toString();

                        newvar2=symbols.get(i + 4).toString();
                        //System.out.println(newvar2);
                        throw new TypeException("Expected (numexpr,numexpr) received: ("+newvar+","+newvar2+")");
                    }
                }

                //CHECKS FOR LOOPS
                if(now.indexOf("loop 'for'")!=-1) {
                    bcorrect=false;
                    if (symbols.get(i + 2).toString().indexOf("N") != -1 && symbols.get(i + 4).toString().indexOf("N") != -1){
                        if (symbols.get(i + 6).toString().indexOf("N") != -1 && symbols.get(i + 8).toString().indexOf("N") != -1){
                            if (symbols.get(i + 10).toString().indexOf("N") != -1){
                                bcorrect=true;
                            }
                        }
                    }
                    if (!bcorrect) {
                        newvar=symbols.get(i + 2).toString();
                        newvar=newvar.substring(newvar.indexOf("'"),newvar.length());
                        newvar2=symbols.get(i + 4).toString();
                        newvar2=newvar2.substring(newvar2.indexOf("'"),newvar2.length());
                        newvar3=symbols.get(i + 6).toString();
                        newvar3=newvar3.substring(newvar3.indexOf("'"),newvar3.length());
                        newvar4=symbols.get(i + 8).toString();
                        newvar4=newvar4.substring(newvar4.indexOf("'"),newvar4.length());
                        newvar5=symbols.get(i + 10).toString();
                        newvar5=newvar5.substring(newvar5.indexOf("'"),newvar5.length());
                        throw new TypeException("For loop syntax expected is: for(numexpr,numxpr>numxpr;add(numxpr,numxpr) received: for("+newvar+"=0;"+newvar2+"<||>"+newvar3+";add("+newvar4+","+newvar5+")"+")");

                    }

                }

                //CHECKS PROCS
                if(now.indexOf("CALL")!=-1) {
                    bcorrect=false;
                    if (symbols.get(i + 1).toString().indexOf("T:proc")!=-1){
                        bcorrect=true;
                    }

                    if (!bcorrect) {
                        newvar=symbols.get(i + 1).toString();
                        newvar=newvar.substring(newvar.indexOf("'"),newvar.length());
                        throw new TypeException("Incorrect proc call: "+newvar+" is not a valid proc");
                    }
                }




        }

    }

    //Check if the symbol is in the variable list
    public static boolean Contains(String iSearch,Vector<Symbol> Nums,Vector<Symbol> Bools,Vector<Symbol> Strings){
        boolean bfound=false;
        String sNum;
        int position;
        //check nums
        for(int i=0;i<Nums.size();i++){
            sNum=Nums.get(i).toString();
            position=sNum.indexOf("T:N");

            if(position!=-1){
                if(iSearch.indexOf(sNum.substring(5,position))!=-1){
                    bfound= true;
                }
            }

        }

        //check bools
        for(int i=0;i<Bools.size();i++){
            sNum=Bools.get(i).toString();
            position=sNum.indexOf("T:B");
            if(position!=-1){
                if(iSearch.indexOf(sNum.substring(5,position))!=-1){
                    bfound= true;
                }
            }

        }

        //check Strings
        for(int i=0;i<Strings.size();i++){
            sNum=Strings.get(i).toString();
            position=sNum.indexOf("T:S");
            if(position!=-1){
                if(iSearch.indexOf(sNum.substring(5,position))!=-1){
                    bfound= true;
                }
            }
        }

        return bfound;
    }

    public static boolean checkCalc( Vector<Symbol> symbols,int i) throws TypeException {
        boolean bcor=false;
        boolean bnested=false;

        if (symbols.get(i + 2).toString().indexOf("NUMEXPR") != -1 && symbols.get(i + 4).toString().indexOf("NUMEXPR") != -1) {

            return true;
        }
        if(symbols.get(i + 2).toString().indexOf("sub")!=-1 || symbols.get(i + 2).toString().indexOf("add")!=-1|| symbols.get(i +2).toString().indexOf("mult")!=-1){
            bnested=checkCalc(symbols,i+2);

            if(bnested==false){
                throw new TypeException("Expected (numexpr,numexpr) but mistake found in "+symbols.get(i + 2).toString());
            }
        }
        bnested=false;
        if(symbols.get(i + 4).toString().indexOf("sub")!=-1 || symbols.get(i + 4).toString().indexOf("add")!=-1|| symbols.get(i +4).toString().indexOf("mult")!=-1){

            bnested=checkCalc(symbols,i+4);

            if(bnested==false){
                throw new TypeException("Expected (numexpr,numexpr) but mistake found in "+symbols.get(i + 4).toString());
            }
        }

        return false;
    };


    //This Can Be Removed as Cache Class is used to rewrite file
    //Write types to symbol file
   /* public static void RewriteFile(String SymFilename,String Current,String Add) throws IOException{
        System.out.println("Filename "+SymFilename);
        String filePath ="output/"+SymFilename+".sym";

        Scanner sc = new Scanner(new File(filePath));

        StringBuffer buffer = new StringBuffer();
        while (sc.hasNextLine()) {
            buffer.append(sc.nextLine()+System.lineSeparator());
        }
        String fileContents = buffer.toString();
        System.out.println("Contents of the file: "+fileContents);
        sc.close();

        String oldLine = Current;
        String newLine = Current+" "+Add;

        fileContents = fileContents.replaceAll(oldLine, newLine);

        FileWriter writer = new FileWriter(filePath);
        System.out.println("");
        System.out.println("new data: "+fileContents);
        writer.append(fileContents);
        writer.flush();
    }*/
};
