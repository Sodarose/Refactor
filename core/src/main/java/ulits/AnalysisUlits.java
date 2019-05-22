package ulits;

import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;

import java.util.List;

public class AnalysisUlits {
    public static void analysisExpr(Expression condition, List<Expression> exprs) {
        if (condition.isBinaryExpr()) {
            analysisExpr(condition.asBinaryExpr().getLeft(), exprs);
            analysisExpr(condition.asBinaryExpr().getRight(), exprs);
        } else if (condition.isMethodCallExpr()) {
            if (!condition.asMethodCallExpr().getName().getIdentifier().contains("equals")) {
                exprs.add(condition);
                return;
            }
            Expression left = condition.asMethodCallExpr().getArguments().get(0);
            Expression right = condition.asMethodCallExpr().getScope().get();
            exprs.add(left);
            exprs.add(right);
            return;
        } else {
            exprs.add(condition);
            return;
        }
    }

    public static void analysisCondition(Expression condition, List<Expression> expressions) {
        if (condition.isBinaryExpr()) {
            if (condition.asBinaryExpr().getOperator().equals(BinaryExpr.Operator.OR)) {
                analysisCondition(condition.asBinaryExpr().getLeft(), expressions);
                analysisCondition(condition.asBinaryExpr().getRight(), expressions);
            } else if (condition.asBinaryExpr().getOperator().equals(BinaryExpr.Operator.EQUALS)) {
                expressions.add(condition);
                return;
            } else {
                expressions.add(null);
                return;
            }
        } else if (condition.isMethodCallExpr()) {
            if (condition.asMethodCallExpr().getName().getIdentifier().contains("equals")) {
                expressions.add(condition);
            } else {
                expressions.add(null);
            }
            return;
        } else {
            expressions.add(null);
            return;
        }
    }
}
