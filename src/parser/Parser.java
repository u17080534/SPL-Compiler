package parser;

import java.util.*;
import exception.*;
import syntax.*;
import lexer.*;
import symtable.*;

//SPL-COMPILER
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
			
			if(tree != null)
				tree.trim();
		}
		catch(SyntaxException ex)
		{
			throw ex;
		}

		return tree;
	}
} 