package analysis.rule;

import analysis.AbstractRuleVisitor;
import analysis.BaseVisitor;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.VariableDeclarator;
import io.FileUlits;
import model.Issue;
import model.IssueContext;
import ulits.SplitName;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LowerCamelCaseVariableNaming  extends AbstractRuleVisitor {
    private BaseVisitor<VariableDeclarator> visitor=new BaseVisitor<VariableDeclarator>(){
        @Override
        public void visit(VariableDeclarator n, Object arg) {
            getList().add(n);
            super.visit(n, arg);
        }
    };
    @Override
    public IssueContext apply(List<CompilationUnit> units) {
       for (CompilationUnit unit:units){
           unit.accept(visitor,null);
       }
        try {
            checkVariableName();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getContext();
    }
    public void checkVariableName() throws IOException {
        List<VariableDeclarator> VariableList=visitor.getList();
        for(VariableDeclarator variableDeclarator:VariableList){
            String name=variableDeclarator.getNameAsString();
            List<String> nameList= SplitName.split(name);
            boolean nameFlag=check(nameList);
            if(!nameFlag){
                Issue issue=new Issue();
                issue.setIssueNode(variableDeclarator);
                issue.setUnitNode(variableDeclarator.findRootNode());
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
    public static void main(String[] args){
        String source= FileUlits.readFile("E:\\w8x-dev\\core\\src\\test\\java\\testName.java");
        CompilationUnit unit= StaticJavaParser.parse(source);
        List<CompilationUnit> list=new ArrayList<>();
        list.add(unit);
        LowerCamelCaseVariableNaming lowerCamelCaseVariableNaming=new LowerCamelCaseVariableNaming();
        lowerCamelCaseVariableNaming.apply(list);
        System.out.println(lowerCamelCaseVariableNaming.getContext().getIssues().toString());
    }
}
