package ast;

import java.util.*;
import java.io.*;	
import ast.expression.*;

public class AbstractSyntaxTree
{
	Expression root;

	public AbstractSyntaxTree(Expression root)
	{
		this.root = root;
	}

	@Override public String toString()
	{
		return this.root.toString();
	}
}