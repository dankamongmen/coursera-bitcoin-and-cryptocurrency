JAVA:=$(wildcard *.java)
CLASS:=$(addsuffix .class,$(basename $(JAVA)))

all: $(CLASS)

%.class: %.java
	javac $^

clean:
	rm -f $(CLASS)
