package refactor.refactorimpl;

import analysis.rule.ClassNamingShouldBeCamelRule;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import io.FileUlits;
import model.Issue;
import refactor.AbstractRefactor;
import ulits.SplitName;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClassNameRefactor extends AbstractRefactor {
    @Override
    public void refactor(Issue issue) {

        ClassOrInterfaceDeclaration classOrInterfaceDeclaration=(ClassOrInterfaceDeclaration) issue.getIssueNode();
        try {
            classNameRefactor(classOrInterfaceDeclaration);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void classNameRefactor(ClassOrInterfaceDeclaration classOrInterfaceDeclaration) throws IOException {
        String name="";
        List<String> nameList= SplitName.split(classOrInterfaceDeclaration.getNameAsString());
        if(nameList==null){
            return;
        }
        for(String data:nameList){
            data=data.substring(0,1).toUpperCase()+data.substring(1);

            name=name+data;
        }
        classOrInterfaceDeclaration.setName(name);
    }

}
