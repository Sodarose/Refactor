package refactor.refactorimpl;

import com.github.javaparser.ast.PackageDeclaration;
import model.Issue;
import refactor.AbstractRefactor;

public class PackageNameRefactor extends AbstractRefactor {
    @Override
    public void refactor(Issue issue) {
        PackageDeclaration packageDeclaration=(PackageDeclaration) issue.getIssueNode();
        packageNameRefactor(packageDeclaration);
    }
    public void packageNameRefactor(PackageDeclaration packageDeclaration){
        packageDeclaration.setName(packageDeclaration.getNameAsString().toLowerCase());
    }
}
