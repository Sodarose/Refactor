package analysis.rule;

import analysis.AbstractRuleVisitor;
import analysis.BaseVisitor;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import io.FileUlits;
import model.Issue;
import model.IssueContext;
import ulits.SplitName;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClassNamingShouldBeCamelRule extends AbstractRuleVisitor {
    private final List<String> classNameList=new ArrayList<>();
    private final List<String> issueNameList=new ArrayList<>();
    private String PublicName=null;
    private final BaseVisitor<ClassOrInterfaceDeclaration> visitor=new BaseVisitor<ClassOrInterfaceDeclaration>(){
        @Override
        public void visit(ClassOrInterfaceDeclaration n, Object arg) {
            if(n.isPublic()){
                PublicName=n.getNameAsString();
            }
                   classNameList.add(n.getNameAsString());
        }
    };

    public List<String> checkClassName() throws IOException {
        for(String name:classNameList){
            List<String> nameList=SplitName.split(name);
            boolean nameFlag=check(nameList);
            if(!nameFlag){
                issueNameList.add(name);
            }
        }
        return null;
    }
    public boolean check(List<String> nameList){
        for(String name:nameList){
            char temp=name.charAt(0);
            if(temp >= 97 && temp <=122){
                return false;
            }
        }
        return true;
    }
    @Override
    public IssueContext apply(List<CompilationUnit> units)  {
        for(CompilationUnit unit:units){
            unit.accept(visitor,null);
            try {
                checkClassName();
            } catch (IOException e) {
                e.printStackTrace();
            }
            for(String name:issueNameList){
                Issue issue=new Issue();
                issue.setClassName(name);
                issue.setFileName(PublicName);
                getContext().getIssues().add(issue);
            }
        }
        return null;
    }
    public static void main(String[] args){
            String source= FileUlits.readFile("E:\\w8x-dev\\core\\src\\test\\java\\testName.java");
            CompilationUnit unit= StaticJavaParser.parse(source);
            List<CompilationUnit> list=new ArrayList<>();
            list.add(unit);
            ClassNamingShouldBeCamelRule classNamingShouldBeCamelRule=new ClassNamingShouldBeCamelRule();
            classNamingShouldBeCamelRule.apply(list);
            System.out.println(classNamingShouldBeCamelRule.getContext().getIssues().toString());
    }
}
