package analysis;

import exception.ValueException;
import symtable.Symbol;
import symtable.SymbolTable;
import syntax.expression.Expression;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.Vector;
import java.util.stream.Collectors;


public class ValueCheck
{

	//display final variable value table
		//0 - no value
		//1 - value
		//3 - maybe a value

	private static Map<String,Integer> variableMap = new HashMap<String, Integer>();
	private static Map<Symbol,String> needsValueMessage = new HashMap<Symbol, String>();

	private static Vector<String> ifCheck = new Vector<>();
	private static Vector<Symbol> PROCsymbols = new Vector<>();
	private static Vector<Symbol> PROCskip= new Vector<>();

	private static Vector<Symbol> needsValue = new Vector<>();
	private static Vector<Symbol> warnings = new Vector<>();
	private static Vector<Symbol> warningsDisplay = new Vector<>();
	private static int numOfCond_Branch;
	private static int numOfCond_Loop;
	private static Expression ifBranch;
	private static Expression prevIfBranch;
	private static boolean thenPart;
	private static boolean elsePart;
	private static int stackCount;


	private static Stack<Integer> markerStack = new Stack<>();
	private static Stack<Integer> holderStack = new Stack<>();
	private static Stack<Integer> procEndStack = new Stack<>();
	private static Vector<String> procDoneList = new Vector<>();

	private static Vector<Symbol> symbols;




