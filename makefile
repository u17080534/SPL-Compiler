JFLAGS = -g -d build

JC = javac

# Change to rm for linux and del for windows
# RM = del 
RM = rm 

.SUFFIXES: .java .class

.java.class:
		$(JC) $(JFLAGS) $*.java

default: portable

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
	src/Cache.java \
	src/SPL.java

portable: clean spl
	jar cfm spl build/manifest -C build/ .

clean:
	$(RM) spl

run:
	java -jar spl -debug -test
