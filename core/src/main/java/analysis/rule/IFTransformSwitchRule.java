package analysis.rule;

import analysis.AbstractRuleVisitor;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.IfStmt;
import io.FileUlits;
import model.Issue;
import model.IssueContext;
import ulits.FindNodeUlits;

import java.io.File;
import java.util.*;

/**
 * if转换为transform
 */
public class IFTransformSwitchRule extends AbstractRuleVisitor {
    private final int min = 3;
    private Expression selector;

    @Override
    public IssueContext apply(List<CompilationUnit> units) {
        for (CompilationUnit unit : units) {
            mayTransformWhile(unit);
        }
        return getContext();
    }

    private void mayTransformWhile(CompilationUnit unit) {
        List<IfStmt> ifStmts = FindNodeUlits.findIfStmtByName(unit, null, false);
        for (IfStmt ifStmt : ifStmts) {
            if (!isTransformWhile(ifStmt)) {
                continue;
            }
            Issue issue = new Issue();
            issue.setIssueNode(ifStmt);
            issue.setUnitNode(ifStmt.findRootNode());
            issue.setRefactorName(getSolutionClassName());
            //避免重复工作 所以在这里放入一些必要的数据
            if (selector != null) {
                Map<String, Object> map = new HashMap<>(2);
                map.put("selector", selector);
                issue.setData(map);
                selector = null;
            }
            getContext().getIssues().add(issue);
        }
    }

    private boolean isTransformWhile(IfStmt ifStmt) {
        int i = 1;
        List<Expression> conditions = new ArrayList<>();
        IfStmt temp = ifStmt;
        conditions.add(ifStmt.getCondition());
        while (temp.hasElseBranch()) {
            if (!temp.getElseStmt().isPresent()) {
                break;
            }
            if (!temp.getElseStmt().get().isIfStmt()) {
                break;
            }
            temp = temp.getElseStmt().get().asIfStmt();
            conditions.add(temp.getCondition());
            i++;
        }
        //小于min不需要转换
        if (i <= min) {
            return false;
        }
        if (!isConformRule(conditions, i)) {
            return false;
        }
        System.out.println(ifStmt);
        return true;
    }

    private boolean isConformRule(List<Expression> conditions, int i) {

        Map<Expression, Integer> map = new LinkedHashMap<>();
        //选出selector 和 cases 如果case重复则不能重构 同时 如果表达式不是 == 和 equals()则不能重构

        for (Expression expr : conditions) {
            //如果不是 xx.equals(xx) 或者 xx == xx类型 返回false
            if (expr.isFieldAccessExpr()) {
                String fullName = expr.asFieldAccessExpr().getName().getIdentifier();
                String eq = fullName.substring(0, fullName.indexOf("("));
                if (!eq.equals("equals")) {
                    return false;
                }
                Expression left = expr.asFieldAccessExpr().getScope();
                eq = fullName.substring(fullName.lastIndexOf("(") + 1, fullName.lastIndexOf(")"));
                Expression right = StaticJavaParser.parseExpression(eq);
                if (map.containsKey(left)) {
                    map.put(left, map.get(left) + 1);
                } else {
                    map.put(left, 1);
                }
                if (map.containsKey(right)) {
                    map.put(right, map.get(right) + 1);
                } else {
                    map.put(right, 1);
                }
            }

            if (!expr.isBinaryExpr()) {
                return false;
            }

            if (expr.isBinaryExpr()) {
                if (!expr.asBinaryExpr().getOperator().equals(BinaryExpr.Operator.EQUALS)) {
                    return false;
                }
                if (map.containsKey(expr.asBinaryExpr().getLeft())) {
                    map.put(expr.asBinaryExpr().getLeft(), map.get(expr.asBinaryExpr().getLeft()) + 1);
                } else {
                    map.put(expr.asBinaryExpr().getLeft(), 1);
                }
                if (map.containsKey(expr.asBinaryExpr().getRight())) {
                    map.put(expr.asBinaryExpr().getRight(), map.get(expr.asBinaryExpr().getRight()) + 1);
                } else {
                    map.put(expr.asBinaryExpr().getRight(), 1);
                }
            }
        }

        Map.Entry<Expression, Integer> m = null;
        for (Map.Entry<Expression, Integer> entry : map.entrySet()) {
            if (entry.getValue() == i) {
                m = entry;
                map.remove(entry.getKey());
                break;
            }
        }

        if (m == null) {
            return false;
        }
        for (Map.Entry<Expression, Integer> entry : map.entrySet()) {
            if (entry.getValue() != 1) {
                return false;
            }
        }
        selector = m.getKey();
        return true;
    }

    public static void main(String[] args) {
        String source = FileUlits.readFile("D:\\gitProject\\W8X\\core\\src\\test\\java\\SwitchSample.java");
        CompilationUnit unit = StaticJavaParser.parse(source);
        List<IfStmt> ifStmts = unit.findAll(IfStmt.class);
        for (IfStmt ifStmt : ifStmts) {

        }
    }
}
