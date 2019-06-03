package refer.classrefer;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import model.Store;
import ulits.BoyerMoore;
import ulits.ImportDeclarationUtil;

import java.util.List;
import java.util.Optional;

public class SetFieldNameReferRefactor {
    public static void nameReferRefactor(String packageName,String oldClassName,String newClassName){
        String flagName="Set<"+oldClassName+">";
        List<CompilationUnit> units= Store.javaFiles;
        for(CompilationUnit unit:units) {
            Optional<PackageDeclaration> packageDeclaration = unit.getPackageDeclaration();
            if (packageDeclaration != null && packageDeclaration.isPresent()) {
                if (packageDeclaration.get().getName().toString().equals(packageName)) {
                    List<FieldDeclaration> fieldDeclarations = unit.findAll(FieldDeclaration.class);
                    for (FieldDeclaration fieldDeclaration : fieldDeclarations) {
                        List<VariableDeclarator> variableDeclarators = fieldDeclaration.getVariables();
                        for (VariableDeclarator variableDeclarator : variableDeclarators) {
                            int importNumber = ulits.ImportDeclarationUtil.importNumberUtil(unit, packageName, oldClassName);
                            if (importNumber >= 1) {

                            }
                            if (importNumber == 0){
                                String name = variableDeclarator.getType().getClass().getName();
                                if (name.equals("com.github.javaparser.ast.type.ClassOrInterfaceType")) {
                                    String className = variableDeclarator.getType().toString();
                                    BoyerMoore boyerMoore = new BoyerMoore();
                                    int pos = boyerMoore.match(className, flagName);
                                    if (pos != -1) {
                                        ClassOrInterfaceType type = (ClassOrInterfaceType) variableDeclarator.getType();
                                        ClassOrInterfaceType classOrInterfaceType=(ClassOrInterfaceType) type.getTypeArguments().get().get(0);
                                        ClassOrInterfaceType type1 = (ClassOrInterfaceType) type.getTypeArguments().get().get(0);
                                        type1.setName(newClassName);
                                        type.setTypeArguments(type1);
                                        Optional<Expression> initializer = variableDeclarator.getInitializer();
                                        if (initializer != null && initializer.isPresent()) {
                                            if (initializer.get().getClass().toString().equals("class com.github.javaparser.ast.expr.ObjectCreationExpr")) {
                                                ObjectCreationExpr objectCreationExpr = (ObjectCreationExpr) initializer.get();
                                                if(!(objectCreationExpr.getType().getTypeArguments().get().isEmpty())) {
                                                    ClassOrInterfaceType Argumenttype = (ClassOrInterfaceType) objectCreationExpr.getType().getTypeArguments().get().get(0);
                                                    if (Argumenttype != null && Argumenttype.getNameAsString().equals(oldClassName)) {
                                                        Argumenttype.setName(newClassName);
                                                        objectCreationExpr.setTypeArguments(Argumenttype);
                                                        objectCreationExpr.removeTypeArguments();
                                                    }
                                                }
                                            }
                                        }
                                        variableDeclarator.setType(type);
                                    }
                                }
                            }
                        }
                    }
                }
                else {
                    int importNumber = ImportDeclarationUtil.importNumberUtil(unit, packageName, oldClassName);
                    if (importNumber == 1) {
                        List<FieldDeclaration> fieldDeclarations = unit.findAll(FieldDeclaration.class);
                        for (FieldDeclaration fieldDeclaration : fieldDeclarations) {
                            List<VariableDeclarator> variableDeclarators = fieldDeclaration.getVariables();
                            for (VariableDeclarator variableDeclarator : variableDeclarators) {
                                String name = variableDeclarator.getType().getClass().getName();
                                if (name.equals("com.github.javaparser.ast.type.ClassOrInterfaceType")) {
                                    String className = variableDeclarator.getType().toString();
                                    BoyerMoore boyerMoore = new BoyerMoore();
                                    int pos = boyerMoore.match(className, flagName);
                                    if (pos != -1) {
                                        ClassOrInterfaceType type = (ClassOrInterfaceType) variableDeclarator.getType();
                                        ClassOrInterfaceType classOrInterfaceType=(ClassOrInterfaceType) type.getTypeArguments().get().get(0);
                                        ClassOrInterfaceType type1 = (ClassOrInterfaceType) type.getTypeArguments().get().get(0);
                                        type1.setName(newClassName);
                                        type.setTypeArguments(type1);
                                        Optional<Expression> initializer = variableDeclarator.getInitializer();
                                        if (initializer != null && initializer.isPresent()) {
                                            if (initializer.get().getClass().toString().equals("class com.github.javaparser.ast.expr.ObjectCreationExpr")) {
                                                ObjectCreationExpr objectCreationExpr = (ObjectCreationExpr) initializer.get();
                                                if(!(objectCreationExpr.getType().getTypeArguments().get().isEmpty())) {
                                                    ClassOrInterfaceType Argumenttype = (ClassOrInterfaceType) objectCreationExpr.getType().getTypeArguments().get().get(0);
                                                    if (Argumenttype != null && Argumenttype.getNameAsString().equals(oldClassName)) {
                                                        Argumenttype.setName(newClassName);
                                                        objectCreationExpr.setTypeArguments(Argumenttype);
                                                        objectCreationExpr.removeTypeArguments();
                                                    }
                                                }
                                            }
                                        }
                                        variableDeclarator.setType(type);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
