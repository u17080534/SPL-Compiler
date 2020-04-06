import java.util.*;
import java.io.*;
import javafx.util.*;

public class Parser
{
	private String filename;
	private Grammar grammar;

	public Parser(String file)
	{
		//parse the file.tok into tokens
		this.grammar = new Grammar();
	}

	public void parse(List<Pair<String, Token>> tokens)
	{
		this.grammar.build(tokens);
	}
} 