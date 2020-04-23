# Documentation
All documentation for the different parts of the development of the SPL-Compiler is stored within the ```docs``` directory.
In this directory are documents which detail the Regular Expressions, Context-Free Grammars, and Finite Automata on which the compiler is based.

All details on the implementation of the algorithms for the different parts of the compiler are explained within ```DESCRIPTION.txt```, every section is accompanied by each group members contributions in detail. The group members' information is contained within ```GROUP MEMBERS.txt```.

## Usage

1.  To compile the SPL-Compiler, it is require you have the following (or compatible) Java Software installed:
	```
	java version "1.8.0_241"
	Java(TM) SE Runtime Environment (build 1.8.0_241-b07)
	```
2.  Run the makefile as follows to create the spl JAR file:
	```
	make
	```
3.  To compile a SPL program:
	```
	java -jar spl [-debug] <file>.spl
	```
	or to compile a given example file (```input/test.spl```):
	```
	make run
	```
	to execute Unit Testing use:
	```
	make test
	```
4.  Once the program is executing, all constructed data structures will be cached in the ```output``` directory (if ```[-debug]``` is enabled they will also be output in the terminal.)
	The executable ```BASIC``` code will be produced in the current directory. (In later parts of this project...)

Note: If multiple files are supplied as arguments, they will be compiled as independent programs.