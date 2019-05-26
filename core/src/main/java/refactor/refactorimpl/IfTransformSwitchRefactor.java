package refactor.refactorimpl;

import analysis.rule.IFTransformSwitchRule;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.sun.org.apache.xml.internal.resolver.readers.ExtendedXMLCatalogReader;
import io.FileUlits;
import io.ParserProject;
import model.Issue;
import model.ReCorrect;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import refactor.AbstractRefactor;
import ulits.AnalysisUlits;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 将超过max的if语句且都一样的
 *
 * @author kangkang
 */
public class IfTransformSwitchRefactor extends AbstractRefactor {
    private boolean isEnum;
    private String enumName;
    private boolean isString;
    private boolean isOther;

    @Override
    public ReCorrect refactor(Issue issue) {
        IfStmt ifStmt = (IfStmt) issue.getIssueNode();
        Map<String, Object> data = issue.getData();
        transFromSwitch(ifStmt, data);
        return null;
    }

    private void transFromSwitch(IfStmt ifStmt, Map<String, Object> data) {
        Expression selector = (Expression) data.get("selector");
        checkSelectType(selector);
        SwitchStmt switchStmt = new SwitchStmt();
        switchStmt.setSelector(selector);
        NodeList<SwitchEntry> switchEntries = createSwitchEntries(ifStmt, selector);
        if (switchEntries == null || switchEntries.size() == 0) {
            return;
        }
        switchStmt.setEntries(switchEntries);
        ifStmt.getParentNode().get().replace(ifStmt, switchStmt);
        //System.out.println(switchStmt);
    }

    /**
     *
     */
    private NodeList<SwitchEntry> createSwitchEntries(Statement stmt, Expression selector) {
        NodeList<SwitchEntry> switchEntries = new NodeList<>();
        while (stmt.isIfStmt()) {
            IfStmt ifStmt = stmt.asIfStmt();
            List<Expression> conditions = new ArrayList<>();
            AnalysisUlits.analysisCondition(ifStmt.getCondition(), conditions);
            List<Expression> tags = new ArrayList<>();
            for (Expression expr : conditions) {
                AnalysisUlits.analysisExpr(expr, tags);
            }
            Iterator<Expression> it = tags.iterator();
            while (it.hasNext()) {
                Expression expr = it.next();
                if (expr.equals(selector)) {
                    it.remove();
                }
            }
            if (tags.size() == 1) {
                SwitchEntry switchEntry = createEntry(tags.get(0), ifStmt.getThenStmt());
                switchEntries.add(switchEntry);
            }

            if (tags.size() > 1) {
                List<SwitchEntry> sws = createEntries(tags, ifStmt.getThenStmt());
                switchEntries.addAll(sws);
            }

            if (!ifStmt.hasElseBranch()) {
                break;
            }
            stmt = ifStmt.getElseStmt().get();
        }
        if (!stmt.isIfStmt()) {
            switchEntries.add(createEntry(null, stmt));
        }
        return switchEntries;
    }

    private List<SwitchEntry> createEntries(List<Expression> tags, Statement thenStmt) {
        List<SwitchEntry> sws = new ArrayList<>();
        BlockStmt blockStmt;
        if (thenStmt.isBlockStmt()) {
            blockStmt = thenStmt.asBlockStmt();
        } else {
            blockStmt = new BlockStmt();
            blockStmt.getStatements().add(thenStmt);
        }
        for (int i = 0; i < tags.size() - 1; i++) {
            SwitchEntry switchEntry = new SwitchEntry();
            Expression expression = tags.get(i);
            if (isEnum) {
                if (expression.isFieldAccessExpr()) {
                    expression = new NameExpr().setName(expression.asFieldAccessExpr().getName());
                }
            }
            switchEntry.getLabels().add(expression);
            sws.add(switchEntry);
        }
        SwitchEntry switchEntry = new SwitchEntry();
        Expression expression = tags.get(tags.size() - 1);
        isEnum(expression, switchEntry, blockStmt);
        sws.add(switchEntry);
        return sws;
    }

