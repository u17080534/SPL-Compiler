JFLAGS = -g -d build 

JC = javac

# Change to rm for linux and del for windows
RM = del 
# RM = rm 

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
	src/analysis/Scoping.java \
	src/Cache.java \
	src/SPL.java
	
portable: spl
	jar cfm spl build/manifest -C build/ .

clean:
	$(RM) spl

run:
	java -jar spl -debug -test

test:
	javac -d build -cp build/junit-platform-console-standalone-1.6.2.jar test/UnitTest.java \
	src/exception/*.java \
	src/lexer/Token.java \
	src/lexer/Lexer.java \
	src/symtable/Symbol.java \
	src/symtable/SymbolTable.java \
	src/ast/AbstractSyntaxTree.java \
	src/ast/expression/*.java \
	src/parser/Grammar.java \
	src/parser/Parser.java \
	src/analysis/Scoping.java \
	src/Cache.java \
	src/SPL.java
	java -jar build/junit-platform-console-standalone-1.6.2.jar --class-path build --scan-class-path
