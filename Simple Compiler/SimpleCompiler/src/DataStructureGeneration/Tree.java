package DataStructureGeneration;//import java.awt.*;
import CodeGeneration.Final.CodeGenerator;
import CodeGeneration.Final.CodeStringData;
import CodeGeneration.Intermediate.AssemblyFunctions;
import Opt.Optimizer;

import java.io.File;
import java.util.*;


public class Tree {

    //TODO
    public static List<String> variableList = new ArrayList<>();

    public static String [] nameIdentifiers = {"]1","]2","]3","]4","]5","]6","]7","]8","]9",};
    public static String [] constantChecker = {"[0","[1","[2","[3","[4","[5","[6","[7","[8","[9",};
    public static String [] operatorIdentifier = {":=", "+", "-", "*", " or", " and","w=", "w>=", "w<=", "w<", "w>" , "i=",  "i<=", "i<", "i>=","i>" , "od","fi", "else"};
    public static String [] branchesList = {"w=", "w>=", "w<=", "w<", "w>" , "i=",  "i<=", "i<", "i>" , "i>="};
    public static String [] boolIdentifier = {"<", ">" , ">=", "<="};
    public static String [] whileIdentifier = {"while", "od"};
    public static Stack<String> elseStack;
    public static List<String> elseList = new ArrayList<>();
    public static int labelCount = 0;

    public static Map<String, ASTNode<String>>  fixChildNames(Map<String, ASTNode<String>> nodeMap, Map<Integer,String>visitOrder){
        /*
            Clean up the childset names to match those of the node itself and the parent.
        */


        boolean found;
        //System.out.println("number of nodes"+visitOrder.size());
        for (int i = 0; i < visitOrder.size(); i ++) {
            ASTNode<String> entry = nodeMap.get(visitOrder.get(i));
            String[] parentChildset = nodeMap.get(entry.getParent()).getChildren();
            // Detect and correct last line in file with no ; to end
            int count = 0;
            for (String temp: parentChildset){
                 //System.out.println("parent node name: "+ nodeMap.get(entry.parent).data+ " child: " + temp);
                if(temp.contains("[[") && temp.contains("]]")){

                    temp = temp.replaceAll("\\[\\[", "[").trim();
                    temp = temp.replaceAll("]]", "]").trim();
                    //System.out.println("we found it " + temp );
                    parentChildset[count] = temp;
                }
                count++;
            }
            int parentChildsetLength  = parentChildset.length;
            found = false;
            //System.out.println( "my node" + entry.getValue().data + " parent: " + entry.getValue().parent);
            for(int j = 0; j < parentChildsetLength && !found ; j++){
                String currentParentChild = parentChildset[j].trim();
                int currentLength = currentParentChild.length();
                if (currentLength < entry.getData().length()) {
                    String testString = entry.getData().substring(0, currentLength);

                    //System.out.println("testing set: " + testString);
                    if (parentChildset[j].contains(testString) && ']' == (parentChildset[j].charAt(parentChildset[j].length()-1))) {
                        parentChildset[j] = entry.getData();
                        nodeMap.replace(entry.getParent(), new ASTNode<>(entry.getParent(), parentChildset,
                            nodeMap.get(entry.getParent()).getParent()));
                        found = true;
                        //System.out.println("WE REPLACED:" + nodeMap.get(entry.parent).children[j]);
                    }
                }
            }
        }

        // AST MESSING AROUND
        //System.out.println("fixed the names");
        return nodeMap;
    }

