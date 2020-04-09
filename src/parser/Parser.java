package parser;

import java.util.*;
import java.io.*;
import ast.*;
import lexer.*;
import symtable.*;

public class Parser
{
	private String filename;
	private Grammar grammar;
	private AbstractSyntaxTree tree;
	private SymbolTable table;

	public Parser(String file)
	{
		this.filename = file;
		//parse the file.tok into tokens
		this.grammar = new Grammar();
		this.table = new SymbolTable();
	}

	public void parse(List<Token> tokens)
	{
		this.tree = this.grammar.build(tokens);
		this.tree.trim();
		this.tree.scope();
		System.out.println(this.tree);

		this.generateSymbols();
		this.export();
	}

	public void generateSymbols()
	{
		Vector<Symbol> symbols = tree.getSymbols();

		for(Symbol sym : symbols)
			this.table.add(sym);

		System.out.println(this.table);
	}

	public void export()
	{
		String astFile = this.filename + ".ast";
		String symFile = this.filename + ".sym";

		try
        {
            FileWriter fileWriter = new FileWriter(new File("../output/" + astFile));

            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write(tree.getExpressions());

            bufferedWriter.close();

            fileWriter.close();

            fileWriter = new FileWriter(new File("../output/" + symFile));

            bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write(this.table.toString());

            bufferedWriter.close();

            fileWriter.close();
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }
	}
} 