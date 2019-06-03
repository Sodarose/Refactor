import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import io.FileUlits;

import java.util.List;

public class ClassImportRefactor {
    public static void main(String args[]) {
        String source = FileUlits.readFile("E:\\w8x-dev\\core\\src\\test\\java\\testName.java");
        CompilationUnit unit = StaticJavaParser.parse(source);
        List<ImportDeclaration> importDeclarations=unit.getImports();
       for(ImportDeclaration importDeclaration:importDeclarations){
           if(importDeclaration.getName().toString().equals("ulits.SundayUtil")){
               importDeclaration.setName("com.op.util");
           }
       }
    }
}
