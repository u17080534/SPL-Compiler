JFLAGS = -g
JC = javac

.SUFFIXES: .java .class

classes:
		$(JC) $(JFLAGS) src/*.java

default: classes

run:
	java ./src/spl

clean:
		rm src/*.class