	public static void check(SymbolTable table) throws ValueException
	{
		variableMap = new HashMap<String, Integer>();
		needsValueMessage = new HashMap<Symbol, String>();

		ifCheck = new Vector<>();
		PROCsymbols = new Vector<>();
		PROCskip = new Vector<>();

		needsValue = new Vector<>();
		warnings = new Vector<>();
		warningsDisplay = new Vector<>();

		symbols = table.list();




		for(int i = 0; i < symbols.size(); i++)
		{




			if(i != symbols.size()-1){
				if(symbols.get(i+1).getExpression().contains("loop 'for'")) { //for loop

					//assign
					if (symbols.get(i + 3).getExpression().contains("variable '")) {
						variableMap.put(symbols.get(i + 3).getExpression(), 1);
						symbols.get(i + 3).hasValue(true);
					}

					//check comparison
					if (symbols.get(i + 7).getExpression().contains("variable '")) {
						if (!variableMap.containsKey(symbols.get(i + 7).getExpression())) {
							variableMap.put(symbols.get(i + 7).getExpression(), 0);
						}
						if (variableMap.get(symbols.get(i + 7).getExpression()) == 0) {

							needsValue.add(symbols.get(i + 7));
							needsValueMessage.put(symbols.get(i + 7), "undefined when used in a for loop");
						}
					}
					i = i + 11;
					continue;
				}
			}


			//check for proc call
			//...
			if(symbols.get(i).getExpression().contains("call '")){

				String procName = symbols.get(i).getExpression().substring(5);

				markerStack.push(i+1);		//return here

				for(int p = 0; p < symbols.size(); p++){
					if(symbols.get(p).getExpression().contains("proc " + procName)){
						procDoneList.add("proc " + procName);
						if(symbols.get(p+1).getExpression().contains("PROG")){
							PROCsymbols.clear();
							recursiveProc(symbols.get(p+1).getExpr().getDescendents());
							i = PROCsymbols.get(0).getID();

							procEndStack.push(PROCsymbols.get(PROCsymbols.size()-1).getID());
						}
					}
				}
			}
			else if(symbols.get(i).getExpression().contains("proc ")){
				if(procDoneList.contains(symbols.get(i).getExpression())){
					PROCskip.clear();
					recursiveProcSkip(symbols.get(i+1).getExpr().getDescendents());
					i = PROCskip.get(PROCskip.size()-1).getID();
				}
			}






			if(symbols.get(i).getExpression().contains("variable '")){

				//if we haven't seen this variable before, initialize it with a 0.
				if(!variableMap.containsKey(symbols.get(i).getExpression())){
					variableMap.put(symbols.get(i).getExpression(),0);
				}

				if(symbols.get(i).getExpr().getParent().getParent().getExpr().equals("ASSIGN")){			// =



					if(variableMap.get(symbols.get(i).getExpression()) != 1){


						numOfCond_Loop = 0;
						numOfCond_Branch = 0;
						stackCount = 0;
						recursiveGetParent(symbols.get(i).getExpr());


						if(numOfCond_Branch == 1 && numOfCond_Loop == 0){

							thenPart = false;
							elsePart = false;
							prevIfBranch = ifBranch;
							recursiveCheckIf(symbols.get(i).getExpr());
							if(prevIfBranch != ifBranch){
								ifCheck.clear();
							}

							if(thenPart){
								ifCheck.add(symbols.get(i).getExpression());
							}
							else if(elsePart){
								if(ifCheck.contains(symbols.get(i).getExpression()) && prevIfBranch == ifBranch){
									//System.out.println("in both");
									variableMap.put(symbols.get(i).getExpression(), 1);
									symbols.get(i).hasValue(true);
								}
							}

						}
						if(variableMap.get(symbols.get(i).getExpression()) != 1){
							if(numOfCond_Loop == 0 && numOfCond_Branch == 0){
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
							else{
								if(symbols.get(i+2).getExpression().contains("assign 'variable'")){
									//is being assigned a variable
									if(!(variableMap.get(symbols.get(i).getExpr().getParent().getParent().getDescendents().get(1).getDescendents().get(1).getDescendents().get(0).getExpr()) == 0)){
										//variable it is being assign has a value
										variableMap.put(symbols.get(i).getExpression(), 3);
									}
								}
								else{
									//not being assign a variable but a literal
									variableMap.put(symbols.get(i).getExpression(),3);
								}
							}
						}
					}

				}else if(symbols.get(i).getExpr().getParent().getParent().getDescendents().get(0).getExpr().contains("input")){

					if(variableMap.get(symbols.get(i).getExpression()) != 1){
						numOfCond_Loop = 0;
						numOfCond_Branch = 0;
						stackCount = 0;
						recursiveGetParent(symbols.get(i).getExpr());


						if(numOfCond_Branch == 1 && numOfCond_Loop == 0){

							thenPart = false;
							elsePart = false;
							prevIfBranch = ifBranch;
							recursiveCheckIf(symbols.get(i).getExpr());
							if(prevIfBranch != ifBranch){
								ifCheck.clear();
							}

							if(thenPart){
								ifCheck.add(symbols.get(i).getExpression());
							}
							else if(elsePart){
								if(ifCheck.contains(symbols.get(i).getExpression()) && prevIfBranch == ifBranch){
									//System.out.println("in both");
									variableMap.put(symbols.get(i).getExpression(), 1);
									symbols.get(i).hasValue(true);
								}
							}

						}
						if(variableMap.get(symbols.get(i).getExpression()) != 1){
							if(numOfCond_Loop == 0 && numOfCond_Branch == 0){

								//not being assign a variable but a literal
								variableMap.put(symbols.get(i).getExpression(),1);
								symbols.get(i).hasValue(true);

							}
							else{
								//not being assign a variable but a literal
								variableMap.put(symbols.get(i).getExpression(),3);
							}
						}

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
					else if(variableMap.get(symbols.get(i).getExpression()) == 3){
						warningsDisplay.add(symbols.get(i));
					}
				}
			}


			if(!procEndStack.empty()){
				if(procEndStack.peek() == i){
					procEndStack.pop();
					i = markerStack.pop();
				}
			}

		}



		/*variableMap.entrySet().forEach(entry->{
			System.out.println(entry.getKey() + " " + entry.getValue());
		});*/


		//table
		/*System.out.println("Updated table:");
		System.out.println(table.toString());
		System.out.println();*/



		//warnings
		warningsDisplay = warningsDisplay.stream().distinct().collect(Collectors.toCollection(Vector::new));
		for (Symbol symbol : warningsDisplay)
			System.out.println("Value Warning: Variable might not be assigned a value [" + symbol.getAlias() + "]" + symbol.getLocation());


		//value errors
		needsValue = needsValue.stream().distinct().collect(Collectors.toCollection(Vector::new));
		String valueErrors = "";
		for(int index = 0; index < needsValue.size(); index++)
		{
			Symbol symbol = needsValue.get(index);

			String msg = "undefined";

			if(needsValueMessage.containsKey(symbol))
				msg = needsValueMessage.get(symbol);


			valueErrors += "Variable " + msg + " [" + symbol.getAlias() + "]" + symbol.getLocation();
			if(index + 1 < needsValue.size())
				valueErrors += "; ";
		}

		if(!valueErrors.equals(""))
			throw new ValueException(valueErrors);
	}




	private static void recursiveProc(Vector<Expression> code){

		for (Expression expression : code) {

			PROCsymbols.add(expression.getSymbol());

			if (!expression.isTerminal()) {
				recursiveProc(expression.getDescendents());
			}
		}

	}

	private static void recursiveProcSkip(Vector<Expression> code){

		for (Expression expression : code) {

			PROCskip.add(expression.getSymbol());

			if (!expression.isTerminal()) {
				recursiveProcSkip(expression.getDescendents());
			}
		}

	}

	private static void recursiveGetParent(Expression code){



		if(code.getExpr().contains("PROC")){

			for(int i = 0; i < stackCount; i++){
				holderStack.push(markerStack.pop());
			}
			code = symbols.get(markerStack.peek()).getExpr();

			for(int i = 0; i < stackCount; i++){
				markerStack.push(holderStack.pop());
			}

			stackCount++;
		}

		if(code.getExpr().contains("COND_BRANCH")){
				numOfCond_Branch++;
		}
		if(code.getExpr().contains("COND_LOOP")){
			numOfCond_Loop++;
		}
		if(code.getParent() != null){
			 recursiveGetParent(code.getParent());
		}

	}


	private static void recursiveCheckIf(Expression code){



		if(code.getParent() != null){
			if(code.getParent().getExpr().contains("COND_BRANCH")){

				ifBranch = code.getParent();
				//code is CODE
				if(ifBranch.getDescendents().get(1) == code){
					//then part of if
					thenPart = true;
					return;
				}
				else if(ifBranch.getDescendents().size() > 2){
					if(ifBranch.getDescendents().get(2) == code){
						//else part
						elsePart = true;
						return;
					}
				}

			}
		}


		if(code.getParent() != null){
			recursiveCheckIf(code.getParent());
		}

	}





}
