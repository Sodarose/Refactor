package refactor.refactorimpl;

import analysis.rule.MethodNamingShouldBeCamelRule;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import io.FileUlits;
import model.Issue;
import model.ReCorrect;
import refactor.AbstractRefactor;
import ulits.SplitName;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MethodNameRefactor extends AbstractRefactor {
    @Override
    public void refactor(Issue issue) {
        MethodDeclaration methodDeclaration=(MethodDeclaration) issue.getIssueNode();
        try {
            methodNameRefactor(methodDeclaration);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void methodNameRefactor(MethodDeclaration methodDeclaration) throws IOException {
        String name="";
        List<String> nameList= SplitName.split(methodDeclaration.getNameAsString());
        for(String data:nameList){
            if(name.equals("")){
                name=name+data;
                continue;
            }
            data=data.substring(0,1).toUpperCase()+data.substring(1);
            name=name+data;

        }
        methodDeclaration.setName(name);
    }
    public static void main(String[] args){
        String source= FileUlits.readFile("E:\\w8x-dev\\core\\src\\test\\java\\testName.java");
        List<Issue> issueList=new ArrayList<>();
        CompilationUnit unit= StaticJavaParser.parse(source);
        List<CompilationUnit> list=new ArrayList<>();
        list.add(unit);
        MethodNameRefactor methodNameRefactor=new MethodNameRefactor();
        MethodNamingShouldBeCamelRule methodNamingShouldBeCamelRule=new MethodNamingShouldBeCamelRule();
        methodNamingShouldBeCamelRule.apply(list);
        issueList=methodNamingShouldBeCamelRule.getContext().getIssues();
        for(Issue issue:issueList){
            methodNameRefactor.refactor(issue);
        }
    }
}
