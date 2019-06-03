package refer.classrefer;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import io.ParserProject;
import model.Store;
import ulits.ImportDeclarationUtil;

import java.util.List;
import java.util.Optional;

public class InterfaceImplementRefactor {
    public static void implementRefactor(String packageName, String oldInferfaceName, String newInferfaceName) {
        List<CompilationUnit> units = Store.javaFiles;
        for (CompilationUnit unit : units) {
            Optional<PackageDeclaration> packageDeclaration = unit.getPackageDeclaration();
            if (packageDeclaration != null && packageDeclaration.isPresent()) {
                if (packageDeclaration.get().getName().toString().equals(packageName)) {
                    List<ClassOrInterfaceDeclaration> classOrInterfaceDeclarations = unit.findAll(ClassOrInterfaceDeclaration.class);
                    for (ClassOrInterfaceDeclaration classOrInterfaceDeclaration : classOrInterfaceDeclarations) {
                        int importNumber = ulits.ImportDeclarationUtil.importNumberUtil(unit, packageName, oldInferfaceName);
                        if (importNumber >= 1) {

                        }
                        if (importNumber == 0) {
                            List<ClassOrInterfaceType> classOrInterfaceTypeList = classOrInterfaceDeclaration.getImplementedTypes();
                           if (!(classOrInterfaceTypeList.isEmpty())) {
                               int index=0;//位置索引
                                for (ClassOrInterfaceType classOrInterfaceType : classOrInterfaceTypeList) {
                                    if (classOrInterfaceType.getNameAsString().equals(oldInferfaceName)) {
                                        classOrInterfaceType.setName(newInferfaceName);
                                        classOrInterfaceDeclaration.setImplementedType(index,classOrInterfaceType);
                                        System.out.println(unit);
                                    }
                                    index++;
                                }
                            }
                        }
                    }
                } else {
                    List<ClassOrInterfaceDeclaration> classOrInterfaceDeclarations = unit.findAll(ClassOrInterfaceDeclaration.class);
                    for (ClassOrInterfaceDeclaration classOrInterfaceDeclaration : classOrInterfaceDeclarations) {
                                int importNumber = ImportDeclarationUtil.importNumberUtil(unit, packageName, oldInferfaceName);
                                if (importNumber == 1) {
                                    List<ClassOrInterfaceType> classOrInterfaceTypeList = classOrInterfaceDeclaration.getImplementedTypes();
                                    if (!(classOrInterfaceTypeList.isEmpty())) {
                                        int index=0;//位置索引
                                        for (ClassOrInterfaceType classOrInterfaceType : classOrInterfaceTypeList) {

                                   if (classOrInterfaceType.getNameAsString().equals(oldInferfaceName)) {
                                        classOrInterfaceType.setName(newInferfaceName);
                                       classOrInterfaceDeclaration.setImplementedType(index,classOrInterfaceType);
                                        System.out.println(unit);
                                    }
                                   index++;
                                }
                                if (importNumber > 1) {

                                }

                            }
                        }
                    }
                }
            }
        }
    }
    public static void main(String args[]){
       InterfaceImplementRefactor.implementRefactor("nametest","infertest","Infertest");
    }
}