    private SwitchEntry createEntry(Expression expression, Statement thenStmt) {
        SwitchEntry switchEntry = new SwitchEntry();
        BlockStmt blockStmt;
        if (thenStmt.isBlockStmt()) {
            blockStmt = thenStmt.asBlockStmt();
        } else {
            blockStmt = new BlockStmt();
            blockStmt.getStatements().add(thenStmt);
        }
        //default分支
        if (expression == null) {
            switchEntry.getStatements().addAll(blockStmt.getStatements());
            return switchEntry;
        }
        //case标签
        isEnum(expression, switchEntry, blockStmt);
        return switchEntry;
    }

    private void isEnum(Expression expression, SwitchEntry switchEntry, BlockStmt blockStmt) {
        if (isEnum) {
            if (expression.isFieldAccessExpr()) {
                expression = new NameExpr().setName(expression.asFieldAccessExpr().getName());
            }
        }
        switchEntry.getLabels().add(expression);
        if (checkNeedBreak(blockStmt)) {
            blockStmt.getStatements().add(new BreakStmt().removeValue());
        }
        switchEntry.getStatements().addAll(blockStmt.getStatements());
    }

    private boolean checkNeedBreak(BlockStmt blockStmt) {
        if (blockStmt.getStatements().size() == 0) {
            return true;
        }
        Statement endStmt = blockStmt.getStatement(blockStmt.getStatements().size() - 1);
        if (endStmt.isReturnStmt()) {
            return false;
        }
        if (endStmt.isThrowStmt()) {
            return false;
        }
        if (endStmt.isIfStmt()) {
            return isHasReturn(endStmt.asIfStmt());
        }
        return true;
    }

    private boolean isHasReturn(Statement stmt) {
        while (stmt.isIfStmt()) {
            IfStmt ifStmt = stmt.asIfStmt();
            if (ifStmt.findAll(ReturnStmt.class).size() == 0) {
                return true;
            }
            if (!ifStmt.hasElseBranch()) {
                break;
            }
            stmt = ifStmt.getElseStmt().get();
        }
        if (!stmt.isIfStmt()) {
            return stmt.findAll(ReturnStmt.class).size() == 0;
        }
        return false;
    }

    private void checkSelectType(Expression selector) {
        if (selector.isLiteralExpr()) {
            isString = true;
            return;
        }
        ResolvedType s = ParserProject.getJavaParserFacade().getType(selector);
        System.out.println(s.describe());
        //搜索根据类限定名称搜索
        SymbolReference v = ParserProject.getCombinedTypeSolver().tryToSolveType(s.describe());
        if (!v.isSolved()) {
            isOther = true;
            return;
        }
        if (v.getCorrespondingDeclaration().isType()) {
            if (v.getCorrespondingDeclaration().asType().getQualifiedName().contains("java.lang.String")) {
                isString = true;
                return;
            }
            if (v.getCorrespondingDeclaration().asType().isEnum()) {
                isEnum = true;
                enumName = v.getCorrespondingDeclaration().asType().getClassName();
                return;
            }
        }
        isOther = true;
        return;
    }


    public static void main(String[] args) {
        String source = FileUlits.readFile("D:\\gitProject\\W8X\\core\\src\\test\\java\\IfSample.java");
        CompilationUnit unit = StaticJavaParser.parse(source);
        ParserProject.parserProject("D:\\gitProject\\W8X");
        IFTransformSwitchRule ifTransformSwitchRule = new IFTransformSwitchRule();
        List<CompilationUnit> units = new ArrayList<>();
        units.add(unit);
        ifTransformSwitchRule.apply(units);
        for (Issue issue : ifTransformSwitchRule.getContext().getIssues()) {
            IfTransformSwitchRefactor ifTransformSwitchRefactor = new IfTransformSwitchRefactor();
            ifTransformSwitchRefactor.refactor(issue);
        }
    }
}
