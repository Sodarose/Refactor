package analysis.rule;

import analysis.AbstractRuleVisitor;
import analysis.BaseVisitor;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.Parameter;
import model.Issue;
import model.IssueContext;
import ulits.SplitName;

import java.io.IOException;
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
            boolean nameFlag=check(nameList);
            if(!nameFlag){
                Issue issue=new Issue();
                issue.setIssueNode(parameter);
                issue.setUnitNode(parameter.findRootNode());
                getContext().getIssues().add(issue);
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
}
