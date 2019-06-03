package refactor.refactorimpl;

import analysis.rule.ClassConstantNamingRule;
import analysis.rule.LowerCamelCaseVariableNaming;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import io.FileUlits;
import model.Issue;
import refactor.AbstractRefactor;

import java.util.ArrayList;
import java.util.List;

public class ContantNameRefactor extends AbstractRefactor {

    @Override
    public void refactor(Issue issue) {
        FieldDeclaration fieldDeclaration=(FieldDeclaration) issue.getIssueNode();
        constantNameRefactor(fieldDeclaration);
    }
    public void constantNameRefactor(FieldDeclaration fieldDeclaration){
        String constantName=fieldDeclaration.getVariable(0).getNameAsString();
        fieldDeclaration.getVariable(0).setName(constantName.toUpperCase());
    }

}
