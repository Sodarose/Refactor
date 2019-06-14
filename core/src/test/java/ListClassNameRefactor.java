import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import io.FileUlits;
import io.ParserProject;
import ulits.BoyerMoore;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ListClassNameRefactor {
    public static void main(String args[]) {
        String source = FileUlits.readFile("E:\\w8x-dev\\core\\src\\test\\java\\Importtest\\testName.java");
        CompilationUnit unit = StaticJavaParser.parse(source);
            List<VariableDeclarationExpr> variableDeclarationExprs = unit.findAll(VariableDeclarationExpr.class);
            for (VariableDeclarationExpr var : variableDeclarationExprs) {
                for(VariableDeclarator node:var.getVariables()) {
                    String name = node.getType().getClass().getName();
                    if(name.equals("com.github.javaparser.ast.type.ClassOrInterfaceType")) {
                       String className=node.getType().toString();
                        BoyerMoore boyerMoore=new BoyerMoore();
                        int pos=boyerMoore.match(className,"List<ClassVariRefactor>");
                        if (pos!=-1) {
                            ClassOrInterfaceType type = (ClassOrInterfaceType) node.getType();
                           ClassOrInterfaceType classOrInterfaceType=(ClassOrInterfaceType) type.getTypeArguments().get().get(0);
                            ClassOrInterfaceType type1 = (ClassOrInterfaceType) type.getTypeArguments().get().get(0);
                            type1.setName("classVariRefactor");
                            type.setTypeArguments(type1);
                           Optional<Expression> initializer = node.getInitializer();
                            if (initializer != null && initializer.isPresent()) {
                                if (initializer.get().getClass().toString().equals("class com.github.javaparser.ast.expr.ObjectCreationExpr")) {
                                    ObjectCreationExpr objectCreationExpr = (ObjectCreationExpr) initializer.get();
                                    if(!(objectCreationExpr.getType().getTypeArguments().get().isEmpty())) {
                                        ClassOrInterfaceType Argumenttype = (ClassOrInterfaceType) objectCreationExpr.getType().getTypeArguments().get().get(0);
                                        if (Argumenttype != null && Argumenttype.getNameAsString().equals("ClassVariRefactor")) {
                                            Argumenttype.setName("classVariRefactor");
                                            objectCreationExpr.setTypeArguments(Argumenttype);
                                            objectCreationExpr.removeTypeArguments();
                                        }
                                    }
                                }
                            }
                            node.setType(type);

                        }
                    }
                }

        }
    }
    public static Type flag(String oldClassName, ClassOrInterfaceType type,String newClassName){
        ClassOrInterfaceType classOrInterfaceType=(ClassOrInterfaceType) type.getTypeArguments().get().get(0);
        String name=classOrInterfaceType.getNameAsString();
        if(name.equals("")){
            Type type1=flag(oldClassName,classOrInterfaceType,newClassName);
            type.setTypeArguments(type1);
            return type;
        }
        else
        {
            if (type.getNameAsString().equals("")){
                type.setName(newClassName);
                return type;
            }
            return type;
        }
    }
}
