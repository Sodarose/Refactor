import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import io.FileUlits;

public class PackageNameRefactor {
    public static void main(String[] args){
        String source = FileUlits.readFile("E:\\w8x-dev\\core\\src\\test\\java\\Importtest\\testName.java");
        CompilationUnit unit = StaticJavaParser.parse(source);
    }
}
