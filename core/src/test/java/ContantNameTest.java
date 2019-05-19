import JavaParser.BaseVisitor;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import io.FileUlits;

public class ContantNameTest {
    public static void main(String args[]){
        String source = FileUlits.readFile("E:\\w8x-dev\\core\\src\\test\\java\\testName.java");
        CompilationUnit unit = StaticJavaParser.parse(source);
        BaseVisitor<FieldDeclaration> ContantVistor=new BaseVisitor<FieldDeclaration>(){
            @Override
            public void visit(FieldDeclaration n, Object arg) {
                if(!(n.isFinal()&&n.isStatic())){
                    super.visit(n, arg);
                    return;
                }

                super.visit(n, arg);
            }
        };
    }
}
