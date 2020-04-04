JFLAGS = -g
JC = javac

.SUFFIXES: .java .class

.java.class:
		$(JC) $(JFLAGS) $*.java

CLASSES = \
		src/Lexer.java \
		src/spl.java \

default: classes

classes: $(CLASSES:.java=.class)

clean:
		$(RM) src/*.class