package analysis;

import exception.ValueException;
import symtable.Symbol;
import symtable.SymbolTable;
import syntax.expression.Expression;
import java.util.*;
import java.util.stream.Collectors;

public class ValueCheck
{
	public static void check(SymbolTable table) throws ValueException
	{
		//All these are objects that are passed by reference to functions
		Map<String,Integer> variableMap = new HashMap<String, Integer>();
		Map<Symbol,String> needsValueMessage = new HashMap<Symbol, String>();
		Vector<String> ifCheck = new Vector<>();
		Vector<Symbol> PROCsymbols = new Vector<>();
		Vector<Symbol> PROCskip = new Vector<>();
		Vector<Symbol> needsValue = new Vector<>();
		Vector<Symbol> warnings = new Vector<>();
		Vector<Symbol> warningsDisplay = new Vector<>();
		Expression ifBranch = null;
		Expression prevIfBranch = null;
		Stack<Integer> markerStack = new Stack<>();
		Stack<Integer> holderStack = new Stack<>();
		Stack<Integer> procEndStack = new Stack<>();
		Vector<String> procDoneList = new Vector<>();
		Vector<Symbol> symbols = table.list();
		//This object is so that primitive variables may be accessed by reference and not be static
		CheckObject checkObj = new CheckObject(-1, -1, false, false, -1);

		try{

			for(int i = 0; i < symbols.size(); i++){



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
								needsValueMessage.put(symbols.get(i + 7), "not assigned a value when used in a for loop");
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

					if(!procDoneList.contains("proc " + procName)){		//if we haven't done proc
						markerStack.push(i+1);		//return here

						for(int p = 0; p < symbols.size(); p++){

							if(symbols.get(p).getExpression().contains("proc " + procName)){

								procDoneList.add("proc " + procName);
								if(symbols.get(p).getID()+1 != symbols.size()){

									if(symbols.get(p+1).getExpression().contains("PROG")){
										PROCsymbols.clear();
										recursiveProc(symbols.get(p+1).getExpr().getDescendents(), PROCsymbols);



										i = PROCsymbols.get(0).getID();


										procEndStack.push(PROCsymbols.get(PROCsymbols.size()-1).getID());
									}
								}
							}
						}
					}

				}
				else if(symbols.get(i).getExpression().contains("proc ")){
					if(procDoneList.contains(symbols.get(i).getExpression())){
						PROCskip.clear();
						recursiveProcSkip(symbols.get(i+1).getExpr().getDescendents(), PROCskip);
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


							checkObj.numOfCond_Loop = 0;
							checkObj.numOfCond_Branch = 0;
							checkObj.stackCount = 0;
							recursiveGetParent(symbols.get(i).getExpr(), symbols, markerStack, holderStack, checkObj);


							if(checkObj.numOfCond_Branch == 1 && checkObj.numOfCond_Loop == 0){

								checkObj.thenPart = false;
								checkObj.elsePart = false;
								prevIfBranch = ifBranch;
								recursiveCheckIf(symbols.get(i).getExpr(), ifBranch, checkObj);
								if(prevIfBranch != ifBranch){
									ifCheck.clear();
								}

								if(checkObj.thenPart){
									ifCheck.add(symbols.get(i).getExpression());
								}
								else if(checkObj.elsePart){
									if(ifCheck.contains(symbols.get(i).getExpression()) && prevIfBranch == ifBranch){
										//System.out.println("in both");
										variableMap.put(symbols.get(i).getExpression(), 1);
										symbols.get(i).hasValue(true);
									}
								}

							}
							if(variableMap.get(symbols.get(i).getExpression()) != 1){
								if(checkObj.numOfCond_Loop == 0 && checkObj.numOfCond_Branch == 0){
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
										variableMap.put(symbols.get(i).getExpression(), 3);
									}
								}
							}
						}

					}else if(symbols.get(i).getExpr().getParent().getParent().getDescendents().get(0).getExpr().contains("input")){

						if(variableMap.get(symbols.get(i).getExpression()) != 1){
							checkObj.numOfCond_Loop = 0;
							checkObj.numOfCond_Branch = 0;
							checkObj.stackCount = 0;
							recursiveGetParent(symbols.get(i).getExpr(), symbols, markerStack, holderStack, checkObj);


							if(checkObj.numOfCond_Branch == 1 && checkObj.numOfCond_Loop == 0){

								checkObj.thenPart = false;
								checkObj.elsePart = false;
								prevIfBranch = ifBranch;
								recursiveCheckIf(symbols.get(i).getExpr(), ifBranch, checkObj);
								if(prevIfBranch != ifBranch){
									ifCheck.clear();
								}

								if(checkObj.thenPart){
									ifCheck.add(symbols.get(i).getExpression());
								}
								else if(checkObj.elsePart){
									if(ifCheck.contains(symbols.get(i).getExpression()) && prevIfBranch == ifBranch){
										//System.out.println("in both");
										variableMap.put(symbols.get(i).getExpression(), 1);
										symbols.get(i).hasValue(true);
									}
								}

							}
							if(variableMap.get(symbols.get(i).getExpression()) != 1){
								if(checkObj.numOfCond_Loop == 0 && checkObj.numOfCond_Branch == 0){

									//not being assign a variable but a literal
									variableMap.put(symbols.get(i).getExpression(),1);
									symbols.get(i).hasValue(true);

								}
								else{
									//not being assign a variable but a literal
									variableMap.put(symbols.get(i).getExpression(), 3);
								}
							}

						}
					}
					else if(symbols.get(i).getExpr().getParent().getParent().getExpr().contains("DECL")){			
						//type
						variableMap.put(symbols.get(i).getExpression(),0);
					}
					else{
						//other variable usage
						if(variableMap.get(symbols.get(i).getExpression()) == 0){

							needsValue.add(symbols.get(i));

							if(symbols.get(i-2).getExpression().contains("io 'output'")){
								needsValueMessage.put(symbols.get(i), "needs a value when being used for output");
							}
							else if(symbols.get(i-2).getExpression().contains("assign '")){
								needsValueMessage.put(symbols.get(i), "needs a value when being assigned to a variable");
							}
							else if(symbols.get(i).getExpr().getParent().getParent().getExpr().contains("BOOL")){
								needsValueMessage.put(symbols.get(i), "not assigned a value in bool condition");
							}
							else if(symbols.get(i-3).getExpression().contains("loop 'while'")){
								needsValueMessage.put(symbols.get(i), "not assigned a value in while statement condition");
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
		}
		catch(EmptyStackException exception) {
			exception.printStackTrace();
		}

		//warnings
		warningsDisplay = warningsDisplay.stream().distinct().collect(Collectors.toCollection(Vector::new));
		for (Symbol symbol : warningsDisplay)
		{
			if(symbol.getHasValue() == false)
				System.out.println("Value Warning: Variable might not be assigned a value [" + symbol.getAlias() + "]" + symbol.getLocation());
		}

		//value errors
		needsValue = needsValue.stream().distinct().collect(Collectors.toCollection(Vector::new));
		String valueErrors = "";
		for(int index = 0; index < needsValue.size(); index++)
		{
			Symbol symbol = needsValue.get(index);

			String msg = "not assigned a value";

			if(needsValueMessage.containsKey(symbol))
				msg = needsValueMessage.get(symbol);

			valueErrors += "Variable " + msg + " [" + symbol.getAlias() + "]" + symbol.getLocation();
			if(index + 1 < needsValue.size())
				valueErrors += "; ";
		}

		checkObj = null;

		if(!valueErrors.equals(""))
			throw new ValueException(valueErrors);
	}


	private static int findBranchParentID(Symbol symbol)
	{
		int branch_parent_id = 0;
		
		if(symbol != null && symbol.getExpr().getParent() != null)
		{
			Symbol parent = symbol.getExpr().getParent().getSymbol();

			while(parent != null)
			{
				if(parent.getExpression().equals("COND_BRANCH") || parent.getExpression().equals("COND_LOOP"))
				{
					branch_parent_id = parent.getID();
					break;
				}

				if(parent.getExpr().getParent() != null)
					parent = parent.getExpr().getParent().getSymbol();
				else
					parent = null;
			}
		}

		return branch_parent_id;
	}


	private static void recursiveProc(Vector<Expression> code, Vector<Symbol> PROCsymbols){

		for (Expression expression : code) {


			PROCsymbols.add(expression.getSymbol());

			if (!expression.isTerminal()) {
				recursiveProc(expression.getDescendents(), PROCsymbols);
			}
		}

	}

	private static void recursiveProcSkip(Vector<Expression> code, Vector<Symbol> PROCskip){

		for (Expression expression : code) {

			PROCskip.add(expression.getSymbol());

			if (!expression.isTerminal()) {
				recursiveProcSkip(expression.getDescendents(), PROCskip);
			}
		}

	}

	private static void recursiveGetParent(Expression code, Vector<Symbol> symbols, Stack<Integer> markerStack, Stack<Integer> holderStack, CheckObject checkObj){


		if(code.getExpr().contains("PROC")){

			if(!markerStack.empty()){
				for(int i = 0; i < checkObj.stackCount; i++){
					holderStack.push(markerStack.pop());
				}
				code = symbols.get(markerStack.peek()).getExpr();

				for(int i = 0; i < checkObj.stackCount; i++){
					markerStack.push(holderStack.pop());
				}

				checkObj.stackCount++;
			}

		}

		if(code.getExpr().contains("COND_BRANCH")){
				checkObj.numOfCond_Branch++;
		}
		if(code.getExpr().contains("COND_LOOP")){
			checkObj.numOfCond_Loop++;
		}
		if(code.getParent() != null){
			 recursiveGetParent(code.getParent(), symbols, markerStack, holderStack, checkObj);
		}

	}


	private static void recursiveCheckIf(Expression code, Expression ifBranch, CheckObject checkObj){



		if(code.getParent() != null){
			if(code.getParent().getExpr().contains("COND_BRANCH")){

				ifBranch = code.getParent();
				//code is CODE
				if(ifBranch.getDescendents().get(1) == code){
					//then part of if
					checkObj.thenPart = true;
					return;
				}
				else if(ifBranch.getDescendents().size() > 2){
					if(ifBranch.getDescendents().get(2) == code){
						//else part
						checkObj.elsePart = true;
						return;
					}
				}

			}
		}


		if(code.getParent() != null){
			recursiveCheckIf(code.getParent(), ifBranch, checkObj);
		}

	}

	private static class CheckObject
	{
		public int numOfCond_Branch = -1;
		public int numOfCond_Loop = -1;
		public boolean thenPart = false;
		public boolean elsePart = false;
		public int stackCount = -1;

		public CheckObject(int numOfCond_Branch, int numOfCond_Loop, boolean thenPart, boolean elsePart, int stackCount)
		{
			this.numOfCond_Branch = numOfCond_Branch;
			this.numOfCond_Loop = numOfCond_Loop;
			this.thenPart = thenPart;
			this.elsePart = elsePart;
			this.stackCount = stackCount;
		}
	}

}
