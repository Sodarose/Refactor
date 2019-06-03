package analysis.rule;

import analysis.AbstractRuleVisitor;
import analysis.BaseVisitor;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import io.FileUlits;
import model.Issue;
import model.IssueContext;
import model.JavaModel;

import java.util.ArrayList;
import java.util.List;

public class ClassConstantNamingRule extends AbstractRuleVisitor {
    private JavaModel javaModel;
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
                issue.setJavaModel(javaModel);
                issue.setRefactorName(getSolutionClassName());
                issue.setDescription(getDescription());
                issue.setRuleName(getRuleName());
                getContext().getIssues().add(issue);
            }
        }
    }
    @Override
    public IssueContext apply(List<JavaModel> javaModels) {
        for (JavaModel javaModel:javaModels){
            this.javaModel = javaModel;
            javaModel.getUnit().accept(visitor,null);
        }
        checkConstantName();
        return getContext();
    }
    public static void main(String[] args) {

    }
}
