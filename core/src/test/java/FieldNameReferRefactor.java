import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.ThisExpr;
import io.FileUlits;

import java.util.List;

public class FieldNameReferRefactor {
    //FieldAccessExpr
    //NameExpr
    public static void main(String[] args){
        String source = FileUlits.readFile("E:\\w8x-dev\\core\\src\\test\\java\\testName.java");
        CompilationUnit unit = StaticJavaParser.parse(source);
        List<FieldAccessExpr> assignExprList=unit.findAll(FieldAccessExpr.class);
        for (FieldAccessExpr assignExpr:assignExprList){
            System.out.println(assignExpr.getName());
        }
    }
}
