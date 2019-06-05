package ulits;

import refer.packagerefer.PackageNameImportRefer;
import refer.packagerefer.PackageNameReferRefactor;

public class PackageReferUtil {
    public static void referUtil(String oldPackageName,String newPackageName){
        PackageNameReferRefactor.nameRefactor(oldPackageName,newPackageName);
        PackageNameImportRefer.importNameRefer(oldPackageName,newPackageName);
    }
}
