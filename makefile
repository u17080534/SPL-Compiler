JFLAGS = -g -d build 

JC = javac

# Linux
# RM = rm 
# Windows
RM = del 

default: portable

spl:
	$(JC) $(JFLAGS) \
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
	
portable: spl
	jar cfm spl build/manifest -C build/ .

run: portable
	java -jar spl -debug -test

clean:
	$(RM) spl

test: spl
	javac -d build -cp build/junit-platform-console-standalone-1.6.2.jar test/UnitTest.java \
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
	java -jar build/junit-platform-console-standalone-1.6.2.jar --class-path build --scan-class-path
