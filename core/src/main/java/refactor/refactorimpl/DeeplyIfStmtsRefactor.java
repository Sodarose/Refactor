package refactor.refactorimpl;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.*;
import model.Issue;
import refactor.AbstractRefactor;
import refactor.ExpressionTool;
import ulits.AnalysisUlits;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author Administrator
 */
public class DeeplyIfStmtsRefactor extends AbstractRefactor {
    private String interrupt;
    private List<IfStmt> ifStmts = new ArrayList<>();
    private Node parent;
    private boolean first = true;


    @Override
    public void refactor(Issue issue) {
        IfStmt ifStmt = (IfStmt) issue.getIssueNode();
        Map<String, Object> data = issue.getData();
        interrupt = (String) data.get("Interrupt");
        if (!checkReturn(ifStmt)) {
            return;
        }
        int i = 0;
        transformIfStmt(ifStmt);
        first = false;
        for (IfStmt stmt : ifStmts) {
            if (AnalysisUlits.getDeep(stmt) > 2) {
                transformIfStmt(stmt);
            }
        }

        //System.out.println(AnalysisUlits.getDeep(ifStmt));
        parent = ifStmt.getParentNode().get();
        if ("void".equals(interrupt)) {
            cleanVoidReturn(parent);
        }
        if ("continue".equals(interrupt)) {
            cleanContinue(parent);
        }
        SimplerReturn(issue);
        //System.out.println(ifStmt.getParentNode().get().getParentNode().get());
    }

    /**
     * 清理continue;
     */
    private void cleanContinue(Node parent) {
        BlockStmt blockStmt = (BlockStmt) parent;
        Statement statement = blockStmt.getStatement(blockStmt.getStatements().size() - 1);
        if (statement.isContinueStmt()) {
            blockStmt.getStatements().remove(statement);
        }
    }

    /**
     * 清理return
     */
    private void cleanVoidReturn(Node parent) {
        BlockStmt blockStmt = (BlockStmt) parent;
        Statement statement = blockStmt.getStatement(blockStmt.getStatements().size() - 1);
        if (statement.isReturnStmt()) {
            blockStmt.getStatements().remove(statement);
        }
    }


    /**
     * 检查返回语句
     */
    public boolean checkReturn(IfStmt ifStmt) {
        BlockStmt blockStmt = null;
        if (ifStmt.getParentNode().isPresent()) {
            if (ifStmt.getParentNode().get().getClass().getName().equals("com.github.javaparser.ast.stmt.BlockStmt")) {
                blockStmt = (BlockStmt) ifStmt.getParentNode().get();
            } else {
                return false;
            }
        }
        if (blockStmt == null) {
            return false;
        }
        //如果是这个表示本身就有返回语句
        if (interrupt.equals("object")) {
        }
        if (interrupt.equals("void")) {
            blockStmt.getStatements().add(new ReturnStmt());
        }
        if (interrupt.equals("continue")) {
            blockStmt.getStatements().add(new ContinueStmt());
        }
        return true;
    }


    /**
     * 转换方法
     */
    private void transformIfStmt(Statement stmt) {
        if (stmt.isIfStmt()) {
            if (first) {
                ifStmts.add(stmt.asIfStmt());
            }
            BlockStmt parent = (BlockStmt) stmt.getParentNode().get();
            if (checkIFChange(parent, stmt) > 2) {
                return;
            }
            IfStmt ifStmt = stmt.asIfStmt();
            if (ifStmt.hasElseBranch()) {
                solveHasElseIfStmt(parent, ifStmt);
            } else {
                solveNoElseIfStmt(parent, ifStmt);
            }
        }
        if (stmt.isBlockStmt()) {
            List<Statement> stmts = new ArrayList<>();
            for (Statement statement : stmt.asBlockStmt().getStatements()) {
                if (statement.isIfStmt()) {
                    stmts.add(statement);
                }
            }
            for (Statement statement : stmts) {
                transformIfStmt(statement);
            }
        }
    }

    /**
     * 处理含有else的if语句
     */
    private void solveHasElseIfStmt(BlockStmt parent, IfStmt ifStmt) {
        NodeList<Statement> leftoverStmt = getLeftoverStmt(parent, ifStmt);
        BlockStmt thenStmt;
        BlockStmt elseStmt;
        if (ifStmt.getThenStmt().isBlockStmt()) {
            thenStmt = ifStmt.getThenStmt().asBlockStmt();
        } else {
            thenStmt = new BlockStmt();
            thenStmt.getStatements().add(ifStmt.getThenStmt());
            ifStmt.setThenStmt(thenStmt);
        }
        if (ifStmt.getElseStmt().get().isBlockStmt()) {
            elseStmt = ifStmt.getElseStmt().get().asBlockStmt();
        } else {
            elseStmt = new BlockStmt();
            elseStmt.getStatements().add(ifStmt.getElseStmt().get());
            ifStmt.setElseStmt(elseStmt);
        }
        if (!isHasReturn(thenStmt)) {
            ifStmt.getThenStmt().asBlockStmt().getStatements().addAll(leftoverStmt);
            for (Statement stmt : leftoverStmt) {
                stmt.setParentNode(thenStmt);
            }
        }
        if (!isHasReturn(elseStmt)) {
            ifStmt.getElseStmt().get().asBlockStmt().getStatements().addAll(leftoverStmt);
            for (Statement stmt : leftoverStmt) {
                stmt.setParentNode(thenStmt);
            }
        }
        cleanElse(parent, ifStmt, leftoverStmt);
        transformIfStmt(elseStmt);
        transformIfStmt(thenStmt);
    }

