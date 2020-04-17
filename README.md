# Usage
1.  To compile the SPL-Compiler, it is require you have the following Java Software installed:
	```
	java version "1.8.0_241"
	Java(TM) SE Runtime Environment (build 1.8.0_241-b07)
	```
2.  Run the makefile as follows:
	```
	make
	```
3.  To compile a program written in SPL:
	```
	java -jar spl [-debug] <filename>.spl
	```
	Or to compile a given example file:
	```
	java -jar spl [-debug] -test
	```

## Documentation
All documentation for the different parts of the development of the SPL-Compiler is stored within the ```docs``` subdirectory.
In this directory are documents which detail the Regular Expressions and Context-Free Grammars on which the compiler is based.

