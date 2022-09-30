import Opt.Data;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.*;



public class SimpleCompiler {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        if (args.length != 2) {
            System.out.println("SimpleCompiler requires as input the name of a While code file to compile, and the " +
                                   "variable in the source code which is the output.");
            System.out.println("Ex: ./SimpleCompiler WhileCodeFile.while output");
        }
        String filename = args[0];
        Data.outputVariable = args[1];
        File file = new File(filename);
        String[] operators = {":=","+","-","*"};
        try {
            CharStream teststream = CharStreams.fromFileName(file.getAbsolutePath());
            AntlrGen.WhileLexer whileLexer = new AntlrGen.WhileLexer(teststream);
            AntlrGen.WhileParser whileParser = new AntlrGen.WhileParser(new CommonTokenStream(whileLexer));
            ParseTree testParser = whileParser.file();
            whileParser.setBuildParseTree(true);
            ParseTreeWalker.DEFAULT.walk((ParseTreeListener) new AntlrGen.WhileBaseListener(), testParser);

        } catch (IOException e) {
            e.printStackTrace();
        }
        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.println("Time elapsed: " + timeElapsed);
    }

}
