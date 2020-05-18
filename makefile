# SPL-Compiler
# |_build
#   |_<compiled java executables>
# |_docs
#   |_<program documentation>
# |_extern
#   |_<external utilities (example: JUnit JAR)>
# |_input
#   |_<sample SPL program files>
# |_output
#   |_<cache folder for compiled SPL program>
# |_src
#   |_<SPL-Compiler source Java code>
# |_test
#   |_<SPL-Compiler unit test Java code>

JC = javac

BUILDFLAGS = -g -d build

TESTFLAGS = -d build

# RM = rm # Linux
RM = del # Windows

default: portable

run: portable
	java -jar spl -debug -test

test: unit-test
	java -jar extern/junit-platform-console-standalone-1.6.2.jar --class-path build --scan-class-path

portable: spl
	jar cfm spl src/manifest -C build/ .

spl:
	$(JC) $(BUILDFLAGS) \
	src/exception/LexerException/*.java \
	src/exception/SyntaxException/*.java \
	src/exception/AnalysisException/*.java \
	src/lexer/*.java \
	src/parser/*.java \
	src/symtable/*.java \
	src/syntax/*.java \
	src/syntax/expression/*.java \
	src/syntax/code/*.java \
	src/analysis/*.java \
	src/Cache.java \
	src/SPL.java
	
unit-test:
	$(JC) $(BUILDFLAGS) -cp extern/junit-platform-console-standalone-1.6.2.jar \
	test/UnitTest.java \
	src/exception/LexerException/*.java \
	src/exception/SyntaxException/*.java \
	src/exception/AnalysisException/*.java \
	src/lexer/*.java \
	src/parser/*.java \
	src/symtable/*.java \
	src/syntax/*.java \
	src/syntax/expression/*.java \
	src/syntax/code/*.java \
	src/analysis/*.java \
	src/Cache.java \
	src/SPL.java

clean:
	$(RM) spl
