Lisp-Interpreter
================

##Design
In my version of the Lisp Interprester I have the following classes:

`Interpreter.java:` This is the Driver class which accepts the input from the user, scans the input for tokens, and pushes these tokens to the parser for building the parse tree.

`LispScanner.java:` This class has just one method, which accpets a user input as string and returns the valid tokens in the form of a list to the caller. Basic error checking is done here, such as more ')' tokens than '(', or any 	invalid usage of the DOT '.' character.

`LispParser.java:` This class has one core method which recursively builds the parse tree from the tokens produced by scanner. It also has 3 other helper methods to build the tree, such as building the tree if the tokens inside it forms a list. The core method 'getParseTree()' returns the Parse Tree to the caller.

`SExpression.java:` This class defines the basic data structure for a node of an expression. It has two pointers to point to it's left and right subtree. In addition, it has two variables which are initialized to their respective 	types (i.e. Integer or String), in addition to an Enum variable which identifies the type of the node as either an INT, STRING or a COMPOUND node.

`TYPE.java:` Defines the basic structure of the Enumeration to identify the type of the node as explained above.

`Constants.java:` Frequently used constants are all defined here as public static final variables.

`LispException.java:` Subclass of 'Exception' which differentiates java exceptions from the ones produced by our interpreter.

`LispUtil.java:` Contains utility methods such as for printint the parse tree produced by the parser. This class has been updated, and now contains other utility methods, such add, multiply, 'NIL' checks, 'ATOM' checks, and other helper methods that are inherently called from the evaulation part of the interpreter, i.e. LispGrammar.java.

`LispGrammar.java:` This file implements the LISP grammar as-it-is from the ones given in the slides. This class contains methods such as EVAL, EVCON, APPLY etc., which collectively form the grammar of our mini-lisp. They essentialy take the expressions produced by the parser, and apply the rules that were described in the class. 


All the classes except Interpreter.java throws LispException to identify any kind of errors during parsing/scanning. The main() method in Interpreter.java catches these errors and prints it to the user. It will resume normally after any error, and begin reading the tokens from the next line.
Since there can a large number of negative test cases, I've done my best to most of those errors. However, any error, that I felt could not be caught by me, I'm catching it as an exception and guessing that the input maybe wrong in the main class. 


The `src` folder contains all the source code.

##How to Run the program:

Instead of a Makefile, I have provided a bash script to build the java code. Please run the script from my submission directory, i.e. one level above the 'src' dirctory, as follows:

```./build-interpreter.sh
```
 
Otherwise, to build the whole source code, please refer the following:

##Commands:
```
cd <MY_SUBMISSION_FOLDER>/src
javac *.java
java Interpreter	#To run the interpreter 
```

##References:
LISP_1_5_Programmers_Manual.pd

I'm thankfull to my instructor, classmates, peers and seniors, for their inputs to help me through the roadblocks. The discussions at Piazza specially helped me to understand the problem statement better and provide valuable test cases.

