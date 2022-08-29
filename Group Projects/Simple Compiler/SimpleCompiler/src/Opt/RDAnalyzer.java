package Opt;

import Opt.Util.*;
import Opt.Util.Tokens.Constant;
import Opt.Util.Tokens.Expression;
import Opt.Util.Tokens.Variable;

public class RDAnalyzer {
    private ConditionedCFG conditionedCFG;
    private final String[] VARS;

    private int numberOfStatements;
    private EntryRD[] entryRDs; // The entry RD structures.
    private ExitRD[]  exitRDs;  // The exit RD structures.

    public RDAnalyzer(ConditionedCFG conditionedCFG, String[] vars) {
        this.conditionedCFG = conditionedCFG;
        this.VARS = vars;
        initialize();
        calculateRDs();
    }

    private void initialize() {
        numberOfStatements = conditionedCFG.statements.size();
        entryRDs = new EntryRD[numberOfStatements];
        exitRDs = new ExitRD[numberOfStatements];
        for (int i = 0; i < numberOfStatements; i++) {
            entryRDs[i] = new EntryRD(i+1);
            exitRDs[i] = new ExitRD(i+1);
        }
        for (int i = 0; i < numberOfStatements; i++) {
            Statement statement = conditionedCFG.statements.get(i);
            for (Label label : statement.predecessors)
                entryRDs[i].addPred(label.value);
            if (statement.expressions.getLast().operator == Operator.AssignOp.SET_EQ) {
                String assignment = statement.expressions.getLast().getLeftString();
                exitRDs[i].setAssignment(assignment);
            }
        }
    }

    public void calculateRDs() {
        do {
            for (int i = 0; i < numberOfStatements; i++) {
                entryRDs[i].transfer();
                exitRDs[i].transfer();
            }
            for (int i = 0; i < numberOfStatements; i++) {
                entryRDs[i].calculateNextIter();
                exitRDs[i].calculateNextIter();
            }
        } while (setsChanged());
        printRDEquations();
        printRDSets();
    }

    public ConditionedCFG fold() {
        conditionedCFG.reduce();

        boolean changes = true;
        boolean foldChange = false;
        boolean reduceChange = false;

        while (changes) {
            foldChange = false;
            reduceChange = false;
            for (Statement statement : conditionedCFG.statements) {
                Set<Variable> freeVariables = statement.getFreeVariables();
                int sourceLabel = statement.label.value;
                EntryRD entryRD = entryRDs[sourceLabel - 1];
                Set<Definition> entryDefs = new Set<>();
                for (Variable var : freeVariables) {
                    boolean doNotAdd = false;
                    int count = 0;
                    for (Definition def : entryRD.getCurr()) {
                        if (def.VAR.equals(var.name)) {
                            count++;
                            if (def.LBL == 0) doNotAdd = true;
                        }
                    }
                    if (count == 1 && !doNotAdd) {
                        for (Definition def : entryRD.getCurr()) {
                            if (def.VAR.equals(var.name)) entryDefs.add(def);
                        }
                    }
                }

                for (Definition def : entryDefs) {
                    if (conditionedCFG.statements.get(def.LBL - 1).assignsToConstant()) {
                        Constant constant = conditionedCFG.statements.get(def.LBL - 1).getConstantAssigned();
                        statement.foldConstant(new Variable(def.VAR), constant);
                        foldChange = true;
                    }
                }

            }
            reduceChange = conditionedCFG.reduce();
            changes = foldChange || reduceChange;
        }
        return conditionedCFG;
    }

    public void printRDEquations() {
        String labels = "";
        for (int i = 1; i <= numberOfStatements; i++) {
            labels += i;
            labels += ", ";
        }
        labels = "Labels    = { " + labels + "? }";
        String vars = "";
        for (int i = 0; i < VARS.length; i++) {
            vars += VARS[i];
            if (i != VARS.length - 1) vars += ", ";
        }
        vars = "Variables = { " + vars + " }";
        System.out.println("=============== Reaching Definition Equations ===============");
        System.out.println(labels);
        System.out.println(vars);
        System.out.println();
        for (int i = 0; i < numberOfStatements; i++) {
            String definitionEquation = "RD_in(" + (i + 1) + ") = ";
            System.out.printf("%16s", definitionEquation);
            definitionEquation = "";
            if (i == 0) {
                definitionEquation += "{ ";
                for (int j = 0; j < VARS.length; j++) {
                    definitionEquation += "(" + VARS[j] + ", ?)";
                    if (j != VARS.length - 1) definitionEquation += ", ";
                }
                definitionEquation += " }";
            } else {
                Statement statement = conditionedCFG.statements.get(i);
                Set<Label> preds = statement.predecessors;
                for (int j = 0; j < preds.size(); j++) {
                    definitionEquation += "RD_out(" + preds.get(j).value + ")";
                    if (j != preds.size() - 1) definitionEquation += " U ";
                }
            }
            System.out.println(definitionEquation);
        }
        System.out.println();

        for (int i = 0; i < numberOfStatements; i++) {
            String definitionEquation = "RD_out(" + (i + 1) + ") = RD_in(" + (i + 1) + ")";
            System.out.printf("%24s", definitionEquation);
            definitionEquation = "";
            Statement statement = conditionedCFG.statements.get(i);
            Expression expression = statement.expressions.getLast();
            if (expression.isAssign()) {
                String var = expression.getLeftString();
                definitionEquation += " \\ { (" + var + ", L) | L is an element of Labels } U { ("
                                          + var + ", " + (i + 1) + ") }";
            }
            System.out.println(definitionEquation);
        }
        System.out.println();
    }

