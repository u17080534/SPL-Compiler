import java.util.*;
import java.io.*;
import javafx.util.*;

public class Parser
{
	private String filename;

	public Parser(String file)
	{
		//parse the file.tok into tokens
	}

	public void parseAST(List<Pair<String, Token>> tokens)
	{
		Grammar.tokens(tokens);
		Grammar.PROG();
	}
} 