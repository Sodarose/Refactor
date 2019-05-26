import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import io.ParserProject;

import java.util.ArrayList;
import java.util.List;

public class Tcps  {
    public static void main(String []args){
        ParserProject.parserProject("D:\\gitProject\\W8X");
        List<CompilationUnit> units = ParserProject.getJavaFiles();
        for(CompilationUnit unit:units){
            List<ClassOrInterfaceDeclaration> clas = unit.findAll(ClassOrInterfaceDeclaration.class);
            for(ClassOrInterfaceDeclaration c:clas){
                String className = c.getNameAsString();
                check(className);
                String rightClassName = "";
                anaylis(c,rightClassName);
            }
        }
    }

    public static void anaylis(ClassOrInterfaceDeclaration classOrInterfaceDeclaration,String rightName){
        List<CompilationUnit> units = ParserProject.getJavaFiles();
        List<ClassOrInterfaceDeclaration> in = new ArrayList<>();

        //1.
        for(CompilationUnit unit:units){
            List<ClassOrInterfaceDeclaration> clas = unit.findAll(ClassOrInterfaceDeclaration.class);
            for(ClassOrInterfaceDeclaration c:clas){
                c.equals(classOrInterfaceDeclaration);
                in.add(c);

            }
            //
            List<VariableDeclarationExpr> variableDeclarationExprs = unit.findAll(VariableDeclarationExpr.class);
            for(VariableDeclarationExpr var:variableDeclarationExprs){
                for(VariableDeclarator node:var.getVariables()){
                    //node.setType()
                    String name = node.getType().getClass().getName();
                    if(name.equals("com.github.javaparser.ast.type.ClassOrInterfaceType")){
                        ClassOrInterfaceType type = (ClassOrInterfaceType)node.getType();

                        type.setName(rightName);
                    }
                }
            }
        }
        classOrInterfaceDeclaration.setName(rightName);
    }

    private static void check(String className) {
        tcp tcp = new tcp();
    }
}
