import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.MethodCallExpr;
import io.FileUlits;

import java.util.List;

public class MethodReferClassRefactor {
    public static void main(String[] args){
        String oldMethodName="change";
        String newMethodName="";
        String source = FileUlits.readFile("E:\\w8x-dev\\core\\src\\test\\java\\Importtest\\testName.java");
        CompilationUnit unit = StaticJavaParser.parse(source);
        List<MethodCallExpr> methodCallExprList=unit.findAll(MethodCallExpr.class);
        for(MethodCallExpr methodCallExpr:methodCallExprList){
            if(methodCallExpr.getNameAsString().equals(oldMethodName)){
                if(methodCallExpr.getScope().isPresent()){
                    methodCallExpr.setName(newMethodName);
                }
            }
        }
    }
}
