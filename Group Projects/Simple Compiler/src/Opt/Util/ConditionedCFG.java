package Opt.Util;

import DataStructureGeneration.CFG;
import Opt.Util.Tokens.*;
import Opt.Util.Pairs.*;

import java.util.*;

public class ConditionedCFG {
    public LinkedList<Statement> statements = new LinkedList<>();

    public ConditionedCFG() {

    }

    public void addStatement(Statement statement) {
        statements.add(statement);
    }

    public int getSize() {
        return statements.size();
    }


    public Label[] getLabels() {
        Label[] labels = new Label[statements.size()];
        for (int i = 0; i < statements.size(); i++) {
            labels[i] = statements.get(i).label;
        }
        return labels;
    }

    public void generateEdges(Stack<Expression> annotationStack) {

        for (Expression annotation : annotationStack) {
            for (Statement source : statements) {
                for (Expression expression : source.expressions) {
                    // add equals and hashcode override to position
                    if (expression.outgoingEdges.contains(annotation.originalPosition)) {
                        expression.outgoingEdges.remove(annotation.originalPosition);
                        expression.outgoingEdges.union(annotation.outgoingEdges);
                    }
                }
            }
        }

        for (Statement source : statements) {
            for (Expression expression : source.expressions) {
                for (Position position : expression.outgoingEdges) {
                    Label destination = locationOf(position);
                    if (destination == null) {
                        source.successors.add(new Label(0));
                    } else if (destination != source.label) {
                        source.successors.add(destination);
                        statements.get(destination.value - 1).predecessors.add(source.label);
                    }
                }
            }
        }

        for (Statement statement : statements) {
            if (statement.successors.isEmpty()) {
                statement.successors.add(new Label(0));
            }
        }
    }

    public Label locationOf(Position position) {
        for (Statement statement : statements) {
            for (Expression expression : statement.expressions) {
                Position tempPos = expression.originalPosition;
                if (tempPos.line == position.line && tempPos.node == position.node) return statement.label;
            }
        }
        return null;
    }

    public void printOut(String title) {
        String[][] statementPrints = new String[statements.size()][4];
        for (int i = 0; i < statements.size(); i++) {
            String[] response = statements.get(i).printOut();
            for (int j = 0; j < 4; j++) {
                statementPrints[i][j] = response[j];
                if (i == 0 && j == 0) {
                    if (statementPrints[i][j].equals(" ->"))
                        statementPrints[i][j] = "Start" + statementPrints[i][j];
                    else if (statementPrints[i][j].equals("Start ->")) continue;
                    else
                        statementPrints[i][j] = "Start, " + statementPrints[i][j];
                }
            }
        }

        int[] maxLengths = { 0, 0, 0, 0 };

        for (int i = 0; i < statements.size(); i++) {
            for (int j = 0; j < 4; j++) {
                maxLengths[j] = max(statementPrints[i][j].length(), maxLengths[j]);
            }
        }

        String tableLabelZero = "Predecessors:";
        String tableLabelOne = "Label:";
        String tableLabelTwo = "Statement:";
        String tableLabelThree = "Successors:";

        maxLengths[0] = max(maxLengths[0], tableLabelZero.length());
        maxLengths[1] = max(maxLengths[1], tableLabelOne.length());
        maxLengths[2] = max(maxLengths[2], tableLabelTwo.length());
        maxLengths[3] = max(maxLengths[3], tableLabelThree.length());

        title = " " + title + " ";
        int totalLength = Arrays.stream(maxLengths).sum() + 10;
        if (totalLength > title.length()) {
            int padding = (totalLength - title.length())/2;
            StringBuilder paddingString = new StringBuilder();
            for (int i = 0; i < padding; i++) paddingString.append("=");
            title = paddingString.toString() + title + paddingString.toString();
        }

        System.out.println();
        System.out.println(title);

        System.out.printf("%-" + (maxLengths[0]) + "s   ", tableLabelZero);
        System.out.printf("%" + (maxLengths[1]) + "s   ", tableLabelOne);
        System.out.printf("%-" + (maxLengths[2]) + "s   ", tableLabelTwo);
        System.out.printf("%-" + (maxLengths[3]) + "s\n", tableLabelThree);
        for (int i = 0; i < statementPrints.length; i++) {
            System.out.printf("%" + (maxLengths[0]) + "s   ", statementPrints[i][0]);
            System.out.printf("%" + (maxLengths[1]) + "s   ", statementPrints[i][1]);
            System.out.printf("%-" + (maxLengths[2]) + "s   ", statementPrints[i][2]);
            System.out.printf("%-" + (maxLengths[3]) + "s\n", statementPrints[i][3]);
        }
        System.out.println();
    }

    private int max(int first, int second) {
        if (first > second) return first;
        else return second;
    }

