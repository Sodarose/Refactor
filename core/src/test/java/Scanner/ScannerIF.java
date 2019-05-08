package Scanner;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import io.FileUlits;
import lombok.Getter;


public class ScannerIF{
    public static void main(String args[]){
        String source = FileUlits.readFile("D:\\gitProject\\W8X\\core\\src\\test\\java\\TestFile.java");
        CompilationUnit unit = StaticJavaParser.parse(source);
        System.out.println(unit.getRange().get().toString());
    }
}
