package analysis.rule;

import analysis.AbstractRuleVisitor;
import analysis.BaseVisitor;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.Parameter;
import io.FileUlits;
import model.Issue;
import model.IssueContext;
import ulits.SplitName;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ParameterNamingRule extends AbstractRuleVisitor {
    private BaseVisitor<Parameter> visitor=new BaseVisitor<Parameter>(){
        @Override
        public void visit(Parameter n, Object arg) {
            getList().add(n);
            super.visit(n, arg);
        }
    };
    @Override
    public IssueContext apply(List<CompilationUnit> units) {
        for(CompilationUnit unit:units){
            unit.accept(visitor,null);
        }
        try {
            checkParameterName();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getContext();
    }
    public void checkParameterName() throws IOException {
        List<Parameter> parameterList=visitor.getList();
        for(Parameter parameter:parameterList){
            String name=parameter.getNameAsString();
            List<String> nameList= SplitName.split(name);
            if(nameList!=null) {
                boolean nameFlag = check(nameList);
                if (!nameFlag) {
                    Issue issue = new Issue();
                    issue.setIssueNode(parameter);
                    issue.setUnitNode(parameter.findRootNode());
                    issue.setRefactorName(getSolutionClassName());
                    getContext().getIssues().add(issue);
                }
            }
        }
    }
    public boolean check(List<String> nameList){
        boolean flag=false;
        for(String name:nameList){
            if(flag==false){
                flag=true;
                continue;
            }
            char temp=name.charAt(0);
            if(temp >= 97 && temp <=122){
                return false;
            }
        }
        return true;
    }
    public static void main(String[] args){
        String source= FileUlits.readFile("E:\\w8x-dev\\core\\src\\test\\java\\testName.java");
        CompilationUnit unit= StaticJavaParser.parse(source);
        List<CompilationUnit> list=new ArrayList<>();
        list.add(unit);
        ParameterNamingRule parameterNamingRule=new ParameterNamingRule();
        parameterNamingRule.apply(list);
        System.out.println(parameterNamingRule.getContext().getIssues());
    }
}
