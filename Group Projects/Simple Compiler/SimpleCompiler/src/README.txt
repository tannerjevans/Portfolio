Team Members
-----------------------
Del Jones
Nathan Patrizi
Tanner Evans


Languages Used
-------------------
The only language we utilized in this project was Java.
We did describe our grammar with the use of the ANTLR 4 tool.
Additionally, the AST file is a DOT file.

Java JDK 17 is Required to run this project
---------------------------------------------------------------
The provided project jar is compiled with JDK 17, and should be run with the Java SE Development Kit 17. It can be
    downloaded and installed according to the instructions located at:

    https://www.oracle.com/java/technologies/downloads/


Needed External Libraries
-----------------------------
We utilized ANTLR4 to help describe our grammar and make parsing easier. A step-by-step installation guide is presented
    below.

FOR WINDOWS (Java must already be installed):
1. Go to https://www.antlr.org/download.html and download the complete ANTLR 4.9.2 java binaries under the ANTLR tool
    and java Target section.
2. You must then add ANTLR to the classpath environment variable which is done by searching for the environment
    variable in the start menu.
    2a. With the environment variables menu open, look to see if there is a CLASSPATH variable already defined.
        If it is then append the path to your antlr-4.9.2-complete.jar file.
        If it does not exist, create a new variable named CLASSPATH and then set the value to the path to your
            antlr-4.9.2-complete.jar file
3. Verify installation by typing "java org.antlr.v4.Tool" into the command prompt. If it is installed it should bring
    up a list of commands.

Our group only used Windows for this project, and thus we don't have experience installing ANTLR for other operating
    systems. The resource provided below can potentially help with that. This is also the resource that we utilized to
    install for windows.

http://web.archive.org/web/20180710134244/https://github.com/antlr/antlr4/blob/master/doc/getting-started.md


Decorated AST Visualization
---------------------------------------------
The file "AST.gv" is created based on the input and is a DOT file.
This can be viewed by copying the entire file and pasting it into the left panel (replacing any current content in that
    panel)  of https://dreampuf.github.io/GraphvizOnline

CFG Visualization
---------------------------------------------
The file "CFG.txt" is created based on the input and is a DOT file.
This can be viewed by copying the entire file and pasting it into the left panel (replacing any current content in that
    panel)  of https://dreampuf.github.io/GraphvizOnline

Build Project
---------------------------------------------
1. Unzip the gz.tar ball.
2. Build and run steps:
    2.a You must have ANTLR4 installed as mentioned previously.
    2.b run SimpleCompiler.java and provide your created WHILE imperative filepath and a specified output variable as command line arguments.
        e.g. SimpleCompiler.java PATHTOYOURFILE DESIREDOUTPUTVARIABLE
    2.c All of the required optimization outputs will be printed to the terminal when the program is run.
    2.d this will create a .c file and a .s file
    2.e either on your machine or on the RISC-V machine run "gcc CFILENAME.c SFILENAME.s" with your name for the .c file
        substituted for CFILENAME and .s file substituted for SFILENAME
    2.f This creates an a.out executable which when run needs to be passed inputs for each variable.
