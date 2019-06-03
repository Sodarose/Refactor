import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import io.ParserProject;
import model.Store;
import ulits.ImportDeclarationUtil;

import java.util.List;
import java.util.Optional;


public class ClassExtendsRefactor {
    public static void main(String args[]) {
        String packageName = "nametest";
        String className = "ClassVariRefactor";
        List<CompilationUnit> units = Store.javaFiles;
        for (CompilationUnit unit : units) {
            Optional<PackageDeclaration> packageDeclaration = unit.getPackageDeclaration();
            if (packageDeclaration != null && packageDeclaration.isPresent()) {
                if (packageDeclaration.get().getName().toString().equals(packageName)) {
                    List<ClassOrInterfaceDeclaration> classOrInterfaceDeclarations = unit.findAll(ClassOrInterfaceDeclaration.class);
                    for (ClassOrInterfaceDeclaration classOrInterfaceDeclaration : classOrInterfaceDeclarations) {
                        int importNumber = ImportDeclarationUtil.importNumberUtil(unit, packageName, className);
                        if (importNumber >= 1) {

                        }
                        if(importNumber==0) {
                            List<ClassOrInterfaceType> classOrInterfaceTypeList = classOrInterfaceDeclaration.getExtendedTypes();
                        if (!(classOrInterfaceTypeList.isEmpty())) {
                            for (ClassOrInterfaceType classOrInterfaceType : classOrInterfaceTypeList) {
                                if (classOrInterfaceType.getNameAsString().equals(className)) {
                                    classOrInterfaceType.setName("classVariRefactor");
                                    classOrInterfaceDeclaration.setExtendedType(0, classOrInterfaceType);
                                    System.out.println(unit);
                                }
                            }
                        }
                    }
                }} else {
                        List<ClassOrInterfaceDeclaration> classOrInterfaceDeclarations = unit.findAll(ClassOrInterfaceDeclaration.class);
                        for (ClassOrInterfaceDeclaration classOrInterfaceDeclaration : classOrInterfaceDeclarations) {
                            List<ClassOrInterfaceType> classOrInterfaceTypeList = classOrInterfaceDeclaration.getExtendedTypes();
                            if (!(classOrInterfaceTypeList.isEmpty())) {
                                for (ClassOrInterfaceType classOrInterfaceType : classOrInterfaceTypeList) {

                                    int importNumber = ImportDeclarationUtil.importNumberUtil(unit, packageName, className);
                                    if (importNumber == 1) {
                                        if (classOrInterfaceType.getNameAsString().equals(className)) {
                                            classOrInterfaceType.setName("classVariRefactor");
                                            classOrInterfaceDeclaration.setExtendedType(0, classOrInterfaceType);
                                            System.out.println(unit);
                                        }
                                    }
                                    if(importNumber>1){

                                    }

                                }
                            }
                        }
                    }
                }
            }
        }
    }
