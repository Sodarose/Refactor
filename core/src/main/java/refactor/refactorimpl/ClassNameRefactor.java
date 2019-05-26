package refactor.refactorimpl;

import analysis.rule.ClassNamingShouldBeCamelRule;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import io.FileUlits;
import model.Issue;
import model.ReCorrect;
import refactor.AbstractRefactor;
import ulits.SplitName;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClassNameRefactor extends AbstractRefactor {
    @Override
    public ReCorrect refactor(Issue issue) {
        ClassOrInterfaceDeclaration classOrInterfaceDeclaration=(ClassOrInterfaceDeclaration) issue.getIssueNode();
        try {
            classNameRefactor(classOrInterfaceDeclaration);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private void classNameRefactor(ClassOrInterfaceDeclaration classOrInterfaceDeclaration) throws IOException {
        String name="";
        List<String> nameList= SplitName.split(classOrInterfaceDeclaration.getNameAsString());
        for(String data:nameList){
            data=data.substring(0,1).toUpperCase()+data.substring(1);

            name=name+data;
        }
        classOrInterfaceDeclaration.setName(name);
    }
    public static void main(String[] args){
        String source= FileUlits.readFile("E:\\w8x-dev\\core\\src\\test\\java\\testName.java");
        List<Issue> issueList=new ArrayList<>();
        CompilationUnit unit= StaticJavaParser.parse(source);
        List<CompilationUnit> list=new ArrayList<>();
        list.add(unit);
        ClassNameRefactor classNameRefactor=new ClassNameRefactor();
        ClassNamingShouldBeCamelRule classNamingShouldBeCamelRule=new ClassNamingShouldBeCamelRule();
        classNamingShouldBeCamelRule.apply(list);
        issueList=classNamingShouldBeCamelRule.getContext().getIssues();
        for(Issue issue:issueList){
            classNameRefactor.refactor(issue);
        }
    }
}
