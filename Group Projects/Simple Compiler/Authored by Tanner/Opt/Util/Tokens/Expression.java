package Opt.Util.Tokens;

import Opt.Util.Label;
import Opt.Util.Operator;
import Opt.Util.Pairs.*;
import Opt.Util.Set;

import static Opt.Util.Tokens.Value.castVar;

public class Expression {

    public enum ExprType {
        ASSIGNMENT,
        COMPARISON,
        ALGEBRA,
        BOOLEAN,
        ANNOTATION,
        RETURN;
    }

    public ExprType exprType;
    public Set<Position> outgoingEdges;
    public Position originalPosition;
    public String nodeName = "";

    public Operator.OpEnum operator;

    // Assignment
    public Value leftVal;
    public Value rightVal;

    public Expression() {}

    public Expression(ExprType exprType, Value leftVal, Operator.OpEnum operator, Value rightVal) {
        this.exprType = exprType;
        this.operator = operator;
        this.leftVal = leftVal;
        this.rightVal = rightVal;
        this.outgoingEdges = new Set<>();
    }

    public Expression(ExprType exprType) {
        this.exprType = exprType;
        this.operator = Operator.AnnotOp.ANNOT_OP;
        this.leftVal = null;
        this.rightVal = null;
        this.outgoingEdges = new Set<>();
    }


    public boolean isArith() {
        return this.operator instanceof Operator.ArithOp;
    }

    public boolean isAssign() {
        return this.operator instanceof Operator.AssignOp;
    }

    public boolean isComp() {
        return this.operator instanceof Operator.CompareOp;
    }

    public boolean isBool() { return this.operator instanceof Operator.BoolOp; }

    public String getLeftString() {
        if (leftVal == null) return "Indirect";
        else return (leftVal.printOut());
    }

    public String getRightString() {
        if (rightVal == null) return "Indirect";
        else return (rightVal.printOut());
    }

    public Set<Variable> getFreeVariables() {
        Set<Variable> freeVars = new Set<>();
        if (rightVal.isVar()) freeVars.add(castVar(rightVal));
        if (!this.isAssign() && leftVal.isVar()) {
            freeVars.add(castVar(leftVal));
        }
        return freeVars;
    }

    public void foldVariable(Variable var, Constant constant) {
        if (rightVal.isEq(var)) rightVal = constant;
        if (!this.isAssign() && leftVal.isEq(var)) {
            leftVal = constant;
        }
    }

    public boolean reduceable() {
        if (this.isBool() || this.isArith()) {
            if (leftVal.isConst() && rightVal.isConst()) {
                return leftVal.isBool() == rightVal.isBool();
            }
        }
        return false;
    }

    public Constant reduce() {
        int intResult = 0;
        boolean boolResult = false;
        if (this.isArith() && leftVal.isConst() && rightVal.isConst()) {
            int left = leftVal.toConst().value;
            int right = rightVal.toConst().value;
            switch ((Operator.ArithOp) this.operator) {
                case ADD -> {
                    intResult = left + right;
                }
                case MUL -> {
                    intResult = left * right;
                }
                case SUB -> {
                    intResult = left - right;
                }
                case DIV -> {
                    intResult = left / right;
                }
            }
            return new Constant(intResult);
        } else if (this.isBool() && leftVal.isBool() && rightVal.isBool()) {
            boolean left = leftVal.toConst().boolVal;
            boolean right = rightVal.toConst().boolVal;
            switch ((Operator.BoolOp) this.operator) {
                case AND -> {
                    boolResult = left && right;
                }
                case OR -> {
                    boolResult = left || right;
                }
                case NOT -> {
                    ;
                }
            }
        }
        return new Constant(boolResult);
    }

    public void replaceIndirect(Constant constant, String side) {
        if (side.equals("left"))
            leftVal = constant;
        else if (side.equals("right")) rightVal = constant;
    }

    public Set<Variable> getGen() {
        Set<Variable> gen = new Set<>();

        if (rightVal.isVar()) gen.add(rightVal.toVar());
        if ((!this.isAssign() || this.isComp() || this.isBool()) && leftVal.isVar()) gen.add(leftVal.toVar());

        return gen;
    }

    public Set<Variable> getKill() {
        Set<Variable> kill = new Set<>();
        if (this instanceof ReturnExpression) return kill;

        if (this.isAssign()) kill.add(leftVal.toVar());

        return kill;
    }


}
