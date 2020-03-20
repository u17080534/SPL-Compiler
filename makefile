JFLAGS = -g
JC = javac

.SUFFIXES: .java .class

.java.class:
        $(JC) $(JFLAGS) $*.java

CLASSES = \
        spl.java \

default: classes

classes: $(CLASSES:.java=.class)

clean:
        $(RM) *.class