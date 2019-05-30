package refactor.refactorimpl;

import analysis.rule.ClassConstantNamingRule;
import analysis.rule.LowerCamelCaseVariableNaming;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import io.FileUlits;
import model.Issue;
import model.ReCorrect;
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
    public static void main(String[] args){
        String source= FileUlits.readFile("E:\\w8x-dev\\core\\src\\test\\java\\testName.java");
        List<Issue> issueList=new ArrayList<>();
        CompilationUnit unit= StaticJavaParser.parse(source);
        List<CompilationUnit> list=new ArrayList<>();
        list.add(unit);
       ContantNameRefactor contantNameRefactor=new ContantNameRefactor();
        ClassConstantNamingRule constantNamingRule=new ClassConstantNamingRule();
        constantNamingRule.apply(list);
        issueList=constantNamingRule.getContext().getIssues();
        for(Issue issue:issueList){
            contantNameRefactor.refactor(issue);
        }
    }
}
