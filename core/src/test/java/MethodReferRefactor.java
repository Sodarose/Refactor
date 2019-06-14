import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.javaparser.Navigator;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import io.FileUlits;
import ulits.MethodCertainUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class MethodReferRefactor {
    public static void main(String[] args) throws IOException {
        TypeSolver javaParserTypeSolver  = new JavaParserTypeSolver(new File("E:\\w8x-dev\\core\\src"));
        CombinedTypeSolver combinedSolver = new CombinedTypeSolver();
        combinedSolver.add(javaParserTypeSolver);
        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(combinedSolver);
        StaticJavaParser.getConfiguration().setSymbolResolver(symbolSolver);
       // String source = FileUlits.readFile("E:\\w8x-dev\\core\\src\\test\\java\\testName.java");
        CompilationUnit unit = StaticJavaParser.parse(new File("E:\\w8x-dev\\core\\src\\main\\java\\refer\\classrefer\\ClassExtendsReferRefactor.java"));
       FieldDeclaration fieldDeclaration=Navigator.findNodeOfGivenClass(unit,FieldDeclaration.class);
       System.out.println(fieldDeclaration.getVariables().get(0).getType().resolve().asReferenceType().getQualifiedName());
    }
}
