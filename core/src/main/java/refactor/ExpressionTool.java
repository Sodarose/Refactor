package refactor;

import com.github.javaparser.ast.expr.*;

import java.util.HashMap;
import java.util.Map;


public class ExpressionTool {
    private static Map<String,BinaryExpr.Operator>
            logicMap = new HashMap<>();
    private static Map<String,BinaryExpr.Operator>
            JudgeMap = new HashMap<>();
    static {
        /**
         * || and &&
         * | and &
         * */
        logicMap.put(BinaryExpr.Operator.OR.asString(),BinaryExpr.Operator.AND);
        logicMap.put(BinaryExpr.Operator.AND.asString(),BinaryExpr.Operator.OR);
        logicMap.put(BinaryExpr.Operator.BINARY_OR.asString(),BinaryExpr.Operator.BINARY_AND);
        logicMap.put(BinaryExpr.Operator.BINARY_AND.asString(),BinaryExpr.Operator.BINARY_OR);

        /**
         * == and !=
         * < and >=
         * > and <=
         * */
        JudgeMap.put(BinaryExpr.Operator.EQUALS.asString(),BinaryExpr.Operator.NOT_EQUALS);
        JudgeMap.put(BinaryExpr.Operator.NOT_EQUALS.asString(),BinaryExpr.Operator.EQUALS);
        JudgeMap.put(BinaryExpr.Operator.LESS.asString(),BinaryExpr.Operator.GREATER_EQUALS);
        JudgeMap.put(BinaryExpr.Operator.GREATER_EQUALS.asString(),BinaryExpr.Operator.LESS);
        JudgeMap.put(BinaryExpr.Operator.GREATER.asString(),BinaryExpr.Operator.LESS_EQUALS);
        JudgeMap.put(BinaryExpr.Operator.LESS_EQUALS.asString(),BinaryExpr.Operator.GREATER);
    }

    public static Expression reverse(Expression expression) {
        Expression expr = null;
        if (expression.isBinaryExpr()) {
            expr =  reverseExpression(expression.asBinaryExpr());
        }
        if (expression.isMethodCallExpr()) {
            expr = reverseExpression(expression.asMethodCallExpr());
        }
        if (expression.isUnaryExpr()) {
            //System.out.println(expression.asUnaryExpr().getOperator());
            expr = reverseExpression(expression.asUnaryExpr());
        }
        if (expression.isFieldAccessExpr()) {
            expr = reverseExpression(expression.asFieldAccessExpr());
        }
        return expr;
    }


    private static Expression reverseExpression(BinaryExpr binaryExpr) {
        Expression left = binaryExpr.getLeft();
        Expression right = binaryExpr.getRight();
        BinaryExpr.Operator operator = binaryExpr.getOperator();
        if(logicMap.containsKey(operator.asString())){
            if(left.isUnaryExpr()){
                left = left.asUnaryExpr().getExpression();
            }
            else{
                UnaryExpr unaryExpr = new UnaryExpr();
                unaryExpr.setExpression(left);
                unaryExpr.setOperator(UnaryExpr.Operator.LOGICAL_COMPLEMENT);
                left = unaryExpr;
            }
            if(right.isUnaryExpr()){
                right = right.asUnaryExpr();
            }
            else{
                UnaryExpr unaryExpr = new UnaryExpr();
                unaryExpr.setExpression(left);
                unaryExpr.setOperator(UnaryExpr.Operator.LOGICAL_COMPLEMENT);
                right = unaryExpr;
            }
            operator = logicMap.get(operator.asString());
        }
        if(JudgeMap.containsKey(operator.asString())){
            operator = JudgeMap.get(operator.asString());
        }
        BinaryExpr binary = new BinaryExpr();
        binary.setLeft(left);
        binary.setRight(right);
        binary.setOperator(operator);
        return binary;
    }


    private static Expression reverseExpression(UnaryExpr unaryExpr) {
        return unaryExpr.getExpression();
    }

    private static Expression reverseExpression(MethodCallExpr methodCallExpr) {
        UnaryExpr unaryExpr = new UnaryExpr();
        unaryExpr.setExpression(methodCallExpr);
        unaryExpr.setOperator(UnaryExpr.Operator.LOGICAL_COMPLEMENT);
        return unaryExpr;
    }

    private static Expression reverseExpression(FieldAccessExpr fieldAccessExpr) {
        UnaryExpr unaryExpr = new UnaryExpr();
        unaryExpr.setExpression(fieldAccessExpr);
        unaryExpr.setOperator(UnaryExpr.Operator.LOGICAL_COMPLEMENT);
        return unaryExpr;
    }


}