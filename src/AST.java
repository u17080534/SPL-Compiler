import java.util.*;
import java.io.*;	

public class AST
{
	private class ExprNode
	{	
		Token token;
		List<ExprNode> descendents;

		public ExprNode(Token token)
		{
			this.token = token;
			this.descendents = new ArrayList<ExprNode>();
		}

		public void add(ExprNode node)
		{
			this.descendents.add(node);
		}
	}

	List<ExprNode> roots;

	public void add(Token token)
	{

	}

}