import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.ClassExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.resolution.types.ResolvedType;
import io.FileUlits;
import io.ParserProject;
import model.Store;

import java.util.List;
import java.util.Optional;

public class ClassNameRefactor {
    public static void main(String args[]) {
        String source = FileUlits.readFile("E:\\w8x-dev\\core\\src\\test\\java\\testName.java");
        CompilationUnit unit = StaticJavaParser.parse(source);
            List<VariableDeclarationExpr> variableDeclarationExprs = unit.findAll(VariableDeclarationExpr.class);
            for (VariableDeclarationExpr var : variableDeclarationExprs) {
                for (VariableDeclarator variableDeclarator : var.getVariables()) {
                    String name = variableDeclarator.getType().getClass().getName();
                    if (name.equals("com.github.javaparser.ast.type.ClassOrInterfaceType")) {
                        ClassOrInterfaceType type = (ClassOrInterfaceType) variableDeclarator.getType();
                        if (type.getName().toString().equals("ClassVariRefactor")) {
                            System.out.println(variableDeclarator.getName());
                            /*
                            type.setName("classVariRefactor");
                            Optional<Expression> initializer = variableDeclarator.getInitializer();
                            if(initializer!=null&&initializer.isPresent()){
                                if(initializer.get().getClass().toString().equals("class com.github.javaparser.ast.expr.ObjectCreationExpr")){
                                    ObjectCreationExpr objectCreationExpr = (ObjectCreationExpr) initializer.get();
                                    if(objectCreationExpr.getType().toString().equals("ClassVariRefactor")) {
                                        objectCreationExpr.setType(type);
                                        variableDeclarator.setInitializer(objectCreationExpr);
                                    }
                                }
                            }
                            variableDeclarator.setType(type);
                   /*if (type.getName().toString().equals("ClassVariRefactor")) {
                        Optional<Expression> initializer=node.getInitializer();
                        ObjectCreationExpr objectCreationExpr= (ObjectCreationExpr) initializer.get();
                        type.setName("classVariRefactor");
                        objectCreationExpr.setType(type);
                        node.setInitializer(objectCreationExpr);
                        node.setType(type);
                       // System.out.println(initializer.get());
                        System.out.println(node);
                        System.out.println(var);
                        if (type.getName().toString().equals("SundayUtil")) {
                            System.out.println(type);
                            */
                        }                        }
                    }
                }
            }
        }



