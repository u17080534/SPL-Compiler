import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.io.StringReader;

public class LexarRead
{
  public static char ch;
  public static boolean alreadyExecuted = false;
  public static void main(String[] args)throws Exception
  {
    File f = new File(args[0]);
    BufferedReader br = new BufferedReader(new FileReader(f));
    BufferedReader r;
    BufferedReader ahead;
    int lineNum = 0;
    int columnNum = 0;


    int state = 26; //start state
    List<String> tokens = new ArrayList<String>();
    String strOfWords = "";


    try {
      String line;

      while ((line = br.readLine()) != null) {
        // process the line
        lineNum++;
        r = new BufferedReader(new StringReader(line));
        ahead = new BufferedReader(new StringReader(line));


        bigWhile:
        while(checkEOF(r) != -1){
          checkEOF(ahead);
          smallWhile:
          while(true){

            if(state == 26){  //start state
              if(ch == '"'){
                strOfWords += ch;
                state = 1;
                continue bigWhile;
              }
              else if(ch == '>' || ch == '<' || ch == ')' || ch == '(' || ch == '}' || ch == '{' || ch == '=' || ch == ',' || ch == ';'){
                strOfWords += ch;
                state = 65;
              }
              else if(ch >= '1' && ch <= '9'){
                state = 24;
              }
              else if(ch == '-'){
                strOfWords += ch;
                state = 25;
                continue bigWhile;
              }
              else if(ch == 'a'){
                strOfWords += ch;
                state = 45;
                continue bigWhile;
              }
              else if(ch == 'b'){
                strOfWords += ch;
                state = 42;
                continue bigWhile;
              }
              else if(ch == 'e'){
                strOfWords += ch;
                state = 39;
                continue bigWhile;
              }
              else if(ch == 'T' || ch == 'F'){
                strOfWords += ch;
                state = 32;
                continue bigWhile;
              }
              else if(ch == 'f'){
                strOfWords += ch;
                state = 37;
                continue bigWhile;
              }
              else if(ch == 'i'){
                strOfWords += ch;
                state = 29;
                continue bigWhile;
              }
              else if(ch == 'h'){
                strOfWords += ch;
                state = 30;
                continue bigWhile;
              }
              else if(ch == 'm'){
                strOfWords += ch;
                state = 27;
                continue bigWhile;
              }
              else if(ch == 'n'){
                strOfWords += ch;
                state = 35;
                continue bigWhile;
              }
              else if(ch == 'o'){
                strOfWords += ch;
                state = 47;
                continue bigWhile;
              }
              else if(ch == 'p'){
                strOfWords += ch;
                state = 49;
                continue bigWhile;
              }
              else if(ch == 's'){
                strOfWords += ch;
                state = 52;
                continue bigWhile;
              }
              else if(ch == 't'){
                strOfWords += ch;
                state = 58;
                continue bigWhile;
              }
              else if(ch == 'w'){
                strOfWords += ch;
                state = 61;
                continue bigWhile;
              }
              else if(ch == '0'){
                state = 0;
              }
              else if(ch == ' ' || ch == '#'){
                continue bigWhile;
              }
              else{
                System.out.println("Rejected");
              }
            }









              if(state == 1){
                if((ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'z') || (ch == ' ')){
                  strOfWords += ch;
                  state = 3;
                  continue bigWhile;
                }
                else if(ch == '"'){
                  state = 2;
                  strOfWords += ch;
                }
                else{
                  System.out.println("String rejected");
                  System.exit(0);
                }
              }
              if(state == 3){
                if((ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'z') || (ch == ' ')){
                  strOfWords += ch;
                  state = 5;
                  continue bigWhile;
                }
                else if(ch == '"'){
                  strOfWords += ch;
                  state = 2;
                }
                else{
                  System.out.println("String rejected");
                  System.exit(0);
                }
              }
              if(state == 5){
                if((ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'z') || (ch == ' ')){
                  strOfWords += ch;
                  state = 7;
                  continue bigWhile;
                }
                else if(ch == '"'){
                  strOfWords += ch;
                  state = 2;
                }
                else{
                  System.out.println("String rejected");
                  System.exit(0);
                }
              }
              if(state == 7){
                if((ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'z') || (ch == ' ')){
                  state = 9;
                  strOfWords += ch;
                  continue bigWhile;
                }
                else if(ch == '"'){
                  strOfWords += ch;
                  state = 2;
                }
                else{
                  System.out.println("String rejected");
                  System.exit(0);
                }
              }
              if(state == 9){
                if((ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'z') || (ch == ' ')){
                  strOfWords += ch;
                  state = 11;
                  continue bigWhile;
                }
                else if(ch == '"'){
                  strOfWords += ch;
                  state = 2;
                }
                else{
                  System.out.println("String rejected");
                  System.exit(0);
                }
              }
              if(state == 11){
                if((ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'z') || (ch == ' ')){
                  strOfWords += ch;
                  state = 13;
                  continue bigWhile;
                }
                else if(ch == '"'){
                  strOfWords += ch;
                  state = 2;
                }
                else{
                  System.out.println("String rejected");
                  System.exit(0);
                }
              }
              if(state == 13){
                if((ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'z') || (ch == ' ')){
                  strOfWords += ch;
                  state = 15;
                  continue bigWhile;
                }
                else if(ch == '"'){
                  strOfWords += ch;
                  state = 2;
                }
                else{
                  System.out.println("String rejected");
                  System.exit(0);
                }
              }
              if(state == 15){
                if((ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'z') || (ch == ' ')){
                  strOfWords += ch;
                  state = 17;
                  continue bigWhile;
                }
                else if(ch == '"'){
                  strOfWords += ch;
                  state = 2;
                }
                else{
                  System.out.println("String rejected");
                  System.exit(0);
                }
              }
              if(state == 17){
                if(ch == '"'){
                  strOfWords += ch;
                  state = 2;
                }
                else{
                  System.out.println("String rejected");
                  System.exit(0);
                }
              }

              if(state == 25){
                if(ch >= '1' && ch <= '9'){
                  state = 24;
                }
                else{
                  System.out.println("Integer rejected");
                  System.exit(0);
                }
              }


              if(state == 45){
                if(ch == 'd' || ch == 'n'){
                  strOfWords += ch;
                  state = 46;
                  continue bigWhile;
                }
                else{
                  state = 20;
                  continue smallWhile;
                }
              }
              if(state == 46){
                if(ch == 'd'){
                  strOfWords += ch;
                  state = 32;
                }
                else{
                  state = 20;
                  continue smallWhile;
                }
              }

              if(state == 42){
                if(ch == 'o'){
                  strOfWords += ch;
                  state = 43;
                  continue bigWhile;
                }
                else{
                  state = 20;
                  continue smallWhile;
                }
              }

              if(state == 43){
                if(ch == 'o'){
                  strOfWords += ch;
                  state = 44;
                  continue bigWhile;
                }
                else{
                  state = 20;
                  continue smallWhile;
                }
              }

              if(state == 44){
                if(ch == 'l'){
                  strOfWords += ch;
                  state = 32;
                }
                else{
                  state = 20;
                  continue smallWhile;
                }
              }

              if(state == 39){
                if(ch == 'q'){
                  strOfWords += ch;
                  state = 32;
                }
                else if(ch == 'l'){
                  strOfWords += ch;
                  state = 40;
                  continue bigWhile;
                }
                else{
                  state = 20;
                  continue smallWhile;
                }
              }

              if(state == 40){
                if(ch == 's'){
                  strOfWords += ch;
                  state = 41;
                  continue bigWhile;
                }
                else{
                  state = 20;
                  continue smallWhile;
                }
              }

              if(state == 41){      //connection
                if(ch == 'e'){
                  strOfWords += ch;
                  state = 32;
                }
                else{
                  state = 20;
                  continue smallWhile;
                }
              }
              if(state == 37){
                if(ch == 'o'){
                  strOfWords += ch;
                  state = 38;
                  continue bigWhile;
                }
                else{
                  state = 20;
                  continue smallWhile;
                }
              }

              if(state == 38){
                if(ch == 'r'){
                  strOfWords += ch;
                  state = 32;
                }
                else{
                  state = 20;
                  continue smallWhile;
                }
              }

              if(state == 29){
                if(ch == 'n'){
                  strOfWords += ch;
                  state = 33;
                  continue bigWhile;
                }
                else{
                  state = 20;
                  continue smallWhile;
                }
              }

              if(state == 33){      //connection
                if(ch == 'p'){
                  strOfWords += ch;
                  state = 34;
                  continue bigWhile;
                }
                else{
                  state = 20;
                  continue smallWhile;
                }
              }

              if(state == 34){
                if(ch == 'u'){
                  strOfWords += ch;
                  state = 31;
                  continue bigWhile;
                }
                else{
                  state = 20;
                  continue smallWhile;
                }
              }

              if(state == 31){        //connection
                if(ch == 't'){
                  strOfWords += ch;
                  state = 32;
                }
                else{
                  state = 20;
                  continue smallWhile;
                }
              }

              if(state == 30){
                if(ch == 'a'){
                  strOfWords += ch;
                  state = 28;
                  continue bigWhile;
                }
                else{
                  state = 20;
                  continue smallWhile;
                }
              }

              if(state == 28){    //connection
                if(ch == 'l'){
                  strOfWords += ch;
                  state = 31;
                  continue bigWhile;
                }
                else{
                  state = 20;
                  continue smallWhile;
                }
              }

              if(state == 27){
                if(ch == 'u'){
                  strOfWords += ch;
                  state = 28;
                  continue bigWhile;
                }
                else{
                  state = 20;
                  continue smallWhile;
                }
              }

              if(state == 35){
                if(ch == 'o'){
                  strOfWords += ch;
                  state = 31;
                  continue bigWhile;
                }
                else if(ch == 'u'){
                  strOfWords += ch;
                  state = 36;
                  continue bigWhile;
                }
                else{
                  state = 20;
                  continue smallWhile;
                }
              }

              if(state == 36){
                if(ch == 'm'){
                  strOfWords += ch;
                  state = 32;
                }
                else{
                  state = 20;
                  continue smallWhile;
                }
              }

              if(state == 47){
                if(ch == 'u'){
                  strOfWords += ch;
                  state = 48;
                  continue bigWhile;
                }
                else if(ch == 'r'){
                  strOfWords += ch;
                  state = 32;
                }
                else{
                  state = 20;
                  continue smallWhile;
                }
              }

              if(state == 48){
                if(ch == 't'){
                  strOfWords += ch;
                  state = 33;
                  continue bigWhile;
                }
                else{
                  state = 20;
                  continue smallWhile;
                }
              }

              if(state == 49){
                if(ch == 'r'){
                  strOfWords += ch;
                  state = 50;
                  continue bigWhile;
                }
                else{
                  state = 20;
                  continue smallWhile;
                }
              }

              if(state == 50){
                if(ch == 'o'){
                  strOfWords += ch;
                  state = 51;
                  continue bigWhile;
                }
                else{
                  state = 20;
                  continue smallWhile;
                }
              }

              if(state == 51){
                if(ch == 'c'){
                  strOfWords += ch;
                  state = 32;
                }
                else{
                  state = 20;
                  continue smallWhile;
                }
              }

              if(state == 52){
                if(ch == 't'){
                  strOfWords += ch;
                  state = 53;
                  continue bigWhile;
                }
                else if(ch == 'u'){
                  strOfWords += ch;
                  state = 57;
                  continue bigWhile;
                }
                else{
                  state = 20;
                  continue smallWhile;
                }
              }

              if(state == 53){
                if(ch == 'r'){
                  strOfWords += ch;
                  state = 54;
                  continue bigWhile;
                }
                else{
                  state = 20;
                  continue smallWhile;
                }
              }

              if(state == 54){
                if(ch == 'i'){
                  strOfWords += ch;
                  state = 55;
                  continue bigWhile;
                }
                else{
                  state = 20;
                  continue smallWhile;
                }
              }

              if(state == 55){
                if(ch == 'n'){
                  strOfWords += ch;
                  state = 56;
                  continue bigWhile;
                }
                else{
                  state = 20;
                  continue smallWhile;
                }
              }

              if(state == 56){
                if(ch == 'g'){
                  strOfWords += ch;
                  state = 32;
                }
                else{
                  state = 20;
                  continue smallWhile;
                }
              }

              if(state == 57){
                if(ch == 'b'){
                  strOfWords += ch;
                  state = 32;
                }
                else{
                  state = 20;
                  continue smallWhile;
                }
              }

              if(state == 58){
                if(ch == 'h'){
                  strOfWords += ch;
                  state = 59;
                  continue bigWhile;
                }
                else{
                  state = 20;
                  continue smallWhile;
                }
              }

              if(state == 59){
                if(ch == 'e'){
                  strOfWords += ch;
                  state = 60;
                  continue bigWhile;
                }
                else{
                  state = 20;
                  continue smallWhile;
                }
              }

              if(state == 60){
                if(ch == 'n'){
                  strOfWords += ch;
                  state = 32;
                }
                else{
                  state = 20;
                  continue smallWhile;
                }
              }

              if(state == 61){
                if(ch == 'h'){
                  strOfWords += ch;
                  state = 62;
                  continue bigWhile;
                }
                else{
                  state = 20;
                  continue smallWhile;
                }
              }

              if(state == 62){
                if(ch == 'i'){
                  strOfWords += ch;
                  state = 63;
                  continue bigWhile;
                }
                else{
                  state = 20;
                  continue smallWhile;
                }
              }

              if(state == 63){
                if(ch == 'l'){
                  strOfWords += ch;
                  state = 41;
                  continue bigWhile;
                }
                else{
                  state = 20;
                  continue smallWhile;
                }
              }












              if(state == 65){ // ***ACCEPT STATE -> create token and send back to start state
                System.out.println("Operator accepted " + strOfWords);

                strOfWords = "";
                state = 26;
                continue bigWhile;
              }

              if(state == 2){ // ***ACCEPT STATE -> create token and send back to start state
                System.out.println("String accepted " + strOfWords + "  " + lineNum);
                tokens.add(strOfWords + " (tok_str)");
                strOfWords = "";
                state = 26;
                continue bigWhile;
              }

              if(state == 24){ // ***ACCEPT STATE -> check if the end of the integer and then create token and send back to start
                if(ch >= '0' && ch <= '9'){
                  strOfWords += ch;
                  state = 24;
                  if(lookAhead(ahead)){
                    System.out.println("Integer accepted "  + strOfWords  + "  " + lineNum);
                    tokens.add(strOfWords + " (tok_int)");
                    strOfWords = "";
                    state = 26;
                    continue bigWhile;
                  }
                  continue bigWhile;
                }
                else if(ch == '>' || ch == '<' || ch == ' ' || ch == '#' || ch == ')' || ch == '(' || ch == '}' || ch == '{' || ch == '=' || ch == ',' || ch == ';'){
                  System.out.println("Integer accepted "  + strOfWords);
                  tokens.add(strOfWords + " (tok_int)");
                  strOfWords = "";
                  state = 26;
                  continue smallWhile;
                }
                else{
                  System.out.println("Digit rejected " + ch);
                  System.exit(0);
                }
              }

              if(state == 0){ // ***ACCEPT STATE -> check if the end of the integer and then create token and send back to start
                if(ch == '0'){
                  strOfWords += ch;
                  state = 0;
                  if(lookAhead(ahead)){
                    System.out.println("Integer accepted "  + strOfWords  + "  " + lineNum);
                    tokens.add(strOfWords + " (tok_int)");
                    strOfWords = "";
                    state = 26;
                    continue bigWhile;
                  }
                  continue bigWhile;
                }
                else if(ch == '>' || ch == '<' || ch == ' ' || ch == '#' || ch == ')' || ch == '(' || ch == '}' || ch == '{' || ch == '=' || ch == ',' || ch == ';'){
                  System.out.println("Integer accepted "  + strOfWords);
                  tokens.add(strOfWords + " (tok_int)");
                  strOfWords = "";
                  state = 26;
                  continue smallWhile;
                }
                else{
                  System.out.println("Digit rejected " + ch);
                  System.exit(0);
                }
              }

              if(state == 32){  //***ACCEPT STATE -> check if end of file, check if operator next and check if more letters (possible identifier)

                if(lookAhead(ahead)){
                  System.out.println("Keyword accepted "  + strOfWords); //end of file or line
                  tokens.add(strOfWords + " (tok_" + strOfWords + ")");
                  strOfWords = "";
                  state = 26;
                  continue bigWhile;
                }

                if(ch == '>' || ch == '<' || ch == ' ' || ch == '#' || ch == ')' || ch == '(' || ch == '}' || ch == '{' || ch == '=' || ch == ',' || ch == ';'){
                  state = 26;
                  System.out.println("Keyword accepted " + strOfWords);
                  tokens.add(strOfWords + " (tok_" + strOfWords + ")");
                  strOfWords = "";
                  continue smallWhile;
                }
                else if((ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'z')){
                  state = 20;
                  continue smallWhile;
                }
              }

              if(state == 20){ //***ACCEPT STATE -> chech if identidier
                if((ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'z')){
                  strOfWords += ch;
                  if(lookAhead(ahead)){
                    System.out.println("identifier accepted "  + strOfWords); //end of file or line
                    tokens.add(strOfWords + " (tok_identifier)");
                    strOfWords = "";
                  }

                  state = 20;
                  continue bigWhile;
                }
                else if(ch == '>' || ch == '<' || ch == ' ' || ch == '#' || ch == ')' || ch == '(' || ch == '}' || ch == '{' || ch == '=' || ch == ',' || ch == ';'){
                  state = 26;
                  System.out.println("identifier accepted " + strOfWords);
                  tokens.add(strOfWords + " (tok_identifier)");
                  strOfWords = "";
                  continue smallWhile;
                }
                else{
                  System.out.println("identifier rejected");
                  System.exit(0);
                }
              }



            }//smallWhile
          }//bigWhile
          System.out.println("--");
      }//lineWhile
    } finally {
      br.close();

      for(int i = 0; i < tokens.size(); i++){
        System.out.println(tokens.get(i));
      }
    }
  }

  public static int checkEOF(BufferedReader br)throws Exception{
    int num = br.read();
    ch = (char)num;
    return num;
  }

  public static boolean lookAhead(BufferedReader br)throws Exception{
    br.mark(20);
    if(br.read() == -1){
      br.reset();
      return true;
    }
    else{
      br.reset();
      return false;
    }
  }

}
