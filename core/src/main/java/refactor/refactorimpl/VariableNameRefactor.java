package refactor.refactorimpl;

import analysis.rule.LowerCamelCaseVariableNaming;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import io.FileUlits;
import model.Issue;
import refactor.AbstractRefactor;
import ulits.SplitName;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VariableNameRefactor extends AbstractRefactor {
    @Override
    public void refactor(Issue issue) {
        VariableDeclarationExpr variableDeclarationExpr=(VariableDeclarationExpr) issue.getIssueNode();
        try {
            variableNameRefactor(variableDeclarationExpr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void variableNameRefactor(VariableDeclarationExpr variableDeclarationExpr) throws IOException {
        String name="";
        List<String> nameList = SplitName.split(variableDeclarationExpr.getVariable(0).getNameAsString());
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
        variableDeclarationExpr.getVariable(0).setName(name);
    }
}
