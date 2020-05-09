package analysis;

import java.awt.datatransfer.SystemFlavorMap;
import java.util.*;

import exception.EmptyStreamException;
import exception.ValueException;
import syntax.*;
import symtable.*;
import exception.UsageException;
import syntax.expression.Expression;

public class variableValueCheck
{
	
	//check first 3 in spec
	//display final variable value table


		//0 - no value
		//1 - value
		//2 - if pairing
		//4 - maybe a value from if statement
		//3 - maybe a value from while loop
		//5 - maybe a value from a for loop



	private static Map<String,Integer> variableMap = new HashMap<String, Integer>();
	private static Map<String,Integer> assignmentInScope = new HashMap<String, Integer>();
	private static Map<Symbol,String> needsValueMessage = new HashMap<Symbol, String>();

	private static Vector<Symbol> PROG1symbols = new Vector<>();
	private static Vector<Symbol> PROG2symbols = new Vector<>();
	private static Vector<Symbol> WHILEsymbols = new Vector<>();
	private static Vector<Symbol> FORsymbols = new Vector<>();

	private static Vector<Symbol> needsValue = new Vector<>();
	private static Vector<Symbol> warnings = new Vector<>();
	private static Vector<Symbol> declarationWarnings = new Vector<>();

	public static void check(SymbolTable table) throws  ValueException {


		int firstIfScope = -1;
		System.out.println("Variable value test: ");


		Vector<Symbol> symbols = table.list();

		for(int i = 0; i < symbols.size(); i++)  {


			variableMap.entrySet().forEach(entry->{

				if(entry.getValue() == 2){
					entry.setValue(4);
				}

			});






			if(symbols.get(i).getExpression().contains("variable '")){

				//if we haven't seen this variable before, initialize it with a 0.
				if(!variableMap.containsKey(symbols.get(i).getExpression())){
					variableMap.put(symbols.get(i).getExpression(),0);
				}

				if(symbols.get(i-2).getExpression().contains("ASSIGN")){			// =
					variableMap.put(symbols.get(i).getExpression(),1);
					assignmentInScope.put(symbols.get(i).getExpression(), symbols.get(i).getScope());
				}
				else if(symbols.get(i-2).getExpression().contains("input")){   		// input()
					variableMap.put(symbols.get(i).getExpression(),1);
					assignmentInScope.put(symbols.get(i).getExpression(), symbols.get(i).getScope());
				}
				else if(symbols.get(i-3).getExpression().contains("TYPE")){			//type
					variableMap.put(symbols.get(i).getExpression(),0);
				}
				else{
					if(variableMap.get(symbols.get(i).getExpression()) == 0){

						needsValue.add(symbols.get(i));

						if(symbols.get(i-2).getExpression().contains("io 'output'")){
							needsValueMessage.put(symbols.get(i), "needs a value to be outputted to the screen");
						}
						else if(symbols.get(i-2).getExpression().contains("assign '")){
						    needsValueMessage.put(symbols.get(i), "needs a value when being assigned to something else");
						}
						else if(symbols.get(i-3).getExpression().contains("tok_if")){
							needsValueMessage.put(symbols.get(i), "cannot be undefined in if statement condition");
						}
						else if(symbols.get(i-3).getExpression().contains("loop 'while'")){
							needsValueMessage.put(symbols.get(i), "cannot be undefined in while statement condition");
						}

					}
				}

			}
			else if(symbols.get(i).getExpression().equals("COND_BRANCH")){        //if statements. (1 depth)

				//check variable in if:

				if(symbols.get(i+4).getExpression().contains("variable '")){
					if(variableMap.get(symbols.get(i+4).getExpression()) == 0){
						needsValue.add(symbols.get(i+4));
						needsValueMessage.put(symbols.get(i+4), "cannot be undefined in if statement condition");
					}
				}
				

				//start rest of if:


				if(symbols.get(i).getActualExpression().getActualDescendents().get(1).getActualDescendents().get(0).getExpr().contains("bool 'T'")){
					//System.out.println("Do the if part only");

					continue;

				}
				else if(symbols.get(i).getActualExpression().getActualDescendents().get(1).getActualDescendents().get(0).getExpr().contains("bool 'F'")){
					//System.out.println("Do the else part only");

					Expression PROG1 = null;
					for(int p = 0; p < symbols.get(i).getActualExpression().getActualDescendents().size(); p++){
						if(symbols.get(i).getActualExpression().getActualDescendents().get(p).getExpr().contains("PROG")){
							PROG1 = symbols.get(i).getActualExpression().getActualDescendents().get(p);
							break;
						}
					}

					if(PROG1 != null){ // skip PROG1 (first part of if statement)
						PROG1symbols.clear();
						recursivePROG1(PROG1.getActualDescendents());
						i = PROG1symbols.get(PROG1symbols.size() - 1).getID() + 1;

					}
					continue;
				}
				else{
					//System.out.println("Do both parts");
					Expression PROG1 = null;
					Expression PROG2 = null;
					for(int p = 0; p < symbols.get(i).getActualExpression().getActualDescendents().size(); p++){
						if(symbols.get(i).getActualExpression().getActualDescendents().get(p).getExpr().contains("PROG")){
							PROG1 = symbols.get(i).getActualExpression().getActualDescendents().get(p);
						}
						if (symbols.get(i).getActualExpression().getActualDescendents().get(p).getExpr().contains("COND_BRANCH_")) {
							PROG2 = symbols.get(i).getActualExpression().getActualDescendents().get(3).getActualDescendents().get(0);
							break;
						}
					}
					//now we possibly have PROG1 and possibly PROG2
					if(PROG1 != null){

						PROG1symbols.clear();

						recursivePROG1(PROG1.getActualDescendents());

						firstIfScope = PROG1symbols.get(0).getScope();

						for(int s = 0; s < PROG1symbols.size(); s++){
							//DEAL WITH VARIABLES


							
							if(PROG1symbols.get(s).getExpression().contains("variable '")){


								
								//if we haven't seen this variable before, initialize it with a 0.
								if(!variableMap.containsKey(PROG1symbols.get(s).getExpression())){
									variableMap.put(PROG1symbols.get(s).getExpression(),0);
								}


								if(PROG1symbols.get(s-2).getExpression().contains("ASSIGN")){			// =

									if(variableMap.get(PROG1symbols.get(s).getExpression()) != 1){
										if(PROG1symbols.get(s).getScope() == firstIfScope){
											variableMap.put(PROG1symbols.get(s).getExpression(),2);
											assignmentInScope.put(PROG1symbols.get(s).getExpression(), PROG1symbols.get(s).getScope());
											warnings.add(PROG1symbols.get(s));
										}
										else{
											variableMap.put(PROG1symbols.get(s).getExpression(),4);
											assignmentInScope.put(PROG1symbols.get(s).getExpression(), PROG1symbols.get(s).getScope());
											warnings.add(PROG1symbols.get(s));
										}

									}

								}
								else if(PROG1symbols.get(s-2).getExpression().contains("input")){   		// input()

									if(variableMap.get(PROG1symbols.get(s).getExpression()) != 1){
										if(PROG1symbols.get(s).getScope() == firstIfScope){
											variableMap.put(PROG1symbols.get(s).getExpression(),2);
											assignmentInScope.put(PROG1symbols.get(s).getExpression(), PROG1symbols.get(s).getScope());
											warnings.add(PROG1symbols.get(s));
										}
										else{
											variableMap.put(PROG1symbols.get(s).getExpression(),4);
											assignmentInScope.put(PROG1symbols.get(s).getExpression(), PROG1symbols.get(s).getScope());
											warnings.add(PROG1symbols.get(s));
										}

									}

								}
								else if(PROG1symbols.get(s-3).getExpression().contains("TYPE")){			//type
									variableMap.put(PROG1symbols.get(s).getExpression(),0);
								}
								else{
									if(variableMap.get(PROG1symbols.get(s).getExpression()) == 0){

										needsValue.add(PROG1symbols.get(s));

										if(PROG1symbols.get(s-2).getExpression().contains("io 'output'")){
											needsValueMessage.put(PROG1symbols.get(s), "needs a value to be outputted to the screen");
										}
										else if(PROG1symbols.get(s-2).getExpression().contains("assign '")){
											needsValueMessage.put(PROG1symbols.get(s), "needs a value when being assigned to something else");
										}
										else if(PROG1symbols.get(s-3).getExpression().contains("tok_if")){
											needsValueMessage.put(PROG1symbols.get(s), "cannot be undefined in if statement condition");
										}
										else if(PROG1symbols.get(s-3).getExpression().contains("loop 'while'")){
											needsValueMessage.put(PROG1symbols.get(s), "cannot be undefined in while statement condition");
										}

									}
								}

							}
							else if(PROG1symbols.get(s).getExpression().contains("loop 'for")){		//for loop in branches
								if(PROG1symbols.get(s+2).getExpression().contains("variable")){
									variableMap.put(PROG1symbols.get(s+2).getExpression(),1);
								}
							}
						}


						i = PROG1symbols.get(PROG1symbols.size()-1).getID();
					}
					if(PROG2 != null){
						PROG2symbols.clear();
						recursivePROG2(PROG2.getActualDescendents());

						for(int s = 0; s < PROG2symbols.size(); s++){
							//DEAL WITH VARIABLES
							if(PROG2symbols.get(s).getExpression().contains("variable '")){

								//if we haven't seen this variable before, initialize it with a 0.
								if(!variableMap.containsKey(PROG2symbols.get(s).getExpression())){
									variableMap.put(PROG2symbols.get(s).getExpression(),0);
								}

								if(PROG2symbols.get(s-2).getExpression().contains("ASSIGN")){			// =


									if(PROG2symbols.get(s).getScope() == firstIfScope){
										if(variableMap.get(PROG2symbols.get(s).getExpression()) == 2){
											if(assignmentInScope.get(PROG2symbols.get(s).getExpression()) == PROG2symbols.get(s).getScope()){
												//if the variable was previously assigned a 2 in the same scope first if scope
												variableMap.put(PROG2symbols.get(s).getExpression(),1);
												assignmentInScope.put(PROG2symbols.get(s).getExpression(), PROG2symbols.get(s).getScope());

												//remove from warnings vector
												for(int w = 0; w < warnings.size(); w++){
													if (warnings.get(w).getExpression().equals(PROG2symbols.get(s).getExpression())){
														warnings.removeElement(warnings.get(w));
													}
												}
											}
										}
										else{
											if(variableMap.get(PROG2symbols.get(s).getExpression()) != 1){

												variableMap.put(PROG2symbols.get(s).getExpression(),4);
												assignmentInScope.put(PROG2symbols.get(s).getExpression(), PROG2symbols.get(s).getScope());
												warnings.add(PROG2symbols.get(s));

											}
										}
									}else{
										if(variableMap.get(PROG2symbols.get(s).getExpression()) != 1){

											variableMap.put(PROG2symbols.get(s).getExpression(),4);
											assignmentInScope.put(PROG2symbols.get(s).getExpression(), PROG2symbols.get(s).getScope());
											warnings.add(PROG2symbols.get(s));

										}
									}

								}
								else if(PROG2symbols.get(s-2).getExpression().contains("input")){   		// input()

									if(PROG2symbols.get(s).getScope() == firstIfScope){
										if(variableMap.get(PROG2symbols.get(s).getExpression()) == 2){
											if(assignmentInScope.get(PROG2symbols.get(s).getExpression()) == PROG2symbols.get(s).getScope()){
												//if the variable was previously assigned a 2 in the same scope first if scope
												variableMap.put(PROG2symbols.get(s).getExpression(),1);
												assignmentInScope.put(PROG2symbols.get(s).getExpression(), PROG2symbols.get(s).getScope());

												//remove from warnings vector
												for(int w = 0; w < warnings.size(); w++){
													if (warnings.get(w).getExpression().equals(PROG2symbols.get(s).getExpression())){
														warnings.removeElement(warnings.get(w));
													}
												}

											}
										}
										else{
											if(variableMap.get(PROG2symbols.get(s).getExpression()) != 1){

												variableMap.put(PROG2symbols.get(s).getExpression(),4);
												assignmentInScope.put(PROG2symbols.get(s).getExpression(), PROG2symbols.get(s).getScope());
												warnings.add(PROG2symbols.get(s));

											}
										}
									}else{
										if(variableMap.get(PROG2symbols.get(s).getExpression()) != 1){

											variableMap.put(PROG2symbols.get(s).getExpression(),4);
											assignmentInScope.put(PROG2symbols.get(s).getExpression(), PROG2symbols.get(s).getScope());
											warnings.add(PROG2symbols.get(s));

										}
									}

								}
								else if(PROG2symbols.get(s-3).getExpression().contains("TYPE")){			//type
									variableMap.put(PROG2symbols.get(s).getExpression(),0);
								}
								else{
									if(variableMap.get(PROG2symbols.get(s).getExpression()) == 0){

										needsValue.add(PROG2symbols.get(s));

										if(PROG2symbols.get(s-2).getExpression().contains("io 'output'")){
											needsValueMessage.put(PROG2symbols.get(s), "needs a value to be outputted to the screen");
										}
										else if(PROG2symbols.get(s-2).getExpression().contains("assign '")){
											needsValueMessage.put(PROG2symbols.get(s), "needs a value when being assigned to something else");
										}
										else if(PROG2symbols.get(s-3).getExpression().contains("tok_if")){
											needsValueMessage.put(PROG2symbols.get(s), "cannot be undefined in if statement condition");
										}
										else if(PROG2symbols.get(s-3).getExpression().contains("loop 'while'")){
											needsValueMessage.put(PROG2symbols.get(s), "cannot be undefined in while statement condition");
										}

									}
								}

							}
							else if(PROG2symbols.get(s).getExpression().contains("loop 'for")){		//for loop in branches
								if(PROG2symbols.get(s+2).getExpression().contains("variable")){
									variableMap.put(PROG2symbols.get(s+2).getExpression(),1);
								}
							}

						}

						i = PROG2symbols.get(PROG2symbols.size()-1).getID();


					}

				}
			}
			else if(symbols.get(i).getExpression().equals("COND_BRANCH_")){
				Expression PROG2 = null;
				if(symbols.get(i).getActualExpression().getActualDescendents() != null){
					PROG2symbols.clear();
					recursivePROG2(symbols.get(i).getActualExpression().getActualDescendents());
					i = PROG2symbols.get(PROG2symbols.size()-1).getID();
				}
				continue;
			}
			else if(symbols.get(i).getExpression().contains("COND_LOOP") && symbols.get(i+1).getExpression().contains("loop 'while")){       //while statements.

				if(symbols.get(i+3).getExpression().contains("bool 'T")){
					continue;
				}
				else if(symbols.get(i+3).getExpression().contains("bool 'F")){
					//don't process while loop, get end of while and continue
					WHILEsymbols.clear();
					recursiveWhile(symbols.get(i).getActualExpression().getActualDescendents());
					i = WHILEsymbols.get(WHILEsymbols.size()-1).getID();
					continue;
				}
				else{
					WHILEsymbols.clear();
					recursiveWhile(symbols.get(i).getActualExpression().getActualDescendents());



					for(int s = 0; s < WHILEsymbols.size(); s++){

						if(WHILEsymbols.get(s).getExpression().contains("variable '")){

							//if we haven't seen this variable before, initialize it with a 0.
							if(!variableMap.containsKey(WHILEsymbols.get(s).getExpression())){
								variableMap.put(WHILEsymbols.get(s).getExpression(),0);
							}

							if(WHILEsymbols.get(s-2).getExpression().contains("ASSIGN")){			// =

								if(variableMap.get(WHILEsymbols.get(s).getExpression()) != 1){
									variableMap.put(WHILEsymbols.get(s).getExpression(),3);
									assignmentInScope.put(WHILEsymbols.get(s).getExpression(), WHILEsymbols.get(s).getScope());
									warnings.add(WHILEsymbols.get(s));
								}

							}
							else if(WHILEsymbols.get(s-2).getExpression().contains("input")){   		// input()

								if(variableMap.get(WHILEsymbols.get(s).getExpression()) != 1){
									variableMap.put(WHILEsymbols.get(s).getExpression(),3);
									assignmentInScope.put(WHILEsymbols.get(s).getExpression(), WHILEsymbols.get(s).getScope());
									warnings.add(WHILEsymbols.get(s));
								}

							}
							else if(WHILEsymbols.get(s-3).getExpression().contains("TYPE")){			//type
								variableMap.put(WHILEsymbols.get(s).getExpression(),0);
								declarationWarnings.add(WHILEsymbols.get(s));
							}
							else{
								if(variableMap.get(WHILEsymbols.get(s).getExpression()) == 0){

									needsValue.add(WHILEsymbols.get(s));

									if(WHILEsymbols.get(s-2).getExpression().contains("io 'output'")){
										needsValueMessage.put(WHILEsymbols.get(s), "needs a value to be outputted to the screen");
									}
									else if(WHILEsymbols.get(s-2).getExpression().contains("assign '")){
										needsValueMessage.put(WHILEsymbols.get(s), "needs a value when being assigned to something else");
									}
									else if(WHILEsymbols.get(s-3).getExpression().contains("tok_if")){
										needsValueMessage.put(WHILEsymbols.get(s), "cannot be undefined in if statement condition");
									}
									else if(WHILEsymbols.get(s-3).getExpression().contains("loop 'while'")){
										needsValueMessage.put(WHILEsymbols.get(s), "cannot be undefined in while statement condition");
									}

								}
							}
						}
						else if(WHILEsymbols.get(s).getExpression().contains("loop 'for")){		//for loop in branches
							if(WHILEsymbols.get(s+2).getExpression().contains("variable")){
								variableMap.put(WHILEsymbols.get(s+2).getExpression(),1);
							}
						}


					}

					i = WHILEsymbols.get(WHILEsymbols.size()-1).getID();
				}



			}
			else if(symbols.get(i).getExpression().contains("COND_LOOP") && symbols.get(i+1).getExpression().contains("loop 'for")){

				FORsymbols.clear();
				recursiveFor(symbols.get(i).getActualExpression().getActualDescendents());


				if(FORsymbols.get(2).getExpression().contains("variable")){ //assign value to this var
					variableMap.put(FORsymbols.get(2).getExpression(), 1);
					assignmentInScope.put(FORsymbols.get(2).getExpression(), FORsymbols.get(2).getScope());
				}

				if(FORsymbols.get(6).getExpression().contains("variable")){ //check if variable used in for has a value
					if(variableMap.get(FORsymbols.get(6).getExpression()) == 0){
						needsValue.add(FORsymbols.get(6));
						needsValueMessage.put(FORsymbols.get(6), " cannot be undefined in for loop");
					}
				}

				if(FORsymbols.size() > 7){
					for(int s = 7; s < FORsymbols.size(); s++){

						if(FORsymbols.get(s).getExpression().contains("variable '")){

							//if we haven't seen this variable before, initialize it with a 0.
							if(!variableMap.containsKey(FORsymbols.get(s).getExpression())){
								variableMap.put(FORsymbols.get(s).getExpression(),0);
							}

							if(FORsymbols.get(s-2).getExpression().contains("ASSIGN")){			// =

								if(variableMap.get(FORsymbols.get(s).getExpression()) != 1){
									variableMap.put(FORsymbols.get(s).getExpression(),5);
									assignmentInScope.put(FORsymbols.get(s).getExpression(), FORsymbols.get(s).getScope());
									warnings.add(FORsymbols.get(s));
								}

							}
							else if(FORsymbols.get(s-2).getExpression().contains("input")){   		// input()

								if(variableMap.get(FORsymbols.get(s).getExpression()) != 1){
									variableMap.put(FORsymbols.get(s).getExpression(),5);
									assignmentInScope.put(FORsymbols.get(s).getExpression(), FORsymbols.get(s).getScope());
									warnings.add(FORsymbols.get(s));
								}

							}
							else if(FORsymbols.get(s-3).getExpression().contains("TYPE")){			//type
								variableMap.put(FORsymbols.get(s).getExpression(),0);
								declarationWarnings.add(FORsymbols.get(s));
							}
							else{
								if(variableMap.get(FORsymbols.get(s).getExpression()) == 0){

									needsValue.add(FORsymbols.get(s));

									if(FORsymbols.get(s-2).getExpression().contains("io 'output'")){
										needsValueMessage.put(FORsymbols.get(s), "needs a value to be outputted to the screen");
									}
									else if(FORsymbols.get(s-2).getExpression().contains("assign '")){
										needsValueMessage.put(FORsymbols.get(s), "needs a value when being assigned to something else");
									}
									else if(FORsymbols.get(s-3).getExpression().contains("tok_if")){
										needsValueMessage.put(FORsymbols.get(s), "cannot be undefined in if statement condition");
									}
									else if(FORsymbols.get(s-3).getExpression().contains("loop 'while'")){
										needsValueMessage.put(FORsymbols.get(s), "cannot be undefined in while statement condition");
									}

								}
							}
						}
						else if(FORsymbols.get(s).getExpression().contains("loop 'for")){		//for loop in for loop
							if(FORsymbols.get(s+2).getExpression().contains("variable")){
								variableMap.put(FORsymbols.get(s+2).getExpression(),1);
							}
						}

					}
				}

				i = FORsymbols.get(FORsymbols.size()-1).getID();
			}

		}

		//done





		for (Symbol symbol : declarationWarnings) {
			System.out.println("WARNING: "  + symbol.getExpression() + " might not be declared at " + symbol.getLocation());

		}

		for (Symbol symbol : warnings) {
			System.out.println("WARNING: " + symbol.getExpression() + " might not be assigned a value at " + symbol.getLocation());

		}

		for (Symbol symbol : needsValue) {
			//throw new ValueException(symbol, "Variable needs value: ");

			// OR

			String msg = "undefined";
			if(needsValueMessage.containsKey(symbol)){
				msg = needsValueMessage.get(symbol);
			}
			System.out.println("VALUE ERROR: " + symbol.getExpression() + " " + msg + " at " + symbol.getLocation());
		}




	}

	private static void recursivePROG1(Vector<Expression> code){

		for (Expression expression : code) {

			PROG1symbols.add(expression.getSymbol());

			if (!expression.isTerminal()) {
				recursivePROG1(expression.getActualDescendents());
			}
		}
	}

	private static void recursivePROG2(Vector<Expression> code){

		for (Expression expression : code) {

			PROG2symbols.add(expression.getSymbol());

			if (!expression.isTerminal()) {
				recursivePROG2(expression.getActualDescendents());
			}
		}
	}


	private static void recursiveWhile(Vector<Expression> code){

		for (Expression expression : code) {

			WHILEsymbols.add(expression.getSymbol());

			if (!expression.isTerminal()) {
				recursiveWhile(expression.getActualDescendents());
			}
		}


	}

	private static void recursiveFor(Vector<Expression> code){

		for (Expression expression : code) {

			FORsymbols.add(expression.getSymbol());

			if (!expression.isTerminal()) {
				recursiveFor(expression.getActualDescendents());
			}
		}


	}




}
