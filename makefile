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
	src/ast/expression/*.java \
	src/ast/AbstractSyntaxTree.java \
	src/parser/Grammar.java \
	src/parser/Parser.java \
	src/spl.java

run:
	java spl
