JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
		$(JC) $(JFLAGS) $*.java

default: spl

spl:
	$(JC) $(JFLAGS) \
	src/exception/*.java \
	src/lexer/Token.java \
	src/lexer/Lexer.java \
	src/symtable/Symbol.java \
	src/symtable/SymbolTable.java \
	src/ast/AbstractSyntaxTree.java \
	src/ast/expression/*.java \
	src/parser/Grammar.java \
	src/parser/Parser.java \
	src/spl.java

run:
	java spl
