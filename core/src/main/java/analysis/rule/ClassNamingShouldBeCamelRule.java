package analysis.rule;

import analysis.AbstractRuleVisitor;
import analysis.BaseVisitor;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import io.FileUlits;
import model.Issue;
import model.IssueContext;
import model.JavaModel;
import ulits.SplitName;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClassNamingShouldBeCamelRule extends AbstractRuleVisitor {
    private final List<String> issueNameList=new ArrayList<>();
    private final BaseVisitor<ClassOrInterfaceDeclaration> visitor=new BaseVisitor<ClassOrInterfaceDeclaration>(){
        @Override
        public void visit(ClassOrInterfaceDeclaration n, Object arg) {
                getList().add(n);
        }
    };

    public void checkClassName(JavaModel javaModel) throws IOException {
        List<ClassOrInterfaceDeclaration> classList=visitor.getList();
        for(ClassOrInterfaceDeclaration classOrInterfaceDeclaration:classList) {
            String name = classOrInterfaceDeclaration.getNameAsString();
            List<String> nameList = SplitName.split(name);
            if (!(nameList.isEmpty())) {
                boolean nameFlag = check(nameList);
                if (!nameFlag) {
                    System.out.println(name);
                    Issue issue = new Issue();
                    issue.setIssueNode(classOrInterfaceDeclaration);
                    issue.setJavaModel(javaModel);
                    System.out.println(javaModel.getReadPath());
                    issue.setRefactorName(getSolutionClassName());
                    issue.setDescription(getDescription());
                    issue.setRuleName(getRuleName());
                    getContext().getIssues().add(issue);
                }
            }
        }
    }
    public boolean check(List<String> nameList){
            for (String name : nameList) {
                char temp = name.charAt(0);
                if (temp >= 97 && temp <= 122) {
                    return false;
                }
            }
            return true;
    }
    @Override
    public IssueContext apply(List<JavaModel> javaModels)  {
        for (JavaModel javaModel:javaModels){
            javaModel.getUnit().accept(visitor,null);
        }
        for (JavaModel javaModel:javaModels){
            try {
                checkClassName(javaModel);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return getContext();
    }

}
