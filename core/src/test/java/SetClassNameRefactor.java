import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import io.FileUlits;
import ulits.BoyerMoore;

import java.util.List;
import java.util.Optional;

public class SetClassNameRefactor {
    public String test;
    public static void main(String args[]) {
        String source = FileUlits.readFile("E:\\w8x-dev\\core\\src\\test\\java\\testName.java");
        CompilationUnit unit = StaticJavaParser.parse(source);
        List<VariableDeclarationExpr> variableDeclarationExprs = unit.findAll(VariableDeclarationExpr.class);
        for (VariableDeclarationExpr var : variableDeclarationExprs) {
            for(VariableDeclarator node:var.getVariables()) {
                String name = node.getType().getClass().getName();

                if(name.equals("com.github.javaparser.ast.type.ClassOrInterfaceType")) {
                    String className=node.getType().toString();
                    BoyerMoore boyerMoore=new BoyerMoore();
                    int pos=boyerMoore.match(className,"Set<ClassVariRefactor>");
                    if (pos!=-1) {
                        ClassOrInterfaceType type = (ClassOrInterfaceType) node.getType();
                        ClassOrInterfaceType type1=(ClassOrInterfaceType) type.getTypeArguments().get().get(0);
                        type1.setName("classVariRefactor");
                        type.setTypeArguments(type1);
                        Optional<Expression> initializer=node.getInitializer();
                        ObjectCreationExpr objectCreationExpr= (ObjectCreationExpr) initializer.get();
                        ClassOrInterfaceType type2=(ClassOrInterfaceType) objectCreationExpr.getType().getTypeArguments().get().get(0);
                        type2.setName("classVariRefactor");
                        objectCreationExpr.setTypeArguments(type2);
                        objectCreationExpr.removeTypeArguments();
                        node.setInitializer(objectCreationExpr);
                        node.setType(type);
                    }
                    ClassOrInterfaceType type = (ClassOrInterfaceType) node.getType();
                    if (type.getName().toString().equals("ClassVariRefactor")) {
                        Optional<Expression> initializer=node.getInitializer();
                        ObjectCreationExpr objectCreationExpr= (ObjectCreationExpr) initializer.get();
                        type.setName("classVariRefactor");
                        objectCreationExpr.setType(type);
                        node.setInitializer(objectCreationExpr);
                        node.setType(type);
                        // System.out.println(initializer.get());
                        System.out.println(node);
                        System.out.println(var);
                    }
                }
            }

        }
    }
}
