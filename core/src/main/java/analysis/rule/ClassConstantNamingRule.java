package analysis.rule;

import analysis.AbstractRuleVisitor;
import analysis.BaseVisitor;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import io.FileUlits;
import model.Issue;
import model.IssueContext;

import java.util.ArrayList;
import java.util.List;

public class ClassConstantNamingRule extends AbstractRuleVisitor {
    private BaseVisitor<FieldDeclaration> visitor=new BaseVisitor<FieldDeclaration>(){
        @Override
        public void visit(FieldDeclaration n, Object arg) {
            if(n.isStatic()||n.isFinal()){
                getList().add(n);
            }
            super.visit(n, arg);
        }
    };

    public void checkConstantName(){
        List<FieldDeclaration> fieldDeclarationList=visitor.getList();
        for (FieldDeclaration fieldDeclaration:fieldDeclarationList){
            String constantName=fieldDeclaration.getVariable(0).getNameAsString();
            if(!(constantName.equals(constantName.toUpperCase()))){
                Issue issue=new Issue();
                issue.setIssueNode(fieldDeclaration);
                issue.setUnitNode(fieldDeclaration.findRootNode());
                issue.setRefactorName(getSolutionClassName());
                getContext().getIssues().add(issue);
            }
        }
    }
    @Override
    public IssueContext apply(List<CompilationUnit> units) {
        for (CompilationUnit unit:units){
            unit.accept(visitor,null);
        }
        checkConstantName();
        return getContext();
    }
    public static void main(String[] args) {
        String source = FileUlits.readFile("E:\\w8x-dev\\core\\src\\test\\java\\testName.java");
        CompilationUnit unit = StaticJavaParser.parse(source);
        List<CompilationUnit> list = new ArrayList<>();
        list.add(unit);
        ClassConstantNamingRule classConstantNamingRule=new ClassConstantNamingRule();
        classConstantNamingRule.apply(list);
        System.out.println(classConstantNamingRule.getContext().getIssues());
    }
}
