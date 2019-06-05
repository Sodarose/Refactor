package refactor.refactorimpl;

import com.github.javaparser.ast.PackageDeclaration;
import model.Issue;
import refactor.AbstractRefactor;
import ulits.PackageReferUtil;

public class PackageNameRefactor extends AbstractRefactor {
    @Override
    public void refactor(Issue issue) {
        PackageDeclaration packageDeclaration=(PackageDeclaration) issue.getIssueNode();
        packageNameRefactor(packageDeclaration);
    }
    public void packageNameRefactor(PackageDeclaration packageDeclaration){
        String newName=packageDeclaration.getNameAsString().toLowerCase();
        String oldName=packageDeclaration.getNameAsString();
        packageDeclaration.setName(packageDeclaration.getNameAsString().toLowerCase());
        PackageReferUtil.referUtil(oldName,newName);
    }
}
