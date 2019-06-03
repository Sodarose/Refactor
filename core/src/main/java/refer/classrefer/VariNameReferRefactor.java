package refer.classrefer;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import io.ParserProject;
import model.Store;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import ulits.ImportDeclarationUtil;

import java.util.List;
import java.util.Optional;

public class VariNameReferRefactor {
    public static void NameReferRefactor(String packageName,String oldClassName,String newClassName){
        List<CompilationUnit> units = Store.javaFiles;
        for (CompilationUnit unit : units) {
            Optional<PackageDeclaration> packageDeclaration = unit.getPackageDeclaration();
           if (packageDeclaration != null && packageDeclaration.isPresent()) {
               if (packageDeclaration.get().getName().toString().equals(packageName)) {
                   List<VariableDeclarationExpr> variableDeclarationExprs = unit.findAll(VariableDeclarationExpr.class);
                   for (VariableDeclarationExpr var : variableDeclarationExprs) {
                       for (VariableDeclarator variableDeclarator : var.getVariables()) {
                           String name = variableDeclarator.getType().getClass().getName();
                           if (name.equals("com.github.javaparser.ast.type.ClassOrInterfaceType")) {
                               ClassOrInterfaceType type = (ClassOrInterfaceType) variableDeclarator.getType();
                               if (type.getName().toString().equals(oldClassName)) {
                                   type.setName(newClassName);
                                   Optional<Expression> initializer = variableDeclarator.getInitializer();
                                   if (initializer != null && initializer.isPresent()) {
                                       if (initializer.get().getClass().toString().equals("class com.github.javaparser.ast.expr.ObjectCreationExpr")) {
                                           ObjectCreationExpr objectCreationExpr = (ObjectCreationExpr) initializer.get();
                                           if (objectCreationExpr.getType().toString().equals(oldClassName)) {
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
                       List<VariableDeclarationExpr> variableDeclarationExprs = unit.findAll(VariableDeclarationExpr.class);
                       for (VariableDeclarationExpr var : variableDeclarationExprs) {
                           for (VariableDeclarator variableDeclarator : var.getVariables()) {
                               String name = variableDeclarator.getType().getClass().getName();
                               if (name.equals("com.github.javaparser.ast.type.ClassOrInterfaceType")) {
                                   ClassOrInterfaceType type = (ClassOrInterfaceType) variableDeclarator.getType();
                                   if (type.getName().toString().equals(oldClassName)) {
                                       type.setName(newClassName);
                                       Optional<Expression> initializer = variableDeclarator.getInitializer();
                                       if (initializer != null && initializer.isPresent()) {
                                           if (initializer.get().getClass().toString().equals("class com.github.javaparser.ast.expr.ObjectCreationExpr")) {
                                               ObjectCreationExpr objectCreationExpr = (ObjectCreationExpr) initializer.get();
                                               if (objectCreationExpr.getType().toString().equals(oldClassName)) {
                                                   objectCreationExpr.setType(type);
                                                   variableDeclarator.setInitializer(objectCreationExpr);
                                                   System.out.println(1);
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
    public static void main (String[] args){
       VariNameReferRefactor.NameReferRefactor("nametest", "ClassVariRefactor", "classVariRefactor");
    }
    }