    /**
     * 处理不含else的if语句
     */
    private void solveNoElseIfStmt(BlockStmt parent, IfStmt ifStmt) {
        List<Statement> leftoverStmt = getLeftoverStmt(parent, ifStmt);
        BlockStmt thenStmt;
        if (ifStmt.getThenStmt().isBlockStmt()) {
            thenStmt = ifStmt.getThenStmt().asBlockStmt();
        } else {
            thenStmt = new BlockStmt();
            thenStmt.getStatements().add(ifStmt.getThenStmt());
            ifStmt.setThenStmt(thenStmt);
        }
        ifStmt.setCondition(ExpressionTool.reverse(ifStmt.getCondition()));
        BlockStmt newThen = new BlockStmt();
        newThen.getStatements().addAll(leftoverStmt);
        ifStmt.setThenStmt(newThen);
        int index = parent.getStatements().indexOf(ifStmt);
        parent.getStatements().addAll(index + 1, thenStmt.getStatements());
        if (isHasReturn(thenStmt)) {
            parent.getStatements().removeAll(leftoverStmt);
        }
        transformIfStmt(thenStmt);
    }


    /**
     * 清理else
     */
    private void cleanElse(BlockStmt parent, IfStmt ifStmt, List<Statement> leftoverStmt) {
        parent.getStatements().removeAll(leftoverStmt);
        parent.getStatements().addAll(ifStmt.getElseStmt().get().asBlockStmt().getStatements());
        ifStmt.removeElseStmt();
    }

    /**
     * 得到ifstmt后面的所有语句
     */
    private NodeList<Statement> getLeftoverStmt(BlockStmt blockStmt, Statement stmt) {
        int index = blockStmt.getStatements().indexOf(stmt);
        NodeList<Statement> leftoverStmt = new NodeList<>();
        for (Statement statement : blockStmt.getStatements().subList(index + 1, blockStmt.getStatements().size())) {
            leftoverStmt.add(statement.clone());
        }
        return leftoverStmt;
    }

    /**
     * 判断block中是否有retrun
     */
    private boolean isHasReturn(BlockStmt blockStmt) {
        for (Statement statement : blockStmt.getStatements()) {
            if (interrupt.equals("void") || interrupt.equals("object")) {
                if (statement.isReturnStmt()) {
                    return true;
                }
            } else {
                if (statement.isContinueStmt()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断能否形成卫语句重构
     */
    private int checkIFChange(BlockStmt blockStmt, Statement stmt) {
        int index = blockStmt.getStatements().indexOf(stmt);
        return blockStmt.getStatements().subList(index, blockStmt.getStatements().size()).size();
    }


    /**
     * 简化 卫语句的 return
     */
    private void SimplerReturn(Issue issue) {
        CompilationUnit unit = issue.getJavaModel().getUnit();
        List<IfStmt> ifStmts = unit.findAll(IfStmt.class);
        for (IfStmt ifStmt : ifStmts) {
            if (ifStmt.hasElseBranch()) {
                continue;
            }
            if (!ifStmt.getThenStmt().isBlockStmt()) {
                continue;
            }
            BlockStmt blockStmt = ifStmt.getThenStmt().asBlockStmt();
            runSimple(blockStmt.getStatements(),ifStmt);
        }

        List<MethodDeclaration> methods = unit.findAll(MethodDeclaration.class);
        for (MethodDeclaration method : methods) {
            if (method.getType().isVoidType()) {
                continue;
            }
            if (!method.getBody().isPresent()) {
                continue;
            }
            BlockStmt blockStmt = method.getBody().get();
            runSimple(blockStmt.getStatements(),method);
        }
    }

    private void runSimple(List<Statement> stmts,Node node){
        if (stmts.size() < 2) {
            return;
        }
        Statement rStmt = stmts.get(stmts.size() - 1);
        Statement aStmt = stmts.get(stmts.size() - 2);
        if (!rStmt.isReturnStmt()) {
            return;
        }
        if (!rStmt.asReturnStmt().getExpression().isPresent()) {
            return;
        }
        ReturnStmt returnStmt = rStmt.asReturnStmt();
        if (!aStmt.isExpressionStmt() || !aStmt.asExpressionStmt().getExpression().isAssignExpr()) {
            return;
        }
        AssignExpr assignExpr = aStmt.asExpressionStmt().getExpression().asAssignExpr();
        if (!assignExpr.getTarget().equals(returnStmt.getExpression().get())) {
            return;
        }
        ReturnStmt rt = new ReturnStmt();
        rt.setExpression(assignExpr.getValue());
        stmts.remove(stmts.size()-2);
        stmts.remove(stmts.size()-1);
        stmts.add(rt);
    }

}
