package DataStructureGeneration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static java.nio.file.StandardOpenOption.*;
import java.io.*;
import java.nio.file.*;

public class ASTBuilder {
    public static void  buildDotAST(Map<String, ASTNode<String>> nodeMap, Map<Integer,String>visitOrder){
        String filename = ".\\SimpleCompiler\\src\\DataStructureGeneration\\AST.gv";
        File file = new File(filename);
        List<String> open = new ArrayList<>();
        String [] nameIdentifiers = {"]1","]2","]3","]4","]5","]6","]7","]8","]9",};
        List<String> boolIdent = new ArrayList<>();

        boolIdent.add("od");
        boolIdent.add("fi");

        // Delete the existing dotfile
        try{
            boolean result = Files.deleteIfExists(file.toPath());
        }catch (IOException x){
            System.err.println(x);
        }
        // create the header for the dot file
        String s = """
            digraph "DirectedGraph" {
            graph [label = "AST", labelloc=t, concentrate = true];
            """;
        byte[] data = s.getBytes();
        Path p = Paths.get(filename);

        try (OutputStream out = new BufferedOutputStream(
                Files.newOutputStream(p, CREATE, APPEND))) {
            out.write(data, 0, data.length);
        } catch (IOException x) {
            System.err.println(x);
        }

        boolean found;
        //Correct Labels of node to show operation or operand
        Integer increment = 0;
        for (int i = 0; i < visitOrder.size(); i ++) {
            ASTNode<String> entry = nodeMap.get(visitOrder.get(i));
            String[] activeChildren = entry.getChildren();
            for (String temp: activeChildren){
                found = false;
                for (String nameIdentifier : nameIdentifiers) {
                    if (temp.trim().contains(nameIdentifier)) {
                        found = true;
                    }
                }
                if(!found) {
                    boolean isLabelled = false;
                    //System.out.println("entry test: " +temp);
                    if(temp.trim().contains(":=") || boolIdent.contains(temp.trim())){
                        isLabelled = true;
                    }

                    if(isLabelled) {//check for assignment or booloean and add the label field to those
                        s = "\"" + entry.getData() + "\"" + " [ label=\"" + temp + " -(label: " + increment++ + ")" + "\" ]" + "\n";
                        data = s.getBytes();
                        try (OutputStream out = new BufferedOutputStream(
                                Files.newOutputStream(p, CREATE, APPEND))) {
                            out.write(data, 0, data.length);
                        } catch (IOException x) {
                            System.err.println(x);
                        }
                        //default labeling with no specified label
                    }else{
                        s = "\"" + entry.getData() + "\"" + " [ label=\"" + temp + "\" ]" + "\n";
                        data = s.getBytes();
                        try (OutputStream out = new BufferedOutputStream(
                                Files.newOutputStream(p, CREATE, APPEND))) {
                            out.write(data, 0, data.length);
                        } catch (IOException x) {
                            System.err.println(x);
                        }
                    }
                }
            }
        }
        //For each node in the tree print the relation it has to its children
        for (int i = 0; i < visitOrder.size(); i ++) {
            ASTNode<String> entry = nodeMap.get(visitOrder.get(i));
            String[] activeChildren = entry.getChildren();

            for (String temp: activeChildren){
                found = false;
                for (String nameIdentifier : nameIdentifiers) {
                    if (temp.trim().contains(nameIdentifier)) {
                        found = true;
                    }
                }
                if(found) {
                    s = "\"" + entry.getData() + "\"" + " -> " + "\"" + temp + "\"" + "\n";
                    data = s.getBytes();
                    try (OutputStream out = new BufferedOutputStream(
                            Files.newOutputStream(p, CREATE, APPEND))) {
                        out.write(data, 0, data.length);
                    } catch (IOException x) {
                        System.err.println(x);
                    }
                }
            }
        }


        //create the end of the dotfile
        s = "\n}";
        data = s.getBytes();
        try (OutputStream out = new BufferedOutputStream(
                Files.newOutputStream(p, CREATE, APPEND))) {
            out.write(data, 0, data.length);
        } catch (IOException x) {
            System.err.println(x);
        }


    }

