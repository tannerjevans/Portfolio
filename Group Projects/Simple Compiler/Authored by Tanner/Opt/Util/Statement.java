package Opt.Util;

import Opt.Util.Tokens.*;
import Opt.Util.Tokens.Expression.*;

import java.util.LinkedList;

public class Statement {
    public LinkedList<Expression> expressions;
    public Set<Label> predecessors;
    public Set<Label> successors;
    public Label label;

    public Statement() {
        label = new Label(0);
        expressions = new LinkedList<>();
        predecessors = new Set<>();
        successors = new Set<>();
    }

    public void addExpression(Expression expression) {
        expressions.add(0, expression);
    }

    public String[] printOut() {
        String[] response = new String[4];
        LinkedList<String> redirects = new LinkedList<>();
        LinkedList<String> redirectNames = new LinkedList<>();
        response[1] = "" + label.printOut() + ":";
        for (Expression expression : expressions) {
            String first = "";
            String operator;
            if (expression.exprType == ExprType.ANNOTATION) operator = "";
            else operator = " " + expression.operator.getOperator() + " ";
            String second = "";
            String fullString = "";
            boolean firstIndirect = (expression.leftVal instanceof Indirect);
            boolean secondIndirect = (expression.rightVal instanceof Indirect);
            first = expression.getLeftString();
            second = expression.getRightString();
            if (firstIndirect && !redirects.isEmpty()) {
                for (int i = 0; i < redirectNames.size(); i++) {
                    if (redirectNames.get(i).equals(((Indirect) expression.leftVal).indExprName)) {
                        first = "(" + redirects.remove(i) + ")";
                        redirectNames.remove(i);
                    }
                }
            }
            if (secondIndirect && !redirects.isEmpty()) {
                for (int i = 0; i < redirectNames.size(); i++) {
                    if (redirectNames.get(i).equals(((Indirect) expression.rightVal).indExprName)) {
                        second = "(" + redirects.remove(i) + ")";
                        redirectNames.remove(i);
                    }
                }
            }
            fullString = first + operator + second;
            switch (expression.exprType) {
                case ANNOTATION:
                    break;
                case BOOLEAN:
                case ALGEBRA:
                    redirects.add(fullString);
                    redirectNames.add(expression.nodeName);
                    break;
                case ASSIGNMENT:
                case COMPARISON:
                    response[2] = fullString;
                    break;
            }
        }
        String temp = "";
        if (label.value == 1) {
            temp += "Start";
            if (predecessors.size() != 0) temp += ", ";
        }
        for (int i = 0; i < predecessors.size(); i++) {
            temp += predecessors.get(i).printOut();
            if (i != predecessors.size() - 1) {
                temp += ", ";
            }
        }
        temp += " ->";
        response[0] = temp;

        temp = "-> ";
        for (int i = 0; i < successors.size(); i++) {
            temp += successors.get(i).printOut();
            if (i != successors.size() - 1) {
                temp += ", ";
            }
        }
        response[3] = temp;

        return response;
    }

    public Set<Variable> getFreeVariables() {
        Set<Variable> freeVars = new Set<>();
        for (Expression expression : expressions) {
            freeVars.addAll(expression.getFreeVariables());
        }
        return freeVars;
    }

    public boolean assignsToConstant() {
        return expressions.getLast().rightVal.isConst();
    }

    public Constant getConstantAssigned() {
        return expressions.getLast().rightVal.toConst();
    }

    public void foldConstant(Variable variable, Constant constant) {
        for (Expression expression : expressions) {
            expression.foldVariable(variable, constant);
        }
    }

    public boolean reduce() {
        LinkedList<Pairs.Pair<String, Constant>> constants = new LinkedList<>();
        LinkedList<Expression> newExpressionList = new LinkedList<>();
        boolean result = false;

        for (Expression expression : this.expressions) {
            if (!constants.isEmpty()) {
                if (expression.leftVal.isInd()){
                    String test = expression.leftVal.toInd().indExprName;
                    for (Pairs.Pair pair : constants) {
                        if (test.equals(pair.first)) {
                            expression.replaceIndirect((Constant) pair.second, "left");
                        }
                    }
                }
            }
            if (!constants.isEmpty()) {
                if (expression.rightVal.isInd()){
                    String test = expression.rightVal.toInd().indExprName;
                    for (Pairs.Pair pair : constants) {
                        if (test.equals(pair.first)) {
                            expression.replaceIndirect((Constant) pair.second, "right");
                        }
                    }
                }
            }
            if (expression.reduceable()) {
                result = true;
                constants.add(new Pairs.Pair<>(expression.nodeName, expression.reduce()));
            } else {
                newExpressionList.add(expression);
            }
        }

        this.expressions = newExpressionList;
        return result;
    }


    public VarSet getGen() {
        VarSet gen = new VarSet();

        for (Expression expression : expressions) {
            gen.union(expression.getGen());
        }

        return gen;
    }

    public VarSet getKill() {
        VarSet kill = new VarSet();

        for (Expression expression : expressions) {
            kill.union(expression.getKill());
        }

        return kill;
    }

    public LabSet getSucc() {
        LabSet succ = new LabSet();

        for (Label label : successors) {
            succ.add(label);
        }

        return succ;
    }

    public boolean isAssignmentStatement() {
        return expressions.getLast().operator.equals(Operator.AssignOp.SET_EQ);
    }

    public Variable getAssignment() {
        return expressions.getLast().leftVal.toVar();
    }

}
