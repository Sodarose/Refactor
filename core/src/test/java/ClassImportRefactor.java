import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import io.FileUlits;
import refer.classrefer.ClassImportReferRefactor;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClassImportRefactor {
    public static void importRefactor(String oldClassName,String newClassName) {
        String source = FileUlits.readFile("E:\\w8x-dev\\core\\src\\test\\java\\testName.java");
        CompilationUnit unit = StaticJavaParser.parse(source);
        //List<CompilationUnit> units = Store.javaFiles;

        List<ImportDeclaration> importDeclarations = unit.getImports();
       if (!(importDeclarations.isEmpty())) {
            for (ImportDeclaration importDeclaration : importDeclarations) {
                   int index=importDeclaration.getNameAsString().lastIndexOf(".");
                   String className=importDeclaration.getNameAsString().substring(index+1);
                   if(className.equals(oldClassName)) {
                       importDeclaration.setName(importDeclaration.getNameAsString().substring(0,index+1) + newClassName);
                   }
            }
        }
    }

    public static void main(String args[]){
        ClassImportRefactor.importRefactor("ClassVariRefactor","classVariRefactor");
    }
}
