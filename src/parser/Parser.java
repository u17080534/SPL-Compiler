package parser;

import java.util.*;
import exception.*;
import ast.*;
import lexer.*;
import symtable.*;

public class Parser
{
	private Grammar grammar;

	public Parser()
	{
		//parse the file.tok into tokens
		this.grammar = new Grammar();
	}

	public AbstractSyntaxTree parse(List<Token> tokens) throws SyntaxException
	{
		AbstractSyntaxTree tree;

		try
		{
			tree = this.grammar.build(tokens);
			tree.trim();
			tree.scope();
		}
		catch(SyntaxException ex)
		{
			throw ex;
		}

		return tree;
	}
} 