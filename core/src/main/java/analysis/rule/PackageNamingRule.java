package analysis.rule;

import analysis.AbstractRuleVisitor;
import analysis.BaseVisitor;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import io.FileUlits;
import model.Issue;
import model.IssueContext;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PackageNamingRule extends AbstractRuleVisitor {
    private final Pattern PATTERN=Pattern.compile("^[a-z0-9]+(\\.[a-z][a-z0-9]*)*$");
    private BaseVisitor<PackageDeclaration> visitor=new BaseVisitor<PackageDeclaration>(){
        @Override
        public void visit(PackageDeclaration n, Object arg) {
            getList().add(n);
            super.visit(n, arg);
        }
    };
    @Override
    public IssueContext apply(List<CompilationUnit> units) {
        for (CompilationUnit unit:units){
            unit.accept(visitor,null);
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
                issue.setUnitNode(packageDeclaration.findRootNode());
                getContext().getIssues().add(issue);
            }
        }
    }
    public static void main(String[] args){
        String source= FileUlits.readFile("E:\\w8x-dev\\core\\src\\main\\java\\analysis\\rule\\MethodNamingShouldBeCamelRule.java");
        CompilationUnit unit= StaticJavaParser.parse(source);
        List<CompilationUnit> list=new ArrayList<>();
        list.add(unit);
        PackageNamingRule packageNamingRule=new PackageNamingRule();
        packageNamingRule.apply(list);
        System.out.println(packageNamingRule.getContext().getIssues().toString());
    }
}
