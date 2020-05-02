JC = javac

BUILDFLAGS = -g -d build

TESTFLAGS = -d build

RM = rm # Linux
RM = del # Windows

default: portable

run: portable
	java -jar spl -debug -test

test: unit-test
	java -jar test/junit-platform-console-standalone-1.6.2.jar --class-path build --scan-class-path

portable: spl
	jar cfm spl build/manifest -C build/ .

spl:
	$(JC) $(BUILDFLAGS) \
	src/exception/*.java \
	src/lexer/Token.java \
	src/lexer/Lexer.java \
	src/symtable/Symbol.java \
	src/symtable/SymbolTable.java \
	src/syntax/AbstractSyntaxTree.java \
	src/syntax/expression/*.java \
	src/parser/Grammar.java \
	src/parser/Parser.java \
	src/analysis/Scoping.java \
	src/syntax/code/File.java \
	src/syntax/code/Line.java \
	src/Cache.java \
	src/SPL.java
	
unit-test:
	$(JC) $(BUILDFLAGS) -cp test/junit-platform-console-standalone-1.6.2.jar \
	test/UnitTest.java \
	src/exception/*.java \
	src/lexer/Token.java \
	src/lexer/Lexer.java \
	src/symtable/Symbol.java \
	src/symtable/SymbolTable.java \
	src/syntax/AbstractSyntaxTree.java \
	src/syntax/expression/*.java \
	src/parser/Grammar.java \
	src/parser/Parser.java \
	src/analysis/Scoping.java \
	src/syntax/code/File.java \
	src/syntax/code/Line.java \
	src/Cache.java \
	src/SPL.java

clean:
	$(RM) spl
