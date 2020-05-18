package analysis;

import exception.ValueException;
import symtable.Symbol;
import symtable.SymbolTable;
import syntax.expression.Expression;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;




public class ValueCheck
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
	private static Map<Symbol,String> needsValueMessage = new HashMap<Symbol, String>();

	private static Vector<Symbol> PROG1symbols = new Vector<>();
	private static Vector<Symbol> PROG2symbols = new Vector<>();
	private static Vector<Symbol> WHILEsymbols = new Vector<>();
	private static Vector<Symbol> FORsymbols = new Vector<>();
	private static Vector<Symbol> BOOLsymbols = new Vector<>();

	private static Vector<Symbol> needsValue = new Vector<>();
	private static Vector<Symbol> warnings = new Vector<>();
	private static Vector<Symbol> warningsDisplay = new Vector<>();
	private static int numOfCond_Branch;
	private static int numOfCond_Loop;


	public static void check(SymbolTable table) throws ValueException
	{
		variableMap = new HashMap<String, Integer>();
		needsValueMessage = new HashMap<Symbol, String>();

		PROG1symbols = new Vector<>();
		PROG2symbols = new Vector<>();
		WHILEsymbols = new Vector<>();
		FORsymbols = new Vector<>();
		BOOLsymbols = new Vector<>();

		needsValue = new Vector<>();
		warnings = new Vector<>();
		warningsDisplay = new Vector<>();

		Vector<Symbol> symbols = table.list();
		boolean doElse;

		boolean elseSkip = false;
		int elseStart = -1;
		int elseEnd = -1;

		for(int i = 0; i < symbols.size(); i++)
		{


			doElse = false;

			if(elseSkip && i == elseStart){
				i = elseEnd;
				elseSkip = false;
				elseStart = -1;
				elseEnd = -1;
				continue;
			}

			variableMap.entrySet().forEach(entry->{

				if(entry.getValue() == 2){
					entry.setValue(4);
				}

			});



			//normal check
			if(symbols.get(i).getExpression().contains("variable '")){

					//if we haven't seen this variable before, initialize it with a 0.
					if(!variableMap.containsKey(symbols.get(i).getExpression())){
						variableMap.put(symbols.get(i).getExpression(),0);
					}



					if(symbols.get(i).getExpr().getParent().getParent().getExpr().equals("ASSIGN")){			// =


						if(variableMap.get(symbols.get(i).getExpression()) != 1){

							if(symbols.get(i+2).getExpression().contains("assign 'variable'")){

								//is being assigned a variable

								if(!(variableMap.get(symbols.get(i).getExpr().getParent().getParent().getDescendents().get(1).getDescendents().get(1).getDescendents().get(0).getExpr()) == 0)){
									//variable it is being assign has a value
									variableMap.put(symbols.get(i).getExpression(), 1);
									symbols.get(i).hasValue(true);
								}
							}
							else{
								//not being assign a variable but a literal
								variableMap.put(symbols.get(i).getExpression(),1);
								symbols.get(i).hasValue(true);
							}


						}

					}
					else if(symbols.get(i).getExpr().getParent().getParent().getDescendents().get(0).getExpr().contains("input")){   		// input()
						if(variableMap.get(symbols.get(i).getExpression()) != 1){

							variableMap.put(symbols.get(i).getExpression(), 1);
							symbols.get(i).hasValue(true);

						}

					}
					else if(symbols.get(i).getExpr().getParent().getParent().getExpr().contains("DECL")){			//type
						variableMap.put(symbols.get(i).getExpression(),0);
					}
					else{
						//other variable usage
						if(variableMap.get(symbols.get(i).getExpression()) == 0){

							needsValue.add(symbols.get(i));

							if(symbols.get(i-2).getExpression().contains("io 'output'")){
								needsValueMessage.put(symbols.get(i), "needs a value to be outputted to the screen");
							}
							else if(symbols.get(i-2).getExpression().contains("assign '")){
								needsValueMessage.put(symbols.get(i), "needs a value when being assigned to something else");
							}
							else if(symbols.get(i).getExpr().getParent().getParent().getExpr().contains("BOOL")){
								needsValueMessage.put(symbols.get(i), "undefined in BOOL condition");
							}
							else if(symbols.get(i-3).getExpression().contains("loop 'while'")){
								needsValueMessage.put(symbols.get(i), "undefined in while statement condition");
							}

						}
						else if(variableMap.get(symbols.get(i).getExpression()) == 2 || variableMap.get(symbols.get(i).getExpression()) == 4 || variableMap.get(symbols.get(i).getExpression()) == 3 || variableMap.get(symbols.get(i).getExpression()) == 5){
							warningsDisplay.add(symbols.get(i));
						}
					}




			}
			else if(symbols.get(i).getExpression().contains("COND_BRANCH")){


				if(symbols.get(i+2).getExpression().contains("bool 'T'")){				//check T / F conditions

					PROG2symbols.clear();
					recursivePROG2(symbols.get(i).getExpr().getDescendents().get(2).getDescendents());

					elseSkip = true;
					elseStart = PROG2symbols.get(0).getID();
					elseEnd = PROG2symbols.get(PROG2symbols.size()-1).getID();
					continue;
				}

				if(symbols.get(i+2).getExpression().contains("bool 'F'")){

					PROG1symbols.clear();
					recursivePROG1(symbols.get(i).getExpr().getDescendents().get(1).getDescendents());

					i = PROG1symbols.get(PROG1symbols.size()-1).getID();
					continue;
				}




				BOOLsymbols.clear();
				recursiveBool(symbols.get(i).getExpr().getDescendents().get(0).getDescendents());			//check variable in BOOL

				for(int n = 0; n < BOOLsymbols.size(); n++){

					if(BOOLsymbols.get(n).getExpression().contains("variable '")){

						if(!variableMap.containsKey(BOOLsymbols.get(n).getExpression())){
							variableMap.put(BOOLsymbols.get(n).getExpression(),0);
						}

						if(variableMap.get(BOOLsymbols.get(n).getExpression()) == 0){
							needsValue.add(BOOLsymbols.get(n));
							needsValueMessage.put(BOOLsymbols.get(n), "undefined in if statement condition");
						}
					}

				}


				if(symbols.get(i).getExpr().getDescendents().get(1).getExpr().contains("CODE")){

					if(symbols.get(i).getExpr().getDescendents().size() > 2){
						doElse = true;
					}

					PROG1symbols.clear();
					recursivePROG1(symbols.get(i).getExpr().getDescendents().get(1).getDescendents());			//check PROG1


					//check this code for assignments:
					for(int n = 0; n < PROG1symbols.size(); n++){

						if(PROG1symbols.get(n).getExpression().contains("variable '")){


							//if we haven't seen this variable before, initialize it with a 0.
							if(!variableMap.containsKey(PROG1symbols.get(n).getExpression())){
								variableMap.put(PROG1symbols.get(n).getExpression(),0);
							}


							if(PROG1symbols.get(n).getExpr().getParent().getParent().getExpr().equals("ASSIGN")){			// =


								if(variableMap.get(PROG1symbols.get(n).getExpression()) != 1){


									if(PROG1symbols.get(n).getExpr().getParent().getParent().getDescendents().get(1).getDescendents().get(1).getExpr().contains("VARIABLE")){

										//is being assigned a variable

										if(!(variableMap.get(PROG1symbols.get(n).getExpr().getParent().getParent().getDescendents().get(1).getDescendents().get(1).getDescendents().get(0).getExpr()) == 0)){
											//variable it is being assign has a value

											numOfCond_Branch = 0;
											numOfCond_Loop = 0;
											recursiveGetParent(PROG1symbols.get(n).getExpr());

											if(numOfCond_Branch <= 1 && numOfCond_Loop == 0){
												//in first 'if' and not in a loop
												variableMap.put(PROG1symbols.get(n).getExpression(), 2);
												warnings.add(PROG1symbols.get(n));
												PROG1symbols.get(n).hasValue(true);
											}
											else{
												variableMap.put(PROG1symbols.get(n).getExpression(), 4);
												warnings.add(PROG1symbols.get(n));
												PROG1symbols.get(n).hasValue(true);
											}
										}
									}
									else{
										//not being assigned a variable
										numOfCond_Branch = 0;
										numOfCond_Loop = 0;
										recursiveGetParent(PROG1symbols.get(n).getExpr());

										if(numOfCond_Branch <= 1 && numOfCond_Loop == 0){
											//in first 'if' and not in a loop
											variableMap.put(PROG1symbols.get(n).getExpression(), 2);
											warnings.add(PROG1symbols.get(n));
											PROG1symbols.get(n).hasValue(true);
										}
										else{
											variableMap.put(PROG1symbols.get(n).getExpression(), 4);
											warnings.add(PROG1symbols.get(n));
											PROG1symbols.get(n).hasValue(true);
										}
									}


								}

							}
							else if(PROG1symbols.get(n).getExpr().getParent().getParent().getDescendents().get(0).getExpr().contains("input")){   		// input()

								if(variableMap.get(PROG1symbols.get(n).getExpression()) != 1){

									numOfCond_Branch = 0;
									numOfCond_Loop = 0;
									recursiveGetParent(PROG1symbols.get(n).getExpr());

									if(numOfCond_Branch <= 1 && numOfCond_Loop == 0){
										//in first 'if' and not in a loop
										variableMap.put(PROG1symbols.get(n).getExpression(), 2);
										warnings.add(PROG1symbols.get(n));
										PROG1symbols.get(n).hasValue(true);
									}
									else{
										variableMap.put(PROG1symbols.get(n).getExpression(), 4);
										warnings.add(PROG1symbols.get(n));
										PROG1symbols.get(n).hasValue(true);
									}

								}

							}
							else if(PROG1symbols.get(n).getExpr().getParent().getParent().getExpr().contains("DECL")){			//type
								variableMap.put(PROG1symbols.get(n).getExpression(),0);
							}
							else{
								if(variableMap.get(PROG1symbols.get(n).getExpression()) == 0){

									needsValue.add(PROG1symbols.get(n));

									if(PROG1symbols.get(n-2).getExpression().contains("io 'output'")){
										needsValueMessage.put(PROG1symbols.get(n), "needs a value to be outputted to the screen");
									}
									else if(PROG1symbols.get(n-2).getExpression().contains("assign '")){
										needsValueMessage.put(PROG1symbols.get(n), "needs a value when being assigned to something else");
									}
									else if(PROG1symbols.get(n).getExpr().getParent().getParent().getExpr().contains("BOOL")){
										needsValueMessage.put(PROG1symbols.get(n), "undefined in BOOL condition");
									}
									else if(PROG1symbols.get(n-3).getExpression().contains("loop 'while'")){
										needsValueMessage.put(PROG1symbols.get(n), "undefined in while statement condition");
									}

								}
								else if(variableMap.get(PROG1symbols.get(n).getExpression()) == 2 || variableMap.get(PROG1symbols.get(n).getExpression()) == 4 || variableMap.get(PROG1symbols.get(n).getExpression()) == 3 || variableMap.get(PROG1symbols.get(n).getExpression()) == 5){
									warningsDisplay.add(PROG1symbols.get(n));
								}
							}

						}
					}
					i = PROG1symbols.get(PROG1symbols.size()-1).getID();

					if(doElse){
						i++;
						PROG2symbols.clear();
						recursivePROG2(symbols.get(i).getExpr().getDescendents());			//check PROG2

						for(int n = 0; n < PROG2symbols.size(); n++){

							if(PROG2symbols.get(n).getExpression().contains("variable '")){

								//if we haven't seen this variable before, initialize it with a 0.
								if(!variableMap.containsKey(PROG2symbols.get(n).getExpression())){
									variableMap.put(PROG2symbols.get(n).getExpression(),0);
								}

								if(PROG2symbols.get(n).getExpr().getParent().getParent().getExpr().equals("ASSIGN")){			// =

									if(variableMap.get(PROG2symbols.get(n).getExpression()) != 1){
										if(variableMap.get(PROG2symbols.get(n).getExpression()) == 2){
											variableMap.put(PROG2symbols.get(n).getExpression(), 1);
											PROG2symbols.get(n).hasValue(true);

											//remove from warnings
											for(int w = 0; w < warnings.size(); w++){
												if (warnings.get(w).getExpression().equals(PROG2symbols.get(n).getExpression())){
													warnings.removeElement(warnings.get(w));
												}
											}
											for(int w = 0; w < warningsDisplay.size(); w++){
												if (warningsDisplay.get(w).getExpression().equals(PROG2symbols.get(n).getExpression())){
													warningsDisplay.removeElement(warningsDisplay.get(w));
												}
											}

										}
										else{
											variableMap.put(PROG2symbols.get(n).getExpression(), 4);
											warnings.add(PROG2symbols.get(n));
											PROG2symbols.get(n).hasValue(true);
										}
									}

								}
								else if(PROG2symbols.get(n).getExpr().getParent().getParent().getDescendents().get(0).getExpr().contains("input")){   		// input()

									if(variableMap.get(PROG2symbols.get(n).getExpression()) != 1){
										if(variableMap.get(PROG2symbols.get(n).getExpression()) == 2){
											variableMap.put(PROG2symbols.get(n).getExpression(), 1);
											PROG2symbols.get(n).hasValue(true);

											//remove from warnings
											for(int w = 0; w < warnings.size(); w++){
												if (warnings.get(w).getExpression().equals(PROG2symbols.get(n).getExpression())){
													warnings.removeElement(warnings.get(w));
												}
											}

										}
										else{
											variableMap.put(PROG2symbols.get(n).getExpression(), 4);
											warnings.add(PROG2symbols.get(n));
											PROG2symbols.get(n).hasValue(true);
										}
									}

								}
								else if(PROG2symbols.get(n).getExpr().getParent().getParent().getExpr().contains("DECL")){			//type
									variableMap.put(PROG2symbols.get(n).getExpression(),0);
								}
								else{
									if(variableMap.get(PROG2symbols.get(n).getExpression()) == 0){

										needsValue.add(PROG2symbols.get(n));

										if(PROG2symbols.get(n-2).getExpression().contains("io 'output'")){
											needsValueMessage.put(PROG2symbols.get(n), "needs a value to be outputted to the screen");
										}
										else if(PROG2symbols.get(n-2).getExpression().contains("assign '")){
											needsValueMessage.put(PROG2symbols.get(n), "needs a value when being assigned to something else");
										}
										else if(PROG2symbols.get(n).getExpr().getParent().getParent().getExpr().contains("BOOL")){
											needsValueMessage.put(PROG2symbols.get(n), "undefined in BOOL condition");
										}


									}
									else if(variableMap.get(PROG2symbols.get(n).getExpression()) == 2 || variableMap.get(PROG2symbols.get(n).getExpression()) == 4 || variableMap.get(PROG2symbols.get(n).getExpression()) == 3 || variableMap.get(PROG2symbols.get(n).getExpression()) == 5){
										warningsDisplay.add(PROG2symbols.get(n));
									}

								}

							}


						}

						i = PROG2symbols.get(PROG2symbols.size()-1).getID();
					}
				}
			}
			else if(symbols.get(i).getExpression().contains("COND_LOOP")){

					if(symbols.get(i+1).getExpression().contains("loop 'while")){ //while loop


						if(symbols.get(i+3).getExpression().contains("bool 'T'")){		//check T / F conditions
							continue;
						}

						if(symbols.get(i+3).getExpression().contains("bool 'F'")){
							WHILEsymbols.clear();
							recursiveWhile(symbols.get(i).getExpr().getDescendents().get(2).getDescendents());
							i = WHILEsymbols.get(WHILEsymbols.size()-1).getID();
							continue;
						}



						BOOLsymbols.clear();
						recursiveBool(symbols.get(i+2).getExpr().getDescendents());		//while loop condition

						for(int n = 0; n < BOOLsymbols.size(); n++){

							if(BOOLsymbols.get(n).getExpression().contains("variable '")){

								if(!variableMap.containsKey(BOOLsymbols.get(n).getExpression())){
									variableMap.put(BOOLsymbols.get(n).getExpression(),0);
								}

								if(variableMap.get(BOOLsymbols.get(n).getExpression()) == 0){
									needsValue.add(BOOLsymbols.get(n));
									needsValueMessage.put(BOOLsymbols.get(n), "undefined in while statement condition");
								}
							}

						}


						WHILEsymbols.clear();
						recursiveWhile(symbols.get(i).getExpr().getDescendents().get(2).getDescendents());		//inside while loop

						for(int n = 0; n < WHILEsymbols.size(); n++){

							if(WHILEsymbols.get(n).getExpression().contains("COND_LOOP")){  //do beginning of nested for loops
								if(WHILEsymbols.get(n+1).getExpression().contains("loop 'for'")){

									//assign
									if(WHILEsymbols.get(n+3).getExpression().contains("variable '")){
										variableMap.put(WHILEsymbols.get(n+3).getExpression(), 1);
										WHILEsymbols.get(n+3).hasValue(true);
									}

									//check comparison
									if(WHILEsymbols.get(n+7).getExpression().contains("variable '")){
										if(!variableMap.containsKey(WHILEsymbols.get(n+7).getExpression())){
											variableMap.put(WHILEsymbols.get(n+7).getExpression(), 0);
										}
										if(variableMap.get(WHILEsymbols.get(n+7).getExpression()) == 0){

											needsValue.add(WHILEsymbols.get(n+7));
											needsValueMessage.put(WHILEsymbols.get(n+7), "undefined when used in a for loop");
										}
									}
								}
								n = n + 12;
							}


							if(WHILEsymbols.get(n).getExpression().contains("variable '")){					//normal variables in while

								//if we haven't seen this variable before, initialize it with a 0.
								if(!variableMap.containsKey(WHILEsymbols.get(n).getExpression())){
									variableMap.put(WHILEsymbols.get(n).getExpression(),0);
								}


								if(WHILEsymbols.get(n).getExpr().getParent().getParent().getExpr().equals("ASSIGN")){			// =


									if(variableMap.get(WHILEsymbols.get(n).getExpression()) != 1){


										if(WHILEsymbols.get(n).getExpr().getParent().getParent().getDescendents().get(1).getDescendents().get(1).getExpr().contains("VARIABLE")){

											//is being assigned a variable
											if(!(variableMap.get(WHILEsymbols.get(n).getExpr().getParent().getParent().getDescendents().get(1).getDescendents().get(1).getDescendents().get(0).getExpr()) == 0)){
												//variable it is being assign has a value
												variableMap.put(WHILEsymbols.get(n).getExpression(), 3);
												warnings.add(WHILEsymbols.get(n));
												WHILEsymbols.get(n).hasValue(true);
											}
										}
										else{
											//not being assigned a variable
											variableMap.put(WHILEsymbols.get(n).getExpression(), 3);
											warnings.add(WHILEsymbols.get(n));
											WHILEsymbols.get(n).hasValue(true);
										}


									}

								}
								else if(WHILEsymbols.get(n).getExpr().getParent().getParent().getDescendents().get(0).getExpr().contains("input")){   		// input()

									if(variableMap.get(WHILEsymbols.get(n).getExpression()) != 1){
										variableMap.put(WHILEsymbols.get(n).getExpression(), 3);
										warnings.add(WHILEsymbols.get(n));
										WHILEsymbols.get(n).hasValue(true);
									}

								}
								else if(WHILEsymbols.get(n).getExpr().getParent().getParent().getExpr().contains("DECL")){			//type
									variableMap.put(WHILEsymbols.get(n).getExpression(),0);
								}
								else{
									if(variableMap.get(WHILEsymbols.get(n).getExpression()) == 0){

										needsValue.add(WHILEsymbols.get(n));

										if(WHILEsymbols.get(n-2).getExpression().contains("io 'output'")){
											needsValueMessage.put(WHILEsymbols.get(n), "needs a value to be outputted to the screen");
										}
										else if(WHILEsymbols.get(n-2).getExpression().contains("assign '")){
											needsValueMessage.put(WHILEsymbols.get(n), "needs a value when being assigned to something else");
										}
										else if(WHILEsymbols.get(n).getExpr().getParent().getParent().getExpr().contains("BOOL")){
											needsValueMessage.put(WHILEsymbols.get(n), "undefined in BOOL condition");
										}
										else if(WHILEsymbols.get(n-3).getExpression().contains("loop 'while'")){
											needsValueMessage.put(WHILEsymbols.get(n), "undefined in while statement condition");
										}

									}
									else if(variableMap.get(WHILEsymbols.get(n).getExpression()) == 2 || variableMap.get(WHILEsymbols.get(n).getExpression()) == 4 || variableMap.get(WHILEsymbols.get(n).getExpression()) == 3 || variableMap.get(WHILEsymbols.get(n).getExpression()) == 5){
										warningsDisplay.add(WHILEsymbols.get(n));
									}
								}

							}

						} //end of WHILEsymbols loop

						i = WHILEsymbols.get(WHILEsymbols.size()-1).getID();

					}
					else if(symbols.get(i+1).getExpression().contains("loop 'for'")){ //for loop


						//assign
						if(symbols.get(i+3).getExpression().contains("variable '")){
							variableMap.put(symbols.get(i+3).getExpression(), 1);
							symbols.get(i+3).hasValue(true);
						}

						//check comparison
						if(symbols.get(i+7).getExpression().contains("variable '")){
							if(!variableMap.containsKey(symbols.get(i+7).getExpression())){
								variableMap.put(symbols.get(i+7).getExpression(), 0);
							}
							if(variableMap.get(symbols.get(i+7).getExpression()) == 0){

								needsValue.add(symbols.get(i+7));
								needsValueMessage.put(symbols.get(i+7), "undefined when used in a for loop");
							}
						}

						//do inside the for
						if(symbols.get(i+12).getExpression().contains("CODE")){
							FORsymbols.clear();
							recursiveFor(symbols.get(i+12).getExpr().getDescendents());

							for(int n = 0; n < FORsymbols.size(); n++){

								if(FORsymbols.get(n).getExpression().contains("COND_LOOP")){  //do beginning of nested for loops
									if(FORsymbols.get(n+1).getExpression().contains("loop 'for'")){

										//assign
										if(FORsymbols.get(n+3).getExpression().contains("variable '")){
											variableMap.put(FORsymbols.get(n+3).getExpression(), 1);
											FORsymbols.get(n+3).hasValue(true);
										}

										//check comparison
										if(FORsymbols.get(n+7).getExpression().contains("variable '")){
											if(!variableMap.containsKey(FORsymbols.get(n+7).getExpression())){
												variableMap.put(FORsymbols.get(n+7).getExpression(), 0);
											}
											if(variableMap.get(FORsymbols.get(n+7).getExpression()) == 0){

												needsValue.add(FORsymbols.get(n+7));
												needsValueMessage.put(FORsymbols.get(n+7), "undefined when used in a for loop");
											}
										}
									}
									n = n + 12;
								}


								if(FORsymbols.get(n).getExpression().contains("variable '")){			//normal variables inside for loop

									//if we haven't seen this variable before, initialize it with a 0.
									if(!variableMap.containsKey(FORsymbols.get(n).getExpression())){
										variableMap.put(FORsymbols.get(n).getExpression(),0);
									}


									if(FORsymbols.get(n).getExpr().getParent().getParent().getExpr().equals("ASSIGN")){			// =


										if(variableMap.get(FORsymbols.get(n).getExpression()) != 1){


											if(FORsymbols.get(n).getExpr().getParent().getParent().getDescendents().get(1).getDescendents().get(1).getExpr().contains("VARIABLE")){

												//is being assigned a variable
												if(!(variableMap.get(FORsymbols.get(n).getExpr().getParent().getParent().getDescendents().get(1).getDescendents().get(1).getDescendents().get(0).getExpr()) == 0)){
													//variable it is being assign has a value
													variableMap.put(FORsymbols.get(n).getExpression(), 5);
													warnings.add(FORsymbols.get(n));
													FORsymbols.get(n).hasValue(true);
												}
											}
											else{
												//not being assigned a variable
												variableMap.put(FORsymbols.get(n).getExpression(), 5);
												warnings.add(FORsymbols.get(n));
												FORsymbols.get(n).hasValue(true);
											}


										}

									}
									else if(FORsymbols.get(n).getExpr().getParent().getParent().getDescendents().get(0).getExpr().contains("input")){   		// input()

										if(variableMap.get(FORsymbols.get(n).getExpression()) != 1){
											variableMap.put(FORsymbols.get(n).getExpression(), 5);
											warnings.add(FORsymbols.get(n));
											FORsymbols.get(n).hasValue(true);
										}

									}
									else if(FORsymbols.get(n).getExpr().getParent().getParent().getExpr().contains("DECL")){			//type
										variableMap.put(FORsymbols.get(n).getExpression(),0);
									}
									else{
										if(variableMap.get(FORsymbols.get(n).getExpression()) == 0){

											needsValue.add(FORsymbols.get(n));

											if(FORsymbols.get(n-2).getExpression().contains("io 'output'")){
												needsValueMessage.put(FORsymbols.get(n), "needs a value to be outputted to the screen");
											}
											else if(FORsymbols.get(n-2).getExpression().contains("assign '")){
												needsValueMessage.put(FORsymbols.get(n), "needs a value when being assigned to something else");
											}
											else if(FORsymbols.get(n).getExpr().getParent().getParent().getExpr().contains("BOOL")){
												needsValueMessage.put(FORsymbols.get(n), "undefined in BOOL condition");
											}
											else if(FORsymbols.get(n-3).getExpression().contains("loop 'while'")){
												needsValueMessage.put(FORsymbols.get(n), "undefined in while statement condition");
											}

										}
										else if(variableMap.get(FORsymbols.get(n).getExpression()) == 2 || variableMap.get(FORsymbols.get(n).getExpression()) == 4 || variableMap.get(FORsymbols.get(n).getExpression()) == 3 || variableMap.get(FORsymbols.get(n).getExpression()) == 5){
											warningsDisplay.add(FORsymbols.get(n));
										}
									}

								}

							}//end of FORsymbols loop

							i = FORsymbols.get(FORsymbols.size()-1).getID();
						}
					}

			}


		}




		for(int i = 0; i < symbols.size(); i++)
		{
			if(symbols.get(i).getHasValue()){

				for(int m = 0; m < symbols.size(); m++)
				{
					if(symbols.get(m).getExpression().equals(symbols.get(i).getExpression())){
						symbols.get(m).hasValue(true);
					}
				}

			}
		}


		//done

		/*variableMap.entrySet().forEach(entry->{

			System.out.println(entry.getKey() + " " + entry.getValue());

		});*/






		for (Symbol symbol : warningsDisplay)
			System.out.println("Value Warning: Variable might not be assigned a value [" + symbol.getAlias() + "]" + symbol.getLocation());

		String valueErrors = "";

		for (Symbol symbol : needsValue) 
		{

			String msg = "undefined";

			if(needsValueMessage.containsKey(symbol))
				msg = needsValueMessage.get(symbol);


			valueErrors += "Variable " + msg + " [" + symbol.getAlias() + "]" + symbol.getLocation() + "; ";
		}

		if(!valueErrors.equals(""))
			throw new ValueException(valueErrors);
	}

	private static void recursivePROG1(Vector<Expression> code){

		for (Expression expression : code) {

			PROG1symbols.add(expression.getSymbol());

			if (!expression.isTerminal()) {
				recursivePROG1(expression.getDescendents());
			}
		}
	}

	private static void recursivePROG2(Vector<Expression> code){

		for (Expression expression : code) {

			PROG2symbols.add(expression.getSymbol());

			if (!expression.isTerminal()) {
				recursivePROG2(expression.getDescendents());
			}
		}
	}


	private static void recursiveWhile(Vector<Expression> code){

		for (Expression expression : code) {

			WHILEsymbols.add(expression.getSymbol());

			if (!expression.isTerminal()) {
				recursiveWhile(expression.getDescendents());
			}
		}


	}

	private static void recursiveFor(Vector<Expression> code){

		for (Expression expression : code) {

			FORsymbols.add(expression.getSymbol());

			if (!expression.isTerminal()) {
				recursiveFor(expression.getDescendents());
			}
		}

	}

	private static void recursiveBool(Vector<Expression> code){

		for (Expression expression : code) {

			BOOLsymbols.add(expression.getSymbol());

			if (!expression.isTerminal()) {
				recursiveBool(expression.getDescendents());
			}
		}

	}

	private static void recursiveGetParent(Expression code){

		if(code.getExpr().contains("COND_BRANCH")){
			if(!code.getDescendents().get(0).getDescendents().get(0).getExpr().contains("bool 'T'") && !code.getDescendents().get(0).getDescendents().get(0).getExpr().contains("bool 'F'")){
				numOfCond_Branch++;
			}
		}
		if(code.getExpr().contains("COND_LOOP")){
			if(code.getDescendents().get(0).getExpr().contains("loop 'while'")){
				if(!code.getDescendents().get(1).getDescendents().get(0).getExpr().contains("bool 'T'") && !code.getDescendents().get(1).getDescendents().get(0).getExpr().contains("bool 'F'")){
					numOfCond_Loop++;
				}
			}
			else{
				numOfCond_Loop++;
			}

		}
		if(code.getParent() != null){
			 recursiveGetParent(code.getParent());
		}

	}





}
