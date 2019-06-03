package refer.classrefer;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.PackageDeclaration;
import io.ParserProject;
import model.Store;

import java.util.List;
import java.util.Optional;

public class ClassImportReferRefactor {
    public static void importRefactor(String packageName,String oldClassName,String newClassName){
        List<CompilationUnit> units = Store.javaFiles;
        for (CompilationUnit unit : units) {
            Optional<PackageDeclaration> packageDeclarationOptional=unit.getPackageDeclaration();
            if(packageDeclarationOptional!=null&&packageDeclarationOptional.isPresent()){
                String nowPackageName=packageDeclarationOptional.get().getNameAsString();
                if(nowPackageName.equals(packageName)){
                    continue;
                }
                else {
                    List<ImportDeclaration> importDeclarations=unit.getImports();
                    for(ImportDeclaration importDeclaration:importDeclarations){
                        if(importDeclaration.getName().toString().equals(packageName+"."+oldClassName)){
                            importDeclaration.setName(packageName+"."+newClassName);
                            System.out.println(unit);
                        }
                    }
                }
            }
        }
        }
        public static void main(String args[]){
            ClassImportReferRefactor.importRefactor("nametest","ClassVariRefactor","classVariRefactor");
        }
    }
