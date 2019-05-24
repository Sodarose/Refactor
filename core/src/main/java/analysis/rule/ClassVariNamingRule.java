package analysis.rule;

import analysis.AbstractRuleVisitor;
import analysis.BaseVisitor;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import io.FileUlits;
import model.Issue;
import model.IssueContext;
import ulits.SplitName;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClassVariNamingRule extends AbstractRuleVisitor {
    private BaseVisitor<FieldDeclaration> visitor=new BaseVisitor<FieldDeclaration>(){
        @Override
        public void visit(FieldDeclaration n, Object arg) {
            if(!(n.isStatic()||n.isFinal())){
                getList().add(n);
            }
            super.visit(n, arg);
        }
    };
    @Override
    public IssueContext apply(List<CompilationUnit> units) {
        for (CompilationUnit unit : units) {
            unit.accept(visitor, null);
        }
        try {
            checkVariableName();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getContext();
    }
    public void checkVariableName() throws IOException {
        List<FieldDeclaration> fieldDeclarationList=visitor.getList();
        for (FieldDeclaration fieldDeclaration:fieldDeclarationList){
            String name=fieldDeclaration.getVariable(0).getNameAsString();
            List<String> nameList= SplitName.split(name);
            if (nameList!=null) {
                boolean nameFlag = check(nameList);
                if (!nameFlag) {
                    Issue issue = new Issue();
                    issue.setIssueNode(fieldDeclaration);
                    issue.setUnitNode(fieldDeclaration.findRootNode());
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
       ClassVariNamingRule classVariNamingRule=new ClassVariNamingRule();
        classVariNamingRule.apply(list);
        System.out.println(classVariNamingRule.getContext().getIssues());
    }
}