    public static void decode(String expression, String identifier, AssemblyFunctions registerSet){
        //take the expression and compare the operation to determine which assembly operation it is
        String operator = "";
        String[] split;
        String temp;
        String temp1;
        expression = expression.replaceAll("\\(", "");
        expression = expression.replaceAll("\\)", "");
        for(String testOperator : operatorIdentifier){
            if(expression.contains(testOperator)){
                operator = testOperator;
                // System.out.println(operator);
                break;
            }
        }
        //System.out.println(operator+ " " + identifier);
        switch (operator) {
            case ":=" -> {
                split = expression.split(operator);  //THESE NEED IDENTIFIERS I THINk
                split[0] = split[0].trim();
                split[1] = split[1].trim();
                if(split[0].contains("true")){
                    split[0] = "[1]";
                }
                if(split[0].contains("false")){
                    split[0] = "[0]";
                }
                if(split[1].contains("true")){
                    split[1] = "[1]";
                }
                if(split[1].contains("false")){
                    split[1] = "[0]";
                }
                //System.out.println("ADD: " + split[0] + ", " + split[1]);
                if (!Character.isDigit(split[1].charAt(split[1].length() - 1))) { //assign immediate detection
                    if (Character.isDigit(split[1].charAt(1))) {
                        temp = split[1].replace("[", "");
                        temp = temp.replace("]", "");
                        //System.out.println("Assign I: " + split[0] + ", " + Integer.parseInt(temp));
                        for (int i = 0; i < split.length; i++) {
                            for (String test : variableList) {
                                if (split[i].contains("[" + test + "]")) {
                                    //System.out.println("found variable: " + test);
                                    split[i] = split[i].replaceAll("\\[" + test + "]", test);
                                    // System.out.println(split[i]);
                                }
                            }
                        }
                        registerSet.ASSIGN(split[0].trim(), Integer.parseInt(temp));
                        break;
                    }

                    // System.out.println("Assign: " + split[0] + ", " + split[1]);
                    for (int i = 0; i < split.length; i++) {
                        for (String test : variableList) {
                            if (split[i].contains("[" + test + "]")) {
                                // System.out.println("found variable: " + test);
                                split[i] = split[i].replaceAll("\\[" + test + "]", test);
                                // System.out.println(split[i]);
                            }
                        }
                    }
                    registerSet.ASSIGN(split[0].trim(), split[1].trim());

                } else {
                    //System.out.println("Assign: " + split[0] + ", " + split[1]);
                    for (int i = 0; i < split.length; i++) {
                        for (String test : variableList) {
                            if (split[i].contains("[" + test + "]")) {
                                // System.out.println("found variable: " + test);
                                split[i] = split[i].replaceAll("\\[" + test + "]", test);
                                //System.out.println(split[i]);
                            }
                        }
                    }
                    registerSet.ASSIGN(split[0].trim(), split[1].trim());
                }
            }
            case "+" -> {
                split = expression.split("\\+");
                split[0] = split[0].trim();
                split[1] = split[1].trim();
                if(split[0].contains("true")){
                    split[0] = "[1]";
                }
                if(split[0].contains("false")){
                    split[0] = "[0]";
                }
                if(split[1].contains("true")){
                    split[1] = "[1]";
                }
                if(split[1].contains("false")){
                    split[1] = "[0]";
                }
                //System.out.println("ADD: " + split[0] + ", " + split[1]);
                if ((!Character.isDigit(split[1].trim().charAt(split[1].length() - 1)) && Character.isDigit(split[1].trim().charAt(1))) && (!Character.isDigit(split[0].charAt(split[0].length() - 1)) && Character.isDigit(split[0].charAt(1)))) { //add immediate detection
                    // System.out.println("got here");
                    //if(Character.isDigit(split[1].charAt(1))){
                    temp = split[0].replace("[", "");
                    temp = temp.replace("]", "");
                    temp1 = split[1].replace("[", "");
                    temp1 = temp1.replace("]", "");

                    for (int i = 0; i < split.length; i++) {
                        for (String test : variableList) {
                            if (split[i].contains("[" + test + "]")) {
                                //System.out.println("found variable: " + test);
                                split[i] = split[i].replaceAll("\\[" + test + "]", test);
                                // System.out.println(split[i]);
                            }
                        }
                    }
                    registerSet.ADDI(identifier, Integer.parseInt(temp.trim()), Integer.parseInt(temp1.trim()));
                    break;
                    // }
                }
                if (!Character.isDigit(split[1].trim().charAt(split[1].length() - 1)) && Character.isDigit(split[1].trim().charAt(1))) { //add immediate detection
                    // System.out.println("got here");
                    //if(Character.isDigit(split[1].charAt(1))){
                    temp = split[1].replace("[", "");
                    temp = temp.replace("]", "");
                    for (int i = 0; i < split.length; i++) {
                        for (String test : variableList) {
                            if (split[i].contains("[" + test + "]")) {
                                //System.out.println("found variable: " + test);
                                split[i] = split[i].replaceAll("\\[" + test + "]", test);
                                // System.out.println(split[i]);
                            }
                        }
                    }
                    registerSet.ADDI(identifier, split[0].trim(), Integer.parseInt(temp.trim()));
                    break;
                    // }
                }
                //System.out.println(split[0].charAt(split[0].length()-1));
                if (!Character.isDigit(split[0].charAt(split[0].length() - 1)) && Character.isDigit(split[0].charAt(1))) {
                    //if (Character.isDigit(split[0].charAt(1))) {
                    temp = split[0].replace("[", "");
                    temp = temp.replace("]", "");
                    for (int i = 0; i < split.length; i++) {
                        for (String test : variableList) {
                            if (split[i].contains("[" + test + "]")) {
                                //System.out.println("found variable: " + test);
                                split[i] = split[i].replaceAll("\\[" + test + "]", test);
                                // System.out.println(split[i]);
                            }
                        }
                    }
                    registerSet.ADDI(identifier, Integer.parseInt(temp.trim()), split[1].trim());
                    break;
                    // }
                }//else{
                for (int i = 0; i < split.length; i++) {
                    for (String test : variableList) {
                        if (split[i].contains("[" + test + "]")) {
                            //System.out.println("found variable: " + test);
                            split[i] = split[i].replaceAll("\\[" + test + "]", test);
                            // System.out.println(split[i]);
                        }
                    }
                }
                registerSet.ADD(identifier, split[0].trim(), split[1].trim());
            }
            //}

            case "-" -> {
                split = expression.split(operator);
                split[0] = split[0].trim();
                split[1] = split[1].trim();
                if(split[0].contains("true")){
                    split[0] = "[1]";
                }
                if(split[0].contains("false")){
                    split[0] = "[0]";
                }
                if(split[1].contains("true")){
                    split[1] = "[1]";
                }
                if(split[1].contains("false")){
                    split[1] = "[0]";
                }
                //System.out.println("Sub: " + split[0] + ", " + split[1]);

                if ((!Character.isDigit(split[1].trim().charAt(split[1].length() - 1)) && Character.isDigit(split[1].trim().charAt(1))) && (!Character.isDigit(split[0].charAt(split[0].length() - 1)) && Character.isDigit(split[0].charAt(1)))) { //add immediate detection
                    // System.out.println("got here");
                    //if(Character.isDigit(split[1].charAt(1))){
                    temp = split[0].replace("[", "");
                    temp = temp.replace("]", "");
                    temp1 = split[1].replace("[", "");
                    temp1 = temp1.replace("]", "");
                    for (int i = 0; i < split.length; i++) {
                        for (String test : variableList) {
                            if (split[i].contains("[" + test + "]")) {
                                //System.out.println("found variable: " + test);
                                split[i] = split[i].replaceAll("\\[" + test + "]", test);
                                // System.out.println(split[i]);
                            }
                        }
                    }
                    registerSet.SUB(identifier, Integer.parseInt(temp.trim()), Integer.parseInt(temp1.trim()));
                    break;
                    // }
                }
                if (!Character.isDigit(split[1].charAt(split[1].length() - 1)) && Character.isDigit(split[1].charAt(1))) { // sub immediate detection
                    //if(Character.isDigit(split[1].charAt(1))){
                    temp = split[1].replace("[", "");
                    temp = temp.replace("]", "");
                    for (int i = 0; i < split.length; i++) {
                        for (String test : variableList) {
                            if (split[i].contains("[" + test + "]")) {
                                //System.out.println("found variable: " + test);
                                split[i] = split[i].replaceAll("\\[" + test + "]", test);
                                // System.out.println(split[i]);
                            }
                        }
                    }
                    registerSet.SUB(identifier, split[0].trim(), Integer.parseInt(temp));
                    break;
                    //}

                }
                if (!Character.isDigit(split[0].charAt(split[0].length() - 1)) && Character.isDigit(split[0].charAt(1))) { // sub immediate detection
                    //if(Character.isDigit(split[1].charAt(1))){
                    temp = split[0].replace("[", "");
                    temp = temp.replace("]", "");
                    for (int i = 0; i < split.length; i++) {
                        for (String test : variableList) {
                            if (split[i].contains("[" + test + "]")) {
                                //System.out.println("found variable: " + test);
                                split[i] = split[i].replaceAll("\\[" + test + "]", test);
                                // System.out.println(split[i]);
                            }
                        }
                    }
                    registerSet.SUB(identifier, Integer.parseInt(temp), split[1].trim());
                    break;
                    //}

                }
                //else {
                for (int i = 0; i < split.length; i++) {
                    for (String test : variableList) {
                        if (split[i].contains("[" + test + "]")) {
                            //System.out.println("found variable: " + test);
                            split[i] = split[i].replaceAll("\\[" + test + "]", test);
                            // System.out.println(split[i]);
                        }
                    }
                }
                registerSet.SUB(identifier, split[0].trim(), split[1].trim());
            }
            // }


            case "*" -> {
                split = expression.split("\\*");
                split[0] = split[0].trim();
                split[1] = split[1].trim();
                if(split[0].contains("true")){
                    split[0] = "[1]";
                }
                if(split[0].contains("false")){
                    split[0] = "[0]";
                }
                if(split[1].contains("true")){
                    split[1] = "[1]";
                }
                if(split[1].contains("false")){
                    split[1] = "[0]";
                }
                if ((!Character.isDigit(split[1].trim().charAt(split[1].length() - 1)) && Character.isDigit(split[1].trim().charAt(1))) && (!Character.isDigit(split[0].charAt(split[0].length() - 1)) && Character.isDigit(split[0].charAt(1)))) { //add immediate detection
                    // System.out.println("got here");
                    //if(Character.isDigit(split[1].charAt(1))){
                    temp = split[0].replace("[", "");
                    temp = temp.replace("]", "");
                    temp1 = split[1].replace("[", "");
                    temp1 = temp1.replace("]", "");
                    for (int i = 0; i < split.length; i++) {
                        for (String test : variableList) {
                            if (split[i].contains("[" + test + "]")) {
                                //System.out.println("found variable: " + test);
                                split[i] = split[i].replaceAll("\\[" + test + "]", test);
                                // System.out.println(split[i]);
                            }
                        }
                    }
                    registerSet.MUL(identifier, Integer.parseInt(temp.trim()), Integer.parseInt(temp1.trim()));
                    break;
                    // }
                }
                if (!Character.isDigit(split[1].charAt(split[1].length() - 1)) && Character.isDigit(split[1].charAt(1))) { //add immediate detection
                    // if(Character.isDigit(split[1].charAt(1))){
                    temp = split[1].replace("[", "");
                    temp = temp.replace("]", "");
                    for (int i = 0; i < split.length; i++) {
                        for (String test : variableList) {
                            if (split[i].contains("[" + test + "]")) {
                                //System.out.println("found variable: " + test);
                                split[i] = split[i].replaceAll("\\[" + test + "]", test);
                                // System.out.println(split[i]);
                            }
                        }
                    }
                    registerSet.MUL(identifier, split[0].trim(), Integer.parseInt(temp));
                    break;
                    // }
                }
                if (!Character.isDigit(split[0].charAt(split[0].length() - 1)) && Character.isDigit(split[0].charAt(1))) { //add immediate detection
                    // if(Character.isDigit(split[1].charAt(1))){
                    temp = split[0].replace("[", "");
                    temp = temp.replace("]", "");
                    for (int i = 0; i < split.length; i++) {
                        for (String test : variableList) {
                            if (split[i].contains("[" + test + "]")) {
                                //System.out.println("found variable: " + test);
                                split[i] = split[i].replaceAll("\\[" + test + "]", test);
                                // System.out.println(split[i]);
                            }
                        }
                    }
                    registerSet.MUL(identifier, Integer.parseInt(temp), split[1].trim());
                    break;
                    // }
                }
                //else {
                for (int i = 0; i < split.length; i++) {
                    for (String test : variableList) {
                        if (split[i].contains("[" + test + "]")) {
                            //System.out.println("found variable: " + test);
                            split[i] = split[i].replaceAll("\\[" + test + "]", test);
                            // System.out.println(split[i]);
                        }
                    }
                }
                registerSet.MUL(identifier, split[0].trim(), split[1].trim());
            }
            // }

            case "or" -> {
                split = expression.split(operator);
                split[0] = split[0].trim();
                split[1] = split[1].trim();
                if(split[0].contains("true")){
                    split[0] = "[1]";
                }
                if(split[0].contains("false")){
                    split[0] = "[0]";
                }
                if(split[1].contains("true")){
                    split[1] = "[1]";
                }
                if(split[1].contains("false")){
                    split[1] = "[0]";
                }
                for (int i = 0; i < split.length; i++) {
                    if (split[i].contains("true")) {
                        split[i] = "1";
                    }
                    if (split[i].contains("false")) {
                        split[i] = "0";
                    }
                }
                if ((!Character.isDigit(split[1].trim().charAt(split[1].length() - 1)) && Character.isDigit(split[1].trim().charAt(1))) && (!Character.isDigit(split[0].charAt(split[0].length() - 1)) && Character.isDigit(split[0].charAt(1)))) { //add immediate detection
                    // System.out.println("got here");
                    //if(Character.isDigit(split[1].charAt(1))){
                    temp = split[0].replace("[", "");
                    temp = temp.replace("]", "");
                    temp1 = split[1].replace("[", "");
                    temp1 = temp1.replace("]", "");
                    //registerSet.ORI(identifier, Integer.parseInt(temp.trim()), Integer.parseInt(temp1.trim()));
                    break;
                    // }
                }
                if (!Character.isDigit(split[1].charAt(split[1].length() - 1)) && Character.isDigit(split[1].charAt(1))) { //add immediate detection
                    // if(Character.isDigit(split[1].charAt(1))){
                    temp = split[1].replace("[", "");
                    temp = temp.replace("]", "");
                    for (int i = 0; i < split.length; i++) {
                        for (String test : variableList) {
                            if (split[i].contains("[" + test + "]")) {
                                //System.out.println("found variable: " + test);
                                split[i] = split[i].replaceAll("\\[" + test + "]", test);
                                // System.out.println(split[i]);
                            }
                        }
                    }
                    registerSet.ORI(identifier, split[0].trim(), Integer.parseInt(temp));
                    break;
                    // }
                    //registerSet.OR(identifier, split[0].trim(), split[1].trim());
                }
                if (!Character.isDigit(split[0].charAt(split[0].length() - 1)) && Character.isDigit(split[0].charAt(1))) { //add immediate detection
                    // if(Character.isDigit(split[1].charAt(1))){
                    temp = split[0].replace("[", "");
                    temp = temp.replace("]", "");
                    for (int i = 0; i < split.length; i++) {
                        for (String test : variableList) {
                            if (split[i].contains("[" + test + "]")) {
                                //System.out.println("found variable: " + test);
                                split[i] = split[i].replaceAll("\\[" + test + "]", test);
                                // System.out.println(split[i]);
                            }
                        }
                    }
                    registerSet.ORI(identifier, Integer.parseInt(temp), split[1].trim());
                    break;
                    // }
                    //registerSet.OR(identifier, split[0].trim(), split[1].trim());
                }
                //else {
                for (int i = 0; i < split.length; i++) {
                    for (String test : variableList) {
                        if (split[i].contains("[" + test + "]")) {
                            //System.out.println("found variable: " + test);
                            split[i] = split[i].replaceAll("\\[" + test + "]", test);
                            // System.out.println(split[i]);
                        }
                    }
                }
                registerSet.OR(identifier, split[0].trim(), split[1].trim());
            }
            //}

            case "and" -> {
                split = expression.split(operator);
                split[0] = split[0].trim();
                split[1] = split[1].trim();
                if(split[0].contains("true")){
                    split[0] = "[1]";
                }
                if(split[0].contains("false")){
                    split[0] = "[0]";
                }
                if(split[1].contains("true")){
                    split[1] = "[1]";
                }
                if(split[1].contains("false")){
                    split[1] = "[0]";
                }
                for (int i = 0; i < split.length; i++) {
                    if (split[i].contains("true")) {
                        split[i] = "1";
                    }
                    if (split[i].contains("false")) {
                        split[i] = "0";
                    }
                }

                // System.out.println("AND: " + split[0] + ", " + split[1]);

                if ((!Character.isDigit(split[1].trim().charAt(split[1].length() - 1)) && Character.isDigit(split[1].trim().charAt(1))) && (!Character.isDigit(split[0].charAt(split[0].length() - 1)) && Character.isDigit(split[0].charAt(1)))) { //add immediate detection
                    // System.out.println("got here");
                    //if(Character.isDigit(split[1].charAt(1))){
                    temp = split[0].replace("[", "");
                    temp = temp.replace("]", "");
                    temp1 = split[1].replace("[", "");
                    temp1 = temp1.replace("]", "");
                    for (int i = 0; i < split.length; i++) {
                        for (String test : variableList) {
                            if (split[i].contains("[" + test + "]")) {
                                //System.out.println("found variable: " + test);
                                split[i] = split[i].replaceAll("\\[" + test + "]", test);
                                // System.out.println(split[i]);
                            }
                        }
                    }
                    registerSet.ANDI(identifier, Integer.parseInt(temp.trim()), Integer.parseInt(temp1.trim()));
                    break;
                    // }
                }
                if (!Character.isDigit(split[1].charAt(split[1].length() - 1)) && Character.isDigit(split[1].charAt(1))) { //add immediate detection
                    //if(Character.isDigit(split[1].charAt(1))){
                    temp = split[1].replace("[", "");
                    temp = temp.replace("]", "");
                    for (int i = 0; i < split.length; i++) {
                        for (String test : variableList) {
                            if (split[i].contains("[" + test + "]")) {
                                //System.out.println("found variable: " + test);
                                split[i] = split[i].replaceAll("\\[" + test + "]", test);
                                // System.out.println(split[i]);
                            }
                        }
                    }
                    registerSet.ANDI(identifier, split[0].trim(), Integer.parseInt(temp));
                    break;
                    // }
                }
                if (!Character.isDigit(split[0].charAt(split[0].length() - 1)) && Character.isDigit(split[0].charAt(1))) { //add immediate detection
                    //if(Character.isDigit(split[1].charAt(1))){
                    temp = split[0].replace("[", "");
                    temp = temp.replace("]", "");
                    for (int i = 0; i < split.length; i++) {
                        for (String test : variableList) {
                            if (split[i].contains("[" + test + "]")) {
                                //System.out.println("found variable: " + test);
                                split[i] = split[i].replaceAll("\\[" + test + "]", test);
                                // System.out.println(split[i]);
                            }
                        }
                    }
                    registerSet.ANDI(identifier, Integer.parseInt(temp), split[1].trim());
                    break;
                    // }
                }
                //else {
                for (int i = 0; i < split.length; i++) {
                    for (String test : variableList) {
                        if (split[i].contains("[" + test + "]")) {
                            //System.out.println("found variable: " + test);
                            split[i] = split[i].replaceAll("\\[" + test + "]", test);
                            // System.out.println(split[i]);
                        }
                    }
                }
                registerSet.AND(identifier, split[0].trim(), split[1].trim());
            }
            // }
            case "w=" -> {
                split = expression.split(operator);
                split[0] = split[0].trim();
                split[1] = split[1].trim();
                if(split[0].contains("true")){
                    split[0] = "[1]";
                }
                if(split[0].contains("false")){
                    split[0] = "[0]";
                }
                if(split[1].contains("true")){
                    split[1] = "[1]";
                }
                if(split[1].contains("false")){
                    split[1] = "[0]";
                }
                // System.out.println("AND: " + split[0] + ", " + split[1]);
                if (!Character.isDigit(split[1].charAt(split[1].length() - 1)) && Character.isDigit(split[1].charAt(1))) { //add immediate detection
                    //if(Character.isDigit(split[1].charAt(1))){
                    temp = split[1].replace("[", "");
                    temp = temp.replace("]", "");
                    labelCount = labelCount + 1;
                    //System.out.println(labelCount);
                    for (int i = 0; i < split.length; i++) {
                        for (String test : variableList) {
                            if (split[i].contains("[" + test + "]")) {
                                //System.out.println("found variable: " + test);
                                split[i] = split[i].replaceAll("\\[" + test + "]", test);
                                // System.out.println(split[i]);
                            }
                        }
                    }
                    registerSet.WHILE("label" + labelCount, "=", split[0].trim(), Integer.parseInt(temp));
                    break;
                    // }
                }
                if (!Character.isDigit(split[0].charAt(split[0].length() - 1)) && Character.isDigit(split[0].charAt(1))) { //add immediate detection
                    //if(Character.isDigit(split[1].charAt(1))){
                    temp = split[0].replace("[", "");
                    temp = temp.replace("]", "");
                    registerSet.ANDI(identifier, Integer.parseInt(temp), split[1].trim());
                    labelCount = labelCount + 1;
                    //System.out.println(labelCount);
                    for (int i = 0; i < split.length; i++) {
                        for (String test : variableList) {
                            if (split[i].contains("[" + test + "]")) {
                                //System.out.println("found variable: " + test);
                                split[i] = split[i].replaceAll("\\[" + test + "]", test);
                                // System.out.println(split[i]);
                            }
                        }
                    }
                    registerSet.WHILE("label" + labelCount, "=", Integer.parseInt(temp), split[1].trim());
                    break;
                    // }
                }
                //else {
                labelCount = labelCount + 1;
                //System.out.println(labelCount);
                for (int i = 0; i < split.length; i++) {
                    for (String test : variableList) {
                        if (split[i].contains("[" + test + "]")) {
                            //System.out.println("found variable: " + test);
                            split[i] = split[i].replaceAll("\\[" + test + "]", test);
                            // System.out.println(split[i]);
                        }
                    }
                }
                registerSet.WHILE("label" + labelCount, "=", split[0].trim(), split[1].trim());
            }
            case "w<" -> {
                split = expression.split(operator);
                split[0] = split[0].trim();
                split[1] = split[1].trim();
                if(split[0].contains("true")){
                    split[0] = "[1]";
                }
                if(split[0].contains("false")){
                    split[0] = "[0]";
                }
                if(split[1].contains("true")){
                    split[1] = "[1]";
                }
                if(split[1].contains("false")){
                    split[1] = "[0]";
                }
                // System.out.println("AND: " + split[0] + ", " + split[1]);
                if (!Character.isDigit(split[1].charAt(split[1].length() - 1)) && Character.isDigit(split[1].charAt(1))) { //add immediate detection
                    //if(Character.isDigit(split[1].charAt(1))){
                    temp = split[1].replace("[", "");
                    temp = temp.replace("]", "");
                    labelCount = labelCount + 1;
                    //System.out.println(labelCount);
                    for (int i = 0; i < split.length; i++) {
                        for (String test : variableList) {
                            if (split[i].contains("[" + test + "]")) {
                                //System.out.println("found variable: " + test);
                                split[i] = split[i].replaceAll("\\[" + test + "]", test);
                                // System.out.println(split[i]);
                            }
                        }
                    }
                    registerSet.WHILE("label" + labelCount, "<", split[0].trim(), Integer.parseInt(temp));
                    break;
                    // }
                }
                if (!Character.isDigit(split[0].charAt(split[0].length() - 1)) && Character.isDigit(split[0].charAt(1))) { //add immediate detection
                    //if(Character.isDigit(split[1].charAt(1))){
                    temp = split[0].replace("[", "");
                    temp = temp.replace("]", "");
                    registerSet.ANDI(identifier, Integer.parseInt(temp), split[1].trim());
                    labelCount = labelCount + 1;
                    //System.out.println(labelCount);
                    for (int i = 0; i < split.length; i++) {
                        for (String test : variableList) {
                            if (split[i].contains("[" + test + "]")) {
                                //System.out.println("found variable: " + test);
                                split[i] = split[i].replaceAll("\\[" + test + "]", test);
                                // System.out.println(split[i]);
                            }
                        }
                    }
                    registerSet.WHILE("label" + labelCount, "<", Integer.parseInt(temp), split[1].trim());
                    break;
                    // }
                }
                //else {
                labelCount = labelCount + 1;
                //System.out.println(labelCount);
                for (int i = 0; i < split.length; i++) {
                    for (String test : variableList) {
                        if (split[i].contains("[" + test + "]")) {
                            //System.out.println("found variable: " + test);
                            split[i] = split[i].replaceAll("\\[" + test + "]", test);
                            // System.out.println(split[i]);
                        }
                    }
                }
                registerSet.WHILE("label" + labelCount, "<", split[0].trim(), split[1].trim());
            }
            case "w>" -> {
                split = expression.split(operator);
                split[0] = split[0].trim();
                split[1] = split[1].trim();
                if(split[0].contains("true")){
                    split[0] = "[1]";
                }
                if(split[0].contains("false")){
                    split[0] = "[0]";
                }
                if(split[1].contains("true")){
                    split[1] = "[1]";
                }
                if(split[1].contains("false")){
                    split[1] = "[0]";
                }
                // System.out.println("AND: " + split[0] + ", " + split[1]);
                if (!Character.isDigit(split[1].charAt(split[1].length() - 1)) && Character.isDigit(split[1].charAt(1))) { //add immediate detection
                    //if(Character.isDigit(split[1].charAt(1))){
                    temp = split[1].replace("[", "");
                    temp = temp.replace("]", "");
                    labelCount = labelCount + 1;
                    //System.out.println(labelCount);
                    for (int i = 0; i < split.length; i++) {
                        for (String test : variableList) {
                            if (split[i].contains("[" + test + "]")) {
                                //System.out.println("found variable: " + test);
                                split[i] = split[i].replaceAll("\\[" + test + "]", test);
                                // System.out.println(split[i]);
                            }
                        }
                    }
                    registerSet.WHILE("label" + labelCount, ">", split[0].trim(), Integer.parseInt(temp));
                    break;
                    // }
                }
                if (!Character.isDigit(split[0].charAt(split[0].length() - 1)) && Character.isDigit(split[0].charAt(1))) { //add immediate detection
                    //if(Character.isDigit(split[1].charAt(1))){
                    temp = split[0].replace("[", "");
                    temp = temp.replace("]", "");
                    registerSet.ANDI(identifier, Integer.parseInt(temp), split[1].trim());
                    labelCount = labelCount + 1;
                    //System.out.println(labelCount);
                    for (int i = 0; i < split.length; i++) {
                        for (String test : variableList) {
                            if (split[i].contains("[" + test + "]")) {
                                //System.out.println("found variable: " + test);
                                split[i] = split[i].replaceAll("\\[" + test + "]", test);
                                // System.out.println(split[i]);
                            }
                        }
                    }
                    registerSet.WHILE("label" + labelCount, ">", Integer.parseInt(temp), split[1].trim());
                    break;
                    // }
                }
                //else {
                labelCount = labelCount + 1;
                //System.out.println(labelCount);
                for (int i = 0; i < split.length; i++) {
                    for (String test : variableList) {
                        if (split[i].contains("[" + test + "]")) {
                            //System.out.println("found variable: " + test);
                            split[i] = split[i].replaceAll("\\[" + test + "]", test);
                            // System.out.println(split[i]);
                        }
                    }
                }
                registerSet.WHILE("label" + labelCount, ">", split[0].trim(), split[1].trim());
            }
            case "w>=" -> {
                split = expression.split(operator);
                split[0] = split[0].trim();
                split[1] = split[1].trim();
                if(split[0].contains("true")){
                    split[0] = "[1]";
                }
                if(split[0].contains("false")){
                    split[0] = "[0]";
                }
                if(split[1].contains("true")){
                    split[1] = "[1]";
                }
                if(split[1].contains("false")){
                    split[1] = "[0]";
                }
                // System.out.println("AND: " + split[0] + ", " + split[1]);
                if (!Character.isDigit(split[1].charAt(split[1].length() - 1)) && Character.isDigit(split[1].charAt(1))) { //add immediate detection
                    //if(Character.isDigit(split[1].charAt(1))){
                    temp = split[1].replace("[", "");
                    temp = temp.replace("]", "");
                    labelCount = labelCount + 1;
                    //System.out.println(labelCount);
                    for (int i = 0; i < split.length; i++) {
                        for (String test : variableList) {
                            if (split[i].contains("[" + test + "]")) {
                                //System.out.println("found variable: " + test);
                                split[i] = split[i].replaceAll("\\[" + test + "]", test);
                                // System.out.println(split[i]);
                            }
                        }
                    }
                    registerSet.WHILE("label" + labelCount, ">=", split[0].trim(), Integer.parseInt(temp));
                    break;
                    // }
                }
                if (!Character.isDigit(split[0].charAt(split[0].length() - 1)) && Character.isDigit(split[0].charAt(1))) { //add immediate detection
                    //if(Character.isDigit(split[1].charAt(1))){
                    temp = split[0].replace("[", "");
                    temp = temp.replace("]", "");
                    registerSet.ANDI(identifier, Integer.parseInt(temp), split[1].trim());
                    labelCount = labelCount + 1;
                    //System.out.println(labelCount);
                    for (int i = 0; i < split.length; i++) {
                        for (String test : variableList) {
                            if (split[i].contains("[" + test + "]")) {
                                //System.out.println("found variable: " + test);
                                split[i] = split[i].replaceAll("\\[" + test + "]", test);
                                // System.out.println(split[i]);
                            }
                        }
                    }
                    registerSet.WHILE("label" + labelCount, ">=", Integer.parseInt(temp), split[1].trim());
                    break;
                    // }
                }
                //else {
                labelCount = labelCount + 1;
                //System.out.println(labelCount);
                for (int i = 0; i < split.length; i++) {
                    for (String test : variableList) {
                        if (split[i].contains("[" + test + "]")) {
                            //System.out.println("found variable: " + test);
                            split[i] = split[i].replaceAll("\\[" + test + "]", test);
                            // System.out.println(split[i]);
                        }
                    }
                }
                registerSet.WHILE("label" + labelCount, ">=", split[0].trim(), split[1].trim());
            }
            case "w<=" -> {
                split = expression.split(operator);
                split[0] = split[0].trim();
                split[1] = split[1].trim();
                if(split[0].contains("true")){
                    split[0] = "[1]";
                }
                if(split[0].contains("false")){
                    split[0] = "[0]";
                }
                if(split[1].contains("true")){
                    split[1] = "[1]";
                }
                if(split[1].contains("false")){
                    split[1] = "[0]";
                }
                // System.out.println("AND: " + split[0] + ", " + split[1]);
                if (!Character.isDigit(split[1].charAt(split[1].length() - 1)) && Character.isDigit(split[1].charAt(1))) { //add immediate detection
                    //if(Character.isDigit(split[1].charAt(1))){
                    temp = split[1].replace("[", "");
                    temp = temp.replace("]", "");
                    labelCount = labelCount + 1;
                    //System.out.println(labelCount);
                    for (int i = 0; i < split.length; i++) {
                        for (String test : variableList) {
                            if (split[i].contains("[" + test + "]")) {
                                //System.out.println("found variable: " + test);
                                split[i] = split[i].replaceAll("\\[" + test + "]", test);
                                // System.out.println(split[i]);
                            }
                        }
                    }
                    registerSet.WHILE("label" + labelCount, "<=", split[0].trim(), Integer.parseInt(temp));
                    break;
                    // }
                }
                if (!Character.isDigit(split[0].charAt(split[0].length() - 1)) && Character.isDigit(split[0].charAt(1))) { //add immediate detection
                    //if(Character.isDigit(split[1].charAt(1))){
                    temp = split[0].replace("[", "");
                    temp = temp.replace("]", "");
                    registerSet.ANDI(identifier, Integer.parseInt(temp), split[1].trim());
                    labelCount = labelCount + 1;
                    //System.out.println(labelCount);
                    for (int i = 0; i < split.length; i++) {
                        for (String test : variableList) {
                            if (split[i].contains("[" + test + "]")) {
                                //System.out.println("found variable: " + test);
                                split[i] = split[i].replaceAll("\\[" + test + "]", test);
                                // System.out.println(split[i]);
                            }
                        }
                    }
                    registerSet.WHILE("label" + labelCount, "<=", Integer.parseInt(temp), split[1].trim());
                    break;
                    // }
                }
                //else {
                labelCount = labelCount + 1;
                //System.out.println(labelCount);
                for (int i = 0; i < split.length; i++) {
                    for (String test : variableList) {
                        if (split[i].contains("[" + test + "]")) {
                            //System.out.println("found variable: " + test);
                            split[i] = split[i].replaceAll("\\[" + test + "]", test);
                            // System.out.println(split[i]);
                        }
                    }
                }
                registerSet.WHILE("label" + labelCount, "<=", split[0].trim(), split[1].trim());
            }
            // }

            case "i=" -> {
                split = expression.split(operator);
                split[0] = split[0].trim();
                split[1] = split[1].trim();
                if(split[0].contains("true")){
                    split[0] = "[1]";
                }
                if(split[0].contains("false")){
                    split[0] = "[0]";
                }
                if(split[1].contains("true")){
                    split[1] = "[1]";
                }
                if(split[1].contains("false")){
                    split[1] = "[0]";
                }
                // System.out.println("AND: " + split[0] + ", " + split[1]);
                if (!Character.isDigit(split[1].charAt(split[1].length() - 1)) && Character.isDigit(split[1].charAt(1))) { //add immediate detection
                    //if(Character.isDigit(split[1].charAt(1))){
                    temp = split[1].replace("[", "");
                    temp = temp.replace("]", "");
                    labelCount = labelCount + 1;
                    //System.out.println(labelCount);
                    for (int i = 0; i < split.length; i++) {
                        for (String test : variableList) {
                            if (split[i].contains("[" + test + "]")) {
                                //System.out.println("found variable: " + test);
                                split[i] = split[i].replaceAll("\\[" + test + "]", test);
                                // System.out.println(split[i]);
                            }
                        }
                    }
                    registerSet.IF("=", split[0].trim(), Integer.parseInt(temp));
                    break;
                    // }
                }
                if (!Character.isDigit(split[0].charAt(split[0].length() - 1)) && Character.isDigit(split[0].charAt(1))) { //add immediate detection
                    //if(Character.isDigit(split[1].charAt(1))){
                    temp = split[0].replace("[", "");
                    temp = temp.replace("]", "");
                    registerSet.ANDI(identifier, Integer.parseInt(temp), split[1].trim());
                    labelCount = labelCount + 1;
                    //System.out.println(labelCount);
                    for (int i = 0; i < split.length; i++) {
                        for (String test : variableList) {
                            if (split[i].contains("[" + test + "]")) {
                                //System.out.println("found variable: " + test);
                                split[i] = split[i].replaceAll("\\[" + test + "]", test);
                                // System.out.println(split[i]);
                            }
                        }
                    }
                    registerSet.IF("=", Integer.parseInt(temp), split[1].trim());
                    break;
                    // }
                }
                //else {
                labelCount = labelCount + 1;
                //System.out.println(labelCount);
                for (int i = 0; i < split.length; i++) {
                    for (String test : variableList) {
                        if (split[i].contains("[" + test + "]")) {
                            //System.out.println("found variable: " + test);
                            split[i] = split[i].replaceAll("\\[" + test + "]", test);
                            // System.out.println(split[i]);
                        }
                    }
                }
                registerSet.IF("=", split[0].trim(), split[1].trim());
            }
            case "i<" -> {
                split = expression.split(operator);
                split[0] = split[0].trim();
                split[1] = split[1].trim();
                if(split[0].contains("true")){
                    split[0] = "[1]";
                }
                if(split[0].contains("false")){
                    split[0] = "[0]";
                }
                if(split[1].contains("true")){
                    split[1] = "[1]";
                }
                if(split[1].contains("false")){
                    split[1] = "[0]";
                }
                // System.out.println("AND: " + split[0] + ", " + split[1]);
                if (!Character.isDigit(split[1].charAt(split[1].length() - 1)) && Character.isDigit(split[1].charAt(1))) { //add immediate detection
                    //if(Character.isDigit(split[1].charAt(1))){
                    temp = split[1].replace("[", "");
                    temp = temp.replace("]", "");
                    labelCount = labelCount + 1;
                    //System.out.println(labelCount);
                    for (int i = 0; i < split.length; i++) {
                        for (String test : variableList) {
                            if (split[i].contains("[" + test + "]")) {
                                //System.out.println("found variable: " + test);
                                split[i] = split[i].replaceAll("\\[" + test + "]", test);
                                // System.out.println(split[i]);
                            }
                        }
                    }
                    registerSet.IF("<", split[0].trim(), Integer.parseInt(temp));
                    break;
                    // }
                }
                if (!Character.isDigit(split[0].charAt(split[0].length() - 1)) && Character.isDigit(split[0].charAt(1))) { //add immediate detection
                    //if(Character.isDigit(split[1].charAt(1))){
                    temp = split[0].replace("[", "");
                    temp = temp.replace("]", "");
                    registerSet.ANDI(identifier, Integer.parseInt(temp), split[1].trim());
                    labelCount = labelCount + 1;
                    //System.out.println(labelCount);
                    for (int i = 0; i < split.length; i++) {
                        for (String test : variableList) {
                            if (split[i].contains("[" + test + "]")) {
                                //System.out.println("found variable: " + test);
                                split[i] = split[i].replaceAll("\\[" + test + "]", test);
                                // System.out.println(split[i]);
                            }
                        }
                    }
                    registerSet.IF("<", Integer.parseInt(temp), split[1].trim());
                    break;
                    // }
                }
                //else {
                labelCount = labelCount + 1;
                //System.out.println(labelCount);
                for (int i = 0; i < split.length; i++) {
                    for (String test : variableList) {
                        if (split[i].contains("[" + test + "]")) {
                            //System.out.println("found variable: " + test);
                            split[i] = split[i].replaceAll("\\[" + test + "]", test);
                            // System.out.println(split[i]);
                        }
                    }
                }
                registerSet.IF("<", split[0].trim(), split[1].trim());
            }
            case "i>" -> {
                split = expression.split(operator);
                split[0] = split[0].trim();
                split[1] = split[1].trim();
                if(split[0].contains("true")){
                    split[0] = "[1]";
                }
                if(split[0].contains("false")){
                    split[0] = "[0]";
                }
                if(split[1].contains("true")){
                    split[1] = "[1]";
                }
                if(split[1].contains("false")){
                    split[1] = "[0]";
                }
                // System.out.println("AND: " + split[0] + ", " + split[1]);
                if (!Character.isDigit(split[1].charAt(split[1].length() - 1)) && Character.isDigit(split[1].charAt(1))) { //add immediate detection
                    //if(Character.isDigit(split[1].charAt(1))){
                    temp = split[1].replace("[", "");
                    temp = temp.replace("]", "");
                    labelCount = labelCount + 1;
                    //System.out.println(labelCount);
                    for (int i = 0; i < split.length; i++) {
                        for (String test : variableList) {
                            if (split[i].contains("[" + test + "]")) {
                                //System.out.println("found variable: " + test);
                                split[i] = split[i].replaceAll("\\[" + test + "]", test);
                                // System.out.println(split[i]);
                            }
                        }
                    }
                    registerSet.IF(">", split[0].trim(), Integer.parseInt(temp));
                    break;
                    // }
                }
                if (!Character.isDigit(split[0].charAt(split[0].length() - 1)) && Character.isDigit(split[0].charAt(1))) { //add immediate detection
                    //if(Character.isDigit(split[1].charAt(1))){
                    temp = split[0].replace("[", "");
                    temp = temp.replace("]", "");
                    registerSet.ANDI(identifier, Integer.parseInt(temp), split[1].trim());
                    labelCount = labelCount + 1;
                    //System.out.println(labelCount);
                    for (int i = 0; i < split.length; i++) {
                        for (String test : variableList) {
                            if (split[i].contains("[" + test + "]")) {
                                //System.out.println("found variable: " + test);
                                split[i] = split[i].replaceAll("\\[" + test + "]", test);
                                // System.out.println(split[i]);
                            }
                        }
                    }
                    registerSet.IF(">", Integer.parseInt(temp), split[1].trim());
                    break;
                    // }
                }
                //else {
                labelCount = labelCount + 1;
                //System.out.println(labelCount);
                for (int i = 0; i < split.length; i++) {
                    for (String test : variableList) {
                        if (split[i].contains("[" + test + "]")) {
                            //System.out.println("found variable: " + test);
                            split[i] = split[i].replaceAll("\\[" + test + "]", test);
                            // System.out.println(split[i]);
                        }
                    }
                }
                registerSet.IF(">", split[0].trim(), split[1].trim());
            }
            case "i>=" -> {
                split = expression.split(operator);
                split[0] = split[0].trim();
                split[1] = split[1].trim();
                if(split[0].contains("true")){
                    split[0] = "[1]";
                }
                if(split[0].contains("false")){
                    split[0] = "[0]";
                }
                if(split[1].contains("true")){
                    split[1] = "[1]";
                }
                if(split[1].contains("false")){
                    split[1] = "[0]";
                }
                // System.out.println("AND: " + split[0] + ", " + split[1]);
                if (!Character.isDigit(split[1].charAt(split[1].length() - 1)) && Character.isDigit(split[1].charAt(1))) { //add immediate detection
                    //if(Character.isDigit(split[1].charAt(1))){
                    temp = split[1].replace("[", "");
                    temp = temp.replace("]", "");
                    labelCount = labelCount + 1;
                    //System.out.println(labelCount);
                    for (int i = 0; i < split.length; i++) {
                        for (String test : variableList) {
                            if (split[i].contains("[" + test + "]")) {
                                //System.out.println("found variable: " + test);
                                split[i] = split[i].replaceAll("\\[" + test + "]", test);
                                // System.out.println(split[i]);
                            }
                        }
                    }
                    registerSet.IF(">=", split[0].trim(), Integer.parseInt(temp));
                    break;
                    // }
                }
                if (!Character.isDigit(split[0].charAt(split[0].length() - 1)) && Character.isDigit(split[0].charAt(1))) { //add immediate detection
                    //if(Character.isDigit(split[1].charAt(1))){
                    temp = split[0].replace("[", "");
                    temp = temp.replace("]", "");
                    registerSet.ANDI(identifier, Integer.parseInt(temp), split[1].trim());
                    labelCount = labelCount + 1;
                    //System.out.println(labelCount);
                    for (int i = 0; i < split.length; i++) {
                        for (String test : variableList) {
                            if (split[i].contains("[" + test + "]")) {
                                //System.out.println("found variable: " + test);
                                split[i] = split[i].replaceAll("\\[" + test + "]", test);
                                // System.out.println(split[i]);
                            }
                        }
                    }
                    registerSet.IF(">=", Integer.parseInt(temp), split[1].trim());
                    break;
                    // }
                }
                //else {
                labelCount = labelCount + 1;
                //System.out.println(labelCount);
                for (int i = 0; i < split.length; i++) {
                    for (String test : variableList) {
                        if (split[i].contains("[" + test + "]")) {
                            //System.out.println("found variable: " + test);
                            split[i] = split[i].replaceAll("\\[" + test + "]", test);
                            // System.out.println(split[i]);
                        }
                    }
                }
                registerSet.IF(">=", split[0].trim(), split[1].trim());
            }
            case "i<=" -> {
                split = expression.split(operator);
                split[0] = split[0].trim();
                split[1] = split[1].trim();
                if(split[0].contains("true")){
                    split[0] = "[1]";
                }
                if(split[0].contains("false")){
                    split[0] = "[0]";
                }
                if(split[1].contains("true")){
                    split[1] = "[1]";
                }
                if(split[1].contains("false")){
                    split[1] = "[0]";
                }
                // System.out.println("AND: " + split[0] + ", " + split[1]);
                if (!Character.isDigit(split[1].charAt(split[1].length() - 1)) && Character.isDigit(split[1].charAt(1))) { //add immediate detection
                    //if(Character.isDigit(split[1].charAt(1))){
                    temp = split[1].replace("[", "");
                    temp = temp.replace("]", "");
                    labelCount = labelCount + 1;
                    //System.out.println(labelCount);
                    for (int i = 0; i < split.length; i++) {
                        for (String test : variableList) {
                            if (split[i].contains("[" + test + "]")) {
                                //System.out.println("found variable: " + test);
                                split[i] = split[i].replaceAll("\\[" + test + "]", test);
                                // System.out.println(split[i]);
                            }
                        }
                    }
                    registerSet.IF("<=", split[0].trim(), Integer.parseInt(temp));
                    break;
                    // }
                }
                if (!Character.isDigit(split[0].charAt(split[0].length() - 1)) && Character.isDigit(split[0].charAt(1))) { //add immediate detection
                    //if(Character.isDigit(split[1].charAt(1))){
                    temp = split[0].replace("[", "");
                    temp = temp.replace("]", "");
                    registerSet.ANDI(identifier, Integer.parseInt(temp), split[1].trim());
                    labelCount = labelCount + 1;
                    //System.out.println(labelCount);
                    for (int i = 0; i < split.length; i++) {
                        for (String test : variableList) {
                            if (split[i].contains("[" + test + "]")) {
                                //System.out.println("found variable: " + test);
                                split[i] = split[i].replaceAll("\\[" + test + "]", test);
                                // System.out.println(split[i]);
                            }
                        }
                    }
                    registerSet.IF("<=", Integer.parseInt(temp), split[1].trim());
                    break;
                    // }
                }
                //else {
                labelCount = labelCount + 1;
                //System.out.println(labelCount);
                for (int i = 0; i < split.length; i++) {
                    for (String test : variableList) {
                        if (split[i].contains("[" + test + "]")) {
                            //System.out.println("found variable: " + test);
                            split[i] = split[i].replaceAll("\\[" + test + "]", test);
                            // System.out.println(split[i]);
                        }
                    }
                }
                registerSet.IF("<=", split[0].trim(), split[1].trim());
            }
            // }


            case "else" -> registerSet.ELSE();
            case "od" -> registerSet.OD();
            case "fi" -> registerSet.FI();
        }
    }

