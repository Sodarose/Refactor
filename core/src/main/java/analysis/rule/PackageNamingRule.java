package analysis.rule;

import analysis.AbstractRuleVisitor;
import analysis.BaseVisitor;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import io.FileUlits;
import model.Issue;
import model.IssueContext;
import model.JavaModel;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PackageNamingRule extends AbstractRuleVisitor {
    private JavaModel javaModel;
    private final Pattern PATTERN=Pattern.compile("^[a-z0-9]+(\\.[a-z][a-z0-9]*)*$");
    private BaseVisitor<PackageDeclaration> visitor=new BaseVisitor<PackageDeclaration>(){
        @Override
        public void visit(PackageDeclaration n, Object arg) {
            getList().add(n);
            super.visit(n, arg);
        }
    };
    @Override
    public IssueContext apply(List<JavaModel> javaModels) {
        for (JavaModel javaModel:javaModels){
            this.javaModel = javaModel;
            javaModel.getUnit().accept(visitor,null);
        }
        checkPackageName();
        return getContext();
    }

    public void checkPackageName(){
        List<PackageDeclaration> packageDeclarationList=visitor.getList();
        for(PackageDeclaration packageDeclaration:packageDeclarationList){
            String name=packageDeclaration.getNameAsString();
            Matcher matcher=PATTERN.matcher(name);
            boolean nameFlag=matcher.matches();
            if(!nameFlag){
                Issue issue=new Issue();
                issue.setIssueNode(packageDeclaration);
                issue.setJavaModel(javaModel);
                issue.setRefactorName(getSolutionClassName());
                issue.setDescription(getDescription());
                issue.setRuleName(getRuleName());
                getContext().getIssues().add(issue);
            }
        }
    }
}
