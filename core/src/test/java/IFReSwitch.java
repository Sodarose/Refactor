import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.*;
import io.FileUlits;
import lombok.Data;
import JavaParser.BaseVisitor;

import java.util.*;

public class IFReSwitch {
    public static void main(String agrs[]) {
        String source = FileUlits.readFile("D:\\gitProject\\W8X\\core\\src\\test\\java\\SwitchSample.java");
        CompilationUnit unit = StaticJavaParser.parse(source);
        BaseVisitor<MethodDeclaration> visitorByJP = new BaseVisitor<MethodDeclaration>() {
            @Override
            public void visit(MethodDeclaration n, Object arg) {
                if (n.getName().getIdentifier().equals("Pay")) {
                    getList().add(n);
                }
            }
        };
        unit.accept(visitorByJP, null);
        MethodDeclaration method = visitorByJP.getList().get(0);
        BaseVisitor<IfStmt> ifVisitor = new BaseVisitor<IfStmt>() {
            @Override
            public void visit(IfStmt n, Object arg) {
                getList().add(n);
            }
        };
        method.accept(ifVisitor, null);
        IfStmt ifStmt = ifVisitor.getList().get(0);
        transformSwitch(ifStmt);
        System.out.println(method);
    }

    private static void transformIf(SwitchStmt switchStmt) {
        Expression selector = switchStmt.getSelector();
        IfNode head = new IfNode();
        IfNode p = head, q;
        Iterator<SwitchEntry> it = switchStmt.getEntries().iterator();
        BlockStmt then = null;
        BinaryExpr binaryExpr = null;
        while (it.hasNext()) {
            SwitchEntry switchEntry = it.next();
            it.remove();
            if (switchEntry.getLabels().size() != 0) {
                binaryExpr = new BinaryExpr();
                binaryExpr.setLeft(selector);
                binaryExpr.setRight(switchEntry.getLabels().get(0));
                binaryExpr.setOperator(BinaryExpr.Operator.EQUALS);
            } else {
                binaryExpr = null;
            }
            if (switchEntry.getStatements().size() != 0 && switchEntry.getStatements().get(0).isBlockStmt()) {
                then = switchEntry.getStatements().get(0).asBlockStmt();
            } else {
                then = new BlockStmt();
                then.getStatements().addAll(switchEntry.getStatements());
            }
            q = new IfNode(binaryExpr, then);
            p.next = q;
            p = q;
        }
        IfStmt ifStmt = create(head);
        BlockStmt blockStmt = (BlockStmt) switchStmt.getParentNode().get();
        int i = blockStmt.getStatements().indexOf(switchStmt);
        blockStmt.remove(switchStmt);
        blockStmt.getStatements().add(i, ifStmt);
    }

    private static IfStmt create(IfNode head) {
        IfStmt ifStmt = new IfStmt();
        IfStmt p = ifStmt;
        IfNode q = head.next;
        while (q != null) {
            if (q.getExpr() != null) {
                ifStmt.setCondition(q.getExpr());
                ifStmt.setThenStmt(q.getBlockStmt());
                if (q.next!=null&&q.getNext().getExpr() != null) {
                    IfStmt t = new IfStmt();
                    ifStmt.setElseStmt(t);
                    ifStmt = t;
                }
            } else {
                    ifStmt.setElseStmt(q.getBlockStmt());
            }
            q = q.next;
        }
        return p;
    }

    private static void removeBreak(BlockStmt value) {
        Iterator<Statement> it = value.getStatements().iterator();
        Statement st = null;
        while (it.hasNext()) {
            st = it.next();
            if (st.isBreakStmt()) {
                value.remove(st);
            }
        }
    }


    private static void transformSwitch(IfStmt ifStmt) {
        BaseVisitor<IfStmt> visitor = new BaseVisitor<IfStmt>(){
            @Override
            public void visit(IfStmt n, Object arg) {
                getList().add(n);
                super.visit(n, arg);
            }
        };
        ifStmt.accept(visitor,null);
        Expression selector = check(ifStmt,visitor.getList());
        SwitchStmt switchStmt = new SwitchStmt();
        switchStmt.setSelector(selector);
        Iterator<IfStmt> it = visitor.getList().iterator();
        NodeList<SwitchEntry> entries = new NodeList<>();
        IfStmt ifs= null;
        while(it.hasNext()){
            SwitchEntry entry = new SwitchEntry();
            ifs = it.next();
            if(!ifs.getCondition().isBinaryExpr()){
                continue;
            }
            if(!ifs.getCondition().asBinaryExpr().getLeft().equals(selector)){
                continue;
            }
            entry.getLabels().add(ifs.getCondition().asBinaryExpr().getRight());
            entry.getStatements().add(ifs.getThenStmt());
            entries.add(entry);
            if(ifs.hasElseBlock()){
                SwitchEntry t = new SwitchEntry();
                t.getStatements().add(ifs.getElseStmt().get());
                entries.add(t);
            }
        }
        switchStmt.setEntries(entries);
        System.out.println(switchStmt);
    }

    private static Expression check(IfStmt ifStmt, List<IfStmt> list) {
        Expression left = ifStmt.getCondition().asBinaryExpr().getLeft();
        Expression right = ifStmt.getCondition().asBinaryExpr().getRight();
        int l=0,r=0;
        Iterator<IfStmt> it = list.iterator();

        return left;
    }


    @Data
    static class IfNode {
        private Expression expr;
        private BlockStmt blockStmt;
        private IfNode next;

        public IfNode() {

        }

        public IfNode(Expression expr, BlockStmt blockStmt) {
            this.expr = expr;
            this.blockStmt = blockStmt;
        }

    }
}