    public static void Search(Map<String, ASTNode<String>> nodeMap, String root){
        /*
        Search down and left through child nodes and write to a visited set once it has been checked. if all children are valid, meaning they are the actual arguments print them with the node name being the destination if it isn't an assignment
        for example: expression z - 1 will happen if those children are all valid and then would have a destination of the node name: [1 2 3 4]1 z - 1

         */






        Map<Integer, CFG> controlGraph = new HashMap<>();

        Map<String, operandNode<String>> operandMap = new HashMap<>();
        List<String> resolved = new ArrayList<>();
        List<String> open = new ArrayList<>();
        List<String> order = new ArrayList<>();
        elseStack = new Stack<>();
        String activeNode = root;

        boolean found;

        open.add(activeNode);
        while(!open.isEmpty()){ //while some nodes are not resolved
            String[] activeChildren = nodeMap.get(activeNode).getChildren();
            for (String temp: activeChildren){
                //System.out.println("node id: " + nodeMap.get(activeNode).data + " child: " + temp);
            }
            found = false;
            for(int j = 0; j < activeChildren.length && !found; j ++){ //we want to check if a child is an operator or a valid input which we can do by checking if that node has a ]+digit in it.
                if(!resolved.contains(activeChildren[j])) {
                    //System.out.println("checking child: " + activeChildren[j]);
                    if(activeChildren[j].contains("<EOF>")){
                        found = true;
                        resolved.add((activeNode));
                        open.remove(activeNode);
                    }
                    for (int k = 0; k < nameIdentifiers.length && !found; k++) { //check if the child is a node
                        if (activeChildren[j].trim().contains(nameIdentifiers[k])) { //if the child is a name for a node on the tree
                            found = true;
                            open.add(activeChildren[j]);
                            activeNode = activeChildren[j];
                        }
                    }

                    for (int k = 0; k < operatorIdentifier.length && !found; k++) { //check if the child is an operator
                        if (activeChildren[j].trim().contains(operatorIdentifier[k])) { //if the child is a name for an operator
                            found = true;
                            resolved.add(activeChildren[j]);
                            //System.out.println("Child is operator: " + activeChildren[j]);
                        }
                    }
                    if (!found) { // it isn't an operator or a node meaning that it is a variable or a number
                        found = true;
                        resolved.add(activeChildren[j]);
                        open.remove(activeNode);
                       // System.out.println("open: " + open);
                    }

                }
            }

            boolean testChildren = true;
            for (String activeChild : activeChildren) { //check if all children are in the resolved list. If they are then we should add this node to resolved and take it out of open
                if (!resolved.contains(activeChild)) {
                    testChildren = false;
                    break;
                }
            }
            if (testChildren){
                StringBuilder exprBuilder = new StringBuilder();
                for(int i = 0; i < activeChildren.length; i ++){
                    if(i == 0) {
                        exprBuilder = new StringBuilder(activeChildren[i]);
                    }else{
                        if(activeChildren[i].trim().equals(">".trim()) && nodeMap.get(nodeMap.get(activeNode).getParent()).getChildren()[0].trim().contains("while".trim())) {
                            activeChildren[i] = "w>";
                        }
                        if(activeChildren[i].trim().equals("<".trim()) && nodeMap.get(nodeMap.get(activeNode).getParent()).getChildren()[0].trim().contains("while".trim())) {
                            activeChildren[i] = "w<";
                        }
                        if(activeChildren[i].trim().equals(">=".trim()) && nodeMap.get(nodeMap.get(activeNode).getParent()).getChildren()[0].trim().contains("while".trim())) {
                            activeChildren[i] = "w>=";
                        }
                        if(activeChildren[i].trim().equals("<=".trim()) && nodeMap.get(nodeMap.get(activeNode).getParent()).getChildren()[0].trim().contains("while".trim())) {
                            activeChildren[i] = "w<=";
                        }
                        if(activeChildren[i].trim().equals("=".trim()) && nodeMap.get(nodeMap.get(activeNode).getParent()).getChildren()[0].trim().contains("while".trim())) {
                            activeChildren[i] = "w=";
                        }
                        if(activeChildren[i].trim().equals("=".trim()) && nodeMap.get(nodeMap.get(activeNode).getParent()).getChildren()[0].trim().contains("if".trim())) {
                            activeChildren[i] = "i=";
                        }if(activeChildren[i].trim().equals("<".trim()) && nodeMap.get(nodeMap.get(activeNode).getParent()).getChildren()[0].trim().contains("if".trim())) {

                            activeChildren[i] = "i<";
                        }
                        if(activeChildren[i].trim().equals(">".trim()) && nodeMap.get(nodeMap.get(activeNode).getParent()).getChildren()[0].trim().contains("if".trim())) {
                            activeChildren[i] = "i>";
                        }
                        if(activeChildren[i].trim().equals(">=".trim()) && nodeMap.get(nodeMap.get(activeNode).getParent()).getChildren()[0].trim().contains("if".trim())) {
                            activeChildren[i] = "i>=";
                        }
                        if(activeChildren[i].trim().equals("<=".trim()) && nodeMap.get(nodeMap.get(activeNode).getParent()).getChildren()[0].trim().contains("if".trim())) {
                            activeChildren[i] = "i<=";
                        }
                        if(activeChildren[i].trim().equals("else".trim()) && nodeMap.get(activeNode).getChildren()[0].trim().contains("if".trim())) {
                            //System.out.println("node next to else: " + activeChildren[i-1]);
                            elseStack.push(activeChildren[i-1]);
                        }

                        exprBuilder.append(activeChildren[i]);

                    }
                }
                String expr = exprBuilder.toString();
                //System.out.println("expression : " + expr);
                if(!expr.contains(";")) { // just to filter out ; from prints to make the output look nice
                    //System.out.println("resolved expression: " + expr + " My node ID: " + activeNode);
                    operandMap.put(activeNode, new operandNode<>(expr.trim(), false));
                    //System.out.println("put: " + expr.trim() + " value: " + activeNode);
                }


                //System.out.println("expr: " + expr );
                if(expr.contains("else")){
                    //System.out.println("got else");
                    String [] thenSplit = expr.split("then");
                    String [] elseSplit = thenSplit[1].split("else");
                    int openParen = elseSplit[0].trim().lastIndexOf("[");
                    // System.out.println("DataStructureGeneration.ASTNode before else: " + elseSplit[0].trim() + "open paren: "+ openParen);
                    String substring = elseSplit[0].trim().substring(openParen);
                    //System.out.println("DataStructureGeneration.ASTNode before else: " + substring);
                    elseList.add(substring);
                }
                if(expr.contains("skip")){
                    order.add("skip");
                    //System.out.println("expr: " + expr);
                }
                boolean check = false;
                for (String nameIdentifier : nameIdentifiers) {
                    if (expr.contains(nameIdentifier)) {
                        check = true;
                    }
                }
                for (String s : constantChecker) {
                    if (expr.contains(s)) {
                        check = true;
                    }
                }
                if(!check){

                    String tempvar = expr.replaceAll("]", "");
                    tempvar = tempvar.replaceAll("\\[", "").trim();

                    if(!variableList.contains(tempvar)){
                        if(!tempvar.trim().contains("skip")) {
                            variableList.add(tempvar);
                            //System.out.println("new variable added: " + tempvar );
                        }

                    }

                }

                for(String tempoperator : operatorIdentifier){

                    if(expr.contains(tempoperator)){
                        //System.out.println(tempoperator);
                        if(tempoperator.equals("+")){
                            //System.out.println("asdfajlsdfjalksd");
                            tempoperator = "\\+";
                        }
                        if(tempoperator.equals("*")){
                            tempoperator = "\\*";
                        }
                        if(tempoperator.equals("od")){
                            operandMap.put(activeNode, new operandNode<>(expr.trim(), true));
                            order.add(activeNode);
                            break;
                        }
                        if(tempoperator.equals("fi")){
                            operandMap.put(activeNode, new operandNode<>(expr.trim(), true));
                            order.add(activeNode);
                            break;
                        }
                        if(tempoperator.equals("skip")){
                            operandMap.put(activeNode, new operandNode<>(expr.trim(), true));
                            order.add(activeNode);
                            break;
                        }
                        if(tempoperator.equals("else")){
                            // System.out.println("got the else");
                            operandMap.put(activeNode, new operandNode<>(expr.trim(), true));
                            order.add(activeNode);
                            break;
                        }

                        String[] operands = expr.split(tempoperator);
                        //System.out.println(tempoperator);
                        //System.out.println("we did it" + operands[0] + ",  "+ operands[1] );
                        for (String operand : operands) {
                            if(!operandMap.get(operand.trim()).containsExpression) {
                                expr = expr.replace(operand.trim(), operandMap.get(operand.trim()).data);
                            }

                        }

                        operandMap.put(activeNode, new operandNode<>(expr.trim(), true));
                        order.add(activeNode);
                        break;
                    }
                }

                // Detect if we have a while statement

                resolved.add(activeNode);
                open.remove(activeNode);
                activeNode = open.get(open.size()-1);
            }
        }
        //System.out.println("elselist: "+ elseList);

        for (String value : elseList) {
            order.add(order.indexOf(value) + 1, "else");
        }
        //System.out.println(order);
        ASTBuilder.createAnnotatedFile(operandMap,nodeMap,order);
        CFGBuilder.buildDotCFG(controlGraph);
        //CREATE CONTROL FLOW GRAPH TO PERFORM OPTIMIZATIONS BEFORE WE CREATE THE ASSEMBLY

        /*
        for (String s : order) {
            // System.out.println("ordernode: " + order.get(i) + "contains expr: " + operandMap.get(order.get(i)).containsExpression);

            if (s.contains("else")) {

                System.out.println("else");
            } else if(s.contains("skip")){

                System.out.println("skip");
            } else {
                if (operandMap.get(s).containsExpression) {
                    String operation = operandMap.get(s).data;

                    System.out.println("expression: " + operation + " node id: " + s + " parent node: " + nodeMap.get(s).getParent());

                }
            }
        }

         */





        controlGraph = CFGBuilder.createCFG(operandMap,nodeMap,order);
        CFGBuilder.buildDotCFG(controlGraph);
        for(int i = 0; i <controlGraph.size(); i++){
            CFG tempnode = controlGraph.get(i);
            //System.out.println("Basicblock size: " + tempnode.basicBlock.size());
           // System.out.println("nodename size: " + tempnode.nodeNames.size());
            for(int j = 0; j < tempnode.basicBlock.size(); j++){
                //System.out.println("block expr " + j + ":" + tempnode.basicBlock.get(j));
               // System.out.println("block name " + j + ":" + tempnode.nodeNames.get(j));
            }
        }
        Optimizer optimizer = new Optimizer(controlGraph, variableList);
        optimizer.optimizeDefault();
        Map<Integer, CFG> optimizedCFG = optimizer.getOptimizedCFG();



         // optimizedCFG or controlGraph



        List<String> optimizedvariableList = new ArrayList<>();
        List<Integer> elsePos = new ArrayList<>();
        List<Integer> fiPos = new ArrayList<>();
        List<Integer> odPos = new ArrayList<>();
        //System.out.println("Optimized cfg testing");
        for (int i = 0; i < optimizedCFG.size(); i ++){
            CFG block = optimizedCFG.get(i);
            // if we have more than 1 edge we need to find where to place the fi/else/od
            if(block.edges.size() > 1){
                for(int j = 0; j < branchesList.length; j++) { //check each type of branch
                    if (block.basicBlock.get(0).contains(branchesList[j])){
                        if(branchesList[j].contains("i")){ // if
                            elsePos.add(block.edges.get(0)); //add the identifier to put the else after
                            fiPos.add(block.edges.get(1)); //add the identifier to put the fi after
                        }else{ //while
                            odPos.add(block.edges.get(1)); //add the identifier to put the od after
                        }
                    }
                }
            }
            //Update the new variable list
            for(int j = 0; j < block.basicBlock.size(); j++){
                String testExpr = block.basicBlock.get(j);
                String testoperator = null ;
                for(int k = 0; k < operatorIdentifier.length; k++){
                    if(testExpr.contains(operatorIdentifier[k])){
                        testoperator = operatorIdentifier[k];
                    }
                }
                if(testoperator.equals("+")){
                    testoperator = "\\+";
                }
                if(testoperator.equals("*")){
                    testoperator = "\\*";
                }
                String[] split = testExpr.split(testoperator);
                for(int k = 0; k < split.length; k ++){ //check if both sides of the split are not node names and not a constant
                    String testsplit = split[k];
                    boolean isVar = true;
                    for(int l = 0; l < nameIdentifiers.length; l ++){ //check if name
                        if(testsplit.contains(nameIdentifiers[l])){
                            isVar = false;
                            break;
                        }
                    }
                    for(int l =0; l < constantChecker.length; l ++){ //check if constant
                        if(testsplit.contains(constantChecker[l])){
                            isVar = false;
                            break;
                        }
                    }
                    if(isVar){
                        String testTempStuff = testsplit.trim().replace("]", "");
                        testTempStuff = testTempStuff.trim().replace("[", "");
                        if(!optimizedvariableList.contains(testTempStuff.trim())) {
                            optimizedvariableList.add(testTempStuff.trim());
                        }
                    }
                }
            }


            //System.out.println(block.basicBlock.get(0));
            //System.out.println("edges: " + block.edges);
        }
        //System.out.println("Optimized list: " + optimizedvariableList);
        //System.out.println("input list: " + variableList);
        //System.out.println("");

        AssemblyFunctions registerSet = new AssemblyFunctions();
        if(optimizedvariableList.contains("true")){
            optimizedvariableList.remove("true");
        }
        if(optimizedvariableList.contains("false")){
            optimizedvariableList.remove("false");
        }
        registerSet.initializeMemory(optimizedvariableList);//now uses optimized list
        //registerSet.addHeader();

        //System.out.println("elsePos: " + elsePos);
        //System.out.println("fiPos: " + fiPos);
        //System.out.println("od: " + odPos);
        for (int i = 0; i < optimizedCFG.size(); i ++){
            CFG node = optimizedCFG.get(i);
            // if the basic block is of size 1 then we just do the expression
            //System.out.println("node id: " + i + " node block: " + node.basicBlock + " block size: " + node.basicBlock.size() + " edges: "+ node.edges);
            if( node.basicBlock.size() == 1){
                        //System.out.println("expression: " + node.basicBlock.get(0) + " nodeName: " + node.nodeNames.get(0));
                        decode(node.basicBlock.get(0), node.nodeNames.get(0), registerSet);
            }
            //multi-line expression should have a queue where each instruction feeds into the one below it

            if(node.basicBlock.size() > 1){
                for(int j = 0; j < node.basicBlock.size();j++){
                    String tempExpr = node.basicBlock.get(j);
                    String tempIdent = node.nodeNames.get(j);
                    decode(tempExpr, tempIdent, registerSet);
                }
            }
            // if the current node is in the else/fi/odlist we need to do the corresponding operation
            if(elsePos.contains(i)){
                decode("else", null, registerSet);
            }
            if(fiPos.contains(i)){
                decode("fi", null, registerSet);
            }
            if(odPos.contains(i)){
                decode("od", null, registerSet);
            }
        }
        registerSet.saveMemory();

/*
        for (String s : order) {
            // System.out.println("ordernode: " + order.get(i) + "contains expr: " + operandMap.get(order.get(i)).containsExpression);

            if (s.contains("else")) {
                decode("else", null, registerSet);
                //System.out.println("else");
            } else if(s.contains("skip")){
                decode("skip", null, registerSet);
                //System.out.println("skip");
            } else {
                if (operandMap.get(s).containsExpression) {
                    String operation = operandMap.get(s).data;

                    //System.out.println("expression: " + operation + " node id: " + s + " parent node: " + nodeMap.get(s).getParent());
                    decode(operation, s, registerSet);

                }
            }
        }


 */

        //todo:

        CodeGenerator codeGenerator = new CodeGenerator("test",
                optimizedvariableList.toArray(new String[0]));
        CodeStringData codeStringData = codeGenerator.getStringData();
        registerSet.addPrefix(codeStringData.getAssemblyPrefix());
        registerSet.addSuffix(codeStringData.getAssemblySuffix());
        String directoryPath = ".\\CompiledCode\\" + codeGenerator.getPlainName();
        new File(directoryPath).mkdirs();
        directoryPath += "\\";
        registerSet.printToFile(directoryPath + codeStringData.getAssemblyFileName());
        codeGenerator.printToFile(directoryPath);


    }

}
