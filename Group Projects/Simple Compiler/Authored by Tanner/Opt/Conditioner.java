package Opt;

import DataStructureGeneration.CFG;
import Opt.Util.*;
import Opt.Util.Pairs.*;
import Opt.Util.Tokens.*;
import Opt.Util.Operator.*;
import Opt.Util.Tokens.Expression.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;

public class Conditioner {
    ConditionedCFG conditionedCFG;
    Stack<Expression> expressionStack = new Stack<>();
    Stack<Expression> annotationStack = new Stack<>();

    public Conditioner(Map<Integer, CFG> cfg) {
        conditionedCFG = new ConditionedCFG();
        convertToConditionedCFG(cfg);
        conditionedCFG.generateEdges(annotationStack);
    }

    public ConditionedCFG getConditionedCFG() {
        return conditionedCFG;
    }

    private void convertToConditionedCFG(Map<Integer, CFG> cfg) {
        // Loop over nodes i and lines j
        LinkedList<Expression> indirects = new LinkedList<>();
        for (int i = 0; i < cfg.size(); i++) {

            ArrayList<String> block = cfg.get(i).basicBlock;
            for (int j = 0; j < block.size(); j++) {

                String line = block.get(j);
                String name = cfg.get(i).nodeNames.get(j);
                String[] splitStr;
                OpEnum op = null;
                Expression expression;
                ExprType exprType = null;

                if (null != (op = splitIfAny(line, AssignOp.values(), CompareOp.values(), ArithOp.values(), BoolOp.values()))) {
                    if (op == ArithOp.ADD) {
                        splitStr = line.split("\\+");
                    } else if (op == ArithOp.MUL) {
                        splitStr = line.split("\\*");
                    } else if (op.equals(BoolOp.OR)) {
                        splitStr =  line.split(" or\\[");
                        splitStr[1] = "[" + splitStr[1];
                    }
                    else if (op.equals(BoolOp.AND)) {
                        splitStr = line.split(" and\\[");
                        splitStr[1] = "[" + splitStr[1];
                    }
                    else if (op.equals(BoolOp.NOT)) {
                        splitStr = line.split("not\\(");
                        splitStr[1] = "[" + splitStr[1];
                    } else {
                        splitStr = line.split(op.getOperator());
                    }

                    if (op instanceof AssignOp) exprType = ExprType.ASSIGNMENT;
                    else if (op instanceof CompareOp) exprType = ExprType.COMPARISON;
                    else if (op instanceof BoolOp) exprType = ExprType.BOOLEAN;
                    else exprType = ExprType.ALGEBRA;

                    Value firstValue = Value.parseValue(splitStr[0].trim());
                    Value secondValue = Value.parseValue(splitStr[1].trim());

                    expression = new Expression(exprType, firstValue, op, secondValue);


                } else {
                    exprType = ExprType.ANNOTATION;
                    expression = new Expression(exprType, null, null, null);
                }

                expression.nodeName = name;

                expression.originalPosition = new Position(i, j);

                if (j < block.size() - 1) {
                    expression.outgoingEdges.add(new Position(i, j+1));
                } else if (j == block.size() - 1) {
                    for (int edge : cfg.get(i).edges) {
                        expression.outgoingEdges.add(new Position(edge, 0));
                    }
                }

                if (exprType == ExprType.ANNOTATION) {
                    annotationStack.push(expression);
                    continue;
                }
                expressionStack.push(expression);

                if (exprType == ExprType.ASSIGNMENT || exprType == ExprType.COMPARISON) {
                    Statement statement = new Statement();
                    while (!expressionStack.isEmpty()) {
                        statement.addExpression(expressionStack.pop());
                    }
                    statement.label = new Label(conditionedCFG.statements.size() + 1);
                    conditionedCFG.addStatement(statement);
                }
            }
        }
    }

    private OpEnum splitIfAny(String line, OpEnum[] ... arguments) {
        String[] splitString;
        for (OpEnum[] opEnums : arguments) {
            for (OpEnum opEnum : opEnums) {
                String operator = opEnum.getOperator();
                if (operator.equals("+")) splitString = line.split("\\+");
                else if (operator.equals("*")) splitString = line.split("\\*");
                else if (operator.equals("or")) splitString = line.split(" or\\[");
                else if (operator.equals("and")) splitString = line.split(" and\\[");
                else if (operator.equals("not")) splitString = line.split("not\\(");
                else splitString = line.split(operator);
                if (splitString.length > 1) return opEnum;
            }
        }
        return null;
    }


}
