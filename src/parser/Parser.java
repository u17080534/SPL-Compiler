package parser;

import java.util.*;
import java.io.*;
import ast.*;
import lexer.*;

public class Parser
{
	private String filename;
	private Grammar grammar;
	private AbstractSyntaxTree tree;

	public Parser(String file)
	{
		this.filename = file;
		//parse the file.tok into tokens
		this.grammar = new Grammar();
	}

	public void parse(List<Token> tokens)
	{
		this.tree = this.grammar.build(tokens);
		this.tree.trim();
		System.out.println(this.tree);
		this.export();
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

            bufferedWriter.write(tree.getSymbols());

            bufferedWriter.close();

            fileWriter.close();
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }
	}
} 