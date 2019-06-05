package refer.packagerefer;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import model.Store;

import java.util.List;

public class PackageNameReferRefactor {
    public static void nameRefactor(String oldPackageName,String newPackageName){
        List<CompilationUnit> units= Store.javaFiles;
        for (CompilationUnit unit:units){
           if(unit.getPackageDeclaration()!=null&&unit.getPackageDeclaration().isPresent()){
               PackageDeclaration packageDeclaration=unit.getPackageDeclaration().get();
               if(packageDeclaration.getNameAsString().equals(oldPackageName)){
                   packageDeclaration.setName(newPackageName);
                   unit.setPackageDeclaration(packageDeclaration);
               }
           }
        }
    }
}
