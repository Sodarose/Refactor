package refactor.refactorimpl;

import com.github.javaparser.ast.PackageDeclaration;
import model.Issue;
import model.ReCorrect;
import refactor.AbstractRefactor;

public class PackageNameRefactor extends AbstractRefactor {
    @Override
    public ReCorrect refactor(Issue issue) {
        PackageDeclaration packageDeclaration=(PackageDeclaration) issue.getIssueNode();
        packageNameRefactor(packageDeclaration);
        return null;
    }
    public void packageNameRefactor(PackageDeclaration packageDeclaration){
        packageDeclaration.setName(packageDeclaration.getNameAsString().toLowerCase());
    }
}
