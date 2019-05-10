import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.Statement;
import io.FileUlits;
import JavaParser.BaseVisitor;
import refactor.ExpressionTool;

import java.util.Iterator;

public class IFRe2 {
    public static void main(String args[]){
        String source = FileUlits.readFile("D:\\gitProject\\W8X\\core\\src\\test\\java\\TestFile.java");
        CompilationUnit unit = StaticJavaParser.parse(source);
        BaseVisitor<MethodDeclaration> visitorByJP = new BaseVisitor<MethodDeclaration>() {
            @Override
            public void visit(MethodDeclaration n, Object arg) {
                if (n.getName().getIdentifier().equals("k")) {
                    getList().add(n);
                }
            }
        };
        unit.accept(visitorByJP, null);
        MethodDeclaration method = visitorByJP.getList().get(0);
        BlockStmt block = method.getBody().get();
        //sloveIF(block);
        sloveTF(block);
        System.out.println(method);
    }

    private static void sloveTF(BlockStmt blockStmt) {
        BaseVisitor<IfStmt> visitor = new BaseVisitor<IfStmt>(){
            @Override
            public void visit(IfStmt n, Object arg) {
                getList().add(n);
                super.visit(n,arg);
            }
        };
        blockStmt.accept(visitor,null);
        Iterator<IfStmt> ifs = visitor.getList().iterator();
        Statement returnStmt = returnStmt = seachReturnStmt(blockStmt);
        while(ifs.hasNext()){
            IfStmt ifStmt = ifs.next();
            //取反
            ifStmt.setCondition(ExpressionTool.reverse(ifStmt.getCondition()));
            //提取then信息到父类模块中
            BlockStmt parent = (BlockStmt) ifStmt.getParentNode().get();
            int i = parent.getStatements().indexOf(ifStmt);

            if(ifStmt.getThenStmt().isBlockStmt()){
                parent.getStatements().addAll(i+1,ifStmt.getThenStmt().asBlockStmt().getStatements());
            }
            else{
                parent.getStatements().add(i+1,ifStmt.getThenStmt());
            }
            //删除then信息
            BlockStmt thenBlock = new BlockStmt();
            //创建退出Statements
            thenBlock.getStatements().add(returnStmt);
            //赋值新的then
            ifStmt.setThenStmt(thenBlock);
        }
    }

    private static Statement seachReturnStmt(BlockStmt blockStmt) {
        return new ReturnStmt();
    }

    private static void sloveIF(BlockStmt blockStmt){
        BaseVisitor<IfStmt> visitor = new BaseVisitor<IfStmt>(){
            @Override
            public void visit(IfStmt n, Object arg) {
                getList().add(n);
                super.visit(n,arg);
            }
        };
        blockStmt.accept(visitor,null);
        ReturnStmt returnStmt = new ReturnStmt();
        Iterator<IfStmt> ifs = visitor.getList().iterator();
        while(ifs.hasNext()){
            IfStmt ifStmt = ifs.next();
            ifStmt.getCondition().asBinaryExpr().setOperator(BinaryExpr.Operator.NOT_EQUALS);
            Statement then = ifStmt.getThenStmt();
            int i = blockStmt.getStatements().indexOf(ifStmt);
            blockStmt.getStatements().addAll(i+1,then.asBlockStmt().getStatements());
            then.asBlockStmt().getStatements().clear();
            then.asBlockStmt().getStatements().add(returnStmt);
        }
    }


}