    public static void createAnnotatedFile(Map<String, operandNode<String>> operandMap, Map<String, ASTNode<String>> nodeMap, List<String> order){
    System.out.println("Starting annotated file");
    String [] branches = {"w=", "w>=", "w<=", "w<", "w>" , "i=",  "i<=", "i<", "i>" , "i>="};
    String [] nameIdentifiers = {"]1","]2","]3","]4","]5","]6","]7","]8","]9",};
    String [] semicolonChecks = {"od", "fi", "else"};
    String [] operators = {"+", "-", "*", "or", "and"};
    List<String> line = new ArrayList<>();
    Integer label = 0;
    int count = 0;
    for(String entry: order){

        if(entry.trim().equals("else")){
            System.out.println("else");
        }else if (entry.trim().contains("skip")) {
            System.out.println("skip" + " --label " + label++);
        }else{
            String expr = operandMap.get(entry).data;
            Boolean containsBranch = false;
            for (String testBranch : branches) {
                if (expr.contains(testBranch)) {
                    containsBranch = true;
                    if (expr.contains("]w")) { //while printing
                        expr = expr.replaceAll(testBranch, testBranch.substring(1));
                        expr = expr.replaceAll("]", "");
                        expr = expr.replaceAll("\\[", "");
                        System.out.println("while " + expr + " do" + " --label: " + label++);
                    }
                    if (expr.contains("]i")) {
                        expr = expr.replaceAll(testBranch, testBranch.substring(1));
                        expr = expr.replaceAll("]", "");
                        expr = expr.replaceAll("\\[", "");
                        System.out.println("if " + expr + " then" + " --label: " + label++);
                    }
                    line.clear();
                    break;

                }
            }

            if (expr.contains(":=")) {
                String[] split = expr.split(":=");
                if (line.isEmpty()) {
                    line.add(split[1].trim());
                }
                line.add(0, split[0].trim());
                line.add(1, ":=");
                if(count < order.size()-1) {
                    Boolean needSemicolon = true;
                    //System.out.println("count: " + count + " next expr: " + operandMap.get(order.get(count+1)).data);
                    for(int i = 0; i < semicolonChecks.length; i++) {
                        if(!order.get(count+1).contains("skip") && !order.get(count+1).trim().equals("else")) {
                            if (operandMap.get(order.get(count + 1)).data.contains(semicolonChecks[i])) {
                                needSemicolon = false;
                                break;
                            }
                        }
                    }
                    if(needSemicolon) {
                        String testing = line + ";" + "  --label: " + label++;
                        testing = testing.replaceAll("]","");
                        testing = testing.replaceAll("\\[","");
                        testing = testing.replaceAll(",","");
                        System.out.println(testing);
                    }else{
                        String testing = line + "  --label: " + label++;
                        testing = testing.replaceAll("]","");
                        testing = testing.replaceAll("\\[","");
                        testing = testing.replaceAll(",","");
                        System.out.println(testing);
                    }
                }else{
                    String testing = line + "  --label: " + label++;
                    testing = testing.replaceAll("]","");
                    testing = testing.replaceAll("\\[","");
                    testing = testing.replaceAll(",","");
                    System.out.println(testing);
                }
                line.clear();
            }
            if (expr.contains("od")) {
                containsBranch = true;
                System.out.println("od");
            }
            if (expr.contains("fi")) {
                containsBranch = true;
                System.out.println("fi");
            }
            if (!containsBranch && !expr.contains(":=")) {
                String [] split = {};
                String tempOp = null;
                for(String operator: operators){

                    if(expr.contains(operator)){
                        String tempoperator = operator;
                        if(tempoperator.equals("+")){
                            tempoperator = "\\+";
                        }
                        if(tempoperator.equals("*")){
                            tempoperator = "\\*";
                        }
                        tempOp = operator;
                        split = expr.split(tempoperator);
                    }
                }
                Boolean containsNode = false;
                for(String name: nameIdentifiers){
                    //System.out.println("split 0 : " + split[0]+ " split 1: " + split[1]+ " name: " + name);
                    if(split[0].contains(name)){
                        containsNode = true;
                        if(!split[1].contains(name)) {
                            String templine = tempOp + split[1];
                            line.add(templine);
                        }
                        break;
                    }
                    if(split[1].contains(name)){
                        containsNode = true;
                        if(split[0].contains(name)) {
                            String templine = split[0] + tempOp;
                            line.add(templine);
                        }
                        break;
                    }
                }
                if(!containsNode) {
                    //System.out.println("1: " + expr);
                    line.add(expr);
                }
            }
        }
        count++;
    }
        System.out.println("end annotated file");
        System.out.println("");
    }

}
