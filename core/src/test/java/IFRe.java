import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.*;
import io.FileUlits;
import analysis.visitor.BaseVisitor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class IFRe {
    public static void main(String agrs[]){
        String source = FileUlits.readFile("D:\\gitProject\\W8X\\core\\src\\test\\java\\TestFile.java");
        CompilationUnit unit = StaticJavaParser.parse(source);
        BaseVisitor<MethodDeclaration> visitorByJP = new BaseVisitor<MethodDeclaration>() {
            @Override
            public void visit(MethodDeclaration n, Object arg) {
                if (n.getName().getIdentifier().equals("checkQQ")) {
                    getList().add(n);
                }
            }
        };
        unit.accept(visitorByJP, null);
        MethodDeclaration method = visitorByJP.getList().get(0);
        BlockStmt block = method.getBody().get();
        slove(block);
        System.out.println(method);
    }

    public static void slove(BlockStmt blockStmt){
        ReturnStmt returnStmt = null;
        Iterator<Statement> it = blockStmt.getStatements().iterator();
        List<IfStmt> ifs = new ArrayList<>();
        Statement statement = null;
        while(it.hasNext()){
            statement = it.next();
            if(statement.isIfStmt()){
                ifs.add(statement.asIfStmt());
            }
            if(statement.isReturnStmt()){
                returnStmt = statement.asReturnStmt();
            }
        }
        if(returnStmt==null){
            returnStmt = new ReturnStmt();
            blockStmt.getStatements().add(returnStmt);
        }
        Iterator<IfStmt> itIf = ifs.iterator();
        IfStmt ifItem = null;
        while(itIf.hasNext()){
            ifItem = itIf.next();
            sloveIFThen(ifItem,returnStmt);
            if(ifItem.hasElseBranch()){
               sloveElseThen(ifItem,returnStmt);
               BlockStmt blockStmt1 = ifItem.getElseStmt().get().asBlockStmt();
               slove(blockStmt1);
               ifItem.removeElseStmt();
            }
            slove(ifItem.getThenStmt().asBlockStmt());
        }
    }

    private static void sloveIFThen(IfStmt ifs,ReturnStmt returnStmt){
        BlockStmt statement = ifs.getThenStmt().asBlockStmt();
        if(check(statement)){
            statement.getStatements().add(returnStmt);
        }
    }

    private static void sloveElseThen(IfStmt ifs,ReturnStmt returnStmt){
        if(!ifs.hasElseBlock()){

        }
        BlockStmt block = ifs.getElseStmt().get().asBlockStmt();
        if(check(block)){
            block.getStatements().add(returnStmt);
        }
        BlockStmt parent = (BlockStmt) ifs.getParentNode().get();
        parent.remove(returnStmt);
        parent.getStatements().addAll(block.getStatements());
    }

    private static boolean check(BlockStmt blockStmt) {
        Iterator<Statement> it = blockStmt.getStatements().iterator();
        Boolean has = true;
        while (it.hasNext()) {
            if(it.next().isReturnStmt()){
                has = false;
                return has;
            }
        }
        return has;
    }



}
