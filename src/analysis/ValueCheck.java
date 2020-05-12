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
	private static Map<String,Integer> assignmentInScope = new HashMap<String, Integer>();
	private static Map<Symbol,String> needsValueMessage = new HashMap<Symbol, String>();

	private static Vector<Symbol> PROG1symbols = new Vector<>();
	private static Vector<Symbol> PROG2symbols = new Vector<>();
	private static Vector<Symbol> WHILEsymbols = new Vector<>();
	private static Vector<Symbol> FORsymbols = new Vector<>();

	private static Vector<Symbol> needsValue = new Vector<>();
	private static Vector<Symbol> warnings = new Vector<>();
	private static Vector<Symbol> declarationWarnings = new Vector<>();

	public static void check(SymbolTable table) throws ValueException
	{
		System.out.println("Variable value test: ");
		Vector<Symbol> symbols = table.list();
		boolean doElse = false;


		for(int i = 0; i < symbols.size(); i++)
		{

			doElse = false;
			variableMap.entrySet().forEach(entry->{

				if(entry.getValue() == 2){
					entry.setValue(4);
				}

			});




			if(symbols.get(i).getExpression().contains("COND_BRANCH")){


				//check variable in BOOL


				//check PROG1
				if(symbols.get(i).getExpr().getDescendents().get(1).getExpr().contains("CODE")){

					if(symbols.get(i).getExpr().getDescendents().size() > 2){
						doElse = true;
					}


					PROG1symbols.clear();
					recursivePROG1(symbols.get(i).getExpr().getDescendents().get(1).getDescendents());


					//check this code for assignments:
					for(int n = 0; n < PROG1symbols.size(); n++){

						if(PROG1symbols.get(n).getExpression().contains("variable '")){


							//if we haven't seen this variable before, initialize it with a 0.
							if(!variableMap.containsKey(PROG1symbols.get(n).getExpression())){
								variableMap.put(PROG1symbols.get(n).getExpression(),0);
							}


							if(PROG1symbols.get(n).getExpr().getParent().getParent().getExpr().equals("ASSIGN")){			// =

								if(PROG1symbols.get(n).getExpr().getParent().getParent().getDescendents().get(1).getDescendents().get(1).getExpr().contains("VARIABLE")){
									if(variableMap.get(PROG1symbols.get(n).getExpr().getParent().getParent().getDescendents().get(1).getDescendents().get(1).getDescendents().get(0).getExpr()) == 0){
										variableMap.put(PROG1symbols.get(n).getExpression(), 0);
									}
									else if(variableMap.get(PROG1symbols.get(n).getExpression()) != 1){
										variableMap.put(PROG1symbols.get(n).getExpression(), 2);
										warnings.add(PROG1symbols.get(n));
									}
								}
								else if(variableMap.get(PROG1symbols.get(n).getExpression()) != 1){
									variableMap.put(PROG1symbols.get(n).getExpression(), 2);
									warnings.add(PROG1symbols.get(n));
								}

							}
							else if(PROG1symbols.get(n).getExpr().getParent().getParent().getDescendents().get(0).getExpr().contains("input")){   		// input()

								if(variableMap.get(PROG1symbols.get(n).getExpression()) != 1){
									variableMap.put(PROG1symbols.get(n).getExpression(), 2);
									warnings.add(PROG1symbols.get(n));
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
									else if(PROG1symbols.get(n-3).getExpression().contains("tok_if")){
										needsValueMessage.put(PROG1symbols.get(n), "cannot be undefined in if statement condition");
									}
									else if(PROG1symbols.get(n-3).getExpression().contains("loop 'while'")){
										needsValueMessage.put(PROG1symbols.get(n), "cannot be undefined in while statement condition");
									}

								}
							}

						}
					}
					i = PROG1symbols.get(PROG1symbols.size()-1).getID();
					//check PROG2
					if(doElse){
						i++;
						PROG2symbols.clear();
						recursivePROG2(symbols.get(i).getExpr().getDescendents());

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

											//remove from warnings
											for(int w = 0; w < warnings.size(); w++){
												if (warnings.get(w).getExpression().equals(PROG2symbols.get(n).getExpression())){
													warnings.removeElement(warnings.get(w));
												}
											}

										}
										else{
											variableMap.put(PROG2symbols.get(n).getExpression(), 2);
											warnings.add(PROG2symbols.get(n));
										}
									}

								}
								else if(PROG2symbols.get(n).getExpr().getParent().getParent().getDescendents().get(0).getExpr().contains("input")){   		// input()

									if(variableMap.get(PROG2symbols.get(n).getExpression()) != 1){
										if(variableMap.get(PROG2symbols.get(n).getExpression()) == 2){
											variableMap.put(PROG2symbols.get(n).getExpression(), 1);

											//remove from warnings
											for(int w = 0; w < warnings.size(); w++){
												if (warnings.get(w).getExpression().equals(PROG2symbols.get(n).getExpression())){
													warnings.removeElement(warnings.get(w));
												}
											}

										}
										else{
											variableMap.put(PROG2symbols.get(n).getExpression(), 2);
											warnings.add(PROG2symbols.get(n));
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
											needsValueMessage.put(PROG2symbols.get(n), "cannot be undefined in BOOL condition");
										}

									}
								}

							}


						}

						i = PROG2symbols.get(PROG2symbols.size()-1).getID();
					}
				}




			}
			else if(symbols.get(i).getExpression().contains("COND_LOOP")){

			}


		}





		//done

		variableMap.entrySet().forEach(entry->{

			System.out.println(entry.getKey() + " " + entry.getValue());

		});


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




}
