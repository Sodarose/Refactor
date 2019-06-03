package refactor.refactorimpl;

import analysis.rule.ClassVariNamingRule;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import io.FileUlits;
import model.Issue;
import refactor.AbstractRefactor;
import ulits.SplitName;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClassVariNameRefactor  extends AbstractRefactor {
    @Override
    public void refactor(Issue issue) {
        FieldDeclaration fieldDeclaration=(FieldDeclaration)issue.getIssueNode();
        System.out.println(fieldDeclaration);
        try {
            variableNameRefactor(fieldDeclaration);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void variableNameRefactor(FieldDeclaration fieldDeclaration) throws IOException {
        String name="";
        List<String> nameList = SplitName.split(fieldDeclaration.getVariable(0).getNameAsString());
        if(nameList==null){
            return;
        }
        for(String data:nameList){
            if(name.equals("")){
                name=name+data;
                continue;
            }
            data=data.substring(0,1).toUpperCase()+data.substring(1);
            name=name+data;
        }
        fieldDeclaration.getVariable(0).setName(name);
    }

}
