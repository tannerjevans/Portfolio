package Opt;

import Opt.Util.*;
import Opt.Util.Tokens.Variable;

import java.util.Arrays;
import java.util.LinkedList;

public class LivenessAnalyzer {
    private ConditionedCFG conditionedCFG;
    private VarSet liveVarsAtEnd;
    private int numEquations;
    private VarSet[] gen;
    private VarSet[] kill;
    private LabSet[] succ;
    private VarSet[] in;
    private VarSet[] out;

    public LivenessAnalyzer(ConditionedCFG conditionedCFG, Set<Variable> liveVarsAtEnd) {
        this.conditionedCFG = conditionedCFG;
        this.liveVarsAtEnd = new VarSet();
        this.liveVarsAtEnd.union(liveVarsAtEnd);
        conditionedCFG.addReturn(this.liveVarsAtEnd);
        this.numEquations = conditionedCFG.statements.size();

        this.gen = conditionedCFG.getGen();
        this.gen[numEquations - 1].union(this.liveVarsAtEnd);
        this.kill = conditionedCFG.getKill();
        this.succ = conditionedCFG.getSucc();

        this.in = new VarSet[numEquations];
        this.out = new VarSet[numEquations];

        for (int i = 0; i < numEquations; i++) {
            this.in[i] = new VarSet();
            this.out[i] = new VarSet();
        }

        iterate();
        printOut();
        eliminateDeadCode();
    }

    private void eliminateDeadCode() {
        conditionedCFG.removeReturn();
        LinkedList<Statement> tempStatements = conditionedCFG.statements;
        Set<Label> deadStatements = new Set<>();

        for (int i = 0; i < tempStatements.size(); i++) {
            Statement statement = conditionedCFG.statements.get(i);
            if (statement.isAssignmentStatement() && !out[i].contains(statement.getAssignment())) {
                deadStatements.add(statement.label);
            }
        }
        conditionedCFG.removeStatements(deadStatements);
    }

    private void iterate() {
        boolean changes = true;
        while (changes) {
            changes = false;

            for (int i = numEquations - 1; i >= 0; i--) {
                for (Label label : succ[i]) {
                    changes = changes | out[i].union(in[label.index]);
                }
                VarSet temp = new VarSet();
                for (Variable variable : out[i]) temp.add(variable);
                temp.difference(kill[i]);
                VarSet temp2 = new VarSet();
                for (Variable variable : gen[i]) temp2.add(variable);
                temp2.union(temp);
                changes = changes | in[i].union(temp2);
            }
        }
    }

