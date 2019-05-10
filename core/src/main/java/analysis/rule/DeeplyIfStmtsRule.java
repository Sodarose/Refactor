package analysis.rule;


import analysis.AbstractRuleVisitor;
import analysis.BaseVisitor;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.stmt.IfStmt;
import io.FileUlits;
import model.Issue;
import model.IssueContext;
import sun.security.krb5.internal.PAData;

import java.util.List;

/**
 * rule if深层嵌套扫描
 * @author kangkang
 * */
public class DeeplyIfStmtsRule extends AbstractRuleVisitor {

    private final int MAX_DEEP = 3;

    @Override
    public IssueContext apply(List<CompilationUnit> units) {
        for(CompilationUnit unit:units){

        }
        return getContext();
    }

    private void check(CompilationUnit unit){
        BaseVisitor<IfStmt>  exteVisitor = new BaseVisitor<IfStmt>(){
            @Override
            public void visit(IfStmt n, Object arg) {
                getList().add(n);
            }
        };
        unit.accept(exteVisitor,null);
        /**
         * 拿到类表层ifstmts
         * */
        List<IfStmt> exteIfStmts = exteVisitor.getList();
        BaseVisitor<IfStmt> internalVisitor = new BaseVisitor<IfStmt>(){
            @Override
            public void visit(IfStmt n, Object arg) {
                int deep=(int)arg;
                if(!n.hasElseBranch()){
                    ++deep;
                }
                if(deep>=MAX_DEEP){
                    Issue issue = new Issue();
                    issue.setUnitNode(unit);
                    issue.setIssueNode(getCaller());
                    getContext().getIssues().add(issue);
                    return;
                }
                super.visit(n, deep);
            }

        };
        for(IfStmt ifStmt:exteIfStmts){
            int deep = 0;
            internalVisitor.setCaller(ifStmt);
            ifStmt.accept(internalVisitor,deep);

        }
    }
}
