JFLAGS = -g
JC = javac

.SUFFIXES: .java .class

.java.class:
        $(JC) $(JFLAGS) $*.java

CLASSES = \
        src/spl.java \
        src/Lexer.java \

default: classes

classes: $(CLASSES:.java=.class)

clean:
        $(RM) *.class