    public void printOut() {
        String title       = " Liveness Analysis ";
        String tableLab    = "i:";
        String tableSucc   = "succ[i]:";
        String tableGen    = "gen[i]:";
        String tableKill   = "kill[i]:";
        String tableInEq   = "in[i] equation:";
        String tableOutEq  = "out[i] equation:";
        String tableInRes  = "in[i] result:";
        String tableOutRes = "out[i] result:";

        int[] maxLengths = new int[8];

        maxLengths[0] = tableLab.length();
        maxLengths[1] = tableSucc.length();
        maxLengths[2] = tableGen.length();
        maxLengths[3] = tableKill.length();
        maxLengths[4] = tableInEq.length();
        maxLengths[5] = tableOutEq.length();
        maxLengths[6] = tableInRes.length();
        maxLengths[7] = tableOutRes.length();

        String[] labelStrings  = new String[numEquations];
        String[] succStrings   = getStringSet(succ);
        String[] genStrings    = getStringSet(gen);
        String[] killStrings   = getStringSet(kill);
        String[] inEqStrings   = new String[numEquations];
        String[] outEqStrings  = new String[numEquations];
        String[] inResStrings  = getStringSet(in);
        String[] outResStrings = getStringSet(out);

        Label[] labels = conditionedCFG.getLabels();
        for (int i = 0; i < labels.length; i++) {
            labelStrings[i] = Integer.toString(labels[i].value);
        }

        for (int i = 0; i < numEquations; i++) {
            int label = i + 1;

            inEqStrings[i] = "gen[" + label + "] U ( out[" + label + "] \\ kill[" + label + "] )";
            outEqStrings[i] = "";
            if (succ[i].size() == 0) outEqStrings[i] += "{ }";
            else {
                for (int j = 0; j < succ[i].size(); j++) {
                    Label temp = succ[i].get(j);
                    outEqStrings[i] += "in[" + temp.value + "]";
                    if (j < succ[i].size() - 1) {
                        outEqStrings[i] += " U ";
                    }
                }
            }
        }

        int totalLength = 0;

        maxLengths[0] = max(maxLength(labelStrings),  maxLengths[0]);
        maxLengths[1] = max(maxLength(succStrings),   maxLengths[1]);
        maxLengths[2] = max(maxLength(genStrings),    maxLengths[2]);
        maxLengths[3] = max(maxLength(killStrings),   maxLengths[3]);
        maxLengths[4] = max(maxLength(inEqStrings),   maxLengths[4]);
        maxLengths[5] = max(maxLength(outEqStrings),  maxLengths[5]);
        maxLengths[6] = max(maxLength(inResStrings),  maxLengths[6]);
        maxLengths[7] = max(maxLength(outResStrings), maxLengths[7]);

        totalLength = Arrays.stream(maxLengths).sum() + (3 * 7);
        if (totalLength > title.length()) {
            int padding = (totalLength - title.length())/2;
            StringBuilder paddingString = new StringBuilder();
            for (int i = 0; i < padding; i++) paddingString.append("=");
            title = paddingString.toString() + title + paddingString.toString();
        }

        System.out.println();
        System.out.println(title);

        System.out.printf("%-" + maxLengths[0] + "s   ", tableLab);
        System.out.printf("%-" + maxLengths[1] + "s   ", tableSucc);
        System.out.printf("%-" + maxLengths[2] + "s   ", tableGen);
        System.out.printf("%-" + maxLengths[3] + "s   ", tableKill);
        System.out.printf("%-" + maxLengths[4] + "s   ", tableInEq);
        System.out.printf("%-" + maxLengths[5] + "s   ", tableOutEq);
        System.out.printf("%-" + maxLengths[6] + "s   ", tableInRes);
        System.out.printf("%-" + maxLengths[7] + "s   ", tableOutRes);
        System.out.println();

        for (int i = 0; i < numEquations; i++) {
            System.out.printf("%-" + maxLengths[0] + "s   ", labelStrings[i]);
            System.out.printf("%-" + maxLengths[1] + "s   ", succStrings[i]);
            System.out.printf("%-" + maxLengths[2] + "s   ", genStrings[i]);
            System.out.printf("%-" + maxLengths[3] + "s   ", killStrings[i]);
            System.out.printf("%-" + maxLengths[4] + "s   ", inEqStrings[i]);
            System.out.printf("%-" + maxLengths[5] + "s   ", outEqStrings[i]);
            System.out.printf("%-" + maxLengths[6] + "s   ", inResStrings[i]);
            System.out.printf("%-" + maxLengths[7] + "s   ", outResStrings[i]);
            System.out.println();
        }

        System.out.println();
    }

    public int maxLength(String[] stringSet) {
        int max = 0;

        for (String string : stringSet) {
            if (string.length() > max) max = string.length();
        }

        return max;
    }

    public String[] getStringSet(VarSet[] varSets) {
        String[] stringSet = new String[varSets.length];

        String setStart = "{ ";
        String setEnd = " }";
        for (int i = 0; i < varSets.length; i++) {
            String temp = setStart;
            for (int j = 0; j < varSets[i].size(); j++) {
                temp += varSets[i].get(j).name;
                if (j != varSets[i].size() - 1) temp += ", ";
            }
            stringSet[i] = temp + setEnd;
        }
        return stringSet;
    }

    public String[] getStringSet(LabSet[] labSets) {
        String[] stringSet = new String[labSets.length];

        String setStart = "{ ";
        String setEnd = " }";
        for (int i = 0; i < labSets.length; i++) {
            String temp = setStart;
            for (int j = 0; j < labSets[i].size(); j++) {
                temp += labSets[i].get(j).value;
                if (j != labSets[i].size() - 1) temp += ", ";
            }
            stringSet[i] = temp + setEnd;
        }
        return stringSet;
    }

    private int max(int first, int second) {
        if (first > second) return first;
        else return second;
    }

    public ConditionedCFG getConditionedCFG() {
        return conditionedCFG;
    }
}
