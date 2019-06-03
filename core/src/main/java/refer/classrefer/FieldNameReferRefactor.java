package refer.classrefer;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import io.ParserProject;
import model.Store;
import ulits.ImportDeclarationUtil;

import java.util.List;
import java.util.Optional;

public class FieldNameReferRefactor {
        public  static  void nameReferRefactor(String packageName,String oldClassName,String newClassName) {
            List<CompilationUnit> units = Store.javaFiles;
            for (CompilationUnit unit : units) {
                Optional<PackageDeclaration> packageDeclaration = unit.getPackageDeclaration();
                if (packageDeclaration != null && packageDeclaration.isPresent()) {
                    if (packageDeclaration.get().getName().toString().equals(packageName)) {
                        List<FieldDeclaration> fieldDeclarations = unit.findAll(FieldDeclaration.class);
                        for (FieldDeclaration fieldDeclaration : fieldDeclarations) {
                            List<VariableDeclarator> variableDeclarators = fieldDeclaration.getVariables();
                            for (VariableDeclarator variableDeclarator : variableDeclarators) {
                                String name = variableDeclarator.getType().getClass().getName();
                                if (name.equals("com.github.javaparser.ast.type.ClassOrInterfaceType")) {
                                    ClassOrInterfaceType type = (ClassOrInterfaceType) variableDeclarator.getType();
                                    if (type.getName().toString().equals(oldClassName)) {
                                        type.setName(newClassName);
                                        Optional<Expression> initializer = variableDeclarator.getInitializer();
                                        if (initializer != null && initializer.isPresent()) {
                                            if(initializer.get().getClass().toString().equals("class com.github.javaparser.ast.expr.ObjectCreationExpr")){
                                                ObjectCreationExpr objectCreationExpr = (ObjectCreationExpr) initializer.get();
                                                if(objectCreationExpr.getType().toString().equals(oldClassName)) {
                                                    objectCreationExpr.setType(type);
                                                    variableDeclarator.setInitializer(objectCreationExpr);
                                                }
                                            }
                                        }
                                        variableDeclarator.setType(type);
                                        System.out.println(unit);
                                    }
                                }
                            }
                        }
                    } else {
                        int importNumber = ImportDeclarationUtil.importNumberUtil(unit, packageName, oldClassName);
                        if (importNumber == 1) {
                            List<FieldDeclaration> fieldDeclarations = unit.findAll(FieldDeclaration.class);
                            for (FieldDeclaration fieldDeclaration : fieldDeclarations) {
                                List<VariableDeclarator> variableDeclarators = fieldDeclaration.getVariables();
                                for (VariableDeclarator variableDeclarator : variableDeclarators) {
                                    String name = variableDeclarator.getType().getClass().getName();
                                    if (name.equals("com.github.javaparser.ast.type.ClassOrInterfaceType")) {
                                        ClassOrInterfaceType type = (ClassOrInterfaceType) variableDeclarator.getType();
                                        if (type.getName().toString().equals(oldClassName)) {
                                            type.setName(newClassName);
                                            Optional<Expression> initializer = variableDeclarator.getInitializer();
                                            if (initializer != null && initializer.isPresent()) {
                                                if(initializer.get().getClass().toString().equals("class com.github.javaparser.ast.expr.ObjectCreationExpr")){
                                                    ObjectCreationExpr objectCreationExpr = (ObjectCreationExpr) initializer.get();
                                                    if(objectCreationExpr.getType().toString().equals(oldClassName)) {
                                                        objectCreationExpr.setType(type);
                                                        variableDeclarator.setInitializer(objectCreationExpr);
                                                    }
                                                }
                                            }
                                            variableDeclarator.setType(type);
                                            System.out.println(unit);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
            public static void main (String[]args){
                FieldNameReferRefactor.nameReferRefactor("nametest", "ClassVariRefactor", "classVariRefactor");
            }

        }
