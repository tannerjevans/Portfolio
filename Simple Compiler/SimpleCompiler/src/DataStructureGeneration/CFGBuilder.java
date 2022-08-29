package DataStructureGeneration;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

public class CFGBuilder {

    private static Boolean checkBranches(String expr){
        String [] branchIdentifiers = {"w=", "w>=", "w<=", "w<", "w>" , "i=",  "i<=", "i<","i>=", "i>" , "i>=", "od","fi", "else"};
        for(String identifier : branchIdentifiers) {
            if (expr.contains(identifier)) {
                return true;
            }
        }
        return false;
    }
    private static Boolean checkWhileStart(String expr){
        String [] branchIdentifiers = {"w=", "w>=", "w<=", "w<", "w>" };
        for(String identifier : branchIdentifiers) {
            if (expr.contains(identifier)) {
                return true;
            }
        }
        return false;
    }
    private static Boolean checkIfStart(String expr){
        String [] branchIdentifiers = { "i=",  "i<=",  "i>=", "i<", "i>"};
        for(String identifier : branchIdentifiers) {
            if (expr.contains(identifier)) {
                return true;
            }
        }
        return false;
    }

    public static Map<Integer, CFG> createCFG(Map<String, operandNode<String>> operandMap, Map<String, ASTNode<String>> nodeMap, List<String> order){
        Map<Integer, CFG> controlGraph = new HashMap<>();
        Integer identifier = 0;
        ArrayList<String> temp = new ArrayList<>();
        ArrayList<String> tempName = new ArrayList<>();
        ArrayList<String> branches = new ArrayList<>();
        ArrayList<Integer> elseBlock = new ArrayList<>();
        //find basic blocks and add to graph
        //basic block is a continuous section of code without branches out of it
        //so the first block would be from the start until the first branch
        controlGraph.put(identifier,new CFG(identifier,new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));

        for (String s : order) {
            String expr = "";
            if ((!s.contains("else") || s.contains("fi")) && !s.contains("skip")) {
                expr = operandMap.get(s).data;
            } else {
                if (s.contains("skip")){
                    expr = "skip";
                }else{
                    expr = "else";
                }
            }
            if (!checkBranches(expr)) { //checks if expression is a branch
                // if expr isn't a branch we add it to the current basic block
                temp = controlGraph.get(identifier).basicBlock;
                temp.add(expr);
                tempName = controlGraph.get(identifier).nodeNames;
                tempName.add(s);
                controlGraph.put(identifier, new CFG(controlGraph.get(identifier).identifier, temp, controlGraph.get(identifier).edges, tempName));
            } else {
                //add the branch statement to the DataStructureGeneration.CFG
                if (!expr.contains("else") || expr.contains("fi")) { //for an else we don't actually want to create a new block
                    if (controlGraph.get(identifier).basicBlock.isEmpty()) { //check for back to back end of while/if statements
                        branches.add(expr);
                        ArrayList<String> branchTest = new ArrayList<>();
                        branchTest.add(expr);
                        ArrayList<String> branchName = new ArrayList<>();
                        branchName.add(s);
                        controlGraph.put(identifier, new CFG(identifier, branchTest, new ArrayList<>(), branchName));
                    } else {
                        identifier++;
                        branches.add(expr);
                        ArrayList<String> branchTest = new ArrayList<>();
                        branchTest.add(expr);
                        ArrayList<String> branchName = new ArrayList<>();
                        branchName.add(s);
                        controlGraph.put(identifier, new CFG(identifier, branchTest, new ArrayList<>(), branchName));
                    }
                } else {
                    elseBlock.add(identifier + 1);
                }
                // move to next basic block
                identifier++;
                controlGraph.put(identifier, new CFG(identifier, new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
            }
            //System.out.println("expression: " + expr + " node id: " + s);//+  " parent node: " + nodeMap.get(order.get(i)).parent);

        }

        //System.out.println();
        //add edges for flow like with while and if statements
        for(String branch: branches){
            //System.out.println("Branch: " + branch);
        }
        //System.out.println();

        Stack<Integer> branchIdent = new Stack<>();
        Stack<Integer> ifIdent = new Stack<>();
        for(int i = 0; i < controlGraph.size(); i ++){
            ArrayList<Integer> edges = new ArrayList<>();
            //any branch will include the next node
            //System.out.println(controlGraph.get(i).basicBlock.size());
            if(controlGraph.get(i).basicBlock.size() > 0) {
                if (!controlGraph.get(i).basicBlock.get(0).contains("od")) { //end of a while loop goes back to conditional

                    edges = controlGraph.get(i).edges;
                    if (i != controlGraph.size() - 1) {
                        edges.add(i + 1);
                    }
                } else {// if this is the end of a while loop we should pop from the stack and add that to the edge set
                    Integer whileLoc = branchIdent.pop();
                    ///System.out.println("popped while: " + whileLoc);
                    edges.add(whileLoc);
                    // we should update the while start to include an edge to the next node if it exists
                    ArrayList<Integer> tempEdges = controlGraph.get(whileLoc).edges;
                    tempEdges.add(i + 1);
                    controlGraph.put(whileLoc, new CFG(controlGraph.get(whileLoc).identifier, controlGraph.get(whileLoc).basicBlock, tempEdges, controlGraph.get(whileLoc).nodeNames));
                }


                //System.out.println("testing: " + controlGraph.get(i).basicBlock.get(0));
                //System.out.println(checkIfStart(controlGraph.get(i).basicBlock.get(0)));
                //a branch can also go to the block after the branch occurs, this happens if the initial condition isn't met.
                if (checkWhileStart(controlGraph.get(i).basicBlock.get(0))) { // Check if this is the start of a branch
                    branchIdent.push(i); //push current identifier onto stack
                    //System.out.println("Check While: " + branchIdent);
                }
                if (checkIfStart(controlGraph.get(i).basicBlock.get(0))) { // Check if this is the start of a branch
                    ifIdent.push(i); //push current identifier onto stack
                    //System.out.println("Check If: " + ifIdent);
                }

                if (controlGraph.get(i).basicBlock.get(0).contains("fi")) {
                    //the end of an if should go back and point to the first else block
                    //System.out.println("end of if");
                    Integer ifLoc = ifIdent.pop();
                    ArrayList<Integer> tempEdges = controlGraph.get(ifLoc).edges;
                    tempEdges.add(elseBlock.get(0));
                    //update if to include the else statement in its flow
                    controlGraph.put(ifLoc, new CFG(controlGraph.get(ifLoc).identifier, controlGraph.get(ifLoc).basicBlock, tempEdges, controlGraph.get(ifLoc).nodeNames));
                    //update first block after if to include the current end block in its edges
                    ArrayList<Integer> tempEdgestwo = new ArrayList<>();
                    tempEdgestwo.add(i);
                    controlGraph.put(elseBlock.get(0) - 1, new CFG(controlGraph.get(elseBlock.get(0) - 1).identifier, controlGraph.get(elseBlock.get(0) - 1).basicBlock, tempEdgestwo,  controlGraph.get(elseBlock.get(0) - 1).nodeNames));
                    elseBlock.remove(0);
                }


                //put the new edges into the data structure
                controlGraph.put(i, new CFG(controlGraph.get(i).identifier, controlGraph.get(i).basicBlock, edges, controlGraph.get(i).nodeNames));
            }
        }

/*
        for(int i  = 0; i < controlGraph.size(); i ++){
            System.out.println("Identifier: " + i);
            for(int j = 0; j < controlGraph.get(i).basicBlock.size(); j ++){
                System.out.println("Basic Block Expr #" + j + " : " + controlGraph.get(i).basicBlock.get(j));
            }
            System.out.println("Edges: " + controlGraph.get(i).edges);
            System.out.println();
        }


 */









        return controlGraph;
    }




    public static void  buildDotCFG(Map<Integer, CFG> cfgMap){
        String filename = "CFG.txt";
        File file = new File(filename);

        // Delete the existing dotfile
        try{
            boolean result = Files.deleteIfExists(file.toPath());
        }catch (IOException x){
            System.err.println(x);
        }
        // create the header for the dot file
        String s = """
            digraph "DirectedGraph" {
            graph [label = "CFG", labelloc=t, concentrate = true];
            """;
        byte[] data = s.getBytes();
        Path p = Paths.get("CFG.txt");

        try (OutputStream out = new BufferedOutputStream(
                Files.newOutputStream(p, CREATE, APPEND))) {
            out.write(data, 0, data.length);
        } catch (IOException x) {
            System.err.println(x);
        }

        boolean found;
        //Correct Labels of node to show operation or operand
        for (int i = 0; i < cfgMap.size(); i ++) {
            CFG entry = cfgMap.get(i);
            s = "\"" + i + "\"" +" [ label=\""+ entry.basicBlock + "\" ]"+"\n";
            data = s.getBytes();
            try (OutputStream out = new BufferedOutputStream(
                    Files.newOutputStream(p, CREATE, APPEND))) {
                out.write(data, 0, data.length);
            } catch (IOException x) {
                System.err.println(x);
            }
        }
        //For each node in the tree print the relation it has to its children
        for (int i = 0; i < cfgMap.size(); i ++) {
            CFG entry = cfgMap.get(i);

            for (int j = 0; j < entry.edges.size(); j++){
                s = "\"" + i + "\"" + " -> " + "\"" + entry.edges.get(j) + "\"" + "\n";
                data = s.getBytes();
                try (OutputStream out = new BufferedOutputStream(
                        Files.newOutputStream(p, CREATE, APPEND))) {
                        out.write(data, 0, data.length);
                } catch (IOException x) {
                        System.err.println(x);
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





}
