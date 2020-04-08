package parser;

import java.util.*;
import java.io.*;
import javafx.util.*;
import lexer.*;

public class Parser
{
	private String filename;
	private Grammar grammar;

	public Parser(String file)
	{
		//parse the file.tok into tokens
		this.grammar = new Grammar();
	}

	public void parse(List<Token> tokens)
	{
		this.grammar.build(tokens);
	}
} 