    public void printRDSets() {
        System.out.println("=============== Reaching Definition Sets ===============");
        for (int i = 0; i < numberOfStatements; i++) {
            System.out.printf("RD_in(%3s):   { ", i + 1);
            for (Definition definition : entryRDs[i].getCurr()) {
                String label = "";
                if (definition.LBL == 0) label = "?";
                else label += definition.LBL;
                System.out.printf("(%s, %s) ", definition.VAR, label);
            }
            System.out.println(" }");
        }
        System.out.println();
        for (int i = 0; i < numberOfStatements; i++) {
            System.out.printf("RD_out(%3s):  { ", i + 1);
            for (Definition definition : exitRDs[i].getCurr()) {
                String label = "";
                if (definition.LBL == 0) label = "?";
                else label += definition.LBL;
                System.out.printf("(%s, %s) ", definition.VAR, label);
            }
            System.out.println(" }");
        }
    }

    // Short-circuit testing for any set having changed.
    private boolean setsChanged() {
        for (int i = 0; i < numberOfStatements; i++)
            if (entryRDs[i].isChanged() || exitRDs[i].isChanged()) return true;
        return false;
    }

    // A class to define a Definition as a tuple of a variable and an expression label.
    // Also provides functionality for comparison to enable easy sorting.
    public class Definition implements Comparable<Definition> {
        final String VAR;
        final int LBL;

        public Definition(String var, int lbl) {
            this.VAR = var;
            this.LBL = lbl;
        }

        public int compareTo(Definition definition) {
            if (!this.VAR.equals(definition.VAR)) {
                return this.VAR.compareTo(definition.VAR);
            } else {
                return this.LBL - definition.LBL;
            }
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null) return false;
            if (this.getClass() != o.getClass()) return false;
            Definition definition = (Definition) o;
            return (this.VAR.equals(definition.VAR) && this.LBL == definition.LBL);
        }
    }


    // The superclass for Entry and Exit RDs, to reduce repetition and enable polymorphism.
    private abstract class RD {
        // Subclasses must define their behavior during each iteration.
        public abstract void calculateNextIter();

        private final Set<Definition> prev; // The RD set obtained on the previous iteration.
        private final Set<Definition> curr; // The working RD set on the current iteration.

        public RD() {
            curr = new Set<>();
            prev = new Set<>();
        }
        public boolean isChanged() {
            prev.sort(Definition::compareTo);
            curr.sort(Definition::compareTo);
            boolean changes = false;
            if (prev.size() != curr.size()) {
                changes = true;
            } else {
                for (int i = 0; i < prev.size(); i++) {
                    if (!prev.get(i).equals(curr.get(i))) changes = true;
                }
            }
            return changes;
        }
        // For use after determination that a fixed point was not reached.
        // Moves curr to prev in preparation for next iteration.
        public void transfer() {
            prev.clear();
            prev.addAll(curr);
            curr.clear();
        }

        // Get previous iteration's set.
        public Set<Definition> getPrev() { return prev; }
        public Set<Definition> getCurr() { return curr; }
        // Add a provided definition to current iteration's set.
        public void add(Definition definition) { if (!curr.contains(definition)) curr.add(definition); }
        // Add a provided set's definitions to current iteration's set.
        public void union(Set<Definition> newSet) { for (Definition definition : newSet) add(definition); }
        // Remove all definitions for a provided variable from current iteration's set.
        public void clearVar(String var) { curr.removeIf(def -> def.VAR.equals(var)); }
    }

    // The subclass of RD that is used for Entry RD sets and equations.
    private class EntryRD extends RD {
        // A flag for the first Entry RD set, since it behaves uniquely.
        private boolean firstEntryRD = false;
        // The set of initial definitions for use by the first Entry RD set.
        private Set<Definition> init;
        // A list of the labels which are predecessors to this Entry RD.
        private Set<Integer> preds;

        public EntryRD(int label) {
            preds = new Set<>();
            init = new Set<>();
            if (label == 1) {
                firstEntryRD = true;
                for (String var : VARS) init.add(new Definition(var, 0));
            }
        }

        // Add a label to the list of predecessors.
        public void addPred(int pred) { preds.add(pred); }
        // Behavior for Entry RD equations.
        @Override
        public void calculateNextIter() {
            if (firstEntryRD) {
                this.union(init);
            } else {
                for (Integer pred : preds) {
                    this.union(exitRDs[pred-1].getPrev());
                }
            }
        }
    }

    // The subclass of RD that is used for Exit RD sets and equations.
    private class ExitRD extends RD {
        // The Exit RD label, since Exit RD equations always depend on their label's Entry RD.
        private final int LABEL;
        // If relevant, the definition given by the assignment in the labelled expression.
        private String assignment = null;

        public ExitRD(int label) { LABEL = label; }

        // Set the assignment if one exists.
        public void setAssignment(String var) { assignment = var; }
        // Behavior for Exit RD equations.
        @Override
        public void calculateNextIter() {
            this.union(entryRDs[LABEL-1].getPrev());
            if (assignment != null) {
                this.clearVar(assignment);
                this.add(new Definition(assignment, LABEL));
            }
        }
    }

}
