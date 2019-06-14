import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import io.FileUlits;

import java.util.List;
import java.util.Optional;

public class ClassFieldVariaNameRefactor {
    public static void main(String args[]) {
        String source = FileUlits.readFile("E:\\w8x-dev\\core\\src\\test\\java\\Importtest\\testName.java");
        CompilationUnit unit = StaticJavaParser.parse(source);
        List<FieldDeclaration> fieldDeclarations = unit.findAll(FieldDeclaration.class);
        for(FieldDeclaration fieldDeclaration:fieldDeclarations){
            List<VariableDeclarator> variableDeclarators=fieldDeclaration.getVariables();
            for (VariableDeclarator variableDeclarator:variableDeclarators){
                String name=variableDeclarator.getType().getClass().getName();
                if (name.equals("com.github.javaparser.ast.type.ClassOrInterfaceType")) {
                    ClassOrInterfaceType type = (ClassOrInterfaceType) variableDeclarator.getType();
                    System.out.println(variableDeclarator);
                        System.out.println(type.getClass());

                    }
                }
            }
        }
    }
