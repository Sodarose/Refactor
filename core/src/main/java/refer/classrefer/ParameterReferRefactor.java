package refer.classrefer;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import io.ParserProject;
import model.Store;
import ulits.ImportDeclarationUtil;

import java.util.List;
import java.util.Optional;

public class ParameterReferRefactor {
    public static void nameReferRefactor(String packageName,String oldClassName,String newClassName){
        List<CompilationUnit> units = Store.javaFiles;
        for (CompilationUnit unit : units) {
            Optional<PackageDeclaration> packageDeclaration = unit.getPackageDeclaration();
            if (packageDeclaration != null && packageDeclaration.isPresent()) {
                if (packageDeclaration.get().getName().toString().equals(packageName)) {
                    List<Parameter> parameterList=unit.findAll(Parameter.class);
                    int importNumber = ulits.ImportDeclarationUtil.importNumberUtil(unit, packageName, oldClassName);
                    if (importNumber >= 1) {

                    }
                    if(importNumber==0){
                        for (Parameter parameter:parameterList) {
                            String name = parameter.getType().getClass().getName().toString();
                            if (name.equals("com.github.javaparser.ast.type.ClassOrInterfaceType")) {
                                ClassOrInterfaceType type= (ClassOrInterfaceType) parameter.getType();
                                if(type.getNameAsString().equals(oldClassName)){
                                    type.setName(newClassName);
                                    parameter.setType(type);
                                    System.out.println(unit);
                                }
                            }
                        }
                    }
                }
                else {
                        int importNumber = ImportDeclarationUtil.importNumberUtil(unit, packageName, oldClassName);
                        if (importNumber == 1) {
                            List<Parameter> parameterList=unit.findAll(Parameter.class);
                            for (Parameter parameter:parameterList) {
                                String name=parameter.getType().getClass().getName().toString();
                                if (name.equals("com.github.javaparser.ast.type.ClassOrInterfaceType")) {
                                    ClassOrInterfaceType type= (ClassOrInterfaceType) parameter.getType();
                                    if(type.getNameAsString().equals(oldClassName)){
                                        type.setName(newClassName);
                                        parameter.setType(type);
                                        System.out.println(unit);
                                    }
                                }
                        }
                    }
                }
        }
        }
    }
    public static void main (String[] args){
        ParameterReferRefactor.nameReferRefactor("nametest", "ClassVariRefactor", "classVariRefactor");
    }
}