    public Map<Integer, CFG> toOutputForm() {
        Map<Integer, CFG> outputForm = new HashMap<>();
        int indirectCount = 0;
        for (Statement statement : statements) {
            int identifier = statement.label.value - 1;
            ArrayList<String> basicBlock = new ArrayList<>();
            ArrayList<Integer> edges = new ArrayList<>();
            ArrayList<String> nodeNames = new ArrayList<>();

            for (Expression expression : statement.expressions) {
                if (!expression.exprType.equals(Expression.ExprType.ANNOTATION)) {
                    String left = "[" + expression.getLeftString() + "]";
                    if (expression.leftVal.isInd()) {
                        left = ((Indirect)expression.leftVal).indExprName;
                    }
                    String right = "[" + expression.getRightString() + "]";
                    if (expression.rightVal.isInd()) {
                        right = ((Indirect)expression.rightVal).indExprName;
                    }
                    String operator = " " + expression.operator.getOperator() + " ";
                    String fullString = left + operator + right;
                    basicBlock.add(fullString);
                    nodeNames.add(expression.nodeName);
                }
            }

            for (Label label : statement.successors) {
                edges.add(label.value - 1);
            }
            outputForm.put(identifier, new CFG(identifier, basicBlock, edges, nodeNames));
        }
        return outputForm;
    }

    public Statement getStatWithLbl(Label label) {
        for (Statement statement : statements) {
            if (statement.label.equals(label)) return statement;
        }
        return null;
    }

    public boolean reduce() {
        boolean result = false;
        for (Statement statement : statements) {
            result = statement.reduce() || result;
        }
        return result;
    }

    public VarSet[] getGen() {
        VarSet[] gen = new VarSet[statements.size()];

        for (int i = 0; i < statements.size(); i++) {
            gen[i] = statements.get(i).getGen();
        }

        return gen;
    }

    public VarSet[] getKill() {
        VarSet[] kill = new VarSet[statements.size()];

        for (int i = 0; i < statements.size(); i++) {
            kill[i] = statements.get(i).getKill();
        }

        return kill;
    }

    public LabSet[] getSucc() {
        LabSet[] succ = new LabSet[statements.size()];

        for (int i = 0; i < statements.size(); i++) {
            succ[i] = statements.get(i).getSucc();
        }

        return succ;
    }

    public void addReturn(VarSet returnVars) {
        Statement returnStatement = new ReturnStatement(returnVars.get(0));
        returnStatement.label = new Label(statements.size() + 1);
        returnStatement.addExpression(new ReturnExpression(Expression.ExprType.RETURN));

        Label label = new Label(0);
        for (Statement statement : statements) {
            if (statement.successors.contains(label)) {
                statement.successors.remove(label);
                statement.successors.add(returnStatement.label);
                returnStatement.predecessors.add(statement.label);
            }
        }

        this.addStatement(returnStatement);
    }

    public void removeReturn() {

        Label returnLabel = statements.getLast().label;
        for (Statement statement : statements) {
            if ( statement.successors.contains(returnLabel) ) {
                statement.successors.remove(returnLabel);
                statement.successors.add(new Label(0));
            }
        }
        statements.removeLast();

    }

    public void removeStatements(Set<Label> labels) {

        for (Label label : labels) {

            Statement dying = getStatWithLbl(label);

            for (Statement statement : statements) {
                if (statement.successors.contains(label)) {
                    statement.successors.union(dying.successors);
                    statement.successors.remove(label);
                }
                if (statement.predecessors.contains(label)) {
                    statement.predecessors.union(dying.predecessors);
                    statement.predecessors.remove(label);
                }
            }
        }

        LinkedList<Statement> tempStatements = new LinkedList<>();
        while (!statements.isEmpty()) {
            Statement tempStat = statements.removeFirst();
            if (!labels.contains(tempStat.label)) {
                tempStatements.add(tempStat);
            }
        }
        statements = tempStatements;

        for (Statement statement : statements) {
            statement.successors.difference(labels);
            statement.predecessors.difference(labels);
        }

        condense();
    }

    public void condense() {
        LinkedList<Statement> tempStatements = new LinkedList<>();
        while (!statements.isEmpty()) {
            Statement tempStat = statements.removeFirst();
            tempStatements.add(tempStat);
            if (tempStat.label.value != tempStatements.size()) {
                Label tempLabel = new Label(tempStatements.size());
                for (Statement statement : tempStatements) {
                    if (statement.predecessors.contains(tempStat.label)) {
                        statement.predecessors.remove(tempStat.label);
                        statement.predecessors.add(tempLabel);
                    }
                    if (statement.successors.contains(tempStat.label)) {
                        statement.successors.remove(tempStat.label);
                        statement.successors.add(tempLabel);
                    }
                }
                for (Statement statement : statements) {
                    if (statement.predecessors.contains(tempStat.label)) {
                        statement.predecessors.remove(tempStat.label);
                        statement.predecessors.add(tempLabel);
                    }
                    if (statement.successors.contains(tempStat.label)) {
                        statement.successors.remove(tempStat.label);
                        statement.successors.add(tempLabel);
                    }
                }
                tempStat.label = tempLabel;
            }
        }

        statements = tempStatements;

        for (Statement statement : statements) {
            Set<Label> tempPred = new Set<>();
            for (Label label : statement.predecessors) {
                if (label.value <= statements.size()) {
                    tempPred.add(label);
                }
            }
            statement.predecessors = tempPred;

            Set<Label> tempSucc = new Set<>();
            for (Label label : statement.successors) {
                if (label.value <= statements.size()) {
                    tempSucc.add(label);
                }
            }
            statement.successors = tempSucc;
        }

    }

}
