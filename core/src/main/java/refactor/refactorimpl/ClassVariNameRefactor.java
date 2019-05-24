package refactor.refactorimpl;

import analysis.rule.ClassVariNamingRule;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import io.FileUlits;
import model.Issue;
import model.ReCorrect;
import refactor.AbstractRefactor;
import ulits.SplitName;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClassVariNameRefactor  extends AbstractRefactor {
    @Override
    public ReCorrect refactor(Issue issue) {
        FieldDeclaration fieldDeclaration=(FieldDeclaration)issue.getIssueNode();
        try {
            variableNameRefactor(fieldDeclaration);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    public void variableNameRefactor(FieldDeclaration fieldDeclaration) throws IOException {
        String name="";
        List<String> nameList = SplitName.split(fieldDeclaration.getVariable(0).getNameAsString());
        for(String data:nameList){
            if(name.equals("")){
                name=name+data;
                continue;
            }
            data=data.substring(0,1).toUpperCase()+data.substring(1);
            name=name+data;
        }
        fieldDeclaration.getVariable(0).setName(name);
    }
    public static void main(String[] args){
        String source= FileUlits.readFile("E:\\w8x-dev\\core\\src\\test\\java\\testName.java");
        List<Issue> issueList=new ArrayList<>();
        CompilationUnit unit= StaticJavaParser.parse(source);
        List<CompilationUnit> list=new ArrayList<>();
        list.add(unit);
        ClassVariNameRefactor classVariNameRefactor=new ClassVariNameRefactor();
        ClassVariNamingRule classVariNamingRule=new ClassVariNamingRule();
        classVariNamingRule.apply(list);
        issueList=classVariNamingRule.getContext().getIssues();
        for(Issue issue:issueList){
            classVariNameRefactor.refactor(issue);
        }
    }
}
