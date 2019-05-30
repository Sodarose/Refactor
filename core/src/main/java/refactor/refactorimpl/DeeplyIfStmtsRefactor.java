package refactor.refactorimpl;

import analysis.rule.DeeplyIfStmtsRule;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.*;
import io.FileUlits;
import io.ParserProject;
import model.Issue;
import model.ReCorrect;
import refactor.AbstractRefactor;
import refactor.ExpressionTool;
import ulits.AnalysisUlits;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author Administrator*/
public class DeeplyIfStmtsRefactor extends AbstractRefactor {
    private String interrupt;
    private List<IfStmt> ifStmts = new ArrayList<>();
    private Node parent;
    private boolean frist = true;

    @Override
    public void refactor(Issue issue) {
        IfStmt ifStmt = (IfStmt) issue.getIssueNode();
        Map<String, Object> data = issue.getData();
        interrupt = (String) data.get("Interrupt");
        if (!checkReturn(ifStmt)) {
            return ;
        }
        int i = 0;
        transformIfStmt(ifStmt);
        frist = false;
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
     *
     */
    private void transformIfStmt(Statement stmt) {
        if (stmt.isIfStmt()) {
            if(frist){
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

    private void solveHasElseIfStmt(BlockStmt parent, IfStmt ifStmt) {
        List<Statement> leftoverStmt = getLeftoverStmt(parent, ifStmt);
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
        }
        if (!isHasReturn(elseStmt)) {
            ifStmt.getElseStmt().get().asBlockStmt().getStatements().addAll(leftoverStmt);
        }
        cleanElse(parent, ifStmt, leftoverStmt);
        transformIfStmt(elseStmt);
        transformIfStmt(thenStmt);
    }

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
     *
     */
    private void cleanElse(BlockStmt parent, IfStmt ifStmt, List<Statement> leftoverStmt) {
        parent.getStatements().removeAll(leftoverStmt);
        parent.getStatements().addAll(ifStmt.getElseStmt().get().asBlockStmt().getStatements());
        ifStmt.removeElseStmt();
    }

    /**
     * 得到ifstmt后面的所有语句
     */
    private List<Statement> getLeftoverStmt(BlockStmt blockStmt, Statement stmt) {
        int index = blockStmt.getStatements().indexOf(stmt);
        List<Statement> leftoverStmt = new ArrayList<>();
        leftoverStmt.addAll(blockStmt.getStatements().subList(index + 1, blockStmt.getStatements().size()));
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


    public static void main(String[] args) {
        String source = FileUlits.readFile("D:\\gitProject\\W8X\\core\\src\\test\\java\\Sample.java");
        CompilationUnit unit = StaticJavaParser.parse(source);
        List<MethodCallExpr> list = unit.findAll(MethodCallExpr.class);
        for (MethodCallExpr callExpr : list) {
            //System.out.println(callExpr);
        }
        ParserProject.parserProject("D:\\gitProject\\W8X");
        DeeplyIfStmtsRule deeplyIfStmtsRule = new DeeplyIfStmtsRule();
        List<CompilationUnit> units = new ArrayList<>();
        units.add(unit);
        deeplyIfStmtsRule.apply(units);
        for (Issue issue : deeplyIfStmtsRule.getContext().getIssues()) {
            DeeplyIfStmtsRefactor deeplyIfStmtsRefactor = new DeeplyIfStmtsRefactor();
            deeplyIfStmtsRefactor.refactor(issue);
        }
    }


}
