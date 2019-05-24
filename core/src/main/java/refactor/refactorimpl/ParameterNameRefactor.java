package refactor.refactorimpl;

import analysis.rule.ParameterNamingRule;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.Parameter;
import io.FileUlits;
import model.Issue;
import model.ReCorrect;
import refactor.AbstractRefactor;
import ulits.SplitName;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ParameterNameRefactor extends AbstractRefactor {
    @Override
    public ReCorrect refactor(Issue issue) {
        Parameter parameter=(Parameter) issue.getIssueNode();
        try {
            parameterNameRefactor(parameter);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    public void parameterNameRefactor(Parameter parameter) throws IOException {
        String name="";
        List<String> nameList= SplitName.split(parameter.getNameAsString());
        for(String data:nameList){
            if(name.equals("")){
                name=name+data;
                continue;
            }
            data=data.substring(0,1).toUpperCase()+data.substring(1);
            name=name+data;

        }
        parameter.setName(name);
    }
    public static void main(String[] args){
        String source= FileUlits.readFile("E:\\w8x-dev\\core\\src\\test\\java\\testName.java");
        List<Issue> issueList=new ArrayList<>();
        CompilationUnit unit= StaticJavaParser.parse(source);
        List<CompilationUnit> list=new ArrayList<>();
        list.add(unit);
        ParameterNameRefactor parameterNameRefactor=new ParameterNameRefactor();
        ParameterNamingRule parameterNamingRule=new ParameterNamingRule();
        parameterNamingRule.apply(list);
        issueList=parameterNamingRule.getContext().getIssues();
        for(Issue issue:issueList){
            parameterNameRefactor.refactor(issue);
        }
    }
}
